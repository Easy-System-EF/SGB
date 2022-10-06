package application;
	
import java.io.IOException;

import gui.MainViewSgbController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


public class MainSgb extends Application {

/* instanciamos um fxmlloader p/ manipular tela antes de carregar
 * loader carrega a view informada (parametro)
 * cena principal com obj da view
 * dps do titulo, mostra o palco	
 	  scene esta no Main - janela principal
	  referencia da janela principal
	  guardamos a tela principal
	  e um metodo para pegar a referencia getMainScene
*/
	
	private static Scene mainScene; 
	private static Integer numEmp = 1;
	private static String titulo = "";
	private static String sistema = "";

	@Override
	public void start(Stage primaryStage) { 
	  try { 
			//  carrega os dados da MainView			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainViewSgb.fxml"));
			ScrollPane scrollpane = loader.load();
 			MainViewSgbController controller = loader.getController();
			controller.numEmp = numEmp;

// isso cola a barra no limite da tela (menubar)
  		scrollpane.setFitToHeight(true);
		scrollpane.setFitToWidth(true);
 
		mainScene = new Scene(scrollpane); 
	    primaryStage.setScene(mainScene); 
	    if (numEmp == 1) {
	    	titulo = "Easy System";
	    	sistema = "Sistema Gerencial";
	    } else {
	    	if (numEmp == 2) {
	    		titulo = "WS Direções";
		    	sistema = "Sistema Gerencial de Oficina Automotiva";
	    	} else {
	    		if (numEmp == 3) {
	    			titulo = "Black Beer";
	    	    	sistema = "Sistema Gerencial de Bares e Restaurantes";
	    		}
	    	}
	    }
	    primaryStage.setTitle (titulo + "                                                 " +
	    "                  " + sistema); 
	    primaryStage.show(); 
	  } catch (IOException e) { 
	    e.printStackTrace(); 
	  } 
	}
	
	public static Scene getMainScene() {
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
