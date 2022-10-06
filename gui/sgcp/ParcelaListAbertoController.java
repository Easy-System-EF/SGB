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
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.exception.ValidationException;

public class ParcelaListAbertoController implements Initializable, DataChangeListener {

	@FXML
	private Label labelTitulo;

	@FXML
	private TableView<Parcela> tableViewParcela;

	@FXML
	private TableColumn<Parcela, String> tableColumnFornecedor;

	@FXML
	private TableColumn<Parcela, Integer> tableColumnNNF;

	@FXML
	private TableColumn<Parcela, Date> tableColumnVencimento;

	@FXML
	private TableColumn<Parcela, Integer> tableColumnParcela;

	@FXML
	private TableColumn<Parcela, Double> tableColumnValor;

	@FXML
	private TableColumn<Parcela, Double> tableColumnJuros;

	@FXML
	private TableColumn<Parcela, Double> tableColumnDesconto;

	@FXML
	private TableColumn<Parcela, Double> tableColumnAPagar;

 	@FXML
	private TableColumn<Parcela, Double> tableColumnTotal;

	@FXML
	private TableColumn<Parcela, Parcela> tableColumnEDITA;

	@FXML
	private Button btPeriodo;

	@FXML
	private Button btFornecedor;

	@FXML
	private Button btTipo;

	@FXML
	private Label labelUser;

	/*
	 * parametros de find... na updateTable 1 = geral aberto 2 = geral pago
	 */
	/*
	 * periodo, fornecedor, tipo p = periodo aberto q = periodo pago f = fornecedor
	 * aberto g = fornecedor pago t = tipo aberto u = tipo pago
	 */

	public String user= "";
	String classe = "Compromissos a Pagar ";
	String nomeTitulo = "Consulta Contas a Pagar";
	public static Integer codigo = null;
 	char opcao = 'o';
	int tipo = 0;

// carrega aqui Updatetableview (metodo)
	private ObservableList<Parcela> obsListPar;

// injeção de dependenia sem implementar a classe (instanciar)
// acoplamento forte - implementa via set

	private ParcelaService parService;
	Parcela parcela = new Parcela();

	public void setParcela(Parcela parcela) {
		this.parcela = parcela;
	}

// 	busca os dados no bco de dados	
	public void setParcelaService(ParcelaService parService) {
		this.parService = parService;
	}

// 	busca os dados no bco de dados	
	public void setFornecedorService(FornecedorService forService) {
		this.forService = forService;
	}

	private FornecedorService forService;
	Fornecedor fornecedor = new Fornecedor();

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public void setTipoConsumoService(TipoConsumoService tipoService) {
		this.tipoService = tipoService;
	}

	private TipoConsumoService tipoService;
	TipoConsumo tipofornecedor = new TipoConsumo();

	public void setTipoFornecedor(TipoConsumo tipofornecedor) {
		this.tipofornecedor = tipofornecedor;
	}

	private ParPeriodoService perService;
	ParPeriodo parPeriodo = new ParPeriodo();

	public void setParPeriodoService(ParPeriodoService perService) {
		this.perService = perService;
	}

	public void setParPeriodo(ParPeriodo parPeriodo) {
		this.parPeriodo = parPeriodo;
	}

	/*
	 * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
	 * com currentStage - janela pai - parentstage vamos abrir o forn form
	 */

 	ParPeriodo obj1 = new ParPeriodo();
	Fornecedor obj3 = new Fornecedor();
	TipoConsumo obj4 = new TipoConsumo();
 	
	@FXML
	public void onBtPeriodoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
// instanciando novo obj  e injetando via
 		setOpcao('p');
		nomeTitulo = "Consulta Contas a Pagar no Periodo";
  		createDialogForms("/gui/sgcp/ParPeriodoForm.fxml", obj1, obj3, obj4, (parentStage), 
  				(ParPeriodoFormController contP) -> {
		contP.setPeriodo(obj1, obj1);
		contP.setPeriodoService(new ParPeriodoService());
   		});
 	}

 	public void onBtFornecedorAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event); 
  		setOpcao('f');
		nomeTitulo = "Consulta Contas a Pagar por Fornecedor";
  		createDialogForms("/gui/sgcp/ParFornecedorForm.fxml", obj1, obj3, obj4, (parentStage), 
  				(ParFornecedorFormController contF) -> {
			contF.setFornecedor(obj3);
			contF.setService(new FornecedorService());
 		});
  		updateTableViewAberto();
	}
 	
	@FXML
	public void onBtTipoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
