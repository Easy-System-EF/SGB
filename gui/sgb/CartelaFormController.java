package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import application.MainSgb;
import db.DbException;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaPagante;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.CartelaPaganteService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.GrupoService;
import gui.sgbmodel.service.ProdutoService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Mascaras;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.exception.ValidationException;

/*
 * Monta formaulário do cartela e virtual
 */

public class CartelaFormController implements Initializable, DataChangeListener {

	private Cartela entity;
	private CartelaVirtual virtual;
	private CartelaPagante pagante;
	private Adiantamento adiantamento;
	private Funcionario funcionario;

	/*
	 * dependencia service com metodo set
	 */
	private CartelaService service;
	private CartelaVirtualService virService;
	private CartelaPaganteService pagService;
	private AdiantamentoService adiService;
	private FuncionarioService funService; 

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private GridPane gridPaneCar;

	@FXML
	private TextField textNumeroCar;

	@FXML
	private DatePicker dpDataCar;

	@FXML
	private TextField textLocalCar;

	@FXML
	private TextField textDescontoCar;

	@FXML
	private TextField textObsCar;

	@FXML
	private TextField textNumeroPaganteCar;

	@FXML
	private RadioButton rbServicoSimCar;

	@FXML
	private RadioButton rbServicoNaoCar;

	@FXML
	private TextField textServicoCar;

	@FXML
	private Label labelTotalCar;

	@FXML
	private Label labelValorPaganteCar;

	@FXML
	private TableView<CartelaVirtual> tableViewVir;

	@FXML
	private TableColumn<CartelaVirtual, String> tableColumnNomeProdVir;

	@FXML
	private TableColumn<CartelaVirtual, Double> tableColumnQtdProdVir;

	@FXML
	private TableColumn<CartelaVirtual, Double> tableColumnVendaProdVir;

	@FXML
	private TableColumn<CartelaVirtual, Double> tableColumnTotalProdVir;

	@FXML
	private TableColumn<CartelaVirtual, CartelaVirtual> tableColumnEditaVir;

	@FXML
	private TableColumn<CartelaVirtual, CartelaVirtual> tableColumnRemoveVir;

	@FXML
	private Button btSaveCar;

	@FXML
	private Button btCancelCar;

	@FXML
	private Button btFechaCar;

	@FXML
	private Button btCaloteCar;

	@FXML
	private Label labelErrorDataCar;

	@FXML
	private Label labelErrorLocalCar;

	@FXML
	private Label labelErrorServicoCar;

	@FXML
	private Button btNewVir;

	@FXML
	private Label labelUser;

	// auxiliar
	public static String situacao = null;
	public static Integer numPag = 0;
	public Integer nivel = 0;
	public String user = "usuário";
	public String local = null;
	String classe = "Cartela ";
	Integer numCar = 0;
	Integer flag = 0;
	int codFun = 0;
	double totProd = 0.00;
	Double vlr = null;

// aux total	
	String vlrTotMasc = "";
	String vlrPagMasc = "";

	private ObservableList<CartelaVirtual> obsListVir;

	@FXML
	public void onBtFechaAction(ActionEvent event) throws ParseException {
		confereTotal();
		String result = "sim";
		if (entity.getTotalCar() == 0.00) {
			Alerts.showAlert(null, "Atenção", "Cartela sem valor!!!", AlertType.ERROR);
			result = "nao";
		} else {
			if (entity.getLocalCar() == null) {
				Alerts.showAlert(null, "Atenção", "Não existe local!!!", AlertType.ERROR);
				result = "nao";
			}
		}
		if (entity.getNumeroPaganteCar() == 0) {
			Alerts.showAlert(null, "Atenção", "Informe número de pagante(s) e Ok", AlertType.INFORMATION);
			result = "nao";
		}
		if (result == "sim") {
			pagService.remove(numCar);
			numPag = 0;
			while (numPag < entity.getNumeroPaganteCar()) {
				numPag += 1;
				pagante = new CartelaPagante();
				Stage parentStage = Utils.currentStage(event);
				createDialogFormFecha(entity, pagante, "/gui/sgb/CartelaPaganteForm.fxml", parentStage);
			}	
		}	

		if (situacao == "P") {
			classe = "CartelaVirtual";
			List<CartelaVirtual> listVir = virService.findCartela(numCar);
			for (CartelaVirtual v : listVir) {
				virtual = v;
				virtual.setSituacaoVir("P");
			}
			virService.saveOrUpdate(virtual);
			comissaoAdi();
			AdiantamentoUpdate.updateSemSalario(adiantamento.getMesAdi(), adiantamento.getAnoAdi());
			entity.setSituacaoCar("P");
			entity.setNomeSituacaoCar("Pago");
			classe = "Cartela";
			service.saveOrUpdate(entity);
			notifyDataChangeListerners();
			Utils.currentStage(event).close();
		}
	}

