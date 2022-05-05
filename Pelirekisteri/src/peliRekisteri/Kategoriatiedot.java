/**
 * 
 */
package peliRekisteri;


/**
 * @author teemu
 * @version 23.4.2020
 * huolehtii kategorioiden id
 */
public class Kategoriatiedot {
    
    private static int MAX_kategoriat = 5;
    private int lkm = 0;
    private Kategoriatiedot[] alkiot;
    
    /**
     * Oletusmuodostaja
     */
    public Kategoriatiedot() {
        // Attribuuttien oma alustus riittÃ¤Ã¤
        alkiot = new Kategoriatiedot[MAX_kategoriat];
    }
    

    /**
     * Lisaa kategorian taulukkoon
     * @param kat kategoria joka lisataan
     * @throws SailoException jos ei onnistu
     */
    public void lisaa(Kategoriatiedot kat) throws SailoException {
        if(lkm >= alkiot.length) {
        	MAX_kategoriat = MAX_kategoriat + 5;
        	Kategoriatiedot[] uudetalkiot = new Kategoriatiedot[MAX_kategoriat];
            for(int i = 0; i<alkiot.length;i++) {
                uudetalkiot[i] = alkiot[i];
            }
            alkiot = uudetalkiot;
        }
        if(lkm >= alkiot.length) throw new SailoException("Liikaa alkiota");
        alkiot[lkm] = kat;
        lkm++;
    }
    
    /**
     * Palauttaa viitteen i:teen pelingenreen
     * @param i monennenko pelingenren viite halutaan
     * @return viite pelingenreen jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Kategoriatiedot anna(int i) throws IndexOutOfBoundsException {
        if(i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    /**
     * Palauttaa rekisterin pelingenrejen lukumÃ¤Ã¤rÃ¤n
     * @return kategorioiden lukumäärä
     */
    public int getLkm() {
        return lkm;
    }

    /**
     * @param args ei käytössä
     * 
     */
    public static void main(String[] args) {
        //
    
    }

}
