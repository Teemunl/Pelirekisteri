package fxPelirekisteri;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import peliRekisteri.Rekisteri;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * @author tnliimvy
 * @version 1.4.2020
 * 
 * Pääohjelma Rekisteri ohjelman käynnistämiseksi
 */
public class PelirekisteriMain extends Application {
	@Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("PelirekisteriGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
            final PelirekisteriGUIController rekisteriCtrl = (PelirekisteriGUIController)ldr.getController();

            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("Pelirekisteri.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Rekisteri");
            
            // Platform.setImplicitExit(false);

            primaryStage.setOnCloseRequest((event) -> {
                    if ( !rekisteriCtrl.voikoSulkea() ) event.consume();
                });
            
            Rekisteri rekisteri = new Rekisteri();  
            rekisteriCtrl.setRekisteri(rekisteri); 
            
            primaryStage.show();
            
            Application.Parameters params = getParameters(); 
            if ( params.getRaw().size() > 0 ) 
            	rekisteriCtrl.lueTiedosto(params.getRaw().get(0));  
            else
                if ( !rekisteriCtrl.avaa() ) Platform.exit();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
	
    /**
     * Käynnistetään käyttöliittymä 
     * @param args komentorivin parametrit
     */
	public static void main(String[] args) {
		launch(args);
	}
}
