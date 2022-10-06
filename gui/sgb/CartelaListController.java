package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgb;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaPagante;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Empresa;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.CartelaPaganteService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.EmpresaService;
import gui.sgbmodel.service.FuncionarioService;
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
 
public class CartelaListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private CartelaService service;
	private EmpresaService empService;
	CartelaVirtual objVir = new CartelaVirtual();
	CartelaPagante objPag = new CartelaPagante();
	Adiantamento objAdi = new Adiantamento();
	Funcionario objFun = new Funcionario();
	

	@FXML
 	private TableView<Cartela> tableViewCartela;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Cartela, Integer>  tableColumnNumeroCar;
 	
 	@FXML
 	private TableColumn<Cartela, Date>  tableColumnDataCar;
 	
 	@FXML
 	private TableColumn<Cartela, String> tableColumnLocalCar;

   	@FXML
 	private TableColumn<Cartela, Double> tableColumnTotalCar;
 	
  	@FXML
 	private TableColumn<Cartela, Cartela> tableColumnEDITA;

 	@FXML
 	private TableColumn<Cartela, Cartela> tableColumnREMOVE ;

 	@FXML
 	private TableColumn<Cartela, Cartela> tableColumnList ;

 	@FXML
 	private Button btNewCar;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	String classe = "Cartela ";
 	public static Integer numEmp = 0;
 	public static Integer nivel = 0;
 	
 	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Cartela> obsList;

 /* 
   * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
	@FXML
  	public void onBtNewCarAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 Cartela obj = new Cartela();
 		 createDialogForm(obj, objVir, objPag, objAdi, objFun,  "/gui/sgb/CartelaForm.fxml", parentStage);
 		 initializeNodes();
   	}
 	
// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setCartelaService(CartelaService service) {
 		this.service = service;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnNumeroCar.setCellValueFactory(new PropertyValueFactory<>("numeroCar"));
		tableColumnDataCar.setCellValueFactory(new PropertyValueFactory<>("dataCar"));
		Utils.formatTableColumnDate(tableColumnDataCar, "dd/MM/yyyy");
   		tableColumnLocalCar.setCellValueFactory(new PropertyValueFactory<>("localCar"));
 		tableColumnTotalCar.setCellValueFactory(new PropertyValueFactory<>("totalCar"));
		Utils.formatTableColumnDouble(tableColumnTotalCar, 2);
 		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewCartela.prefHeightProperty().bind(stage.heightProperty());
 	}

/* 	
 * carregar o obsList para atz tableview	
 * tst de segurança p/ serviço vazio
 *  criando uma lista para receber os services
 *  instanciando o obsList
 *  acrescenta o botao edit e remove
 */  
 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Serviço está vazio");
 		}
 		labelUser.setText(user);
		List<Cartela> list = service.findSituacao("A");
 		obsList = FXCollections.observableArrayList(list);
		tableViewCartela.setItems(obsList);
		notifyDataChangeListerners();
		initEditButtons();
		initRemoveButtons();
		initListButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (só sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/
	private void createDialogForm(Cartela obj, CartelaVirtual objVir, CartelaPagante objPag,
				Adiantamento objAdi, Funcionario objFun, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			classe = "Cartela";
// referencia para o controlador = controlador da tela carregada fornListaForm			
			CartelaFormController controller = loader.getController();
 // injetando passando parametro obj
			controller.setCartelas(obj, objVir, objPag, objAdi, objFun);
			if (obj.getSituacaoCar() == "P") {
				Alerts.showAlert("Cartela fechada ", "Concluído - Sem acesso ", null, AlertType.ERROR);
			}
			else {
 // injetando serviços vindo da tela de formulario fornform
			controller.setServices(new CartelaService(), 
									new CartelaVirtualService(),
									new CartelaPaganteService(),
									new AdiantamentoService(),
									new FuncionarioService());
			controller.user = user;
			controller.local = obj.getLocalCar();
			controller.situacao = "A";
			controller.nivel = nivel;
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
				controller.updateTableView();
				controller.updateFormData();
			
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Digite Cartela                                             ");
				dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
				dialogStage.setResizable(false);
// quem e o stage pai da janela?
				dialogStage.initOwner(parentStage);
// travada enquanto não sair da tela
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.showAndWait();
			}	
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 

	// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
		private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
		private void notifyDataChangeListerners() {
			for (DataChangeListener listener : dataChangeListeners) {
				listener.onDataChanged();
			}
		}

	//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
			// * recebe o evento e inscreve na lista
		public void subscribeDataChangeListener(DataChangeListener listener) {
				dataChangeListeners.add(listener);
		}
		
	//  atualiza minha lista dataChanged com dados novos 	
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
		  tableColumnEDITA.setCellFactory(param -> new TableCell<Cartela, Cartela>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(Cartela obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		      if (obj == null) { 
				 setGraphic(null); 
				 return; 
		      }
		 
		      setGraphic(button); 
		      button.setOnAction( 
					event -> createDialogForm(obj, objVir, objPag, objAdi, objFun,
							"/gui/sgb/CartelaForm.fxml", Utils.currentStage(event)));
		    }
		  }); 
		}
	
	private void initListButtons() {
		  tableColumnList.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnList.setCellFactory(param -> new TableCell<Cartela, Cartela>() { 
		  private final Button button = new Button("lista");
		 
		  @Override 
		  protected void updateItem(Cartela obj, boolean empty) { 
		     super.updateItem(obj, empty); 
		     if (obj == null) { 
		    	  setGraphic(null); 
		    	  return; 
		     }
		     Empresa emp = new Empresa();
		     CartelaVirtual objVir = new CartelaVirtual();
		     setGraphic(button); 
		     button.setOnAction( 
	    	  event -> imprimeCartela(obj, objVir, emp, obj.getNumeroCar()));
		  }
		  }); 
		}
	
	private void imprimeCartela(Cartela obj, CartelaVirtual objVir, Empresa emp, 
 			Integer codCar) {
		CartelaImprimeController cartelaImpr = new CartelaImprimeController();
		cartelaImpr.setCartela(obj, objVir, emp);
		cartelaImpr.numEmp = numEmp;
		cartelaImpr.numCar = obj.getNumeroCar();
		cartelaImpr.setCartelaService(new CartelaService(),
						 new CartelaVirtualService(),
						 new EmpresaService());
		cartelaImpr.imprimeCartela();
	}	
	
	private void initRemoveButtons() {
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<Cartela, Cartela>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(Cartela obj, boolean empty) { 
		            super.updateItem(obj, empty); 
		 
		            if (obj == null) { 
		                setGraphic(null); 
		                return; 
		            } 
		 
		            setGraphic(button); 
		            button.setOnAction(eivent -> removeEntity(obj));
		    		
		        } 
		    });
 		} 

	private void removeEntity(Cartela obj) {
   		if (nivel > 1 && nivel < 9) {
   			Alerts.showAlert(null,"Atenção", "Operação não permitida", AlertType.INFORMATION);
   		} else {
   			Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
   			if (result.get() == ButtonType.OK) {
   				if (service == null) {
   					throw new IllegalStateException("Serviço está vazio");
   				}
   				try {
   					service.remove(obj);
   					updateTableView();
   				}
   				catch (DbIntegrityException e) {
   					Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
   				}
   			}
   		}
	}
}	