// instanciando novo obj  e injetando via
 		setOpcao('t');
		nomeTitulo = "Consulta Contas a Pagar por Tipo";
  		createDialogForms("/gui/sgcp/ParTipoForm.fxml", obj1, obj3, obj4, (parentStage), 
  				(ParTipoFormController contT) -> {
  		contT.setPeriodo(obj1, obj1);
  		contT.setTipoConsumo(obj4);
  		contT.setPeriodoService(new ParPeriodoService(), new TipoConsumoService());
  		});
  		updateTableViewAberto();
  		}
  
	// inicializar as colunas para iniciar nossa tabela initializeNodes
	@Override
	public void initialize(URL url, ResourceBundle rb) {
 		initializeNodes();
	}

// comportamento padrão para iniciar as colunas 	
	private void initializeNodes() {  
  		labelTitulo.setText(String.format("%s ", nomeTitulo));
  		tableColumnFornecedor.setCellValueFactory(new PropertyValueFactory<>("nomeFornecedorPar"));
		tableColumnNNF.setCellValueFactory(new PropertyValueFactory<>("nnfPar"));
		tableColumnVencimento.setCellValueFactory(new PropertyValueFactory<>("dataVencimentoPar"));
		Utils.formatTableColumnDate(tableColumnVencimento, "dd/MM/yyyy");
		tableColumnParcela.setCellValueFactory(new PropertyValueFactory<>("numeroPar"));
		tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valorPar"));
		Utils.formatTableColumnDouble(tableColumnValor, 2);
		tableColumnJuros.setCellValueFactory(new PropertyValueFactory<>("jurosPar"));
		Utils.formatTableColumnDouble(tableColumnJuros, 2);
		tableColumnDesconto.setCellValueFactory(new PropertyValueFactory<>("descontoPar"));
		Utils.formatTableColumnDouble(tableColumnDesconto, 2);
		tableColumnAPagar.setCellValueFactory(new PropertyValueFactory<>("totalPar"));
		Utils.formatTableColumnDouble(tableColumnAPagar, 2);
 		tableColumnTotal.setCellValueFactory(new PropertyValueFactory<>("ResultadoParStr"));
//		Utils.formatTableColumnDouble(tableColumnTotal, 2);
		// para tableview preencher o espaço da tela scroolpane, referencia do stage
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewParcela.prefHeightProperty().bind(stage.heightProperty());
	}
	
 	char setOpcao(char letra) {
		return opcao = letra;
	}
	
 	/*
	 * carregar o obsList para atz tableview tst de segurança p/ serviço vazio
	 * criando uma lista para receber os s instanciando o obsList acrescenta o botao
	 * edit e remove
	 */

	public void updateTableViewAberto() {
		if (opcao == 'o') {
			porPeriodo();
		}
		if (tipo == 1) 
		{	nomeTitulo = "Consulta Contas a Pagar";
		}
		labelUser.setText(user);
  		somaTotal();
		tableViewParcela.setItems(obsListPar);
		initEditButtons();
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
			for (ParPeriodo pe : listPerio) {
				per.setFornecedor(pe.getFornecedor());
				per.setTipoConsumo(pe.getTipoConsumo());
			}
			per.setIdPeriodo(1);
			per.setDtiPeriodo(dti);
			per.setDtfPeriodo(dtf);
			perService.update(per);
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("ParseException ", "Erro Data ", e.getMessage(), AlertType.ERROR);
		}
	}

	private void somaTotal() {
		double soma = 0.00;
		int codFor = 0;
		int codTipo = 0;
		List<ParPeriodo> listPer = perService.findAll();
		for (ParPeriodo per : listPer) {
			codFor = per.getFornecedor().getCodigo();
			codTipo = per.getTipoConsumo().getCodigoTipo();
		}

		List<Parcela> list = new ArrayList<>();
		if (opcao == 'o') {
			list = parService.findAllAberto();
	 		if (list.size() == 0) {
				Alerts.showAlert("Parcela ", null, "Não há parcela (período) em aberto ", AlertType.INFORMATION);
	 		}
		}
 		if (opcao == 'p') {
			list = parService.findPeriodoAberto();
	 		if (list.size() == 0) {
				Alerts.showAlert("Parcela ", null, "Não há parcela (periodo) em aberto ", AlertType.INFORMATION);
	 		}
		}
 		if (opcao == 'f') {
 			if (codigo != null) {
 				list = parService.findByIdFornecedorAberto(codigo);
 				if (list.size() == 0) {
 					Alerts.showAlert("Parcela ", null, "Não há parcela (fornecedor) em aberto ", AlertType.INFORMATION);
 					codigo = null;
 				}
 			}	
		}
 		if (opcao == 't') {
			list = parService.findByIdTipoAberto(codTipo);
	 		if (list.size() == 0) {
				Alerts.showAlert("Parcela ", null, "Não há parcela (tipo) em aberto ", AlertType.INFORMATION);
	 		}
 		}
 		
 		DecimalFormat df = new DecimalFormat("##,##0.00");
 	   	String resultadoParStr = ""; 

   		for (Parcela p : list)
		{	if (p.getResultadoPar() == null)
			{	p.setResultadoPar(0.00);
			}
			soma = soma + p.getTotalPar();
			p.setResultadoPar(soma);
  	 	   	resultadoParStr = df.format(p.getResultadoPar());
 	 	   	p.setResultadoParStr(resultadoParStr);
  		}
		obsListPar = FXCollections.observableArrayList(list);
	}

	/*
	 * parametro informando qual stage criou essa janela de dialogo - stage parent
	 * nome da view - absolutename carregando uma janela de dialogo modal (só sai
	 * qdo sair dela, tem q instaciar um stage e dps a janela dialog
	 */
    	private synchronized <T> void createDialogForms(String absoluteName, ParPeriodo obj1, 
   			Fornecedor obj3, TipoConsumo obj4, Stage parentStag, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			if (opcao == 'f') {
				ParFornecedorFormController controllerF = loader.getController();
 				controllerF.loadAssociatedObjects();
				controllerF.subscribeDataChangeListener(this);
				controllerF.updateFormData();
			}
			if (opcao == 'p') {
				ParPeriodoFormController controllerP = loader.getController();;
 				controllerP.loadAssociatedObjects();
				controllerP.subscribeDataChangeListener(this);
				controllerP.updateFormData();
			}	
 			if (opcao == 't') {
				ParTipoFormController controllerT = loader.getController();
				controllerT.loadAssociatedObjects();
				controllerT.subscribeDataChangeListener(this);
				controllerT.updateFormData();
   			}	

 			Stage dialogStage = new Stage();
			if (opcao == 'f') {
				dialogStage.setTitle("Selecione Fornecdor                                             ");
				classe = "Fornecedor ";
			}
			if (opcao == 't') {
				dialogStage.setTitle("Selecione Tipo                                             ");
				classe = "Tipo Fornecedor ";
			}
			if (opcao == 'p') {
				dialogStage.setTitle("Selecione Periodo                                             ");
				classe = "Periodo ";
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
 	
	private void createDialogForm(Parcela obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Compromisso a Pagar ";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			// referencia para o controlador = controlador da tela carregada fornListaForm
			ParcelaFormController controller = loader.getController();
// injetando passando parametro obj 			
			controller.setParcela(obj);
			controller.setParcelaService(new ParcelaService());

// injetando tb o forn service vindo da tela de formulario fornform
			controller.loadAssociatedObjects();
			// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo ->
			// onDataChangeList...
			controller.subscribeDataChangeListener(this);

//	carregando o obj no formulario (fornecedorFormControl)	
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Baixa Compromisso                                             ");
			dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
//			dialogStage.setMaximized(true); 
// quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
// travada enquanto não sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		} catch (ParseException p) {
			p.printStackTrace();
		}
	}

//  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
 		labelTitulo.setText(String.format(nomeTitulo));
		updateTableViewAberto();
	}
 
	/*
	 * metodo p/ botao edit do frame ele cria botão em cada linha o cell instancia e
	 * cria
	 */
	private void initEditButtons() {
		tableColumnEDITA.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDITA.setCellFactory(param -> new TableCell<Parcela, Parcela>() {
			private final Button button = new Button("edita");

			@Override
			protected void updateItem(Parcela obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);

				button.setOnAction(event -> createDialogForm(obj, "/gui/sgcp/ParcelaForm.fxml", Utils.currentStage(event)));
			}
		});
	}
}