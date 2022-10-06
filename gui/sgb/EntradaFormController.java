package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Entrada;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.EntradaService;
import gui.sgbmodel.service.GrupoService;
import gui.sgbmodel.service.ProdutoService;
import gui.sgcp.FornecedorFormController;
import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.service.FornecedorService;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;

public class EntradaFormController implements Initializable {

	private Entrada entity;
	private Entrada anterior;
 
	/*
	 * dependencia service com metodo set
	 */
	private EntradaService service;
	private FornecedorService fornService;
	private ProdutoService prodService;
 
// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textNumeroEnt;

	@FXML
	private DatePicker dpDataEnt;

	@FXML
	private TextField textNnfEnt;

	@FXML
	private TextField textIniciaisForn;
	
	@FXML
	private ComboBox<Fornecedor> comboBoxFornEnt;

	@FXML
	private TextField textIniciaisProd;
	
	@FXML
	private ComboBox<Produto> comboBoxProdEnt;

	@FXML
	private TextField textQtdProdEnt;

	@FXML
	private TextField textVlrProdEnt;

	@FXML
	private Label labelTotVlrProdEnt;

	@FXML
	private Button btSaveEnt;

	@FXML
	private Button btCancelEnt;

	@FXML
	private Button btPesqForn;
	
	@FXML
	private Button btPesqProd;
	
	@FXML
	private Label labelErrorDataEnt;

	@FXML
	private Label labelErrorNnfEnt;

	@FXML
	private Label labelErrorQtdProdEnt;

	@FXML
	private Label labelErrorVlrProdEnt;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	String classe = "Entrada ";
 	String pesquisaForn = "";
 	String pesquisaProd = "";
// auxiliares	
	double tot = 0.00;
	double totAnt = 0.00;
	int flagN = 0;

	private ObservableList<Fornecedor> obsListForn;
	private ObservableList<Produto> obsListProd;

	public void setEntrada(Entrada entity) {
		this.entity = entity;	
	}

 	// * metodo set /p service
	public void setEntradaService(EntradaService service) {
		this.service = service;
	}

	public void setFornecedorService(FornecedorService fornService) {
		this.fornService = fornService;
	}

