/**
 * 
 */
package peliRekisteri;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;
/**
 * @author teemu
 * @version 26.2.2020
 *
 */
public class Pelit implements Iterable<Peli>{
    private static final int MAX_PELEJA = 15;

    private boolean muutettu = false;
    private int lkm                     = 0;
    private String kokoNimi = "";
    private String      tiedostonNimi   = "";
    private Peli             alkiot[]   = new Peli[MAX_PELEJA];
    
    
    /**
     * OletusMuodostaja
     */
    public Pelit() {
        //
    }
    
    /**
     * Lisaa uuden pelin tietorakenteeseen. Ottaa pelin omistukseensa.
     * @param peli lisättävön pelin viite. 
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * 
     * </pre>
     */
    public void lisaa(Peli peli) throws SailoException {
        if (lkm >= alkiot.length) throw new SailoException("Liikaa alkioit");
        alkiot[lkm] = peli;
        lkm++;
        muutettu = true;    
    }

    /**
     * Korvaa pelin tietorakenteessa.  Ottaa pelin omistukseensa.
     * Etsitöön samalla tunnusnumerolla olevan pelin.  Jos ei löydy,
     * niin lisätäänn uutena pelinä.
     * @param peli lisattavan pelin viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Pelit pelit = new Pelit();
     * Peli csgo1 = new Peli(), csgo = new Peli();
     * csgo1.rekisteroi(); csgo.rekisteroi();
     * pelit.getLkm() === 0;
     * pelit.korvaaTaiLisaa(csgo1); pelit.getLkm() === 1;
     * pelit.korvaaTaiLisaa(csgo); pelit.getLkm() === 2;
     * Peli csgo3 = csgo1.clone();
     * csgo3.aseta(3,"kkk");
     * Iterator<Peli> it = pelit.iterator();
     * it.next() == csgo1 === true;
     * pelit.korvaaTaiLisaa(csgo3); pelit.getLkm() === 2;
     * it = pelit.iterator();
     * Peli j0 = it.next();
     * j0 === csgo3;
     * j0 == csgo3 === true;
     * j0 == csgo1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Peli peli) throws SailoException {
        int id =  peli.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] =  peli;
                muutettu = true;
                return;
            }
        }
        lisaa( peli);
    }
    /** 
     * Poistaa pelin jolla on valittu tunnusnumero  
     * @param id poistettavan pelin tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Pelit pelit = new Pelit(); 
     * Peli csgo1 = new Peli(), csgo = new Peli(), csgo3 = new Peli(); 
     * csgo1.rekisteroi(); csgo.rekisteroi(); csgo3.rekisteroi(); 
     * int id1 = csgo1.getTunnusNro(); 
     * pelit.lisaa(csgo1); pelit.lisaa(csgo); pelit.lisaa(csgo3); 
     * pelit.poista(id1+1) === 1; 
     * pelit.annaId(id1+1) === null; pelit.getLkm() === 2; 
     * pelit.poista(id1) === 1; pelit.getLkm() === 1; 
     * pelit.poista(id1+3) === 0; pelit.getLkm() === 1; 
     * </pre> 
     *  
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 
    /**
     * Palauttaa viitteen i:teen peliin
     * @param i monennnenko pelin viite halutaan
     * @return viite peliin ,jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Peli anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
                throw new IndexOutOfBoundsException("Laiton indeksi: "+ i);
        return alkiot[i];
    }
    
    /**
     * lukee pelit tiedostosta
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException{
        setTiedostonPerusNimi(hakemisto);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            kokoNimi = fi.readLine();
            if ( kokoNimi == null ) throw new SailoException("Rekisterin nimi puuttuu");
            String rivi = fi.readLine();
            if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");
           

            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Peli peli = new Peli();
                peli.parse(rivi);
                lisaa(peli);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    /**
     * Palauttaa pelit tiedostoon
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak); 

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(getKokoNimi());
            fo.println(alkiot.length);
            for (Peli peli : this) {
                fo.println(peli.toString());
            }

        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
    /**
     * Palauttaa Kerhon koko nimen
     * @return Kerhon koko nimi merkkijononna
     */
    public String getKokoNimi() {
        return kokoNimi;
    }
    /**
     * Palauttaa tiedoston nimen, jota k�ytet��n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonNimi;
    }
    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonNimi = nimi;
    }
    /**
     * Palauttaa tiedoston nimen, jota k�ytet��n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }

    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonNimi + ".bak";
    }
    /**
     * Palauttaa rekisterin pelien lukumäärän
     * @return pelien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }

    /**
     * Luokka Peliten iteroimiseksi.
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Pelit pelit = new Pelit();
     * Peli csgo1 = new Peli(), csgo2 = new Peli();
     * csgo1.rekisteroi(); csgo2.rekisteroi();
     *
     * pelit.lisaa(csgo1); 
     * pelit.lisaa(csgo2); 
     * pelit.lisaa(csgo1); 
     * 
     * StringBuffer ids = new StringBuffer(30);
     * for (Peli peli:pelit)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+peli.getTunnusNro());           
     * 
     * String tulos = " " + csgo1.getTunnusNro() + " " + csgo2.getTunnusNro() + " " + csgo1.getTunnusNro();
     * 
     * ids.toString() === tulos; 
     * 
     * ids = new StringBuffer(30);
     * for (Iterator<Peli>  i=pelit.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Peli peli = i.next();
     *   ids.append(" "+peli.getTunnusNro());           
     * }
     * 
     * ids.toString() === tulos;
     * 
     * Iterator<Peli>  i=pelit.iterator();
     * i.next() == csgo1  === true;
     * i.next() == csgo2  === true;
     * i.next() == csgo1  === true;
     * 
     * i.next();  #THROWS NoSuchElementException
     *  
     * </pre>
     */
    public class PelitIterator implements Iterator<Peli> {
        private int kohdalla = 0;


