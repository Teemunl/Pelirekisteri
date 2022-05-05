package fxPelirekisteri;
import static fxPelirekisteri.TietueDialogController.getFieldId;


import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import peliRekisteri.Kategoria;
import peliRekisteri.Peli;
import peliRekisteri.Rekisteri;
import peliRekisteri.SailoException;

/**
 * @author teemu
 * @version 16.1.2020
 * @version 17.3.2020 lisatty pelien kasittely
 * @version 17.3.2020 lisatty kategorioiden kasittely
 */
public class PelirekisteriGUIController implements Initializable {
   
    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private Label labelVirhe;
    @FXML private ScrollPane panelPeli;
    @FXML private GridPane gridPeli;
    @FXML private ListChooser<Peli> chooserPelit;
    @FXML private StringGrid<Kategoria> tableKategoriat;
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();      
    }

    
    @FXML private void handleHakuehto() {
        hae(0); 
    }
    
    
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    
    @FXML private void handleAvaa() {
        avaa();
    }
    
    
    @FXML private void handleTulosta() {
        TulostusController tulostusCtrl = TulostusController.tulosta(null); 
        tulostaValitut(tulostusCtrl.getTextArea()); 
    } 

    
    @FXML private void handleLopeta() {
        tallenna();
        Platform.exit();
    } 

    
    @FXML private void handleUusiPeli() {
        uusiPeli();
    }
    
    
    @FXML private void handleMuokkaaPelia() {
        muokkaa(kentta);
    }
    
    
    @FXML private void handlePoistaPeli() {
        poistaPeli();
    }
    
     
    @FXML private void handleUusiKategoria() {
        uusiKategoria(); 
    }
    

    @FXML private void handleMuokkaaKategoria() {
    	muokkaaKategoria();
    }
    

    @FXML private void handlePoistaKategoria() {
        poistaKategoria();
    }
    

    @FXML private void handleApua() {
        avustus();
    }
    

    @FXML private void handleTietoja() {
    	avustus();
    }
    
    
    
  //===========================================================================================    
 // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia    
    
    private String rekisterinnimi= "miun";
    private Peli peliKohdalla;
    private Rekisteri rekisteri;

    private int kentta = 0; 
    private TextField edits[]; 
    private static Peli apupeli = new Peli(); 
    private static Kategoria apukategoria = new Kategoria(); 
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa Pelin tiedot.
     * Alustetaan myös pelilistan kuuntelija 
     */
    protected void alusta() {
        chooserPelit.clear();
        chooserPelit.addSelectionListener(e -> naytaPeli());
        
        cbKentat.clear(); 
        for (int k = apupeli.ekaKentta(); k < apupeli.getKenttia(); k++) 
            cbKentat.add(apupeli.getKysymys(k), null); 
        cbKentat.getSelectionModel().select(0); 
        
        edits = TietueDialogController.luoKentat(gridPeli, apupeli);  
        for (TextField edit: edits)  
            if ( edit != null ) {  
                edit.setEditable(false);  
                edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(e.getSource(),0)); });  
                edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta));
                edit.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaa(kentta);}); 
            }    
        // alustetaan kategoriataulukon otsikot 
        int eka = apukategoria.ekaKentta(); 
        int lkm = apukategoria.getKenttia(); 
        String[] headings = new String[lkm-eka]; 
        for (int i=0, k=eka; k<lkm; i++, k++) headings[i] = apukategoria.getKysymys(k); 
        tableKategoriat.initTable(headings); 
        tableKategoriat.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
        tableKategoriat.setEditable(false); 
        tableKategoriat.setPlaceholder(new Label("Ei vielä kategorioita")); 
         
        tableKategoriat.setColumnSortOrderNumber(1); 
        tableKategoriat.setColumnSortOrderNumber(2); 
        tableKategoriat.setColumnWidth(1, 60); 
        tableKategoriat.setColumnWidth(2, 60); 
        
        tableKategoriat.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) muokkaaKategoria(); } );
        tableKategoriat.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaaKategoria();}); 
    }


    /*
     * Tiedostojen tallennus
     */
    private String tallenna() { 
        try {
            rekisteri.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    /*
     * Näyttää ohjelman suunnitelman erillisessä selaimessa
     */
    private void avustus() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2020k/ht/tnliimvy");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
     }

    }
    /**
     * Luo uuden pelin, jota aletaan muokata
     */
    protected void uusiPeli() {
        
        
        try {
            Peli uusiPeli = new Peli();
            uusiPeli = TietueDialogController.kysyTietue(null, uusiPeli, 1);    
            if ( uusiPeli == null) return;
            uusiPeli.rekisteroi();
            rekisteri.lisaa(uusiPeli);
            hae(uusiPeli.getTunnusNro());
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }

    }
    /** 
     * Tekee uuden tyhjän kategorian editointia varten 
     */ 
    public void uusiKategoria() { 
        if ( peliKohdalla == null ) return;
        try {        	
            Kategoria uusi = new Kategoria(peliKohdalla.getTunnusNro());
            uusi = TietueDialogController.kysyTietue(null, uusi, 0);
            if ( uusi == null ) return;
            uusi.rekisteroi();
            rekisteri.lisaa(uusi);
            naytaKategoriat(peliKohdalla); 
            tableKategoriat.selectRow(1000);  // järjestetään viimeinen rivi valituksi
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Lisääminen epäonnistui: " + e.getMessage());
        }
    } 
    /**
     * Näyttää listasta valitun pelin tiedot tekstikenttiin. 
     */
    protected void naytaPeli() {
        peliKohdalla = chooserPelit.getSelectedObject();
        if (peliKohdalla == null) return;
        
        TietueDialogController.naytaTietue(edits, peliKohdalla); 
        naytaKategoriat(peliKohdalla);
    }

    private void naytaKategoriat(Peli peli) {
        tableKategoriat.clear();
        if ( peli == null ) return;
        
        try {
            List<Kategoria> kategoriat = rekisteri.annaKategoriat(peli);
            if ( kategoriat.size() == 0 ) return;
            for (Kategoria kat: kategoriat)
                naytaKategoria(kat);
        } catch (SailoException e) {
            naytaVirhe(e.getMessage());
        } 
    }
    
    
    
    private void naytaKategoria(Kategoria kat) {
        int kenttia = kat.getKenttia(); 
        String[] rivi = new String[kenttia-kat.ekaKentta()]; 
        for (int i=0, k=kat.ekaKentta(); k < kenttia; i++, k++) 
            rivi[i] = kat.anna(k); 
        tableKategoriat.add(kat,rivi);
    }
    
    
    
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    
    private void muokkaaKategoria() {
        
        int r = tableKategoriat.getRowNr();
        if ( r < 0 ) return; // klikattu ehkä otsikkoriviä
        Kategoria kat = tableKategoriat.getObject();
        if ( kat == null ) return;
        int k = tableKategoriat.getColumnNr()+kat.ekaKentta();
        try {
            kat = TietueDialogController.kysyTietue(null, kat.clone(), k);
            if ( kat == null ) return;
            rekisteri.korvaaTaiLisaa(kat); 
            naytaKategoriat(peliKohdalla); 
            tableKategoriat.selectRow(r);  // järjestetään sama rivi takaisin valituksi
        } catch (CloneNotSupportedException  e) { /* clone on tehty */  
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia lisäämisessä: " + e.getMessage());
        }
    }
    
    
    
    private void muokkaa(int k) { 
        if ( peliKohdalla == null ) return; 
        try { 
            Peli peli; 
            peli = TietueDialogController.kysyTietue(null, peliKohdalla.clone(), k);     
            if ( peli == null ) return; 
            rekisteri.korvaaTaiLisaa(peli); 
            hae(peli.getTunnusNro()); 
        } catch (CloneNotSupportedException e) { 
            // 
        } catch (SailoException e) { 
            Dialogs.showMessageDialog(e.getMessage()); 
        } 
    }     
    
    /**
     * Hakee pelin tiedot listaan
     * @param pnro pelin numero, joka aktivoidaan haun jälkeen
          */
    protected void hae(int pnro) {

        int pnr = pnro; // pnro pelin numero, joka aktivoidaan haun jälkeen 
        if ( pnr <= 0 ) { 
            Peli kohdalla = peliKohdalla; 
            if ( kohdalla != null ) pnr = kohdalla.getTunnusNro(); 
        }
        
        int k = cbKentat.getSelectionModel().getSelectedIndex() + apupeli.ekaKentta(); 
        String ehto = hakuehto.getText(); 
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 
        
        chooserPelit.clear();

        int index = 0;
        Collection<Peli> pelit;
        try {
            pelit = rekisteri.etsi(ehto, k);
            int i = 0;
            for (Peli peli: pelit) {
                if (peli.getTunnusNro() == pnr) index = i;
                chooserPelit.add(peli.getNimi(),peli);
                i++;
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Pelin hakemisessa ongelmia! " + ex.getMessage());
        }
        chooserPelit.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää pelin
    }
   
    /**
     * asettaa rekiisterin
     * @param rekisteri alustettu rekisteri
    */   
    public void setRekisteri(Rekisteri rekisteri) {
        this.rekisteri = rekisteri;
        naytaPeli();
    }

    /**
     * Poistetaan kategoriataulukosta valitulla kohdalla oleva kategoria
     */
    private void poistaKategoria() {
        int rivi = tableKategoriat.getRowNr();
        if ( rivi < 0 ) return;
        Kategoria kategoria = tableKategoriat.getObject();
        if ( kategoria == null ) return;
        rekisteri.poistaKategoria(kategoria);
        naytaKategoriat(peliKohdalla);
        int kategorioita = tableKategoriat.getItems().size(); 
        if ( rivi >= kategorioita ) rivi = kategorioita -1;
        tableKategoriat.getFocusModel().focus(rivi);
        tableKategoriat.getSelectionModel().select(rivi);
    }

    /*
     * Poistetaan listalta valittu Peli
     */
    private void poistaPeli() {
        Peli peli = peliKohdalla;
        if ( peli == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko peli: " + peli.getNimi(), "Kyllä", "Ei") )
            return;
        rekisteri.poista(peli);
        int index = chooserPelit.getSelectedIndex();
        hae(0);
        chooserPelit.setSelectedIndex(index);
    }


    /**
     * Kysytään tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
         */
    public boolean avaa() {
        String uusinimi = RekisterinNimiController.kysyNimi(null, rekisterinnimi);
        if (uusinimi == null) return false;
        lueTiedosto(uusinimi);
        return true;
    }

    /**
     * Alustaa rekisterin lukemalla sen valitun nimisestä tiedostosta
     * @param nimi tiedosto josta kerhon tiedot luetaan
     * @return mikä meni pieleen
         */
    protected String lueTiedosto(String nimi) {
        rekisterinnimi = nimi;
        setTitle("Rekisteri - " + rekisterinnimi );
        try {
            rekisteri.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
    }

    private void setTitle(String title) {
        ModalController.getStage(hakuehto).setTitle(title);
    }
    
    /**
     * Tulostaa listassa olevat pelit tekstialueeseen
     * @param text alue johon tulostetaan
     */
    public void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki pelit");
            for (Peli peli: chooserPelit.getObjects()) { 
                tulosta(os, peli);
                os.println("\n\n");
            }
        }
    }
    /**
     * Tulostaa pelin tiedot
     * @param os tietovirta johon tulostetaan
     * @param peli tulostettava peli
     */
    public void tulosta(PrintStream os, final Peli peli) {
        os.println("----------------------------------------------");
        peli.tulosta(os);
        os.println("----------------------------------------------");
        try {
            List<Kategoria> kategoriat = rekisteri.annaKategoriat(peli);
            for (Kategoria kat: kategoriat) 
                kat.tulosta(os);     
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Kategorioiden hakemisessa ongelmia! " + ex.getMessage());
        }      
    }
    

    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }

    
}