	public void setProdutoService(ProdutoService prodService) {
		this.prodService = prodService;
	}

//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
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
	public void onBtSaveEntAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		try {
			entity = getFormData();
			acertaProduto();
 			if (flagN == 0)
			{	service.saveOrUpdate(entity);
			}
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

	private void acertaProduto() {
 		Produto prod = prodService.findById(entity.getProd().getCodigoProd());
		if (entity.getValorProdEnt() > prod.getVendaProd()) {
			Optional<ButtonType> result = Alerts.showConfirmation(
					"Atenção!!!", "Custo é maior que o valor de venda");
			if (result.get() == ButtonType.CANCEL) {
				flagN = 1;
			} 
		}	
		Produto prodAnt = new Produto();
		if (flagN == 0) {
			if (anterior != null) {
				if (entity.getNumeroEnt().equals(anterior.getNumeroEnt())) {
					if (entity.getProd().getCodigoProd().equals(anterior.getProd().getCodigoProd()) ) {
						double ent1 = prod.getEntradaProd() - anterior.getQuantidadeProdEnt();
						prod.setEntradaProd(0.00);
						prod.setEntradaProd(ent1);
					}
					if (!entity.getProd().getCodigoProd().equals(anterior.getProd().getCodigoProd()) ) {
						prodAnt = prodService.findById(anterior.getProd().getCodigoProd());
						if (prodAnt.getEntradaProd() >= anterior.getQuantidadeProdEnt()) {
							double ent2 = prodAnt.getEntradaProd() - anterior.getQuantidadeProdEnt();
							prodAnt.setEntradaProd(0.00);
							prodAnt.setEntradaProd(ent2);
							prodService.saveOrUpdate(prodAnt);
						} else {
							Alerts.showAlert("Saldo", "saldo vai ficar negativo",
									anterior.getNomeProdEnt(), AlertType.ERROR);
						}
					}
				}	
			}	
			prod.setEntradaProd(entity.getQuantidadeProdEnt());
			prod.setPrecoProd(entity.getValorProdEnt());	
			if (prod.getSaldoProd() < 0.0 || prod.getEntradaProd() < 0.0) {
				flagN = 1;
				Alerts.showAlert("Negativo", "Produto ",  classe + "Erro!!! Entrada negativa", AlertType.ERROR);
			}
			if (prod.getSaldoProd() < 0.0) {
				flagN = 1;
				Alerts.showAlert("Negativo", "Produto ",  classe + "Erro!!! Saldo negativo", AlertType.ERROR);
			}
		}	
		if (flagN == 0) {	
			prod.calculaCmm();
			prodService.saveOrUpdate(prod);
		}			
	}

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se não for atz tb verificamos se cpos
	 * obrigatórios estão preenchidos, para informar erro(s) para cpos string não
	 * precisa tryParse
	 */
	private Entrada getFormData() throws ParseException {
		ValidationException exception = new ValidationException("Validation exception");
		Entrada obj = new Entrada();
		// instanciando uma exceção, mas não lançado - validation exc....
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setNumeroEnt(Utils.tryParseToInt(textNumeroEnt.getText()));
// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro

		if (dpDataEnt.getValue() != null) {
			Instant instant = Instant.from(dpDataEnt.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataEnt(Date.from(instant));
		}
		if (dpDataEnt.getValue() == null) {
			exception.addErros("data", "Data é obrigatória");
		}

		if (textNnfEnt.getText() == null || textNnfEnt.getText().trim().contentEquals("")) {
			exception.addErros("nnf", "Nota fiscal é obrigatória");
			obj.setNnfEnt(Utils.tryParseToInt(textNnfEnt.getText()));
		} else {
			obj.setNnfEnt(Utils.tryParseToInt(textNnfEnt.getText()));
		}

		obj.setForn(comboBoxFornEnt.getValue());
		Fornecedor forn = comboBoxFornEnt.getSelectionModel().getSelectedItem();
		obj.setNomeFornEnt(forn.getRazaoSocial());

		obj.setProd(comboBoxProdEnt.getValue());
		Produto mat = comboBoxProdEnt.getSelectionModel().getSelectedItem();
		obj.setNomeProdEnt(mat.getNomeProd());
		if (mat.getPrecoProd() != 0.00) {
			obj.setValorProdEnt(mat.getPrecoProd());
			String vlr = Mascaras.formataValor(mat.getPrecoProd());
			textVlrProdEnt.setText(vlr);
		}	

		if (textQtdProdEnt.getText() == null || textQtdProdEnt.getText().trim().contentEquals("")) {
			exception.addErros("qtd", "Qtd é obrigatória");
		} else {
			textQtdProdEnt.getText().replace(".", "");
			obj.setQuantidadeProdEnt(Utils.formatDecimalIn(textQtdProdEnt.getText())); 	 		
		}
		
		if (obj.getQuantidadeProdEnt() == 0.00 || obj.getQuantidadeProdEnt() == null) {
			exception.addErros("qtd", "Qtd é obrigatória");
		} 

		if (textVlrProdEnt.getText() == null || 
				textVlrProdEnt.getText() == "0.0" ||
				textVlrProdEnt.getText().trim().contentEquals("")) {
			exception.addErros("vlr", "Preço é obrigatório");
		} else {
			textVlrProdEnt.getText().replace(".", "");
			obj.setValorProdEnt(Utils.formatDecimalIn(textVlrProdEnt.getText()));
		}
		
		if (obj.getValorProdEnt() == 0.00) {
			exception.addErros("vlr", "Preço é obrigatório");			
		}
		if (obj.getValorProdEnt() > mat.getVendaProd() ) {
			exception.addErros("vlr", "Valor > que venda");
		}

		tot = obj.getQuantidadeProdEnt() * obj.getValorProdEnt();
		if (tot != totAnt) {
			String totM = Mascaras.formataValor(tot);
			totAnt = tot;
			exception.addErros("confirma", "confirma");
		}	
  				
		// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	@FXML
	public void onBtPesqFornAction(ActionEvent event) {
		classe = "Fornecedor";
		pesquisaForn = "";
		try {
	  		pesquisaForn = textIniciaisForn.getText().toUpperCase().trim();
	  		if (pesquisaForn != "") {
	  			List<Fornecedor> listFor = fornService.findPesquisa(pesquisaForn);
				if (listFor.size() == 0) { 
					pesquisaForn = "";
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
				 		 Stage parentStage = Utils.currentStage(event);
		 		 		 Fornecedor obj = new Fornecedor();
		 		 		 createDialogForn(obj, "/gui/sgcp/FornecedorForm.fxml", parentStage);
		 		  	}
					listFor = fornService.findPesquisa(pesquisaForn);
			 	}
	  			obsListForn = FXCollections.observableArrayList(listFor);
	  			comboBoxFornEnt.setItems(obsListForn);
	  			notifyDataChangeListerners();
	  			updateFormData();
	  		}	
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	} 	

	@FXML
	public void onBtPesqProdAction(ActionEvent event) {
		classe = "Produto";
		pesquisaProd = "";
		try {
	  		pesquisaProd = textIniciaisProd.getText().toUpperCase().trim();
	  		if (pesquisaProd != "") {
	  			List<Produto> listProd = prodService.findPesquisa(pesquisaProd);
				if (listProd.size() == 0) { 
					pesquisaProd = "";
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
				 		 Stage parentStage = Utils.currentStage(event);
				 		 Produto obj = new Produto();
				 		 createDialogProd(obj, "/gui/sgb/ProdutoForm.fxml", parentStage);
				   	}
					listProd = prodService.findPesquisa(pesquisaProd);
			 	}
	  			obsListProd = FXCollections.observableArrayList(listProd);
	  			comboBoxProdEnt.setItems(obsListProd);
	  			notifyDataChangeListerners();
	  			updateFormData();
	  		}	
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	} 	

	// msm processo save p/ fechar
	@FXML
	public void onBtCancelEntAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere)	 impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textNumeroEnt);
		Utils.formatDatePicker(dpDataEnt, "dd/MM/yyyy");
		Constraints.setTextFieldInteger(textNnfEnt);
 		Constraints.setTextFieldMaxLength(textNnfEnt, 6);
 		Constraints.setTextFieldMaxLength(textIniciaisForn, 7);
 		Constraints.setTextFieldMaxLength(textIniciaisProd, 7);
		initializeComboBoxFornEnt();
		initializeComboBoxProdEnt();
	}

