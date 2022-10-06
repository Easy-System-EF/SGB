
package gui.sgcp;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.MainSgb;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entites.Compromisso;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.service.CompromissoService;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
public class CompromissoListController implements Initializable, DataChangeListener {

	@FXML
 	private TableView<Compromisso> tableViewCompromisso;

  	@FXML
 	private TableColumn<Compromisso, String>  tableColumnFornecedorCom;
 	
 	@FXML
 	private TableColumn<Compromisso, Integer>  tableColumnNnfCom;

  	@FXML
 	private TableColumn<Compromisso, Date>  tableColumnDataCom;

  	@FXML
 	private TableColumn<Compromisso, Date>  tableColumnDataVencimentoCom;

 	@FXML
 	private TableColumn<Compromisso, Double>  tableColumnValorCom;

 	@FXML
 	private TableColumn<Compromisso, Integer>  tableColumnParcelaCom;

 	@FXML
 	private TableColumn<Compromisso, Integer>  tableColumnPrazoCom;

 	@FXML
 	private TableColumn<Compromisso, Compromisso> tableColumnEDITA;

 	@FXML
 	private TableColumn<Compromisso, Compromisso> tableColumnREMOVE ;

 	@FXML
 	private Button btNew;

	@FXML
	private Label labelUser;

	String classe = "Compromisso ";
	public String user = "usuário";	
	
	Locale ptBR = new Locale("pt", "BR");
 
// injeção de dependenia sem implementar a classe (instanciar)
// acoplamento forte - implementa via set
	private CompromissoService service;
	private FornecedorService forService;
	private CompromissoFormController comFormController;
  	
 // carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Compromisso> obsList;

  	Parcela parcela = new Parcela();
	private ParcelaService parService;

  	public void setParcela (Parcela parcela) {
  		this.parcela = parcela;
 	}
 
// 	busca os dados no bco de dados	
 	public void setParcelaService (ParcelaService parService) {
  		this.parService = parService;
 	}

//// carregando parcelas, se houver 	
// 	public void setParcela(Parcela parcela) {
// 		this.parcela = parcela;
// 	}
// 
  /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
   */

 	@FXML
  	public void onBtNewAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj compromisso e injetando via
 		 Compromisso obj = new Compromisso();
   		 createDialogForm(obj, "/gui/sgcp/CompromissoForm.fxml", parentStage);
   	}
 	  	
 // injeta a dependencia com set (inversão de controle de injeçao)	
  	public void setCompromissoService(CompromissoService service) {
  		this.service = service;
  	}
   	
 // injeta a dependencia com set (inversão de controle de injeçao)	
  	public void setFornecedorService(FornecedorService forService) {
  		this.forService = forService;
  	}
   	
 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
 			initializeNodes();
  	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
 		labelUser.setText(user);
 		tableColumnFornecedorCom.setCellValueFactory(new PropertyValueFactory<>("nomeFornecedorCom"));
		tableColumnNnfCom.setCellValueFactory(new PropertyValueFactory<>("nnfCom"));
		tableColumnDataCom.setCellValueFactory(new PropertyValueFactory<>("dataCom"));
 		Utils.formatTableColumnDate(tableColumnDataCom, "dd/MM/yyyy");
 		tableColumnDataVencimentoCom.setCellValueFactory(new PropertyValueFactory<>("dataVencimentoCom"));
 		Utils.formatTableColumnDate(tableColumnDataVencimentoCom, "dd/MM/yyyy");
 		tableColumnValorCom.setCellValueFactory(new PropertyValueFactory<>("valorCom"));
		Utils.formatTableColumnDouble(tableColumnValorCom, 2);
  		tableColumnParcelaCom.setCellValueFactory(new PropertyValueFactory<>("parcelaCom"));
		tableColumnPrazoCom.setCellValueFactory(new PropertyValueFactory<>("prazoCom"));
 // para tableview preencher o espaço da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewCompromisso.prefHeightProperty().bind(stage.heightProperty());
 	}
 