        /**
         * Onko olemassa viel� seuraavaa Peli�
         * @see java.util.Iterator#hasNext()
         * @return true jos on viel� Pelii�
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava Peli
         * @return seuraava Peli
         * @throws NoSuchElementException jos seuraava alkiota ei en�� ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Peli next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }


    /**
     * Palautetaan iteraattori Peliist��n.
     * @return Peli iteraattori
     */
    @Override
    public Iterator<Peli> iterator() {
        return new PelitIterator();
    }


    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien Peliten viitteet 
     * @param hakuehto hakuehto 
     * @param k etsitt�v�n kent�n indeksi  
     * @return tietorakenteen l�ytyneist� Peliist� 
     */ 
    public Collection<Peli> etsi(String hakuehto, int k) { 
        String ehto = "*"; 
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
        int hk = k; 
        if ( hk < 0 ) hk = 0; // jotta etsii id:n mukaan 
        List<Peli> loytyneet = new ArrayList<Peli>(); 
        for (Peli peli : this) { 
            if (WildChars.onkoSamat(peli.anna(hk), ehto)) loytyneet.add(peli);   
        } 
        Collections.sort(loytyneet, new Peli.Vertailija(hk)); 
        return loytyneet; 
    }
    
    
    /** 
     * Etsii Pelien id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsit��n 
     * @return Peli jolla etsitt�v� id tai null 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Pelit pelit = new Pelit(); 
     * Peli csgo1 = new Peli(), csgo2 = new Peli(), csgo3 = new Peli(); 
     * csgo1.rekisteroi(); csgo2.rekisteroi(); csgo3.rekisteroi(); 
     * int id1 = csgo1.getTunnusNro(); 
     * pelit.lisaa(csgo1); pelit.lisaa(csgo2); pelit.lisaa(csgo3); 
     * pelit.annaId(id1  ) == csgo1 === true; 
     * pelit.annaId(id1+1) == csgo2 === true; 
     * pelit.annaId(id1+2) == csgo3 === true; 
     * </pre> 
     */ 
    public Peli annaId(int id) { 
        for (Peli peli : this) { 
            if (id == peli.getTunnusNro()) return peli; 
        } 
        return null; 
    } 


    /** 
     * Etsii Pelien id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsit��n 
     * @return l�ytyneen Pelien indeksi tai -1 jos ei l�ydy 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Pelit pelit = new Pelit(); 
     * Peli csgo1 = new Peli(), csgo2 = new Peli(), csgo3 = new Peli(); 
     * csgo1.rekisteroi(); csgo2.rekisteroi(); csgo3.rekisteroi(); 
     * int id1 = csgo1.getTunnusNro(); 
     * pelit.lisaa(csgo1); pelit.lisaa(csgo2); pelit.lisaa(csgo3); 
     * pelit.etsiId(id1+1) === 1; 
     * pelit.etsiId(id1+2) === 2; 
     * </pre> 
     */ 
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
        return -1; 
    } 

   
    
    /**
     * Pelit testit paaohjelma
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Pelit pelit = new Pelit();
        
        Peli csgo = new Peli(), csgo2 = new Peli();
        csgo.rekisteroi();
        csgo.vastaa_Csgo();
        csgo2.rekisteroi();
        csgo2.vastaa_Csgo();
        
        try {
            pelit.lisaa(csgo);
            pelit.lisaa(csgo2);
            
            System.out.println("=================== Pelit testi =========================");
            for (int i = 0; i < pelit.getLkm(); i++) {
                Peli peli = pelit.anna(i);
                System.out.println("Peli nro "+ i);
                peli.tulosta(System.out);
            }
            
        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
