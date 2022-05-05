/**
 * 
 */
package peliRekisteri;

/**
 * poikkeusluokka tietorakenteessa aiheutuville poikkeuksille
 *  @author Teemu Liimatta
 *  @version 1.0, 26.02.2020
 *
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;
    
    
    
    
    /**
     * Poikkeuksen muodostaja, jolle tuodaan poikkeuksessa käytettävä viesti
     * @param viesti poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }


}