	Map<Integer, Double> mapFun = new HashMap();
	
	private void comissaoAdi() {
		List<CartelaVirtual> list = virService.findCartela(numCar);
		for (CartelaVirtual cv : list) {
			codFun = cv.getFuncionario().getCodigoFun();
			if (mapFun.containsKey(codFun)) {
				vlr = mapFun.get(codFun);
				mapFun.put(codFun, vlr + cv.getTotalProdVir());
			} else {
				mapFun.put(codFun, cv.getTotalProdVir());
			}
		}
		
		for (Integer key : mapFun.keySet()) {
			vlr = mapFun.get(key);
			funcionario = funService.findById(key);
			adiantamento.setCodigoFun(funcionario.getCodigoFun());
			adiantamento.setNomeFun(funcionario.getNomeFun());
			adiantamento.setCargo(funcionario.getCargo());
			adiantamento.setSituacao(funcionario.getSituacao());
			
			adiantamento.setNumeroAdi(null);
			adiantamento.setDataAdi(entity.getDataCar());
			adiantamento.setValeAdi(0.00);
			adiantamento.setMesAdi(dpDataCar.getValue().getMonthValue());
			adiantamento.setAnoAdi(dpDataCar.getValue().getYear());
			adiantamento.setValorCartelaAdi(vlr);
			adiantamento.setCartelaAdi(numCar);
			adiantamento.setTipoAdi("C");
			adiantamento.setSalarioAdi(0.00);
			adiantamento.setNomeAdi(funcionario.getNomeFun());
			adiantamento.setCargoAdi(funcionario.getCargoFun());
			adiantamento.setSituacaoAdi(funcionario.getSituacaoFun());
			adiantamento.getComissaoAdi();
			adiService.saveOrUpdate(adiantamento);
		}
	}

	@FXML
	public void onBtCaloteAction(ActionEvent event) {
		if (nivel > 1 && nivel < 9) {
			Alerts.showAlert(null, "Atenção", "Operação não permitida", AlertType.INFORMATION);
		} else {
			if (entity != null) {
				confereTotal();
			}
			classe = "CartelaVirtual";
			List<CartelaVirtual> listVir = virService.findCartela(numCar);
			for (CartelaVirtual v : listVir) {
				virtual = v;
				virtual.setSituacaoVir("C");
				virService.saveOrUpdate(virtual);
			}
			entity.setSituacaoCar("C");
			entity.setNomeSituacaoCar("Calote");
			classe = "Cartela";
			service.saveOrUpdate(entity);
			notifyDataChangeListerners();
			Utils.currentStage(event).close();
		}
	}

	@FXML
	public void onBtNewVirAction(ActionEvent event) {
		if (numCar == null) {
			if (dpDataCar.getValue() == null || textLocalCar.getText() == null) {
				Alerts.showAlert("Atenção", "Preencha no mínimo", "data, local ", AlertType.ERROR);
			} else {
				newCartela();
				if (entity.getNumeroCar() == null) {
					Alerts.showAlert("Atenção", "Preencha no mínimo", "data, local ", AlertType.ERROR);
				}
			}
		}
		if (entity.getNumeroCar() != null) {
			Cartela car = service.findById(entity.getNumeroCar());
			local = car.getLocalCar();
			Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
			CartelaVirtual virtual = new CartelaVirtual();
			createDialogForm(virtual, "/gui/sgb/CartelaVirtualForm.fxml", parentStage);
		}
		confereTotal();
	}

