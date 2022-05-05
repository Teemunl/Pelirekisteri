package fxPelirekisteri;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import peliRekisteri.Peli;


/**
 * @author teemu
 * @version 1.4.2020
 *
 */
public class PeliDialogController implements ModalControllerInterface<Peli>,Initializable  {

    @FXML private ScrollPane panelPeli;
    @FXML private GridPane gridPeli;
    @FXML private Label labelVirhe;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();  
    }
    
    @FXML private void handleOK() {
        if ( peliKohdalla != null && peliKohdalla.getNimi().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }

    
    @FXML private void handleCancel() {
        peliKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

// ========================================================    
    private Peli peliKohdalla;
    private static Peli apupeli = new Peli(); // Peli jolta voidaan kysellä tietoja.
    private TextField[] edits;
    private int kentta = 0;
    

    /**
     * Luodaan GridPaneen pelin tiedot
     * @param gridPeli mihin tiedot luodaan
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridPeli) {
        gridPeli.getChildren().clear();
        TextField[] edits = new TextField[apupeli.getKenttia()];
        
        for (int i=0, k = apupeli.ekaKentta(); k < apupeli.getKenttia(); k++, i++) {
            Label label = new Label(apupeli.getKysymys(k));
            gridPeli.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridPeli.add(edit, 1, i);
        }
        return edits;
    }
    

    /**
     * Tyhjentään tekstikentät 
     * @param edits tyhjennettävät kentät
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit: edits) 
            if ( edit != null ) edit.setText(""); 
    }

    /**
     * Palautetaan komponentin id:stä saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mikä arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna 
     */
    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }
    
    
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa jäsenten tiedot.
     */
    protected void alusta() {
        edits = luoKentat(gridPeli);
        for (TextField edit : edits)
            if ( edit != null )
                edit.setOnKeyReleased( e -> kasitteleMuutosJaseneen((TextField)(e.getSource())));
        panelPeli.setFitToHeight(true);
    }
    
    
    @Override
    public void setDefault(Peli oletus) {
        peliKohdalla = oletus;
        naytaJasen(edits, peliKohdalla);
    }

    
    @Override
    public Peli getResult() {
        return peliKohdalla;
    }
    
    
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }
    
    
    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        kentta = Math.max(apupeli.ekaKentta(), Math.min(kentta, apupeli.getKenttia()-1));
        edits[kentta].requestFocus();
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

    
    /**
     * Käsitellään jäseneen tullut muutos
     * @param edit muuttunut kenttä
     */
    protected void kasitteleMuutosJaseneen(TextField edit) {
        if (peliKohdalla == null) return;
        int k = getFieldId(edit,apupeli.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = peliKohdalla.aseta(k,s); 
        if (virhe == null) {
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }
    
    
    /**
     * Näytetään jäsenen tiedot TextField komponentteihin
     * @param edits taulukko TextFieldeistä johon näytetään
     * @param peli näytettävä peli
     */
    public static void naytaJasen(TextField[] edits, Peli peli) {
        if (peli == null) return;
        for (int k = peli.ekaKentta(); k < peli.getKenttia(); k++) {
            edits[k].setText(peli.anna(k));
        }
    }
    
    
    /**
     * Luodaan jäsenen kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * TODO: korjattava toimimaan
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @param kentta mikä kenttä saa fokuksen kun näytetään
     * @return null jos painetaan Cancel, muuten täytetty tietue
     */
    public static Peli kysyJasen(Stage modalityStage, Peli oletus, int kentta) {
        return ModalController.<Peli, PeliDialogController>showModal(
                    PeliDialogController.class.getResource("PeliDialogView.fxml"),
                    "Kerho",
                    modalityStage, oletus,
                    ctrl -> ctrl.setKentta(kentta) 
                );
    }

}
