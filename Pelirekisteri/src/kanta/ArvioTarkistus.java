package kanta;
/**
 * Mahdollistaa realistisen arvion luomisen ja sen muuttamisen stringiksi
 * @author tnliimvy
 * @version 20.2.2020
 *
 */
public class ArvioTarkistus{
    /**
     * Tarkistusmerkit j�rjestyksess�
     */
    private static String TARKISTUSMERKITArvio = "0123456789<=>%";
    
    /**
     * arpoo arvion
     * @return arvottu ja tarkastettu arvio
     */
    public static String arvoArvio() {
        int arvio = VuosiTarkistus.rand(0, 100);
        String tarkistettuArvio = String.format("%02d",arvio);
        return tarkistettuArvio;
    }

    /**
     * @param arvio arvio
     * @return arvio
     */
    public String tarkistaArvio(int arvio) {
        int arvioTarkistettu = arvio;
        if(arvioTarkistettu <= 25) {
            String tarkistettuArvio = " " + TARKISTUSMERKITArvio.charAt(10) + TARKISTUSMERKITArvio.charAt(11) +
                    "25 "  + TARKISTUSMERKITArvio.charAt(TARKISTUSMERKITArvio.length()-1);
            return tarkistettuArvio;
        }
        if(arvioTarkistettu <= 50) {
            String tarkistettuArvio = " " + TARKISTUSMERKITArvio.charAt(10) + TARKISTUSMERKITArvio.charAt(11) +
                    "50 "  + TARKISTUSMERKITArvio.charAt(TARKISTUSMERKITArvio.length()-1);
            return tarkistettuArvio;
        }

        if(arvioTarkistettu <= 75) {
            String tarkistettuArvio = " " + TARKISTUSMERKITArvio.charAt(10) + TARKISTUSMERKITArvio.charAt(11) + "75 "  + TARKISTUSMERKITArvio.charAt(TARKISTUSMERKITArvio.length()-1);
            return tarkistettuArvio;
        }
        if(arvioTarkistettu > 75) {
            String tarkistettuArvio = " " + TARKISTUSMERKITArvio.charAt(12) + "75 "  + TARKISTUSMERKITArvio.charAt(TARKISTUSMERKITArvio.length()-1);
            return tarkistettuArvio;
        }
        return arvoArvio();
    }
   
}
