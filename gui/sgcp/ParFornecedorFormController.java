package gui.sgcp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.service.FornecedorService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.exception.ValidationException;

public class ParFornecedorFormController implements Initializable {

	private Fornecedor entity;
/*
 *  dependencia service com metodo set
 */
 	private FornecedorService forService;

 	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
 	@FXML
 	private TextField textPesquisa;
 	
	@FXML
	private ComboBox<Fornecedor>  comboBoxFornecedor; 
	
  	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Button btPesquisa;
	
	@FXML
	private Label labelErrorComboBoxFornecedor;

 	private ObservableList<Fornecedor> obsListFor;
	private Integer codigo;
	String pesquisa = "";
	String classe = "Fornecedor";
 
 	public void setFornecedor(Fornecedor entity) {		
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setService(FornecedorService forService) {
 		this.forService = forService;
	}
  	
//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
 	
	@FXML
	public void onBtPesquisaAction(ActionEvent event) {
		try {
	  		pesquisa = textPesquisa.getText().toUpperCase().trim();
	  		if (pesquisa != "") {
	  			List<Fornecedor> list = forService.findPesquisa(pesquisa);
				if (list.size() == 0) { 
					pesquisa = "";
					Alerts.showAlert("Fornecedor ", null, "Não encontrado ", AlertType.INFORMATION);
					list = forService.findAll();
			 	}
	  			obsListFor = FXCollections.observableArrayList(list);
				comboBoxFornecedor.setItems(obsListFor);
				comboBoxFornecedor.getSelectionModel().selectFirst();
	  			notifyDataChangeListerners();
	  			updateFormData();
	  		}	
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

/* 
 * vamos instanciar um tipoforn e salvar no bco de dados
 * meu obj entity (lá em cima) vai receber uma chamada do getformdata
 *  metodo q busca dados do formulario convertidos getForm (string p/ int ou string)		
 *  pegou no formulario e retornou (convertido) p/ jogar na variavel entity
 *  chamo o service na rotina saveupdate e mando entity
 *  vamos tst entity e service = null -> não foi injetado
 *  para fechar a janela, pego a referencia para janela atual (event) e close
 *  DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar uma lista
 *  qdo ela salvar obj com sucesso, é só notificar (juntar)
 *  recebe lá no  listController    		 
 */
	@FXML
	public void onBtOkAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		try {
			List<Fornecedor> listFor = new ArrayList<>();
			if (pesquisa != "") { 
				listFor = forService.findPesquisa(pesquisa);
			} else {	
				listFor = forService.findAll();
		 	}			
     		entity = getFormData();
     		ParcelaListAbertoController.codigo = codigo;
//     		ParcelaListPagoController.codigo = codigo;
     		ParcelaPrintAbertoController.codigo = codigo;
//     		ParcelaPrintPagoController.codigo = codigo;
   	    	notifyDataChangeListerners();
	    	Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}	
	}

/*
 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int (la no util)
 * se codigo for nulo insere, se não for atz
 * tb verificamos se cpos obrigatórios estão preenchidos, para informar erro(s)
 * para cpos string não precisa tryParse	
 */
	private Fornecedor getFormData() {
		Fornecedor obj = new Fornecedor();
 // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");

 		if (comboBoxFornecedor.getValue().getCodigo() == null) {
 		 	exception.addErros("fornecedor", "fornecedor inválido");
		} else {
			codigo = comboBoxFornecedor.getValue().getCodigo();
			obj = comboBoxFornecedor.getValue();
		}
  		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
  // msm processo save p/ fechar	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
  		Constraints.setTextFieldMaxLength(textPesquisa, 7);
		initializeComboBoxFornecedor();
    	}

	private void initializeComboBoxFornecedor() {
		Callback<ListView<Fornecedor>, ListCell<Fornecedor>> factory = lv -> new ListCell<Fornecedor>() {
			@Override
			protected void updateItem(Fornecedor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getRazaoSocial());
 			}
		};
		
		comboBoxFornecedor.setCellFactory(factory);
		comboBoxFornecedor.setButtonCell(factory.call(null));
	}		
   	
 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
		comboBoxFornecedor.getSelectionModel().selectFirst();
     }
 	
//	carrega dados do bco  dentro obslist
	public void loadAssociatedObjects() {
// buscando (carregando) os forn q estão no bco de dados		
		List<Fornecedor> listFor = forService.findAll();
 		obsListFor = FXCollections.observableArrayList(listFor);
		comboBoxFornecedor.setItems(obsListFor);
  	}
  	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
		labelErrorComboBoxFornecedor.setText((fields.contains("fornecedor") ? erros.get("fornecedor") : ""));
  	}
}	
