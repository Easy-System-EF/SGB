package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.MainSgb;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.GrupoService;
import gui.sgbmodel.service.ProdutoService;
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
 
public class ProdutoListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private ProdutoService service;
	private GrupoService gruService;
	
	@FXML
 	private TableView<Produto> tableViewProduto;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Produto, Integer>  tableColumnCodigoProd;
 	
 	@FXML
 	private TableColumn<Produto, String> tableColumnNomeProd;

   	@FXML
 	private TableColumn<Produto, Double> tableColumnSaldoProd;
 	
   	@FXML
 	private TableColumn<Produto, Double> tableColumnVendaProd;
 	
   	@FXML
 	private TableColumn<Produto, Double> tableColumnCmmProd;
 	
  	@FXML
 	private TableColumn<Produto, Produto> tableColumnEDITA;

 	@FXML
 	private TableColumn<Produto, Produto> tableColumnREMOVE ;

 	@FXML
 	private Button btNewProd;

	@FXML
	private Label labelUser;

	// Auxiliar
 	String classe = "Produto";
	public String user = "usuário";		
 	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Produto> obsList;
 
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */

 	@FXML
  	public void onBtNewProdAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 Produto obj = new Produto();
 		 createDialogForm(obj, "/gui/sgb/ProdutoForm.fxml", parentStage);
   	}
 	
// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setProdutoService(ProdutoService service) {
 		this.service = service;
 	}

 	public void setGrupoService(GrupoService gruService) {
 		this.gruService = gruService;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnCodigoProd.setCellValueFactory(new PropertyValueFactory<>("codigoProd"));
  		tableColumnNomeProd.setCellValueFactory(new PropertyValueFactory<>("nomeProd"));
		tableColumnSaldoProd.setCellValueFactory(new PropertyValueFactory<>("saldoProd"));
		Utils.formatTableColumnDouble(tableColumnSaldoProd, 2);
		tableColumnVendaProd.setCellValueFactory(new PropertyValueFactory<>("vendaProd"));
		Utils.formatTableColumnDouble(tableColumnVendaProd, 2);
 		tableColumnCmmProd.setCellValueFactory(new PropertyValueFactory<>("cmmProd"));
 		Utils.formatTableColumnDouble(tableColumnCmmProd, 2);
 		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewProduto.prefHeightProperty().bind(stage.heightProperty());
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
		List<Produto> list = service.findAll();
 		obsList = FXCollections.observableArrayList(list);
		tableViewProduto.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (só sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/

	private void createDialogForm(Produto obj, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			ProdutoFormController controller = loader.getController();
			controller.user = user;
 // injetando passando parametro obj 			
			controller.setProduto(obj);
 // injetando tb o forn service vindo da tela de formulario fornform
			controller.setProdutoService(new ProdutoService());
			controller.setGrupoService(new GrupoService());
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Produto                                             ");
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
		  tableColumnEDITA.setCellFactory(param -> new TableCell<Produto, Produto>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(Produto obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      } 
		 
		      setGraphic(button); 
		      button.setOnAction( 
		      event -> createDialogForm( 
		        obj, "/gui/sgb/ProdutoForm.fxml",Utils.currentStage(event)));
 		    } 
		  }); 
		}
	

	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<Produto, Produto>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(Produto obj, boolean empty) { 
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

	private void removeEntity(Produto obj) {
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
