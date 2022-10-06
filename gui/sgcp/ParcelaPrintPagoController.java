package gui.sgcp;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgb;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.service.EmpresaService;
import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.entites.TipoConsumo;
import gui.sgcpmodel.entites.consulta.ParPeriodo;
import gui.sgcpmodel.service.FornecedorService;
import gui.sgcpmodel.service.ParPeriodoService;
import gui.sgcpmodel.service.ParcelaService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.exception.ValidationException;
 
public class ParcelaPrintPagoController implements Initializable, DataChangeListener {

	@FXML
	private Label labelTitulo;
	
	@FXML
 	private TableView<Parcela> tableViewParcelaPago;

  	@FXML
 	private TableColumn<Parcela, String>  tableColumnFornecedorPago;
 	
 	@FXML
 	private TableColumn<Parcela, Integer>  tableColumnNNFPago;

  	@FXML
 	private TableColumn<Parcela, Date>  tableColumnVencimentoPago;

  	@FXML
 	private TableColumn<Parcela, Integer>  tableColumnParcelaPago;

   	@FXML
 	private TableColumn<Parcela, Double>  tableColumnValorPago;

 	@FXML
 	private TableColumn<Parcela, Double>  tableColumnJurosPago;

 	@FXML
 	private TableColumn<Parcela, Double>  tableColumnDescontoPago;

 	@FXML
 	private TableColumn<Parcela, Double>  tableColumnAPagarPago;

 	@FXML
 	private TableColumn<Parcela, Double>  tableColumnPagoPago;

  	@FXML
 	private TableColumn<Parcela, Date>  tableColumnPagamentoPago;

 	@FXML
 	private TableColumn<Parcela, Double>  tableColumnTotalPago;

   	@FXML
 	private Button btPeriodoPago;

   	@FXML
 	private Button btFornecedorPago;

  	@FXML
 	private Button btTipoPago;

  	@FXML
  	private Button btImprimePago;

	@FXML
	private Label labelNada;

  	/*  	
* parametros de find... na updateTable
* 2 = geral pago
*/
/* periodo, fornecedor, tipo
* b = pago geral 	
* q = periodo pago
* g = fornecedor pago
* u = tipo pago
*/
	public String user = "";
	public static Integer codigo = null;
	public static Integer numEmp = null;
  	String nomeTitulo = "Lista Contas Pagas ";
  	String classe = "Compromisso Pago ";
  	char opcao = 'o';
  	int tipo = 0; 
	int codFor = 0;
	int codTipo = 0;
 	
// carrega aqui Updatetableview (metodo)
   	private ObservableList<Parcela> obsListPar;
  
// injeção de dependenia sem implementar a classe (instanciar)
// acoplamento forte - implementa via set
 	
	private ParcelaService parService;
 	Parcela parcela = new Parcela();
 	
  	public void setParcela (Parcela parcela) {
  		this.parcela = parcela;
 	}
 
// 	busca os dados no bco de dados	
 	public void setParcelaService (ParcelaService parService) {
  		this.parService = parService;
 	}

// 	busca os dados no bco de dados	
 	public void setFornecedorService (FornecedorService forService) {
  		this.forService = forService;
 	}

 	private FornecedorService forService;
 	Fornecedor fornecedor = new Fornecedor();
 	 
  	public void setFornecedor (Fornecedor fornecedor) {
  		this.fornecedor = fornecedor;
 	}
 
 	public void setTipoConsumoService (TipoConsumoService tipoService) {
  		this.tipoService = tipoService;
 	}

 	private TipoConsumoService tipoService;
 	TipoConsumo tipoConsumo = new TipoConsumo();
 	 
  	public void setTipoConsumo (TipoConsumo tipoConsumo) {
  		this.tipoConsumo = tipoConsumo;
 	}
 
 	private ParPeriodoService perService;
 	ParPeriodo parPeriodo = new ParPeriodo();
 	
	public void setParPeriodoService(ParPeriodoService perService) {
   		this.perService = perService;
 	}

  	public void setParPeriodo (ParPeriodo parPeriodo) {
  		this.parPeriodo = parPeriodo;
 	}

    /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
   */

 	ParPeriodo obj1 = new ParPeriodo();
	Fornecedor obj3 = new Fornecedor();
	TipoConsumo obj4 = new TipoConsumo();
 	
