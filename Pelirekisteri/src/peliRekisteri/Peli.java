package peliRekisteri;

import java.io.*;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.VuosiTarkistus;
import kanta.Tietue;

import static  kanta.VuosiTarkistus.*;
/**
 * rekisterin peli joka osaa mm. itse huolehtia tunnusNro:staan.
 *
 * @author teemu
 * @version 2.4.2020
 */
public class Peli implements Cloneable, Tietue {
    private int tunnusNro;
    private String pelinNimi = "";
    private String pelinAlusta = "";
    private String pelinVuosi = "";
    private String pelinArvio = "";
    private static int seuraavaNro = 1;


    /** 
     * Pelin vertailija 
     */ 
    public static class Vertailija implements Comparator<Peli> { 
        private int k;  
         
        @SuppressWarnings("javadoc") 
        public Vertailija(int k) { 
            this.k = k; 
        } 
         
        @Override 
        public int compare(Peli peli1, Peli peli2) { 
            return peli1.getAvain(k).compareToIgnoreCase(peli2.getAvain(k)); 
        } 
    } 
     
    
    /** 
     * Antaa k:n kentän sisällön merkkijonona 
     * @param k monenenko kentän sisältö palautetaan 
     * @return kentän sisältö merkkijonona 
     */ 
    public String getAvain(int k) { 
        switch ( k ) { 
        case 0: return "" + tunnusNro; 
        case 1: return "" + pelinNimi.toUpperCase(); 
        case 2: return "" + pelinAlusta;
        case 3: return "" + pelinVuosi; 
        case 4: return "" + pelinArvio; 
        default: return "sss"; 
        } 
    } 
       
    
    /**
     * Palauttaa Pelin kenttien lukumäärän
     * @return kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 5;
    }


    /**
     * Eka kenttä joka on mielekäs kysyttäväksi
     * @return eknn kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 1;
    }


    /**
     * Alustetaan pelin merkkijono-attribuuti tyhjiksi jonoiksi
     * ja tunnusnro = 0.
     */
    public Peli() {
        // Toistaiseksi ei tarvita mitään
    }


