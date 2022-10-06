package gui.sgb;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Cargo;
import gui.sgbmodel.service.CargoService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.exception.ValidationException;
 
public class CargoFormController implements Initializable {

	private static final Double SALARIO_ANO = 1212.00;

	private Cargo entity;
	
/*
 *  dependencia service com metodo set
 */
	private CargoService service;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField textCodigoCargo;
	
	@FXML
	private TextField textNomeCargo;
	
	@FXML
	private TextField textSalarioCargo;
	
	@FXML
	private TextField textComissaoCargo;
	
 	@FXML
	private Button btSaveCargo;
	
	@FXML
	private Button btCancelCargo;
	
	@FXML
	private Label labelErrorNomeCargo;

	@FXML
	private Label labelErrorSalarioCargo;

	@FXML
	private Label labelUser;

// auxiliares
	String classe = "Cargo ";
	public String user = "usuário";		
	
 	public void setCargo(Cargo entity) {
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setCargoService(CargoService service) {
		this.service = service;
	}
	
//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
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
	public void onBtSaveCargoAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula");
		}
		if (service == null)
		{	throw new IllegalStateException("Serviço nulo");
		}
		try {
    		 entity = getFormData();
	    	 service.saveOrUpdate(entity);
	    	 notifyDataChangeListerners();
	    	 Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert(classe + "Erro salvando objeto", null, e.getMessage(), AlertType.ERROR);
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
	private Cargo getFormData() {
		Cargo obj = new Cargo();
   // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setCodigoCargo(Utils.tryParseToInt(textCodigoCargo.getText()));
// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro
		if (textNomeCargo.getText() == null || textNomeCargo.getText().trim().contentEquals("")) {
			exception.addErros("nome", "Nome é obrigatório");
		}
		else {
			obj.setNomeCargo(textNomeCargo.getText());
			if (textNomeCargo.getText().length() < 4) {
				exception.addErros("nome", "Nome inválido");
			}	
		}	
 		
		if (textSalarioCargo.getText() == null || textSalarioCargo.getText().trim().contentEquals("")) {
			exception.addErros("salario", "Salário é obrigatório");
  		}	else 
 			{	obj.setSalarioCargo(Utils.formatDecimalIn(textSalarioCargo.getText().replace(".", "")));
			}	
		if (obj.getSalarioCargo() < SALARIO_ANO) {
			exception.addErros("salario", "Salário inferior ao mínimo");
		}

		if (textComissaoCargo.getText() != null)
 		{	obj.setComissaoCargo(Utils.formatDecimalIn(textComissaoCargo.getText()));
		}
 // tst se houve algum (erro com size > 0)		
		if (exception.getErros().size() > 0)
		{	throw exception;
		}
		return obj;
	} 
	
// msm processo save p/ fechar	
	@FXML
	public void onBtCancelCargoAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
 		Constraints.setTextFieldInteger(textCodigoCargo);
// 		Constraints.setTextFieldDouble(textSalarioCargo);
 		Constraints.setTextFieldDouble(textComissaoCargo);
  		Constraints.setTextFieldMaxLength(textNomeCargo, 20);
  		Constraints.setTextFieldMaxLength(textComissaoCargo, 4);
   	}

 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() throws ParseException {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
 		labelUser.setText(user);
 //  string value of p/ casting int p/ string 		
 		textCodigoCargo.setText(String.valueOf(entity.getCodigoCargo()));
 		textNomeCargo.setText(entity.getNomeCargo());
 		if (entity.getSalarioCargo() == null) {
 			entity.setSalarioCargo(0.0);
 		}
 		String salario = Mascaras.formataValor(entity.getSalarioCargo());
 		textSalarioCargo.setText(String.valueOf(salario));
  		if (entity.getComissaoCargo() == null){
 			entity.setComissaoCargo(0.0);
 		}
  		String comissao = Mascaras.formataCom(entity.getComissaoCargo());
  		textComissaoCargo.setText(String.valueOf(comissao));
   	}
 	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
 		if (fields.contains("nome")) 
 		{	labelErrorNomeCargo.setText(erros.get("nome"));	
 		}
  		labelErrorNomeCargo.setText(fields.contains("nome") ? erros.get("nome") : "");		
  		labelErrorSalarioCargo.setText(fields.contains("salario") ? erros.get("salario") : "");
	}
}	
