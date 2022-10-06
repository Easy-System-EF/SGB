package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgb;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.MesAno;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.AnosService;
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
 
public class ComissaoListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private AdiantamentoService service;
	private Adiantamento adiantamento;
	
	@FXML
 	private TableView<Adiantamento> tableViewComissao;

	@FXML
	private Label labelTitulo;
	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Adiantamento, String> tableColumnFuncionarioAdi;

 	@FXML
 	private TableColumn<Adiantamento, String> tableColumnCartelaAdi;

 	@FXML
 	private TableColumn<Adiantamento, Date> tableColumnDataAdi;

   	@FXML
 	private TableColumn<Adiantamento, String> tableColumnCargoAdi;
 	
   	@FXML
   	private TableColumn<Adiantamento, String> tableColumnSituacaoAdi;
   	
   	@FXML
   	private TableColumn<Adiantamento, Double> tableColumnValorAdi;
   	
   	@FXML
   	private Button btMesesCom;
   	 	
	@FXML
	private Label labelUser;

	public String user = "";
 	 		
// carrega aqui os dados Updatetableview (metodo)
 	private ObservableList<Adiantamento> obsList;
 
// auxiliar
 	String classe = "Adiantamento";
 	static Integer mesConsulta = null;
 	static Integer anoConsulta = null; 	
 	
// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setServices(AdiantamentoService service) {
 		this.service = service;
 	}
 	
 	public void setAdiantamento(Adiantamento adiantamento) {
 		this.adiantamento = adiantamento;
 	}

	@FXML
	public void onBtMesesComAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		MesAno obj = new MesAno();
		classe = "Meses ";
  		createDialogOpcao("/gui/sgb/MesAnoForm.fxml", parentStage, (MesAnoFormController contM) -> {
			contM.setMesAno(obj);
			contM.setServices(new MesesService(),
							  new AnosService(),
							  new AdiantamentoService());
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
		labelTitulo.setText("Comissão");
		tableColumnFuncionarioAdi.setCellValueFactory(new PropertyValueFactory<>("NomeAdi"));
		tableColumnCartelaAdi.setCellValueFactory(new PropertyValueFactory<>("CartelaAdi"));
		tableColumnDataAdi.setCellValueFactory(new PropertyValueFactory<>("dataAdi"));
		Utils.formatTableColumnDate(tableColumnDataAdi, "dd/MM/yyyy");
		tableColumnCargoAdi.setCellValueFactory(new PropertyValueFactory<>("CargoAdi"));
		tableColumnSituacaoAdi.setCellValueFactory(new PropertyValueFactory<>("SituacaoAdi"));
		tableColumnValorAdi.setCellValueFactory(new PropertyValueFactory<>("comissaoAdi"));
		Utils.formatTableColumnDouble(tableColumnValorAdi, 2);
  		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
 		tableViewComissao.prefHeightProperty().bind(stage.heightProperty());
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
 		List<Adiantamento> list = new ArrayList<>();
 		if (mesConsulta != null) {
 			list = service.findMesTipo(mesConsulta, anoConsulta, "C");
 		} 		
//		List<Adiantamento> newList = list.stream()
//				. filter(x -> x.getTipoAdi() != "A")
//				. collect(Collectors.toList());
		
  		obsList = FXCollections.observableArrayList(list);
  		tableViewComissao.setItems(obsList);
	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