    /**
     * @return pelin nimi
     * @example
     * <pre name="test">
     *   Peli csgo = new Peli();
     *   csgo.vastaa_Csgo();
     *   csgo.getNimi() =R= "Csgo .*";
     * </pre>
     */
    public String getNimi() {
        return pelinNimi;
    }


    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    @Override
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + pelinNimi;
        case 2: return "" + pelinAlusta;
        case 3: return "" + pelinVuosi;
        case 4: return "" + pelinArvio;
        default: return "Nope";
        }
    }


    /**
     * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
     * @param k kuinka monennen kentän arvo asetetaan
     * @param jono jonoa joka asetetaan kentän arvoksi
     * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
     */
    @Override
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch ( k ) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
            return null;
        case 1:
            pelinNimi = tjono;
            return null;
        case 2:
        	pelinAlusta = tjono;
            return null;
        case 3:
            VuosiTarkistus vuodet = new VuosiTarkistus();
            
            try{
            	String virhe = vuodet.vuosiTarkistus(tjono);
            	if ( virhe == "Syöte ei ole vuosi" ) return virhe;
                pelinVuosi = vuodet.vuosiTarkistus(tjono);
                return null;
            } catch(Exception NumberFormatException) {
                pelinVuosi = vuodet.vuosiTarkistus(tjono);
                return null;
            }

        case 4:
        	pelinArvio = tjono;
            return null;
        default:
            return "Nope";
        }
    }


    /**
     * Palauttaa k:tta pelin kenttää vastaavan kysymyksen
     * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
     * @return k:netta kenttää vastaava kysymys
     */
    @Override
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "Tunnus nro";
        case 1: return "Pelin Nimi";
        case 2: return "Alusta";
        case 3: return "Julkaisu Vuosi";
        case 4: return "Käyttäjän arvio";
        default: return "npp";
        }
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot pelille.
     * @param apuVuosi = vuosi, joka arvotaan pelille
     * @param apuArvio = arvio, joka arvotaan pelille
     */
    public void vastaa_Csgo(String apuVuosi,String apuArvio) {
    this.pelinNimi = "Csgo" + " "+ rand(1000,9999);
    this.pelinAlusta = "Steam";
    this.pelinVuosi = apuVuosi;
    this.pelinArvio = apuArvio;
       
    }

    /**
     * Apumetodi, jolla täytetään testiarvot pelille
     * arvio ja vuosi arvotaan, jotta kahdella pelill� ei olisi samoja arvoja
     */
    public void vastaa_Csgo() {
        String apuVuosi = kanta.VuosiTarkistus.arvoVuosi();
        String apuArvio = kanta.ArvioTarkistus.arvoArvio();
        vastaa_Csgo(apuVuosi,apuArvio);
    }


    /**
     * Tulostetaan pelin tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro, 3) + "  " + pelinNimi + "  "
                + pelinAlusta);
        out.println("Julkaisu vuosi " + pelinVuosi + " Pelin arvio: " + pelinArvio);
       
    }


    /**
     * Tulostetaan pelin tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa pelille seuraavan rekisterinumeron.
     * @return pelin uusi tunnusNro
     * @example
     * <pre name="test">
     *   Peli csgo1 = new Peli();
     *   csgo1.getTunnusNro() === 0;
     *   csgo1.rekisteroi();
     *   Peli csgo2 = new Peli();
     *   csgo2.rekisteroi();
     *   int n1 = csgo1.getTunnusNro();
     *   int n2 = csgo2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }


    /**
     * Palauttaa pelin tunnusnumeron.
     * @return pelin tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }


    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }
    

    /**
     * Palauttaa pelin  tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return peli tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Peli peli = new Peli();
     *   peli.parse("   3  |  Csgo   | 2020");
     *   peli.toString().startsWith("3|Csgo|2020|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     * </pre>  
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
    }


    /**
     * Selvitää jäsenen tiedot | erotellusta merkkijonosta
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusNro.
     * @param rivi josta jäsenen tiedot otetaan
     * 
     * @example
     * <pre name="test">
     *   Peli peli = new Peli();
     *   peli.parse("   3  |  Csgo  | Steam");
     *   peli.getTunnusNro() === 3;
     *   peli.toString().startsWith("3|Csgo|Steam|") === true;
     *
     *   peli.rekisteroi();
     *   int n = peli.getTunnusNro();
     *   peli.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   peli.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   peli.getTunnusNro() === n+20+1;
     *     
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }
    
    
    /**
     * Tehdään identtinen klooni pelistä
     * @return Object kloonattu peli
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Peli peli = new Peli();
     *   peli.parse("   3  |  Csgo   | 123");
     *   Peli kopio = peli.clone();
     *   kopio.toString() === peli.toString();
     *   peli.parse("   4  |  Csgo1   | 123");
     *   kopio.toString().equals(peli.toString()) === false;
     * </pre>
     */
    @Override
    public Peli clone() throws CloneNotSupportedException {
        Peli uusi;
        uusi = (Peli) super.clone();
        return uusi;
    }
    
    
    /**
     * Tutkii onko pelin tiedot samat kuin parametrina tuodun pelin tiedot
     * @param peli peli johon verrataan
     * @return true jos kaikki tiedot samat, false muuten
     * @example
     * <pre name="test">
     *   Peli peli1 = new Peli();
     *   peli1.parse("   3  |  Csgo   | Steam");
     *   Peli peli2 = new Peli();
     *   peli2.parse("   3  |  Csgo   | Steam");
     *   Peli peli3 = new Peli();
     *   peli3.parse("   3  | Csgo   | Epic Games");
     *   
     *   peli1.equals(peli2) === true;
     *   peli2.equals(peli1) === true;
     *   peli1.equals(peli3) === false;
     *   peli3.equals(peli2) === false;
     * </pre>
     */
    public boolean equals(Peli peli) {
        if ( peli == null ) return false;
        for (int k = 0; k < getKenttia(); k++)
            if ( !anna(k).equals(peli.anna(k)) ) return false;
        return true;
    }


    @Override
    public boolean equals(Object jasen) {
        if ( jasen instanceof Peli ) return equals((Peli)jasen);
        return false;
    }


    @Override
    public int hashCode() {
        return tunnusNro;
    }


    /**
     * Testiohjelma pelille
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Peli csgo = new Peli(), csgo2 = new Peli();
        csgo.rekisteroi();
        csgo2.rekisteroi();
        csgo.tulosta(System.out);
        csgo.vastaa_Csgo();
        csgo.tulosta(System.out);

        csgo2.vastaa_Csgo();
        csgo2.tulosta(System.out);

        csgo2.vastaa_Csgo();
        csgo2.tulosta(System.out);
    }

}