	private void newCartela() {
		if (entity == null) {
			throw new IllegalStateException("Entidade cartela nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço cartela nulo");
		}
		try {
			Instant instant = Instant.from(dpDataCar.getValue().atStartOfDay(ZoneId.systemDefault()));
			entity.setDataCar(Date.from(instant));
			entity.setLocalCar(textLocalCar.getText());
			entity.setDescontoCar(0.00);
			entity.setTotalCar(0.00);
			entity.setSituacaoCar("A");
			entity.setNumeroPaganteCar(1);
			entity.setValorPaganteCar(0.00);
			entity.setMesCar(dpDataCar.getValue().getMonthValue());
			entity.setAnoCar(dpDataCar.getValue().getYear());
			entity.setObsCar("");
			entity.setServicoCar("Sem serviço");
			entity.setValorServicoCar(0.00);
			entity.setSubTotalCar(0.00);
			confereExiste();
			if (entity.getNumeroCar() == null) {
				if (entity.getLocalCar() != null) {
					service.saveOrUpdate(entity);
					numCar = entity.getNumeroCar();
					entity = service.findById(numCar);
					classe = "Cartela";
					service.saveOrUpdate(entity);
					notifyDataChangeListerners();
				}
			}
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void confereExiste() {
		ValidationException exception = new ValidationException("Validation exception");
		List<Cartela> cartelaLocal = service.findSituacao("A");
		if (cartelaLocal.size() > 0) {
			for (Cartela cl : cartelaLocal) {
				if (cl.getLocalCar().equals(entity.getLocalCar()) 
						&& cl.getSituacaoCar().equals(entity.getSituacaoCar())
						&& !cl.getNumeroCar().equals(entity.getNumeroCar())) {
					exception.addErros("local", "Local em uso");
					entity.setLocalCar(null);
					textLocalCar.setText(null);
					notifyDataChangeListerners();
					try {
						updateFormData();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (exception.getErros().size() > 0) {
			throw exception;
		}
	}

	public void setCartelas(Cartela entity, CartelaVirtual virtual, CartelaPagante pagante,
			Adiantamento adiantamento, Funcionario funcionario) {
		this.entity = entity;
		this.virtual = virtual;
		this.pagante = pagante;
		this.funcionario = funcionario;
		this.adiantamento = adiantamento;
	}

	// * metodo set /p service
	public void setServices(CartelaService service, CartelaVirtualService virService,
			CartelaPaganteService pagService, AdiantamentoService adiService,
			FuncionarioService funService) {
		this.service = service;
		this.virService = virService;
		this.pagService = pagService;
		this.adiService = adiService;
		this.funService = funService;
	}

	/*
	 * vamos instanciar um forn e salvar no bco de dados meu obj entity (lá em cima)
	 * vai receber uma chamada do getformdata metodo q busca dados do formulario
	 * convertidos getForm (string p/ int ou string) pegou no formulario e retornou
	 * (convertido) p/ jogar na variavel entity chamo o service na rotina saveupdate
	 * e mando entity vamos tst entity e service = null -> não foi injetado para
	 * fechar a janela, pego a referencia para janela atual (event) e close
	 * DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar
	 * uma lista qdo ela salvar obj com sucesso, é só notificar (juntar) recebe lá
	 * no listController
	 */
	@FXML
	public void onBtSaveCarAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade cartela nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço cartela nulo");
		}

		try {
			classe = "Cartela";
			entity = getFormData();
			confereTotal();
			classe = "Cartela";
			service.saveOrUpdate(entity);
			notifyDataChangeListerners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (ParseException p) {
			p.printStackTrace();
		}
	}

	private void confereTotal() {
		try {
			getFormData();
			if (entity.getNumeroCar() != null) {
				List<CartelaVirtual> listVir = virService.findCartela(numCar);
				if (listVir.size() > 0) {
					entity.calculaTotalCar(listVir); 
					entity.calculaValorPagante();
				} else {
					entity.setTotalCar(0.00);
					entity.setValorPaganteCar(0.00);
				}
				if	(numCar == entity.getNumeroCar()) {
					service.saveOrUpdate(entity);				
					if (entity.getTotalCar() > 0.00) {
						vlrTotMasc = Mascaras.formataValor(entity.getTotalCar());
						labelTotalCar.setText(vlrTotMasc);						
						vlrPagMasc = Mascaras.formataValor(entity.getValorPaganteCar());
						labelValorPaganteCar.setText(vlrPagMasc);
						updateFormData();			
						notifyDataChangeListerners();
					}	
				}
			}
		}	
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (ParseException p) {
			p.printStackTrace();
		}	
	}	

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

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se não for atz tb verificamos se cpos
	 * obrigatórios estão preenchidos, para informar erro(s) para cpos string não
	 * precisa tryParse
	 */
	private Cartela getFormData() throws ParseException {
		Cartela obj = new Cartela();
		obj = entity;
		// instanciando uma exceção, mas não lançado - validation exc....
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null
		if (numCar != null) {
			obj.setNumeroCar(numCar);
		} else {
			obj.setNumeroCar(Utils.tryParseToInt(textNumeroCar.getText()));
		}

// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro

		if (dpDataCar.getValue() != null) {
			Instant instant = Instant.from(dpDataCar.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataCar(Date.from(instant));
		} else {
			if (dpDataCar.getValue() == null) {
				exception.addErros("data", "Data é obrigatória");
			}
		}

		if (textLocalCar.getText() == null || textLocalCar.getText().trim().contentEquals("")) {
			exception.addErros("local", "Local é obrigatório");
		} else {
			obj.setLocalCar(textLocalCar.getText());
		}

		confereExiste();
		if (textLocalCar.getText() == null || textLocalCar.getText().trim().contentEquals("")) {
			exception.addErros("local", "Local é obrigatório");
		} else {
			obj.setLocalCar(textLocalCar.getText());
		}

		if (obj.getSituacaoCar() == null) {
			obj.setSituacaoCar("A");
			obj.setNomeSituacaoCar("Aberto");
		}	

		if (textDescontoCar.getText() == null || textDescontoCar.getText().trim().contentEquals("")) {
			obj.setDescontoCar(0.00);
		} else {
			obj.setDescontoCar(Utils.tryParseToDouble(textDescontoCar.getText().replace(",", ".")));
		}

		obj.setNumeroPaganteCar(Utils.tryParseToInt(textNumeroPaganteCar.getText()));

		obj.setValorServicoCar(0.00);
		obj.setSubTotalCar(0.00);

		int flagServ = 0;
		if (rbServicoSimCar.isSelected()) {
			obj.setServicoCar("Com serviço");
			rbServicoNaoCar.setSelected(false);
			flagServ += 1;
		}
		if (rbServicoNaoCar.isSelected()) {
			obj.setServicoCar("Sem serviço");
			rbServicoSimCar.setSelected(false);
			flagServ += 1;
		}
		if (flagServ > 1) {
			exception.addErros("servico", "Só pode uma opção");
		}

		obj.setObsCar(textObsCar.getText());
		if (dpDataCar.getValue() != null) {
			entity.setMesCar(dpDataCar.getValue().getMonthValue());
			entity.setAnoCar(dpDataCar.getValue().getYear());
		} else {
			obj.setMesCar(0);
			obj.setAnoCar(0);
		}
		// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

	public void updateTableView() {
		if (virService == null) {
			throw new IllegalStateException("Serviço virtual está vazio");
		}
		labelUser.setText(user);
		if (entity.getNumeroCar() != null) {
			List<CartelaVirtual> listVir = virService.findCartela(entity.getNumeroCar());
			obsListVir = FXCollections.observableArrayList(listVir);
			tableViewVir.setItems(obsListVir);
			initEditButtons();
			initRemoveButtons();
		}
	}

	// msm processo save p/ fechar
	@FXML
	public void onBtCancelCarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textNumeroCar);
		Utils.formatDatePicker(dpDataCar, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(textDescontoCar);
		Constraints.setTextFieldMaxLength(textLocalCar, 10);
		Constraints.setTextFieldMaxLength(textNumeroPaganteCar, 02);
		initializeNodes();
	}

	// comportamento padrão para iniciar as colunas
	private void initializeNodes() {
		tableColumnNomeProdVir.setCellValueFactory(new PropertyValueFactory<>("NomeProdVir"));
		tableColumnQtdProdVir.setCellValueFactory(new PropertyValueFactory<>("QuantidadeProdVir"));
		Utils.formatTableColumnDouble(tableColumnQtdProdVir, 2);
		tableColumnVendaProdVir.setCellValueFactory(new PropertyValueFactory<>("VendaProdVir"));
		Utils.formatTableColumnDouble(tableColumnVendaProdVir, 2);
		tableColumnTotalProdVir.setCellValueFactory(new PropertyValueFactory<>("TotalProdVir"));
		Utils.formatTableColumnDouble(tableColumnTotalProdVir, 2);
		// para tableview preencher o espaço da tela scroolpane, referencia do stage
		Stage stage = (Stage) MainSgb.getMainScene().getWindow();
		tableViewVir.prefHeightProperty().bind(stage.heightProperty());
	}

	private void createDialogForm(CartelaVirtual virtual, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			classe = "CartelaVirtual";
//referencia para o controlador = controlador da tela carregada fornListaForm			
			CartelaVirtualFormController controller = loader.getController();
			controller.user = user;
			controller.local = local;
// injetando passando parametro obj 			
			controller.setCartelaVirtual(virtual);
			controller.numCar = numCar;
			controller.mm = dpDataCar.getValue().getMonthValue();
			controller.aa = dpDataCar.getValue().getYear();

// injetando serviços vindo da tela de formulario fornform
			controller.setVirtualServices(new CartelaVirtualService(), new FuncionarioService(), new GrupoService(),
					new ProdutoService());
			controller.loadAssociatedObjects();
//inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite Consumo                                             ");
			dialogStage.setScene(new Scene(pane));
//pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
//quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
//travada enquanto não sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void createDialogFormFecha(Cartela obj, CartelaPagante pagante, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			classe = "CartelaPagante";
//referencia para o controlador = controlador da tela carregada fornListaForm			
			CartelaPaganteFormController controller = loader.getController();

			controller.user = user;
//injetando passando parametro obj 			
			controller.numCar = numCar;
			controller.numPagante = numPag;
			controller.setPagantes(pagante, obj);
//injetando serviços vindo da tela de formulario fornform
			controller.setServices(new CartelaPaganteService(), new CartelaService());
//inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Fechamento Consumo                                             ");
			dialogStage.setScene(new Scene(pane));
//pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
//quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
//travada enquanto não sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void initRemoveButtons() {
		tableColumnRemoveVir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemoveVir.setCellFactory(param -> new TableCell<CartelaVirtual, CartelaVirtual>() {
			private final Button button = new Button("exclui");

			@Override
			protected void updateItem(CartelaVirtual virtual, boolean empty) {
				super.updateItem(virtual, empty);

				if (virtual == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(virtual));
			}
		});
	}

	private void removeEntity(CartelaVirtual virtual) {
		if (nivel > 1 && nivel < 9) {
			Alerts.showAlert(null, "Atenção", "Operação não permitida", AlertType.INFORMATION);
		} else {
			Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
			if (result.get() == ButtonType.OK) {
				if (virService == null) {
					throw new IllegalStateException("Serviço virtual está vazio");
				}
				try {
					classe = "CartelaVirtual";
					estoque(virtual);
					virService.remove(virtual.getNumeroVir());
					acertaCar(virtual);
					updateFormData();
					updateTableView();
				} catch (DbIntegrityException e) {
					Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
				} catch (ParseException p) {
					p.getStackTrace();
				}
			}
		}
	}

	private void estoque(CartelaVirtual virtual2) {
		Produto prod = new Produto();
		ProdutoService prodService = new ProdutoService();
		prod = prodService.findById(virtual2.getProduto().getCodigoProd());
		prod.setSaidaProd(prod.getSaidaProd() - virtual2.getQuantidadeProdVir());
		prodService.saveOrUpdate(prod);
	}

	private void acertaCar(CartelaVirtual obj) {
		Cartela car = service.findById(obj.getOrigemIdCarVir());
		List<CartelaVirtual> listVir = virService.findCartela(numCar);
		car.calculaTotalCar(listVir);
		car.calculaValorPagante();
		confereTotal();
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade cartela esta nula");
		}

		// string value of p/ casting int p/ string
		textNumeroCar.setText(String.valueOf(entity.getNumeroCar()));
		numCar = entity.getNumeroCar();
		// se for uma inclusao, vai posicionar no 1o depto//tipo (First)

		if (entity.getDataCar() == null) {
			entity.setDataCar(new Date());
		} else {
			dpDataCar.setValue(LocalDate.ofInstant(entity.getDataCar().toInstant(), ZoneId.systemDefault()));
		}

		if (entity.getLocalCar() != null) {
			textLocalCar.setText(entity.getLocalCar());
		}

		String vlr = "0.00";
		if (entity.getDescontoCar() == null) {
			entity.setDescontoCar(0.00);
		}
		vlr = Mascaras.formataValor(entity.getDescontoCar());
		textDescontoCar.setText(vlr);

		if (entity.getServicoCar() != null) {
			if (entity.getServicoCar() == "Com serviço") {
				rbServicoSimCar.setSelected(true);
			}
		} else {
			rbServicoSimCar.setSelected(false);			
		}
		
		if (entity.getServicoCar() != null) {
			if (entity.getServicoCar() == "Sem serviço") {
				rbServicoNaoCar.setSelected(true);
			}
		} else {
			rbServicoNaoCar.setSelected(false);
		}
		
		if (entity.getServicoCar() == null) {
			entity.setServicoCar("Sem serviço");
			rbServicoNaoCar.setSelected(true);
			rbServicoNaoCar.setSelected(false);
		}
		textServicoCar.setText(entity.getServicoCar());

		if (entity.getMesCar() == null) {
			entity.setMesCar(0);
		}
		
		if (entity.getAnoCar() == null) {
			entity.setAnoCar(0);
		}

		if (entity.getTotalCar() == null) {
			entity.setTotalCar(0.00);
		}
		vlrTotMasc = Mascaras.formataValor(entity.getTotalCar());
		labelTotalCar.setText(vlrTotMasc);

		if (entity.getNumeroPaganteCar() == null) {
			entity.setNumeroPaganteCar(1);
		}
		textNumeroPaganteCar.setText(String.valueOf(entity.getNumeroPaganteCar()));

		if (entity.getValorPaganteCar() == null) {
			entity.setValorPaganteCar(0.00);
		}
		vlrPagMasc = Mascaras.formataValor(entity.getValorPaganteCar());
		labelValorPaganteCar.setText(vlrPagMasc);

		entity.setSubTotalCar(0.00);
		entity.setValorServicoCar(0.00);
	}

	private void initEditButtons() {
		tableColumnEditaVir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditaVir.setCellFactory(param -> new TableCell<CartelaVirtual, CartelaVirtual>() {
			private final Button button = new Button("edita");

			@Override
			protected void updateItem(CartelaVirtual virtual, boolean empty) {
				super.updateItem(virtual, empty);

				if (virtual == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(virtual, "/gui/sgb/CartelaVirtualForm.fxml",
						Utils.currentStage(event)));
			}
		});
	}

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (virService == null) {
			throw new IllegalStateException("Virtual Serviço esta nulo");
		}
// buscando (carregando) bco de dados
		List<CartelaVirtual> listVir = new ArrayList<>();
		listVir = virService.findCartela(numCar);
// transf p/ obslist		
		obsListVir = FXCollections.observableArrayList(listVir);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorDataCar.setText((fields.contains("data") ? erros.get("data") : ""));
		labelErrorLocalCar.setText((fields.contains("local") ? erros.get("local") : ""));
		labelErrorServicoCar.setText((fields.contains("servico") ? erros.get("servico") : ""));
		if (fields.contains("confirma")) {
			Alerts.showAlert("Total", null, "Conferindo total", AlertType.INFORMATION);
			labelTotalCar.viewOrderProperty();
			labelValorPaganteCar.viewOrderProperty();
			flag = 1;
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
