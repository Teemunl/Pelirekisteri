package peliRekisteri;
import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Tietue;

/**
 * @author teemu
 * @version 17.3.2020
 *
 */
public class Kategoria implements Cloneable, Tietue{
    private int tunnusNro;
    private int pelinNro;
    private String kategoria = "";
    private static int seuraavaNro = 1;
    /**
     * Alustaa kategorian
     */
    public Kategoria() {
        //
        
    }

    /**
     * Alustaa tietyn pelin kategorian
     * @param pelinNro pelin numero
     */
    public Kategoria(int pelinNro) {
        this.pelinNro = pelinNro;
        
    }
    /**
     * Apumetodi, jolla saadaan t�ytetty� testiarvot Kategorialle.
     * @param nro viite peliin, jonka kategoriasta on kyse
     */
    public void vastaaFps(int nro) {
        pelinNro = nro;
        kategoria = "fps";
    }
    /**
     * @return kategorioiden kenttien lukum��r�
     */
    @Override
    public int getKenttia() {
        return 3;
    }
    /**
     * @return ensimm�inen k�ytt�j�n sy�tett�v�n kent�n indeksi
     */
    @Override
    public int ekaKentta() {
        return 2;
    }
    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     */
    @Override
    public String getKysymys(int k) {
        switch (k) {
            case 0:
                return "id";
            case 1:
                return "pelinId";
            case 2:
                return "kategoria";
            default:
                return "???";
        }
    }
    /**
     * @param k Minkä kentän sisältä halutaan
     * @return valitun kentän sisältö
     */
    @Override
    public String anna(int k) {
        switch (k) {
            case 0:
                return "" + tunnusNro;
            case 1:
                return "" + pelinNro;
            case 2:
            	return "" + kategoria;
            default:
                return "???";
        }
    }
    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virhetekst
     */
    @Override
    public String aseta(int k, String s) {
        String st = s.trim();
        StringBuffer sb = new StringBuffer(st);
        switch (k) {
            case 0:
                setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
                return null;
            case 1:
                pelinNro = Mjonot.erota(sb, '$', pelinNro);
                return null;
            case 2:
            	kategoria = st;
                return null;

            default:
                return "Väärä kentän indeksi";
        }
    }
    /**
     * Tehdään identtinen klooni kategoriasta
     * @return Object kloonattu kategoria
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Kategoria kat = new Kategoria();
     *   kat.parse("   2   |  10  |   Csgo  |");
     *   Kategoria kopio = kat.clone();
     *   kopio.toString() === kat.toString();
     *   kat.parse("   1   |  11  |   Csgo  |");
     *   kopio.toString().equals(kat.toString()) === false;
     * </pre>
     */
    @Override
    public Kategoria clone() throws CloneNotSupportedException { 
        return (Kategoria)super.clone();
    }
    
    /**
     * Tulostaa kategorian tiedot
     * @param out tietovirta , johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(
        " " +kategoria + " ");
    }
    
    /**
     * Tulostaa pelin tiedot
     * @param os tietovirta, johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Antaa kategorialle seuraavan rekisterinumeron.
     * @return harrastuksen uusi tunnus_nro
     * @example
     * <pre name="test">
     *   Kategoria fps1 = new Kategoria();
     *   fps1.getTunnusNro() === 0;
     *   fps1.rekisteroi();
     *   Kategoria fps2 = new Kategoria();
     *   fps2.rekisteroi();
     *   int n1 = fps1.getTunnusNro();
     *   int n2 = fps2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */

    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
    /**
     * Palautetaan kategorian oma id
     * @return kategorian id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }

    /**
     * Asettaa tunnusnumeron ja samalla varmistaa ett�
     * seuraava numero on aina suurempi kuin t�h�n menness� suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }
    /**
     * Palautetaan mihin peliin kategoria kuuluu
     * @return Pelin id
     */
    public int getPelinNro() {
        return pelinNro;
    }

    /**
     * Palauttaa kategorian tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return kategoria tolppaeroteltuna merkkijonona 
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
     * Selvit�� kategorian tiedot | erotellusta merkkijonosta.
     * Pit�� huolen ett� seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta harrastuksen tiedot otetaan
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }
    
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }
    @Override
    public int hashCode() {
        return tunnusNro;
    }

    
    /**
     * Testi p��ohjelma kategorialle
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Kategoria kat = new Kategoria();
        kat.vastaaFps(2);
        kat.tulosta(System.out);


    }

}
