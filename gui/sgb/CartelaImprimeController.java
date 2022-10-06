package gui.sgb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.CartelaVirtualImprime;
import gui.sgbmodel.entities.Empresa;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.EmpresaService;
import gui.util.FormataGabarito;
import gui.util.Imprimir;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CartelaImprimeController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

//	private static String COURIER;
//	private static Font getFont(String COURIER, float size) {
//		return null;
//	}

	public static Integer numEmp = 0;
	public static Integer numCar;

	Locale localeBr = new Locale("pt", "BR");
 	DecimalFormat df = new DecimalFormat("##,##0.00"); 
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date data1 = new Date();

	Integer codigo = 0;
	double totalCar = 0.00;
	double subTotalCar = 0.00;
	double descontoCar = 0.00;
	double servicoCar = 0.00;

 	String nomeProd = "";
 	double quantidade = 0.00;
 	double venda = 0.00;
 	double total = 0.00;
	
	String pathI = "C:\\Arqs\\impr\\cartela.txt";

	String linhaNome = "";
	String linhaTelMailPix = "";
	String linhaFiscal = "";
	String linha02 = "";
	String linha03 = "";
	String linha04 = "";
	String linha05 = "";
	String linha06 = "";
	String linha07 = "";
	String linha08 = "";
	String linha09 = "";
	String linha10 = "";
	
	private Cartela cartela;
	private CartelaVirtual virtual;
	private Empresa empresa;
	private CartelaVirtualImprime imprime;
	
	private CartelaService carService;
	private CartelaVirtualService virService;
	private EmpresaService empService;

	public void setCartela(Cartela cartela,
							CartelaVirtual virtual,
							Empresa empresa) {
		this.cartela = cartela;
		this.virtual = virtual;
		this.empresa = empresa;
	}

	public void setCartelaService(CartelaService carService,
									CartelaVirtualService virService,
									EmpresaService empService) {
		this.carService = carService;
		this.virService = virService;
		this.empService = empService;
	}


	public void setCartelaVirtual(CartelaVirtual virtual) {
		this.virtual = virtual;
	}

	public void setCartelaVirtualService(CartelaVirtualService virService) {
		this.virService = virService;
	}

	@FXML
	public void imprimeCartela() {
		montaEmpresa();
   		grava();
		Imprimir.relatorio(pathI);
   	}

	private void montaEmpresa() {
		empresa = empService.findById(numEmp);
		linhaNome = empresa.getNomeEmp();
		linhaTelMailPix = "Pix: " + empresa.getPixEmp();
		linhaFiscal = "**** Não tem valor fiscal ***";
	}
	
	List<CartelaVirtual> listVir = new ArrayList<>();

 	public void grava() {
			try {	
				BufferedWriter bwC = new BufferedWriter(new FileWriter(pathI));
 					{	cartela = carService.findById(numCar);
						linha02 = String.format("Local: %s", cartela.getLocalCar()) +
								  String.format("%s", "       ") + 
						          String.format("%s%s", "Data: ", sdf.format(cartela.getDataCar()));
 					 	linha03 = "                               ";
					 	linha04 = "*******************************"; 
					 	linha05 = String.format("%s", "Prod           Qtd Preço  Valor");
					 	bwC.newLine();
					 	bwC.write(linhaNome);
					 	bwC.newLine();
					 	bwC.newLine();
						bwC.write(linha02);
					 	bwC.newLine();
						bwC.write(linha03);
					 	bwC.newLine();
						bwC.write(linha04);
					 	bwC.newLine();
						bwC.write(linha05);
					 	bwC.newLine();
						bwC.write(linha04);
					 	bwC.newLine();
 					}	
					String[] nomeV = new String[30];
					Double[] qtdV = new Double[30]; 
					Double[] vdaV = new Double[30]; 
					Double[] totV = new Double[30]; 

					for (int i = 0; i < 30 ; i++) {
						nomeV[i] = null;
					}
 					
					int tem = 0;
					int ii = 0;
					int iii = 0;
					List<CartelaVirtual> listVir = virService.findCartela(numCar);
					for (CartelaVirtual v : listVir) {
						nomeProd = v.getNomeProdVir();
						for (int i = 1 ; i < 30 ; i++) {
							if (nomeV[i] ==
									null) {
								ii = i;
								i = 31;
							} else {			
								String nome = nomeV[i];
								if (nome.equals(nomeProd)) {
									qtdV[i] += v.getQuantidadeProdVir();
									vdaV[i] = v.getVendaProdVir();
									totV[i] += v.getTotalProdVir();
									i = 31;
								}	
							}
						}
							for (int i = 1 ; i < 30 ; i++) {
								if (nomeV[ii] == null) {
									nomeV[ii] = nomeProd;
									qtdV[ii] = v.getQuantidadeProdVir();
									vdaV[ii] = v.getVendaProdVir();
									totV[ii] = v.getTotalProdVir();	
									i = 31;
								}
							}	
					}	
					for (int i = 1 ; i < 30 ; i++) {
						if (nomeV[i] != null) {
							nomeProd = nomeV[i];
							if (nomeProd.length() > 10) {
								nomeProd = nomeProd.substring(0, 10);
							}
							quantidade = qtdV[i];
							venda = vdaV[i];
							total = totV[i];
							String qtdFd = FormataGabarito.formataQtd5(quantidade);
							String precoFd = FormataGabarito.formataQtd5(venda);
							String totalFd = FormataGabarito.formataQtd5(total);
							totalCar += total;
							linha06 = String.format("%-13s", nomeProd + " ") +
							  String.format("%s%s", qtdFd, df.format(quantidade) + " ") +
							  String.format("%s%s", precoFd, df.format(venda) + "  ") +
							  String.format("%s%s", totalFd, df.format(total));
							bwC.write(linha06); 
							bwC.newLine();
						}
					}	
					cartela.calculaTotalCar(listVir);
					if (cartela.getDescontoCar() > 0.00 || 
							cartela.getValorServicoCar() > 0.00) {
						String subTotalFd = FormataGabarito.
								formataDouble(cartela.getSubTotalCar());
						subTotalCar = cartela.getSubTotalCar();
						linha07 = String.format("%-19s%s%s", 
							"(=)SubTotal", subTotalFd, df.format(subTotalCar));
						bwC.write(linha07);
						bwC.newLine();
					}	
					if (cartela.getValorServicoCar() > 0.00) {
						String valorServicoFd = FormataGabarito.
								formataDouble(cartela.getValorServicoCar());
						servicoCar = cartela.getValorServicoCar();
						linha08 = 
							String.format("%-19s%s%s", 
									"(+)Serviço ", valorServicoFd, df.format(servicoCar)); 
						bwC.write(linha08);
						bwC.newLine();
					}
					if (cartela.getDescontoCar() > 0.00) {
						String descontoFd = FormataGabarito.
								formataDouble(cartela.getDescontoCar());
						descontoCar = cartela.getDescontoCar();
						linha09 = 
							String.format("%-19s%s%s", "(-)Desconto", 
									descontoFd, df.format(descontoCar));	
						bwC.write(linha09);
						bwC.newLine();
					}	
					String totalFd = FormataGabarito.formataDouble(cartela.getTotalCar());
					totalCar = cartela.getTotalCar();
					linha10 = 
						String.format("%-19s%s%s", "Total   ", 
								totalFd, df.format(totalCar));
					bwC.write(linha10);
					bwC.newLine();
					bwC.write(linha03);
					bwC.newLine();
					bwC.write(linhaTelMailPix);
					bwC.newLine();
					bwC.write(linhaFiscal);
					bwC.newLine();
					bwC.write(linha03);
					bwC.newLine();
					bwC.write(linha03);
					bwC.newLine();
					bwC.close();
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
