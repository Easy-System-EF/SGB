package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgb;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Anos;
import gui.sgbmodel.entities.FolhaMes;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Meses;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.AnosService;
import gui.sgbmodel.service.FolhaMesService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.MesesService;
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
 
public class FolhaMesListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private FolhaMesService service;
	
	@FXML
 	private TableView<FolhaMes> tableViewFolha;

	@FXML
	private Label labelTitulo;
	
// c/ entidade e coluna	
	
 	@FXML
 	private TableColumn<FolhaMes, String> tableColumnFuncionarioFolha;

   	@FXML
 	private TableColumn<FolhaMes, String> tableColumnCargoFolha;
 	
   	@FXML
   	private TableColumn<FolhaMes, String> tableColumnSituacaoFolha;
   	
   	@FXML
   	private TableColumn<FolhaMes, Double> tableColumnSalarioFolha;
   	
   	@FXML
   	private TableColumn<FolhaMes, Double> tableColumnComissaoFolha;
   	
   	@FXML
   	private TableColumn<FolhaMes, Double> tableColumnAdiantamentoFolha;

   	@FXML
   	private TableColumn<FolhaMes, Double> tableColumnReceberFolha;   	
   	
   	@FXML
   	private TableColumn<FolhaMes, Double> tableColumnAPagarFolha;   	
   	
   	@FXML
   	private Button btMeses;
   	 	
	@FXML
	private Label labelUser;

	public String user = "";
 	 		
// carrega aqui lista Updatetableview (metodo)
 	private ObservableList<FolhaMes> obsList;
 
 	public static String mes = "";
 	
// auxiliar
 	String classe = "FolhaMes ";
 	 
 	// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setServices(FolhaMesService service) {
 		this.service = service;
 	}
 	
	@FXML
	public void onBtMesesAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		FolhaMes obj = new FolhaMes();
		Meses objMes = new Meses();
		Anos objAno = new Anos();
		Funcionario objFun = new Funcionario();
		classe = "Meses ";
  		createDialogOpcao("/gui/sgb/FolhaMesForm.fxml", parentStage, (FolhaMesFormController contM) -> {
			contM.setFolhaMes(obj, objMes, objAno, objFun);		
			contM.setServices(new FolhaMesService(),
						      new AdiantamentoService(),
						      new FuncionarioService(),
							  new MesesService(),
							  new AnosService());
			contM.zeraMes();
			contM.loadAssociatedObjects();
			contM.updateFormData();
 		});
		updateTableView();
  		initializeNodes();
	}
 	 	
 	private <T> void createDialogOpcao(String absoluteName, Stage parentStage, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe, e.getMessage(), AlertType.ERROR);
		}
	}
 	
 	 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
		labelTitulo.setText("Folha Mes" + mes);
		tableColumnFuncionarioFolha.setCellValueFactory(new PropertyValueFactory<>("FuncionarioFolha"));
		tableColumnCargoFolha.setCellValueFactory(new PropertyValueFactory<>("CargoFolha"));
		tableColumnSituacaoFolha.setCellValueFactory(new PropertyValueFactory<>("SituacaoFolha"));
		tableColumnSalarioFolha.setCellValueFactory(new PropertyValueFactory<>("SalarioFolha"));
		tableColumnComissaoFolha.setCellValueFactory(new PropertyValueFactory<>("ComissaoFolha"));
		tableColumnAdiantamentoFolha.setCellValueFactory(new PropertyValueFactory<>("ValeFolha"));
		tableColumnReceberFolha.setCellValueFactory(new PropertyValueFactory<>("ReceberFolha"));
		tableColumnAPagarFolha.setCellValueFactory(new PropertyValueFactory<>("TotalFolha"));
  		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
 		tableViewFolha.prefHeightProperty().bind(stage.heightProperty());
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
			throw new IllegalStateException("Serviço Folha está vazio");
 		}
 		labelUser.setText(user);
		classe = "Folha Folha";
		List<FolhaMes> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewFolha.setItems(obsList);
	}
 	
 	public void zeraMes() {
 		service.zeraAll();
 	}

// *  atualiza minha lista dataChanged com Folha novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}

