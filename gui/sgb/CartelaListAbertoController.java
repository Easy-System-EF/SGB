package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgb;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Anos;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaPagante;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Meses;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.AnosService;
import gui.sgbmodel.service.CartelaPaganteService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.MesesService;
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

public class CartelaListAbertoController implements Initializable, DataChangeListener {

	@FXML
	private TableView<Cartela> tableViewCartela;

	@FXML
	private Label labelTitulo;

	@FXML
	private TableColumn<Cartela, Integer> tableColumnNumeroCartela;

	@FXML
	private TableColumn<Cartela, Date> tableColumnDataCartela;

	@FXML
	private TableColumn<Cartela, String> tableColumnLocalCartela;

	@FXML
	private TableColumn<Cartela, Double> tableColumnTotalCartela;

	@FXML
	private TableColumn<Cartela, String> tableColumnNomeSituacaoCartela;

	@FXML
	private TableColumn<Cartela, String> tableColumnObsCartela;

	@FXML
	private TableColumn<Cartela, Cartela> tableColumnEDITA;

	@FXML
	private Button btMesAno;

	@FXML
	private Label labelUser;

	public String user = "";

// auxiliar 	
	String classe = "Cartela";
	String nomeTitulo = "Cartela em  aberto";
	public static int mm = 0;
	public static int aa = 0;

// carrega aqui os dados Updatetableview (metodo)
	private ObservableList<Cartela> obsList;

	// injeção de dependenia sem implementar a classe (instanciat)
	// acoplamento forte - implementa via set
	private CartelaService service;

// injeta a dependencia com set (inversão de controle de injeçao)	
	public void setCartelaService(CartelaService service) {
		this.service = service;
	}

	@FXML
	public void onBtMesAnoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		nomeTitulo = "Cartela em Aberto ";
		Meses objMes = new Meses();
		Anos objAno = new Anos();
		classe = "Mes e Ano";
		createDialogOpcao(objMes, objAno, "/gui/sgb/CartelaMesAnoForm.fxml", (parentStage),
				(CartelaMesAnoFormController contP) -> {
					contP.setMesAno(objMes, objAno);
					contP.setServices(new MesesService(), new AnosService());
					contP.loadAssociatedObjects();
					contP.subscribeDataChangeListener(this);
					contP.updateFormData();
				});
		updateTableView();
	}

	// inicializar as colunas para iniciar nossa tabela initializeNodes
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

// comportamento padrão para iniciar as colunas 	
	private void initializeNodes() {
		labelTitulo.setText(String.format("%s ", nomeTitulo));
		tableColumnNumeroCartela.setCellValueFactory(new PropertyValueFactory<>("numeroCar"));
		tableColumnDataCartela.setCellValueFactory(new PropertyValueFactory<>("dataCar"));
		Utils.formatTableColumnDate(tableColumnDataCartela, "dd/MM/yyyy");
		tableColumnLocalCartela.setCellValueFactory(new PropertyValueFactory<>("localCar"));
		tableColumnTotalCartela.setCellValueFactory(new PropertyValueFactory<>("totalCar"));
		Utils.formatTableColumnDouble(tableColumnTotalCartela, 2);
		tableColumnNomeSituacaoCartela.setCellValueFactory(new PropertyValueFactory<>("nomeSituacaoCar"));
		tableColumnObsCartela.setCellValueFactory(new PropertyValueFactory<>("obsCar"));

		// para tableview preencher o espaço da tela scroolpane, referencia do stage
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewCartela.prefHeightProperty().bind(stage.heightProperty());
	}

	/*
	 * carregar o obsList para atz tableview tst de segurança p/ serviço vazio
	 * criando uma lista para Cartela os services instanciando o obsList acrescenta
	 * o botao edit e remove
	 */
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço  está vazio");
		}
		List<Cartela> list = new ArrayList<>();
		list = service.findSituacaoAberto(mm, aa, "A", "C");
		if (list.size() == 0 && mm > 0) {
			Alerts.showAlert("Cartela por período ", "Período ", "Não há cartela em aberto ", AlertType.INFORMATION);
		}
		labelTitulo.setText(String.format("%s ", nomeTitulo));
		obsList = FXCollections.observableArrayList(list);
		tableViewCartela.setItems(obsList);
		initEditButtons();
	}

	private synchronized <T> void createDialogOpcao(Meses objMes, Anos objAno, String absoluteName, Stage parentStag,
			Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			T cont = loader.getController();
			initializeAction.accept(cont);

			Stage dialogStage = new Stage();
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStag);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void createDialogForm(Cartela obj, CartelaVirtual objVir, CartelaPagante objPag, 
				Funcionario objFun, Adiantamento objAdi, String absoluteName,
			Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			classe = "Cartela";
//referencia para o controlador = controlador da tela carregada fornListaForm			
			CartelaFormController controller = loader.getController();
// injetando passando parametro obj
			controller.setCartelas(obj, objVir, objPag, objAdi, objFun);
			if (obj.getSituacaoCar() == "P") {
				Alerts.showAlert("Cartela fechada ", "Concluído - Sem acesso ", null, AlertType.ERROR);
			} else {
// injetando serviços vindo da tela de formulario fornform
				controller.setServices(new CartelaService(), new CartelaVirtualService(), 
						new CartelaPaganteService(), new AdiantamentoService(), new FuncionarioService());
				controller.user = user;
				controller.local = obj.getLocalCar();
				controller.situacao = "A";
//				controller.nivel = nivel;
				controller.loadAssociatedObjects();
//inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
				controller.subscribeDataChangeListener(this);
//carregando o obj no formulario (fornecedorFormControl)			
				controller.updateTableView();
				controller.updateFormData();

				Stage dialogStage = new Stage();
				dialogStage.setTitle("Digite Cartela                                             ");
				dialogStage.setScene(new Scene(pane));
//pode redimencionar a janela: s/n?
				dialogStage.setResizable(false);
//quem e o stage pai da janela?
				dialogStage.initOwner(parentStage);
//travada enquanto não sair da tela
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

// lista da classe subject (form) - guarda lista de obj p/ Cartela e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * ebe o evento e inscreve na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

//  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}

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

				if (obj.getSituacaoCar().contains("C")) {
					setGraphic(button);
					CartelaVirtual objVir = new CartelaVirtual();
					CartelaPagante objPag = new CartelaPagante();
					Funcionario objFun = new Funcionario();
					Adiantamento objAdi = new Adiantamento();
					button.setOnAction(event -> createDialogForm(obj, objVir, objPag, 
							objFun, objAdi, "/gui/sgb/CartelaForm.fxml",
						Utils.currentStage(event)));
				}
			}	
			
		});
	}
}
