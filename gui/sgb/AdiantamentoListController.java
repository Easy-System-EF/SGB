package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.MainSgb;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.service.AdiantamentoService;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
public class AdiantamentoListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private AdiantamentoService service;
	
	@FXML
 	private TableView<Adiantamento> tableViewAdiantamento;

	@FXML
	private Label labelTitulo;
	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Adiantamento, Integer> tableColumnNumeroAdi;

 	@FXML
 	private TableColumn<Adiantamento, String> tableColumnNomeFunAdi;

 	@FXML
 	private TableColumn<Adiantamento, Date> tableColumnDataAdi;

   	@FXML
 	private TableColumn<Adiantamento, String> tableColumnCargoAdi;
 	
   	@FXML
   	private TableColumn<Adiantamento, String> tableColumnSituacaoAdi;
   	
   	@FXML
   	private TableColumn<Adiantamento, Double> tableColumnValeAdi;
   	
 	@FXML
 	private TableColumn<Adiantamento, Adiantamento> tableColumnEDITA;

 	@FXML
 	private Button btNewAdi;

   	@FXML
   	private Button btMeses;
   	 	
 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	String classe = "Adiantamento";
 	String tipo = "A";
 	public static Integer numEmp = null;

// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Adiantamento> obsList;
 
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
 	@FXML
  	public void onBtNewAdiAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
         classe = "Adiantamento ";
 		 Adiantamento obj = new Adiantamento();
 		 createDialogForm(obj, "/gui/sgb/AdiantamentoForm.fxml", parentStage);
   	}

// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setAdiantamentoService(AdiantamentoService service) {
 		this.service = service;
 	}

 	public void setTipo(String tipo) {
 		this.tipo = tipo;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
		labelTitulo.setText("Adiantamento");
		tableColumnNumeroAdi.setCellValueFactory(new PropertyValueFactory<>("NumeroAdi"));
		tableColumnNomeFunAdi.setCellValueFactory(new PropertyValueFactory<>("nomeAdi"));
		tableColumnDataAdi.setCellValueFactory(new PropertyValueFactory<>("dataAdi"));
		Utils.formatTableColumnDate(tableColumnDataAdi, "dd/MM/yyyy");
		tableColumnCargoAdi.setCellValueFactory(new PropertyValueFactory<>("cargoAdi"));
		tableColumnSituacaoAdi.setCellValueFactory(new PropertyValueFactory<>("situacaoAdi"));
		tableColumnValeAdi.setCellValueFactory(new PropertyValueFactory<>("ValeAdi"));
		Utils.formatTableColumnDouble(tableColumnValeAdi, 2);
  		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
 		tableViewAdiantamento.prefHeightProperty().bind(stage.heightProperty());
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
 	 	Date dataHoje = new Date();
 		Calendar cal = Calendar.getInstance();
 		cal.setTime(dataHoje);
 		int mesHj = cal.get(Calendar.MONTH) + 1;
 		int anoHj = cal.get(Calendar.YEAR);
 		List<Adiantamento> list = new ArrayList<>();
		list = service.findMesTipo(mesHj, anoHj, tipo);
  		obsList = FXCollections.observableArrayList(list);
  		tableViewAdiantamento.setItems(obsList);
 		initEditButtons();
	}
 	
/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (só sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/
	private void createDialogForm(Adiantamento obj, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			AdiantamentoFormController controller = loader.getController();
			controller.user = user;
			controller.numEmp = numEmp;
 // injetando passando parametro obj 			
			controller.setAdiantamento(obj);
// injetando tb o forn service vindo da tela de formulario fornform
			controller.setServices(new AdiantamentoService(), new FuncionarioService());
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Adiantamento                                             ");
 			dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
// quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
// travada enquanto não sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela" + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}

/* * metodo p/ botao edit do frame
 * ele cria botão em cada linha 
 * o cell instancia e cria
*/  
	private void initEditButtons() {
		  tableColumnEDITA.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnEDITA.setCellFactory(param -> new TableCell<Adiantamento, Adiantamento>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(Adiantamento obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      } 
		 
		      setGraphic(button); 
		      button.setOnAction( 
		      event -> createDialogForm( 
		        obj, "/gui/sgb/AdiantamentoForm.fxml",Utils.currentStage(event)));
 		    } 
		  }); 
		}	
}