	private void initializeComboBoxFornEnt() {
		Callback<ListView<Fornecedor>, ListCell<Fornecedor>> factory = lv -> new ListCell<Fornecedor>() {
			@Override
			protected void updateItem(Fornecedor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getRazaoSocial());
			}
		};

		comboBoxFornEnt.setCellFactory(factory);
		comboBoxFornEnt.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxProdEnt() {
		Callback<ListView<Produto>, ListCell<Produto>> factory = lv -> new ListCell<Produto>() {
			@Override
			protected void updateItem(Produto item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeProd() + 
						(String.format(" - R$%.2f", item.getVendaProd())));
			}
		};

		comboBoxProdEnt.setCellFactory(factory);
		comboBoxProdEnt.setButtonCell(factory.call(null));
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		labelUser.setText(user);
//  string value of p/ casting int p/ string 		
		textNumeroEnt.setText(String.valueOf(entity.getNumeroEnt()));
		if (entity.getNumeroEnt() != null) {
			anterior = entity;
		}

		if (entity.getDataEnt() != null) {
			entity.setDataEnt(new Date());
		}
		
		if (entity.getDataEnt() != null) {
			dpDataEnt.setValue(LocalDate.ofInstant(entity.getDataEnt().toInstant(), ZoneId.systemDefault()));
		}

		textNnfEnt.setText(String.valueOf(entity.getNnfEnt()));

		// se for uma inclusao, vai posicionar no 1o registro (First)
		if (entity.getForn() == null) {
			comboBoxFornEnt.getSelectionModel().selectFirst();
		} else {
			comboBoxFornEnt.setValue(entity.getForn());
		}

		if (entity.getProd() == null) {
			comboBoxProdEnt.getSelectionModel().selectFirst();
		} else {
			comboBoxProdEnt.setValue(entity.getProd());
		}
		
		if (entity.getQuantidadeProdEnt() == null) {
			entity.setQuantidadeProdEnt(0.00);
		}
		if (entity.getQuantidadeProdEnt() != null) {
			String qtd = Mascaras.formataValor(entity.getQuantidadeProdEnt());
			textQtdProdEnt.setText(qtd);
		}

		if (entity.getValorProdEnt() == null) {
			entity.setValorProdEnt(0.00);
		}
		if (entity.getValorProdEnt() != null) {
			String vlr = Mascaras.formataValor(entity.getValorProdEnt());
			textVlrProdEnt.setText(vlr);
		}

		totAnt = entity.getQuantidadeProdEnt() * entity.getValorProdEnt();

		String totM = Mascaras.formataValor(totAnt);
		labelTotVlrProdEnt.setText(totM);
		labelTotVlrProdEnt.viewOrderProperty();
		textIniciaisForn.setText(pesquisaForn);
		textIniciaisProd.setText(pesquisaProd);
	}

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (fornService == null) {
			throw new IllegalStateException("FornecedorServiço esta nulo");
		}
		if (prodService == null) {
			throw new IllegalStateException("ProdutoServiço esta nulo");
		}
 // buscando (carregando) os dados do bco de dados		
		List<Fornecedor> listForn = fornService.findAll();
		List<Produto> listProd = prodService.findAll();
		
// transf p/ obslist
		obsListForn = FXCollections.observableArrayList(listForn);
		comboBoxFornEnt.setItems(obsListForn);
		obsListProd = FXCollections.observableArrayList(listProd);
		comboBoxProdEnt.setItems(obsListProd);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorNnfEnt.setText((fields.contains("nnf") ? erros.get("nnf") : ""));
		labelErrorDataEnt.setText((fields.contains("data") ? erros.get("data") : ""));
		labelErrorQtdProdEnt.setText((fields.contains("qtd") ? erros.get("qtd") : ""));
		labelErrorVlrProdEnt.setText((fields.contains("vlr") ? erros.get("vlr") : ""));
		if (fields.contains("confirma")) {
			Alerts.showAlert("Fechamento", null, "Conferindo total", AlertType.INFORMATION);
				try {	String totM = Mascaras.formataValor(tot);
						labelTotVlrProdEnt.setText(totM);
						labelTotVlrProdEnt.viewOrderProperty();
					}
				 	catch (ParseException e) {
				 		e.printStackTrace();
				 	}
		}
	}
	
	private void createDialogForn(Fornecedor obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Fornecedor ";
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			FornecedorFormController controller = loader.getController();
			controller.user = user;
			controller.setFornecedor(obj);
			controller.setFornecedorService(new FornecedorService());
//			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Fornecedor                                             ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
		catch (ParseException p) {
			p.printStackTrace();
		}
 	}

	private void createDialogProd(Produto obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Produto ";
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
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
}
