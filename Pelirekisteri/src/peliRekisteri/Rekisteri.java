package peliRekisteri;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * @author teemu
 * @version 23.4.2020
 * Huolehtii rekisterin yllapidosta
 */
public class Rekisteri  {
    private Pelit pelit = new Pelit();
    private Kategoriat kategoriat = new Kategoriat();
    /**
     * Poistaa pelin peleistä
     * @param peli peli joka poistetaan
     * @return montako peliä poistettiin
     */
    public int poista(Peli peli) {
        if ( peli == null ) return 0;
        int ret = pelit.poista(peli.getTunnusNro()); 
        kategoriat.poistaPelinKategoriat(peli.getTunnusNro()); 
        return ret; 
    }


    /** 
     * Poistaa tämän kategorian
     * @param kategoria poistettava kategoria
     * @example
     */ 
    public void poistaKategoria(Kategoria kategoria) { 
        kategoriat.poista(kategoria); 
    } 

    
    /**
     * LLisää kerhoon uuden pelin
     * @param peli lisättävä peli
     * @throws SailoException jos lisäys ei onnistu
     */
    public void lisaa(Peli peli) throws SailoException {
        pelit.lisaa(peli);
    }


    /** 
     * Korvaa pelin tietorakenteessa.  Ottaa pelin omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva peli.  Jos ei l�ydy, 
     * niin lisää uuden pelin. 
     * @param peli lis�t��v�n pelin viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo t�ynn� 
     * </pre>
     */ 
    public void korvaaTaiLisaa(Peli peli) throws SailoException { 
        pelit.korvaaTaiLisaa(peli); 
    } 

    
    /** 
     * Korvaa kategorian tietorakenteessa.  Ottaa kategoria omistukseensa. 
     * Etsitäänn samalla tunnusnumerolla oleva kategoria.  Jos ei löydy, 
     * niin lisätään uutena kategoriana. 
     * @param kategoria lisättävän kategorian viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo töynnö
     */ 
    public void korvaaTaiLisaa(Kategoria kategoria) throws SailoException { 
        kategoriat.korvaaTaiLisaa(kategoria); 
    } 

    
    /**
     * Lisätään uusi kategoria rekisteriin
     * @param kat lisättävä kategoria
     * @throws SailoException jos tulee ongelmia
     */
    public void lisaa(Kategoria kat ) throws SailoException {
    	
        kategoriat.lisaa(kat);
    }


    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien pelien viitteet 
     * @param hakuehto hakuehto  
     * @param k etsitt�v�n kent�n indeksi  
     * @return tietorakenteen l�ytyneist� peleistä
     * @throws SailoException Jos jotakin menee v��rin
     */ 
    public Collection<Peli> etsi(String hakuehto, int k) throws SailoException { 
        return pelit.etsi(hakuehto, k); 
    } 
    
    
    /**
     * Haetaan kaikki pelin kategoriat
     * @param peli peli jolle kategoria haetaan
     * @return tietorakenne jossa viiteet l�ydetteyihin kategorioihin
     * @throws SailoException jos tulee ongelmia
     * </pre> 
     */
    public List<Kategoria> annaKategoriat(Peli peli) throws SailoException {
        return kategoriat.annaKategoriat(peli.getTunnusNro());
    }


    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        pelit.setTiedostonPerusNimi(hakemistonNimi + "pelintiedot");
        kategoriat.setTiedostonPerusNimi(hakemistonNimi + "kategoriat");
    }
    
      
    /**
     * Lukee rekisterin tiedot tiedostosta
     * @param nimi jota käytetään lukemisessa
     * @throws SailoException jos lukeminen ep�onnistuu
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        pelit = new Pelit(); 
        kategoriat = new Kategoriat();

        setTiedosto(nimi);
        pelit.lueTiedostosta();
        kategoriat.lueTiedostosta();
    }


    /**
     * Tallenttaa kerhon tiedot tiedostoon.  
     * Vaikka pelin tallenttamien epäonistuisi, niin yritet��n silti tallettaa
     * harrastuksia ennen poikkeuksen heitt�mist�.
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            pelit.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }

        try {
            kategoriat.tallenna();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }


    /**
     * Testiohjelma rekisteristä
     * @param args ei k�yt�ss�
     */
    public static void main(String args[]) {
        Rekisteri rekisteri = new Rekisteri();

        try {
            rekisteri.lueTiedostosta("miun");

            Peli csgo1 = new Peli(), csgo2 = new Peli();
            csgo1.rekisteroi();
            csgo1.vastaa_Csgo();
            csgo2.rekisteroi();
            csgo2.vastaa_Csgo();

            rekisteri.lisaa(csgo1);
            rekisteri.lisaa(csgo2);
            int id1 = csgo1.getTunnusNro();
            int id2 = csgo2.getTunnusNro();
            Kategoria csgo11 = new Kategoria(id1);
            csgo11.vastaaFps(id1);
            rekisteri.lisaa(csgo11);
            Kategoria csgo12 = new Kategoria(id1);
            csgo12.vastaaFps(id1);
            rekisteri.lisaa(csgo12);
            Kategoria csgo21 = new Kategoria(id2);
            csgo21.vastaaFps(id2);
            rekisteri.lisaa(csgo21);
            Kategoria csgo22 = new Kategoria(id2);
            csgo22.vastaaFps(id2);
            rekisteri.lisaa(csgo22);
            Kategoria csgo23 = new Kategoria(id2);
            csgo23.vastaaFps(id2);
            rekisteri.lisaa(csgo23);

            System.out.println("============= Rekisterin testi =================");
            Collection<Peli> pelit = rekisteri.etsi("", -1);
            int i = 0;
            for (Peli peli: pelit) {
                System.out.println("Peli paikassa: " + i);
                peli.tulosta(System.out);
                List<Kategoria> loytyneet = rekisteri.annaKategoriat(peli);
                for (Kategoria kategoria : loytyneet)
                    kategoria.tulosta(System.out);
                i++;
            }

        } catch ( SailoException ex ) {
            System.out.println(ex.getMessage());
        }
    }





}
