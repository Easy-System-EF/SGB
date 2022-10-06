package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Grupo;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.CargoService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.GrupoService;
import gui.sgbmodel.service.ProdutoService;
import gui.sgbmodel.service.SituacaoService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Mascaras;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;

public class CartelaVirtualFormController implements Initializable, DataChangeListener {

	private CartelaVirtual entity;
	private CartelaVirtual entityAnterior;
	Funcionario fun = new Funcionario();
	Grupo grupo = new Grupo();
	Produto prod = new Produto();

	/*
	 * dependencia service com metodo set
	 */
	private CartelaVirtualService service;
	private FuncionarioService funService;
	private GrupoService gruService;
	private ProdutoService prodService;

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
		try {
			updateFormData();
			getFormData();
		} catch (ParseException p) {
			p.printStackTrace();
		}
	}

	@FXML
	private Label labelEntidade;

	@FXML
	private TextField textCartelaVir;

	@FXML
	private TextField textLocalVir;

	@FXML
	private TextField textPesquisaFun;

	@FXML
	private ComboBox<Funcionario> comboBoxFunVir;

	@FXML
	private TextField textPesquisaProd;

	@FXML
	private ComboBox<Produto> comboBoxProdVir;

	@FXML
	private TextField textQtdProdVir;

	@FXML
	private Label labelVendaProdVir;

	@FXML
	private Label labelTotalProdVir;

	@FXML
	private Button btPesquisaFun;

	@FXML
	private Button btPesquisaProd;

	@FXML
	private Button btSaveVir;

	@FXML
	private Button btCancelVir;

	@FXML
	private Label labelErrorQtdProdVir;

	@FXML
	private Label labelErrorTotProdVir;

	@FXML
	private Label labelUser;

	// auxiliar
	public String user = "usuário";
	String classe = "CartelaVirtual ";
	String pesquisaFun = "";
	String pesquisaProd = "";
	double totAnt = 0.00;
	int flag = 0;
	int estoque = 0;
	public static Integer numCar = 0;
	public static String local = "null";
	public static String situacao = "null";
	public static int mm = 0;
	public static int aa = 0;
	private int gravaVir = 0;

	private ObservableList<Funcionario> obsListFun;
	private ObservableList<Produto> obsListProd;

	public void setCartelaVirtual(CartelaVirtual entity) {
		this.entity = entity;
	}

	// * metodo set /p service
	public void setVirtualServices(CartelaVirtualService service, FuncionarioService funService,
			GrupoService gruService, ProdutoService prodService) {
		this.service = service;
		this.funService = funService;
		this.gruService = gruService;
		this.prodService = prodService;
	}

	@FXML
	public void onBtPesqProdAction(ActionEvent event) {
		classe = "Produto";
		pesquisaProd = "";
		try {
			pesquisaProd = textPesquisaProd.getText().toUpperCase().trim();
			if (pesquisaProd != "") {
				List<Produto> listPro = prodService.findPesquisa(pesquisaProd);
				if (listPro.size() == 0) {
					pesquisaProd = "";
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
						Stage parentStage = Utils.currentStage(event);
						Produto obj = new Produto();
						createDialogPro(obj, "/gui/sgb/ProdutoForm.fxml", parentStage);
					}
					listPro = prodService.findPesquisa(pesquisaProd);
				}
				obsListProd = FXCollections.observableArrayList(listPro);
				comboBoxProdVir.setItems(obsListProd);
				updateFormData();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtPesqFunAction(ActionEvent event) {
		classe = "Funcionario";
		pesquisaFun = "";
		try {
			pesquisaFun = textPesquisaFun.getText().toUpperCase().trim();
			if (pesquisaFun != "") {
				List<Funcionario> listFun = funService.findPesquisa(
						pesquisaFun, aa, mm);
				if (listFun.size() == 0) {
					pesquisaFun = "";
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
						Stage parentStage = Utils.currentStage(event);
						Funcionario obj = new Funcionario();
						createDialogFun(obj, "/gui/sgb/FuncionarioForm.fxml", parentStage);
					}
					listFun = funService.findPesquisa(
							pesquisaFun, aa, mm);
				}
				obsListFun = FXCollections.observableArrayList(listFun);
				comboBoxFunVir.setItems(obsListFun);
				updateFormData();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}
	
	/*
	 * vamos instanciar um orc e salvar no bco de dados meu obj entity (lá em cima)
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
	public void onBtSaveVirAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		try {
			entity = getFormData();
			gravaVir = 2;
			classe = "CartelaVirtual";
			service.saveOrUpdate(entity);
			if (gravaVir == 2) {
				updateProduto(entity);
			}
			notifyDataChangeListerners();
			updateFormData();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (ParseException p) {
			p.printStackTrace();
		}
	}

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se não for atz tb verificamos se cpos
	 * obrigatórios estão preenchidos, para informar erro(s) para cpos string não
	 * precisa tryParse
	 */
	public CartelaVirtual getFormData() throws ParseException {
		ValidationException exception = new ValidationException("Validation exception");
		CartelaVirtual obj = new CartelaVirtual();
		// instanciando uma exceção, mas não lançado - validation exc....
// set CODIGO c/ utils p/ transf string em int \\ ou null
		obj.setNumeroVir(entity.getNumeroVir());
		if (numCar > 0) {
			obj.setOrigemIdCarVir(numCar);
		}
// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro

		if (textLocalVir.getText() == null || textLocalVir.getText().trim().contentEquals("")) {
			exception.addErros("local", "Local é obrigatório");
		} else {
			obj.setLocalVir(textLocalVir.getText());
		}

		obj.setSituacaoVir("A");
		obj.setFuncionario(comboBoxFunVir.getValue());
		obj.setNomeFuncVir(comboBoxFunVir.getValue().getNomeFun());

		obj.setProduto(comboBoxProdVir.getValue());
		obj.setNomeProdVir(comboBoxProdVir.getValue().getNomeProd());
		obj.setVendaProdVir(comboBoxProdVir.getValue().getVendaProd());
		obj.setPrecoProdVir(comboBoxProdVir.getValue().getPrecoProd());
		int cod = comboBoxProdVir.getValue().getCodigoProd();

		String vlr = Mascaras.formataValor(obj.getVendaProdVir());
		labelVendaProdVir.setText(vlr);

		if (textQtdProdVir.getText() == null || textQtdProdVir.getText().trim().contentEquals("")) {
			exception.addErros("qtd", "Qtd é obrigatória");
		}
		if (textQtdProdVir.getText() != null) {
			obj.setQuantidadeProdVir(Utils.formatDecimalIn(textQtdProdVir.getText().replace(".", "")));
			estoque = 0;
			confereEstoque(cod, obj.getQuantidadeProdVir());
		}
		if (estoque == 1) {
			obj.setQuantidadeProdVir(0.00);
			exception.addErros("qtd", "Qtd é maior que estoque");
		}

		obj.setTotalProdVir(obj.getQuantidadeProdVir() * obj.getVendaProdVir());

		if (obj.getTotalProdVir() != totAnt) {
			totAnt = obj.getTotalProdVir();
			String vlr2 = Mascaras.formataValor(obj.getVendaProdVir());
			labelVendaProdVir.setText(vlr2);
			vlr2 = Mascaras.formataValor(obj.getTotalProdVir());
			labelTotalProdVir.setText(vlr2);
			exception.addErros("tot", "");
		}	

		if (estoque == 1) {
			exception.addErros("qtd", "Qtd é maior que estoque");
		}	
		if (obj.getQuantidadeProdVir() == 0) {
			exception.addErros("qtd", "Quantidade não pode ser 0");
		}

// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

	private void confereEstoque(int cod, Double qtd) {
		classe = "Produto";
		int nada = 0;
		prod = prodService.findById(cod);
		if (prod.getGrupo().getNomeGru().contains("Serviços")) {
			nada += 1;
		}					
		if (nada == 0) {
			if (prod.getSaldoProd() == 0.00 || prod.getSaldoProd() < qtd) {
					Alerts.showAlert("Estoque", prod.getNomeProd(), "não tem estoque ", AlertType.ERROR);
					estoque = 1;
			}
		}
	}	

	private void updateProduto(CartelaVirtual entity2) {
		try {
			GrupoService gruService = new GrupoService();
			Grupo grupo = new Grupo();
			classe = "Produto";
			Produto prodAnt = new Produto();
			ValidationException exception = new ValidationException("Validation exception");
			if (gravaVir == 2) {
				prod = prodService.findById(entity2.getProduto().getCodigoProd());
	            grupo = gruService.findById(prod.getGrupoProd());
				if (grupo.getNomeGru().contains("Serviços")) {
					 prod.setEntradaProd(entity2.getQuantidadeProdVir());
				}
				if (grupo.getNomeGru().contains("Cozinha")) {
					listaCozinha(prod, entity2);
				}
				if (entityAnterior.getNumeroVir() != null) {
					if (entity.getProduto().getCodigoProd().equals(entityAnterior.getProduto().getCodigoProd()) ) {
						double ent1 = prod.getSaidaProd() - entityAnterior.getQuantidadeProdVir();
						prod.setSaidaProd(0.00);
						prod.setSaidaProd(ent1);
						
					}
					if (!entity.getProduto().getCodigoProd().equals(
							entityAnterior.getProduto().getCodigoProd()) ) {
						prodAnt = prodService.findById(entityAnterior.getProduto().getCodigoProd());
						if (prodAnt.getSaidaProd() >= entityAnterior.getQuantidadeProdVir()) {
							double ent2 = prodAnt.getSaidaProd() - 
									entityAnterior.getQuantidadeProdVir();
							prodAnt.setSaidaProd(0.00);
							prodAnt.setSaidaProd(ent2);
							prodService.saveOrUpdate(prodAnt);
						} else {
							Alerts.showAlert("Saldo", "saldo vai ficar negativo",
									entityAnterior.getNomeProdVir(), AlertType.ERROR);
						}
					}
				}	
			}	
			if (prod.getSaldoProd() >= entity2.getQuantidadeProdVir()) {
				prod.setSaidaProd(entity2.getQuantidadeProdVir());
				prod.calculaCmm();
				prodService.saveOrUpdate(prod);
			} else {
				Alerts.showAlert("Estoque", prod.getNomeProd(), "Não tem estoque ", AlertType.ERROR);
			}	
			if (prod.getSaldoProd() < prod.getEstMinProd()) {
				Alerts.showAlert("Estoque", prod.getNomeProd(), "Recompor estoque ", AlertType.WARNING);
			}	
			if (exception.getErros().size() > 0) {
				throw exception;
			}
		}	
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}	

	private void listaCozinha(Produto pro2, CartelaVirtual v2) {
		CartelaCozinhaImprimeController.local = local;
		CartelaCozinhaImprimeController.nomeProd = pro2.getNomeProd();
		CartelaCozinhaImprimeController.quantidade = v2.getQuantidadeProdVir();
		try {
			CartelaCozinhaImprimeController.onBtImprimeProduto();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// msm processo save p/ fechar
	@FXML
	public void onBtCancelVirAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldMaxLength(textPesquisaFun, 7);
		Constraints.setTextFieldMaxLength(textPesquisaProd, 7);
		Constraints.setTextFieldDouble(textQtdProdVir);
		initializeComboBoxProdVir();
		initializeComboBoxFuncVir();
	}

	private void initializeComboBoxProdVir() {
		Callback<ListView<Produto>, ListCell<Produto>> factory = lv -> new ListCell<Produto>() {
			@Override
			protected void updateItem(Produto item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeProd());
			}
		};

		comboBoxProdVir.setCellFactory(factory);
		comboBoxProdVir.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxFuncVir() {
		Callback<ListView<Funcionario>, ListCell<Funcionario>> factory = lv -> new ListCell<Funcionario>() {
			@Override
			protected void updateItem(Funcionario item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeFun());
			}
		};

		comboBoxFunVir.setCellFactory(factory);
		comboBoxFunVir.setButtonCell(factory.call(null));
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		entityAnterior = entity; 
		labelEntidade.setText("Consumo");
		labelUser.setText(user);
//  string value of p/ casting int p/ string
		textCartelaVir.setText(String.valueOf(numCar));
		textLocalVir.setText(entity.getLocalVir());
		if (textLocalVir.getText() == null) {
			textLocalVir.setText(local);
		}	
// se for uma inclusao, vai posicionar no 1o registro (First)
		if (entity.getFuncionario() == null) {
			comboBoxFunVir.getSelectionModel().selectFirst();
		} else {
			comboBoxFunVir.setValue(entity.getFuncionario());
		}

		if (entity.getProduto() == null) {
			comboBoxProdVir.getSelectionModel().selectFirst();
		} else {
			comboBoxProdVir.setValue(entity.getProduto());
		}

		if (entity.getQuantidadeProdVir() != null) {
			String qtd = Mascaras.formataValor(entity.getQuantidadeProdVir());
			textQtdProdVir.setText(qtd);
		} else {
			entity.setQuantidadeProdVir(0.00);
			String qtd = Mascaras.formataValor(entity.getQuantidadeProdVir());
			textQtdProdVir.setText(qtd);
		}

		if (entity.getVendaProdVir() != null) {
			String vlr = Mascaras.formataValor(entity.getProduto().getVendaProd());
			labelVendaProdVir.setText(vlr);
		}

		totAnt = 0.00;
		if (entity.getTotalProdVir() != null) {
			totAnt = entity.getTotalProdVir();
		}

		String vlr2 = Mascaras.formataValor(totAnt);
		labelTotalProdVir.setText(vlr2);
	}

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (funService == null) {
			throw new IllegalStateException("FuncionarioServiço esta nulo");
		}
		if (prodService == null) {
			throw new IllegalStateException("Produto Serviço esta nulo");
		}
		if (service == null) {
			throw new IllegalStateException("VirtualServiço esta nulo");
		}
		labelUser.setText(user);
		// buscando (carregando) os dados do bco de dados
		List<Funcionario> listFun = funService.findAll(aa, mm); 
		List<Produto> listPro = prodService.findAll();
// transf p/ obslist
		obsListFun = FXCollections.observableArrayList(listFun);
		obsListProd = FXCollections.observableArrayList(listPro);
		comboBoxFunVir.setItems(obsListFun);
		comboBoxProdVir.setItems(obsListProd);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorQtdProdVir.setText((fields.contains("qtd") ? erros.get("qtd") : ""));
		labelErrorTotProdVir.setText((fields.contains("tot") ? erros.get("tot") : ""));
	}

	private void createDialogPro(Produto obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Produto ";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ProdutoFormController controller = loader.getController();
			controller.user = user;
			controller.setProduto(obj);
			controller.setProdutoService(new ProdutoService());
			controller.setGrupoService(new GrupoService());
			controller.loadAssociatedObjects();
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite Produto                                             ");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela" + classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void createDialogFun(Funcionario obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Funcionário ";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			FuncionarioFormController controller = loader.getController();
			controller.user = user;
			controller.setFuncionario(obj);
			controller.setServices(new FuncionarioService(), new CargoService(), new SituacaoService());
			controller.loadAssociatedObjects();
			controller.updateFormData();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite Funcionário                                             ");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela" + classe, e.getMessage(), AlertType.ERROR);
		}
	}
}
