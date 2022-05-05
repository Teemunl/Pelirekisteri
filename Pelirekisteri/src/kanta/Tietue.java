package kanta;



/**
 * @author teemu
 * @version 24.4.2020
 *Tietue 
 */
public interface Tietue {

    
    /**
     * @return tietueen kenttien lukum��r�
     * @example
     */
    public abstract int getKenttia();


    /**
     * @return ensimm�inen k�ytt�j�n sy�tett�v�n kent�n indeksi
     * @example
     * <pre name="test">
     *   Kategoria har = new Kategoria();
     *   har.ekaKentta() === 2;
     * </pre>
     */
    public abstract int ekaKentta();


    /**
     * @param k mink� kent�n kysymys halutaan
     * @return valitun kent�n kysymysteksti
     * @example
     * </pre>
     */
    public abstract String getKysymys(int k);


    /**
     * @param k Mink� kent�n sis�lt� halutaan
     * @return valitun kent�n sis�lt�
     * @example
     * </pre>
     */
    public abstract String anna(int k);

    
    /**
     * Asetetaan valitun kent�n sis�lt�.  Mik�li asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k mink� kent�n sis�lt� asetetaan
     * @param s asetettava sis�lt� merkkijonona
     * @return null jos ok, muuten virheteksti
     * @example
     * </pre>
     */
    public abstract String aseta(int k, String s);


    /**
     * Tehd��n identtinen klooni tietueesta
     * @return kloonattu tietue
     * @throws CloneNotSupportedException jos kloonausta ei tueta
     * @example
     */
    public abstract Tietue clone() throws CloneNotSupportedException;


    /**
     * Palauttaa tietueen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return tietue tolppaeroteltuna merkkijonona 
     */
    @Override
    public abstract String toString();

}