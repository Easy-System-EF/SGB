package gui.sgcp;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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

import gui.sgbmodel.entities.Empresa;
import gui.sgbmodel.service.EmpresaService;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.service.ParcelaService;
import gui.util.Imprimir;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ParcelaImprimePagoController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

 	public static Integer numEmp = null;
	Locale localeBr = new Locale("pt", "BR");
 	DecimalFormat df = new DecimalFormat("#,###,##0.00"); 
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date data1 = new Date();
	String dtpI;
	String dtpF;

	int contf = 0;
	int contl = 77;
	int i = 0;
	int flag = 0;
 
	double totVlr = 0.00;
	double totJur = 0.00;
	double totDes = 0.00;
	double totPag = 0.00;
 
 	String dth = sdf.format(data1);
 	String nomeEmp = null;
	String linha01 = "";
 	String linha02 = "";
	String linha03 = "******************************************************************";
	String linha04 = "Fornecedor                                  Nnf Parcela Vencimento";
	String linha05 = "       Valor        Juros     Desconto        Pagto Data Pagamento";
	String linha51 = "";
	String linha52 = "";
	String linha06 = "------------------------------------------------------------------";
	String linha07 = "";

	String pathI = "C:\\Arqs\\impr\\Pago.txt";

	Parcela parcela = new Parcela();
	private ParcelaService parService;
	private Empresa empresa;
	private EmpresaService empService;

 	private char opcao;
	private Integer codFor;
	private Integer codTipo;
	
	List<Parcela> list = new ArrayList<>();
 	
   	char setOpcao(char letra) {
 		return opcao = letra;
 	}

  	int setFor(Integer num) {
 		return codFor = num;
 	}
  	
  	int setcodTipo(Integer num) {
 		return codTipo = num;
 	}
  	
  	
	public void setParcela(Parcela parcela) {
		this.parcela = parcela;
	}

	public void setServices(ParcelaService parService, EmpresaService empService) {
		this.parService = parService;
		this.empService = empService;
	}

	@FXML
	public void onBtImprimePago() {
		flag = 0;  
		empresa = empService.findById(numEmp);
		nomeEmp = empresa.getNomeEmp();
  		grava();
  		Imprimir.relatorio(pathI);
   	}

 	public void grava() {
	 		try	{	BufferedWriter bwP = new BufferedWriter(new FileWriter(pathI));
	 				if (flag == 0) {
 						if (contl == 77) {
 		 					list = listagem(parcela, codFor, codTipo, opcao);	
 						}
 	 	 				for (Parcela p : list) { 
 	 						i += 1;
 	 	 					if (i == 1) {
 	 	 						linha02 = "Contas Pagas";
 	 	 						if (opcao == 'q') {
 	 	 							dtpI = sdf.format(p.getPeriodo().getDtiPeriodo());
 	 	 							dtpF = sdf.format(p.getPeriodo().getDtfPeriodo());
 	 	 							linha02 = String.format("%s", "Contas Pagas por período: ") +
 	 	 									String.format("%s", dtpI);
 					                      	String.format("%s", " a ");
 					                      	String.format("%s", dtpF);
 	 	 						}         	
 	 	 						if (opcao == 'g') {
 	 	 							linha02 = "Contas pagas por Fornecedor";
 	 	 						}	
 	 	 						if (opcao == 'u') {
 	 	 							linha02 = String.format("%s", "Contas Pagas por Tipo: ") +
 	 	 									  String.format("%s", p.getTipoFornecedor().getNomeTipo());
 	 	 						}
 	 	 					}
 				 			if (contl > 49 && flag == 0) {
 				 				bwP.newLine();
 			 					cabecalho();
  	 							bwP.write(linha01);
  				 				bwP.newLine();
  	 							bwP.write(linha02);
  				 				bwP.newLine();
  				 				bwP.newLine();
  	 							bwP.write(linha03);
  				 				bwP.newLine();
  	 							bwP.write(linha04);
  				 				bwP.newLine();
  				 				bwP.write(linha05);
  				 				bwP.newLine();
  	 							bwP.write(linha03);
  				 				bwP.newLine();
 		 						contl = 14;
 			 				}
 	 						
			 				linha51 = String.format("%-40s", p.getNomeFornecedorPar()) +
			 						  String.format("%7d", p.getNnfPar()) +
			 						  String.format("%8d", p.getNumeroPar()) +
			 						  String.format("%11s", sdf.format(p.getDataVencimentoPar()));
			 						
			 				String esp = " ";
			 				linha52 = String.format("%12s%s", df.format(p.getValorPar()), esp) +
			 						  String.format("%12s%s", df.format(p.getJurosPar()), esp) +
			 						  String.format("%12s%s", df.format(p.getDescontoPar()), esp) +
			 						  String.format("%12s", df.format(p.getPagoPar())) +
			 						  String.format("%15s", sdf.format(p.getDataPagamentoPar()));

							totVlr += p.getValorPar();
							totJur += p.getJurosPar();
							totDes += p.getDescontoPar();
							totPag += p.getPagoPar();

							bwP.write(linha51);
							bwP.newLine();
							bwP.write(linha52);
							bwP.newLine();
							bwP.write(linha06);
							bwP.newLine();
 			 				contl += 7;

		 					if (i == list.size()) {
		 						flag = 1;
//		 						linha06 = "-----------------------------Total--------------------------------";
	 	 						linha07 = String.format("%12s%s", df.format(totVlr), esp) +
				 						  String.format("%12s%s", df.format(totJur), esp) +
				 						  String.format("%12s%s", df.format(totDes), esp) +
				 						  String.format("%12s", df.format(totPag));
	 	 						bwP.write(linha06);
	 	 						bwP.newLine();
 	 	 						bwP.write(linha07);
 	 	 						bwP.newLine();
 			 				}	
	 					}
	 				}
 					bwP.close();
	 			}	
	 			catch(IllegalArgumentException e1) {
	 				System.out.println(e1.getMessage());
	 			}	
	 			catch(	IOException e2) {
					System.out.println(e2.getMessage());	 			
				}
 			}

	private void cabecalho() {
			contf += 01;
			linha01 = String.format("%-30s", nomeEmp) +
					  String.format("%s", "Data: ") + 
					  String.format("%-24s", dth) +
					  String.format("%s", "Fl- ") +
					  String.format("%2s", contf);
	}

	private List <Parcela> listagem(Parcela parcela, Integer codFor, Integer codTipo, char opcao) {
   		if (opcao == 'o') {
 			list = parService.findAllPago();
  		}
  		if (opcao == 'q') {
 			list = parService.findPeriodoPago();
 		}
  		if (opcao == 'g') {
 		 	list = parService.findByIdFornecedorPago(codFor);
 		}
  		if (opcao == 'u') {
			list = parService.findByIdTipoPago(codTipo);
		}
 		return list;
	}	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}
