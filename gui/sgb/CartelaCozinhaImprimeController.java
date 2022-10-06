package gui.sgb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ResourceBundle;

import gui.util.Alerts;
import gui.util.Imprimir;
import gui.util.Utils;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;

public class CartelaCozinhaImprimeController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

//	private static String COURIER;
//	private static Font getFont(String COURIER, float size) {
//		return null;
//	}

	public static String local;
 	public static String nomeProd = "";
 	public static double quantidade = 0.00;

 	static DecimalFormat df = new DecimalFormat("##,##0.00"); 

	
	static String pathI = "C:\\Arqs\\Impr\\cozinha.txt";

	static String linha01 = "";
	static String linha02 = "";
	static String linha03 = "";
	static String linha04 = "";
	
	public static void onBtImprimeProduto() throws ParseException, IOException {
   		grava();
		Imprimir.relatorio(pathI);
   	}

 	public static void grava() {
			try {	
				BufferedWriter bwCz = new BufferedWriter(new FileWriter(pathI));
 					{	linha01 = String.format("Local..: %s" + " ", local);
 						linha02 = String.format("Produto: %s" + " ", nomeProd);
 						linha03 = String.format("Qtd....: %s" + " ", df.format(quantidade));
 					 	linha04 = "                               ";
					 	bwCz.write(linha01);
					 	bwCz.newLine();
						bwCz.write(linha02);
					 	bwCz.newLine();
						bwCz.write(linha03);
					 	bwCz.newLine();
						bwCz.write(linha04);
					 	bwCz.newLine();
						bwCz.write(linha04);
					 	bwCz.newLine();
						bwCz.write(linha04);
					 	bwCz.newLine();
 					}	
					bwCz.close();
			}	
	 			catch(	IOException e2) {
	 				e2.getMessage();	 			
	 			}
				finally {					
			}
	 	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}