	@FXML
	public void onBtPeriodoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
// instanciando novo obj  e injetando via
 		setOpcao('q');
		nomeTitulo = "Lista Contas Pagas no Periodo";
  		createDialogForms("/gui/sgcp/ParPeriodoForm.fxml", obj1, obj3, obj4, (parentStage), 
  				(ParPeriodoFormController contP) -> {
		contP.setPeriodo(obj1, obj1);
		contP.setPeriodoService(new ParPeriodoService());
  		contP.loadAssociatedObjects();
  		contP.subscribeDataChangeListener(this);
  		contP.updateFormData();
   		});
 	}
 
 	public void onBtFornecedorAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event); 
  		setOpcao('g');
		nomeTitulo = "Lista Contas Pagas por Fornecedor";
  		createDialogForms("/gui/sgcp/ParFornecedorForm.fxml", obj1, obj3, obj4, (parentStage), 
  				(ParFornecedorFormController contF) -> {
			contF.setFornecedor(obj3);
			contF.setService(new FornecedorService());
	  		contF.loadAssociatedObjects();
	  		contF.subscribeDataChangeListener(this);
	  		contF.updateFormData();
 		});
	}
 	
	@FXML
	public void onBtTipoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
// instanciando novo obj  e injetando via
 		setOpcao('u');
		nomeTitulo = "Lista Contas Pagas por Tipo";
  		createDialogForms("/gui/sgcp/ParTipoForm.fxml", obj1, obj3, obj4, (parentStage), 
  				(ParTipoFormController contP) -> {
  		contP.setPeriodo(obj1, obj1);
  		contP.setTipoConsumo(obj4);
  		contP.setPeriodoService(new ParPeriodoService(), new TipoConsumoService());
  		contP.loadAssociatedObjects();
  		contP.subscribeDataChangeListener(this);
  		contP.updateFormData();
  		});
  	}
  
   	@FXML
  	public void onBtImprimeAction(ActionEvent event) {
  		 Stage parentStage = Utils.currentStage(event);
  		 Parcela obj = new Parcela();
  		 imprimePago(codFor, codTipo, opcao, obj);
  		}
 
	@FXML
  	public void imprimePago(Integer CodFor, Integer codTipo, char op, Parcela obj) {
		ParcelaImprimePagoController controller = new ParcelaImprimePagoController();
		controller.setParcela(obj);
		controller.setServices(new ParcelaService(), new EmpresaService());
		controller .setOpcao(op);
		controller.setcodTipo(codTipo);
		controller.setFor(CodFor);
		controller.numEmp = numEmp;
		controller.onBtImprimePago();
	}
 	// inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
 		initializeNodes();
 	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
   		labelTitulo.setText(String.format(nomeTitulo));
  		tableColumnFornecedorPago.setCellValueFactory(new PropertyValueFactory<>("nomeFornecedorPar"));
 		tableColumnNNFPago.setCellValueFactory(new PropertyValueFactory<>("nnfPar"));
		tableColumnVencimentoPago.setCellValueFactory(new PropertyValueFactory<>("dataVencimentoPar"));
 		Utils.formatTableColumnDate(tableColumnVencimentoPago, "dd/MM/yyyy");
		tableColumnParcelaPago.setCellValueFactory(new PropertyValueFactory<>("numeroPar"));
		tableColumnValorPago.setCellValueFactory(new PropertyValueFactory<>("valorPar"));
		Utils.formatTableColumnDouble(tableColumnValorPago, 2);
   		tableColumnJurosPago.setCellValueFactory(new PropertyValueFactory<>("jurosPar"));
		Utils.formatTableColumnDouble(tableColumnJurosPago, 2);
  		tableColumnDescontoPago.setCellValueFactory(new PropertyValueFactory<>("descontoPar"));
		Utils.formatTableColumnDouble(tableColumnDescontoPago, 2);
  		tableColumnAPagarPago.setCellValueFactory(new PropertyValueFactory<>("totalPar"));
		Utils.formatTableColumnDouble(tableColumnAPagarPago, 2);
  		tableColumnPagoPago.setCellValueFactory(new PropertyValueFactory<>("pagoPar"));
		Utils.formatTableColumnDouble(tableColumnPagoPago, 2);
		tableColumnPagamentoPago.setCellValueFactory(new PropertyValueFactory<>("dataPagamentoPar"));
 		Utils.formatTableColumnDate(tableColumnPagamentoPago, "dd/MM/yyyy");
   		tableColumnTotalPago.setCellValueFactory(new PropertyValueFactory<>("ResultadoParStr"));
