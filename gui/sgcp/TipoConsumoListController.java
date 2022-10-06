package gui.sgcp;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgb;
import db.DbException;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entites.TipoConsumo;
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
 
public class TipoConsumoListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private TipoConsumoService service;
	
	@FXML
 	private TableView<TipoConsumo> tableViewTipoConsumo;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<TipoConsumo, Integer>  tableColumnCodigoTipo;
 	
 	@FXML
 	private TableColumn<TipoConsumo, String> tableColumnNomeTipo;

  	@FXML
 	private TableColumn<TipoConsumo, TipoConsumo> tableColumnEDITA;

 	@FXML
 	private TableColumn<TipoConsumo, TipoConsumo> tableColumnREMOVE ;

 	@FXML
 	private Button btNew;

	@FXML
	private Label labelUser;

	String classe = "Tipo Consumo ";
	public String user = "usuário";	
	
 	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<TipoConsumo> obsList;
 
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
  	@FXML
  	public void onBtNewAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 TipoConsumo obj = new TipoConsumo();
 		 createDialogForm(obj, "/gui/sgcp/TipoConsumoForm.fxml", parentStage);
// 		System.out.println("novo");
  	}
 	
// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setTipoConsumoService(TipoConsumoService service) {
 		this.service = service;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnCodigoTipo.setCellValueFactory(new PropertyValueFactory<>("codigoTipo"));;
		tableColumnNomeTipo.setCellValueFactory(new PropertyValueFactory<>("nomeTipo"));
// para tableview preencher o espaço da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewTipoConsumo.prefHeightProperty().bind(stage.heightProperty());
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
		List<TipoConsumo> list = service.findAll();
 		obsList = FXCollections.observableArrayList(list);
		tableViewTipoConsumo.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (só sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/ 	
	private void createDialogForm(TipoConsumo obj, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			TipoConsumoFormController controller = loader.getController();
			controller.user = user;
// injetando passando parametro obj 			
			controller.setTipoConsumo(obj);
// injetando tb o forn service vindo da tela de formulario fornform
			controller.setTipoConsumoService(new TipoConsumoService());
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Tipo Consumo                                             ");
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
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
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
		  tableColumnEDITA.setCellFactory(param -> new TableCell<TipoConsumo, TipoConsumo>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(TipoConsumo obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      } 
		 
		      setGraphic(button); 
		      button.setOnAction( 
		      event -> createDialogForm( 
		        obj, "/gui/sgcp/TipoConsumoForm.fxml",Utils.currentStage(event)));
 		    } 
		  }); 
		}
	

	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<TipoConsumo, TipoConsumo>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(TipoConsumo obj, boolean empty) { 
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

	private void removeEntity(TipoConsumo obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir");
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serviço está vazio");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbException e) {
				Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
 			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