/* 	
 * carregar o obsList para atz tableview	
 * tst de segurança p/ serviço vazio
 *  criando uma lista para receber os  s
 *  instanciando o obsList
 *  acrescenta o botao edit e remove
 */  
 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Serviço está vazio");
 		}
 		labelUser.setText(user);
  		List<Compromisso> list = service.findAll();
   		obsList = FXCollections.observableArrayList(list);
 		tableViewCompromisso.setItems(obsList);
   		initEditButtons();
		initRemoveButtons();
  	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (só sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/ 	
	private void createDialogForm(Compromisso obj, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			CompromissoFormController controller = loader.getController();
			controller.user = user;
// injetando passando parametro obj 			
			controller.setCompromisso(obj);
  			
// injetando tb o forn service vindo da tela de formulario fornform
			controller.setServices(new CompromissoService(), new FornecedorService(), 
					new TipoConsumoService(), new ParcelaService(), new ParPeriodoService());
			controller.loadAssociatedObjects();
  // inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);

//	carregando o obj no formulario (fornecedorFormControl)	
			controller.updateFormData();
 			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Compromisso ");
 			dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
// quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
// travada enquanto não sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
		catch (ParseException p) {
			p.printStackTrace();
		}
 	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	/*
	 * metodo p/ botao edit do frame
	 * ele cria botão em cada linha 
	 * o cell instancia e cria
	 */  
		private void initEditButtons() {
			  tableColumnEDITA.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
			  tableColumnEDITA.setCellFactory(param -> new TableCell<Compromisso, Compromisso>() { 
			    private final Button button = new Button("edita"); 
			 
			    @Override 
			    protected void updateItem(Compromisso obj, boolean empty) { 
			      super.updateItem(obj, empty); 
			 
			      if (obj == null) { 
			        setGraphic(null); 
			        return; 
			      } 
 			      setGraphic(button); 

 			      button.setOnAction( 
			      event -> createDialogForm( 
			        obj, "/gui/sgcp/CompromissoForm.fxml",Utils.currentStage(event)));
	 		    } 
			  }); 
			}
		

		private void initRemoveButtons() {		
			  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
			  tableColumnREMOVE.setCellFactory(param -> new TableCell<Compromisso, Compromisso>() { 
			        private final Button button = new Button("exclui"); 
			 
			        @Override 
			        protected void updateItem(Compromisso obj, boolean empty) { 
			            super.updateItem(obj, empty); 
			 
			            if (obj == null) { 
			                setGraphic(null); 
			                return; 
			            } 
			 
			            setGraphic(button);
 			            button.setOnAction(event -> removeEntity(obj)); 
			        } 
			    });
	 		} 

		int flagd = 0;
		private void removeEntity(Compromisso obj) {
			conferePagamento(flagd, obj);
 			if (flagd == 0)
			{	Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir");
				if (result.get() == ButtonType.OK) {
					if (service == null) {
						throw new IllegalStateException("Serviço está vazio");
					}
					try {
			 			if 	(flagd == 0)
			 			{	parcela.setNnfPar(obj.getNnfCom());
							parcela.setCodigoFornecedorPar(obj.getCodigoFornecedorCom());
							parService.removeNnf(parcela);
							service.remove(obj);
							updateTableView();
						}
 					}
					catch (DbIntegrityException e) {
						Alerts.showAlert("Erro removendo objeto compromisso ", null, e.getMessage(), AlertType.ERROR);
					}
				}
			}
		}
		
 		private Integer conferePagamento(Integer flag, Compromisso obj) {
			flagd = 0;
   			List<Parcela> list = parService.findAllPago();
  			List<Parcela> st = list.stream()
 				.filter(p -> p.getNnfPar() == (obj.getNnfCom()))
 				.filter(p -> p.getCodigoFornecedorPar() == (obj.getCodigoFornecedorCom()))  
 				.filter(p -> p.getPagoPar() > 0)
  				.collect(Collectors.toList());	
   			if (st.size() > 0)
  			{	Alerts.showAlert("Erro!!! ", "Exclusão inválida ", "Existe(m) parcela(s) paga(s) para o Compromisso!!!", AlertType.ERROR);
				flagd = 1;
   			}	
    			return flagd;
		}	
 }