//		Utils.formatTableColumnDouble(tableColumnTotalPago, 2);
   // para tableview preencher o espaço da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewParcelaPago.prefHeightProperty().bind(stage.heightProperty());
    	}

   	char setOpcao(char letra) {
 		return opcao = letra;
 	}
 
/* 	
 * carregar o obsList para atz tableview	
 * tst de segurança p/ serviço vazio
 *  criando uma lista para receber os  s
 *  instanciando o obsList
 *  acrescenta o botao edit e remove
 */  
 	
 	public void updateTableViewPago() {
 		if (parService == null) {
 			throw new IllegalStateException("Serviço está vazio");
 		}
 		labelNada.setText(user);
 		ValidationException exception = new ValidationException("Validation error");
 		if (opcao == 'o')
   		{	porPeriodo();	
   		}
  		somaTotal();
   		tableViewParcelaPago.setItems(obsListPar);
     	}
 	
 	private void porPeriodo() {
 		try {
 			SimpleDateFormat sdfi = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 			SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 			Date dti = sdfi.parse("01/01/2001 00:00:00");  
 			Date dtf = sdff.parse("31/01/2041 00:00:00");

 			ParPeriodo per = new ParPeriodo();
 			per = parPeriodo;
 			List<ParPeriodo> listPerio = perService.findAll();
 			for (ParPeriodo pe : listPerio)
 			{	per.setFornecedor(pe.getFornecedor());
 				per.setTipoConsumo(pe.getTipoConsumo());
 			}
 			per.setIdPeriodo(1);
 			per.setDtiPeriodo(dti);
 			per.setDtfPeriodo(dtf);
 			perService.update(per);
  		}	catch (ParseException e) {
 			e.printStackTrace();
 			Alerts.showAlert("ParseException ", "Erro Data ", e.getMessage(), AlertType.ERROR);
 		}
   	}

 	private void somaTotal() {
  		double soma = 0.00;
  		List<ParPeriodo> listPer = perService.findAll();
		for (ParPeriodo per : listPer)
		{	codFor = per.getFornecedor().getCodigo();
		    codTipo = per.getTipoConsumo().getCodigoTipo();
		}    
 		List<Parcela> list = new ArrayList<>();
  		if (opcao == 'o')
		{	list = parService.findAllPago();
		}	 
  		if (opcao == 'q')
 		{	list = parService.findPeriodoPago();
 		}
  		if (opcao == 'g') {
  			if (codigo == null) {
  		 		list = parService.findByIdFornecedorPago(codFor);
  			} else {
  				list = parService.findByIdFornecedorPago(codFor);
  			}
  		}	
 		if (opcao == 'u')
		{	list = parService.findByIdTipoPago(codTipo);
		}

 		if (list.size() == 0) {
			Alerts.showAlert("Parcela ", null, "Não há parcela paga ", AlertType.INFORMATION);
 		}

 		DecimalFormat df = new DecimalFormat("##,##0.00");
 	   	String resultadoParStr = ""; 

   		for (Parcela p : list)
		{	if (p.getResultadoPar() == null)
			{	p.setResultadoPar(0.00);
			}
			soma = soma + p.getPagoPar();
 			p.setResultadoPar(soma);
  	 	   	resultadoParStr = df.format(p.getResultadoPar());
 	 	   	p.setResultadoParStr(resultadoParStr);
 		}
   		
		obsListPar = FXCollections.observableArrayList(list);
    }

	private synchronized <T> void createDialogForms(String absoluteName, ParPeriodo obj1, 
   			Fornecedor obj3, TipoConsumo obj4, Stage parentStag, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			if (opcao == 'g') {
				classe= "Fornecedor ";
				dialogStage.setTitle("Selecione Fornecdor                                             ");
			}
			if (opcao == 'u') {
				classe = "Tipo Fornecedor";
				dialogStage.setTitle("Selecione Tipo                                             ");
			}
			if (opcao == 'q') {
				classe = "Período ";
				dialogStage.setTitle("Selecione Periodo                                             ");
			}
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStag);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
	}
 
//  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
   		labelTitulo.setText(String.format(nomeTitulo));
 		updateTableViewPago();
	}
} 