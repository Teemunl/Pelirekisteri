package peliRekisteri;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Rekisterin kategoriat, joka osaa mm. lis�t� kategorian
 * @author teemu
 * @version 17.3.2020
 *
 */
public class Kategoriat implements Iterable<Kategoria>{

    private boolean muutettu = false;
    private String tiedostonNimi = "";
    
    /**  Taulukko Kategorioista */
    private final List<Kategoria> alkiot = new ArrayList<Kategoria>();
    
    
    /**
     * Kategorian alustus
     */
    public Kategoriat(){
        //
    }
    
    /**
     * Lisää tietorakenteeseen uuden kategoria, Ottaa sen omistuksiinsa
     * 
     * @param kat lisattava kategoria
     */
    public void lisaa(Kategoria kat) {
        
            alkiot.add(kat);
            muutettu = true;
       
    }
    
    /**
     * @param kategoria  korvattava
     * @throws SailoException jos ei onnistu
     */
    public void korvaaTaiLisaa(Kategoria kategoria) throws SailoException {
        int id = kategoria.getTunnusNro();

        for (int i = 0; i < getLkm(); i++) {
            if (alkiot.get(i).getTunnusNro() == id) {
                alkiot.set(i, kategoria);
                muutettu = true;
                return;
            }
        }
        lisaa(kategoria);
    }
    /**
     * @param kategoria poistettavan kategorian viite
     * @return tosi, jos poisto mahdollista 
     */
    public boolean poista(Kategoria kategoria) {
        boolean ret = alkiot.remove(kategoria);
        if (ret) muutettu = true;
        return ret;
    }
    /**
     * @param tunnusNro pelin tunnusnro
     * @return monta poistui
     */
    public int poistaPelinKategoriat(int tunnusNro) {
        int n = 0;
        for (Iterator<Kategoria> it = alkiot.iterator(); it.hasNext();) {
            Kategoria kat = it.next();
            if ( kat.getPelinNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }
    /**
     * Lukee kategoriat tiedostosta
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos ei onnistu lukeminen tiedostosta
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        setTiedostonPerusNimi(hakemisto);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Kategoria kat = new Kategoria();
                kat.parse(rivi);
                lisaa(kat);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
        
    }
    
    /**
     * Luetaan aikaisemmin annetun nimisesta tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    /**
     * Tallentaa kategoriat tiedostoon.
     * @throws SailoException jos talletus ep�onnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Kategoria kat : this) {
                fo.println(kat.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }

    
    /**
     * Palauttaa kategorioiden määrän
     * @return kategorioiden määrä
     */
    public int getLkm() {
        return alkiot.size();
    }
    /**
     * Asettaa tiedoston perusnimen 
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonNimi  = tied;
    }
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonNimi;
    }
    /**
     * Palauttaa tiedoston nimen, jota k�ytet��n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonNimi + ".dat";
    }
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonNimi  + ".bak";
    }



    /**
     * Iteraattori, jonka avulla käydään kaikki kategoriat
     * 
     * @return kategoriaiteraattori
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Kategoriat kattit = new Kategoriat();
     * Kategoria fps21 = new Kategoria(2); kattit.lisaa(fps21);
     * Kategoria fps11 = new Kategoria(1); kattit.lisaa(fps11);
     * Kategoria fps22 = new Kategoria(2); kattit.lisaa(fps22);
     * Kategoria fps12 = new Kategoria(1); kattit.lisaa(fps12);
     * Kategoria fps23 = new Kategoria(2); kattit.lisaa(fps23);
     * 
     * Iterator<Kategoria> i2 = kattit.iterator();
     * i2.next() === fps21;
     * i2.next() === fps11;
     * i2.next() === fps22;
     * i2.next() === fps12;
     * i2.next() === fps23;
     * i2.next() === fps12; #THROWS NoSuchElementException
     * int n = 0; 
     * int pnrot[] = {2,1,2,1,2};
     * 
     * for (Kategoria kat : kattit) {
     *   kat.getPelinNro() === pnrot[n]; n++;
     * }
     * 
     * 
     * 
     * n === 5;
     * 
     * 
     * </pre>
     */
    @Override
    public Iterator<Kategoria> iterator() {
        return alkiot.iterator();
    }
    
    
    
    /**
     * Antaa tietyn pelin kategoriat 
     * @param tunnusNro pelin tunnusNro
      * @return tietorakenne jossa viiteet l�ydetteyihin harrastuksiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     *  Kategoriat kattit = new Kategoriat();
     *  Kategoria fps21 = new Kategoria(2); kattit.lisaa(fps21);
     *  Kategoria fps11 = new Kategoria(1); kattit.lisaa(fps11);
     *  Kategoria fps22 = new Kategoria(2); kattit.lisaa(fps22);
     *  Kategoria fps12 = new Kategoria(1); kattit.lisaa(fps12);
     *  Kategoria fps23 = new Kategoria(2); kattit.lisaa(fps23);
     *  Kategoria fps51 = new Kategoria(5); kattit.lisaa(fps51);
     *  
     *  List<Kategoria> loytyneet;
     *  loytyneet = kattit.annaKategoriat(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = kattit.annaKategoriat(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == fps11 === true;
     *  loytyneet.get(1) == fps12 === true;
     *  loytyneet = kattit.annaKategoriat(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == fps51 === true;
     * </pre> 
     */
    public List<Kategoria> annaKategoriat(int tunnusNro){
        List<Kategoria> loydetyt = new ArrayList<Kategoria>();
        for (Kategoria kat : alkiot)
            if(kat.getPelinNro() == tunnusNro) loydetyt.add(kat);
        return loydetyt;
    }
    /**
     * testiohjelma KAtegorioille
     * @param args ei kaytossa
     * 
     */
    public static void main(String[] args) {
        Kategoriat kattit = new Kategoriat();
        Kategoria fps1 = new Kategoria();
        fps1.vastaaFps(2);
        Kategoria fps2 = new Kategoria();
        fps2.vastaaFps(1);
        Kategoria fps3 = new Kategoria();
        fps3.vastaaFps(2);
        Kategoria fps4 = new Kategoria();
        fps4.vastaaFps(2);

        kattit.lisaa(fps1);
        kattit.lisaa(fps2);
        kattit.lisaa(fps3);
        kattit.lisaa(fps2);
        kattit.lisaa(fps4);

        System.out.println("============= Kategoriat testi =================");

        List<Kategoria> kattit2 = kattit.annaKategoriat(2);

        for (Kategoria kat : kattit2) {
            System.out.print(kat.getPelinNro() + " ");
            kat.tulosta(System.out);
        }
    }


}
