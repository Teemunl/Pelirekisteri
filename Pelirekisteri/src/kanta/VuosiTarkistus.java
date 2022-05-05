package kanta;

/**
 * @author tnliimvy
 * @version 20.2.2020
 *
 */
public class VuosiTarkistus {
 /**
 * Tarkistusmerkit, jotka voivat l�yty� vuodesta
 */
public static final String TARKISTUSMERKIT = "0123456789";

 /**
  * tarkastaa,ett� vuosi ei sis�ll� merkkej�, jotka ei sovi vuoteen
  * ja lis�� vuosituhannet alkuun, jos niit� ei ole
 * @param tjono Vuosi, jota tarkistetaan
 * @return tarkistettu vuosi
 * @example
 * <pre name="test">
 * vuosiTarkistus(20) === 2020;
 * vuosiTarkistus(18) === 2018;
 * vuosiTarkistus(84) === 1984;
 * </pre>
 */
 public String vuosiTarkistus(String tjono) {
     
     for (char k : tjono.toCharArray()) {
        if ( TARKISTUSMERKIT.indexOf(k) == -1) {
            return "Syöte ei ole vuosi";
        }
     }
     int vuosiTarkistettu = Integer.parseInt(tjono);
     if(vuosiTarkistettu  <  50) {
         vuosiTarkistettu += 2000;
     }
     if(vuosiTarkistettu < 100) {
         vuosiTarkistettu += 1900;
     }

     if (vuosiTarkistettu > 2020 || vuosiTarkistettu < 1980) {
         vuosiTarkistettu = Integer.parseInt(arvoVuosi());
         }    
     return ""+vuosiTarkistettu; 
     
 }

 /**
  * Arpoo vuoden ja testaa samalla, että se on toimiva
 * @return apuVuosi
 */
public static String arvoVuosi() {
    int arvio = VuosiTarkistus.rand(0, 2020);
    String tarkistettuArvio = String.format("%02d",arvio);
    return tarkistettuArvio;
 }
/**
 * @param ala = alin mahdollinen luku
 * @param yla = ylin mahdollinen luku
 * @return palauttaa satunnaisen luvun väliltä ala-yla
 */
public static int rand(int ala, int yla) {
    double n = (yla-ala)*Math.random()+ala;
    return (int)Math.round(n);
}

    
}
