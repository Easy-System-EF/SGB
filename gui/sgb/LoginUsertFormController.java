package gui.sgb;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Login;
import gui.sgbmodel.service.LoginService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Cryptograf;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.exception.ValidationException;

public class LoginUsertFormController implements Initializable {

	private Login entity;
	
/*
 *  dependencia service com metodo set
 */
	private LoginService service;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField textNumeroLog;
	
	@FXML
	private TextField textNomeLog;
	
	@FXML
	private TextField textNivelLog;
	
	@FXML
	private PasswordField passwordSenhaLog;
	
	@FXML
	private PasswordField passwordConfirmaLog;
	
 	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Label labelErrorNomeLog;

	@FXML
	private Label labelErrorNivelLog;

	@FXML
	private Label labelErrorSenhaLog;

	@FXML
	private Label labelErrorConfirmaLog;

//auxiliar
	String classe = "Login U ";
	String senha = "";
	String confirma = "";
	public static Date dataVc = new Date();
	public static Date dataMx = new Date();
	public static Integer numEmp = 0;
	
 	public void setLogin(Login entity) {
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setLoginService(LoginService service) {
		this.service = service;
	}
	
//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(LoginFormController loginFormController) {
		dataChangeListeners.add(loginFormController);
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
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula" + classe);
		}
		if (service == null)
		{	throw new IllegalStateException("Serviço nulo" + classe);
		}
		try {
    		 entity = getFormData();
	    	 service.insertOrUpdate(entity);
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
	private Login getFormData() {
		Login obj = new Login();
 // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setNumeroLog(Utils.tryParseToInt(textNumeroLog.getText()));
// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro
		if (textNomeLog.getText() == null || textNomeLog.getText().trim().contentEquals("")) {
			exception.addErros("nome", "Nome é obrigatório");
		}
		obj.setNomeLog(textNomeLog.getText());
		
		if (textNivelLog.getText() == null || textNivelLog.getText().trim().contentEquals("")) {
			exception.addErros("nivel", "Nível é obrigatório");
		}
		obj.setNivelLog(Utils.tryParseToInt(textNivelLog.getText()));
		if  (obj.getNivelLog() != null) {
			if (obj.getNivelLog() == 0 || obj.getNivelLog() > 3) {
				exception.addErros("nivel", "Nível 1 = geral; 2 = intermediário; 3 = só cadastro");
			}	
		}
		if (passwordSenhaLog.getText() == null || passwordSenhaLog.getText().trim().contentEquals("")) {
			exception.addErros("senha", "Senha é obrigatória");
		} else {
			if (passwordSenhaLog.getText().length() < 7) {
				exception.addErros("senha", "Senha incompleta");
			}	
		}
		if (passwordConfirmaLog.getText() == null || passwordConfirmaLog.getText().trim().contentEquals("")) {
			exception.addErros("confirma", "Confirmação é obrigatória");
		} else {
			if (passwordConfirmaLog.getText().length() < 7) {
				exception.addErros("confirma", "Confirmação incompleta");
			}	
		}
		Login log = service.findBySenha(senha);
		if (log != null) {
			exception.addErros("senha", "Senha muito fraca!!!");
		}	
		confirma = Cryptograf.criptografa(passwordConfirmaLog.getText().toLowerCase());
		senha = Cryptograf.criptografa(passwordSenhaLog.getText().toLowerCase());
		if (!confirma.contains(senha)) {
			exception.addErros("confirma", "Confirmação inválida");			
		}
		obj.setSenhaLog(senha);
		obj.setAlertaLog(0);
		obj.setDataLog(new Date());
		obj.setAcessoLog(new Date());
		obj.setDataMaximaLog(dataMx);
		obj.setDataVencimentoLog(dataVc);
		obj.setEmpresaLog(numEmp);
// tst se houve algum (erro com size > 0)		
		if (exception.getErros().size() > 0)
		{	throw exception;
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
 		Constraints.setTextFieldInteger(textNumeroLog);
 		Constraints.setTextFieldInteger(textNivelLog);
  		Constraints.setTextFieldMaxLength(textNomeLog, 15);
  		Constraints.setTextFieldMaxLength(textNivelLog, 1);
  		Constraints.setTextFieldMaxLength(passwordSenhaLog, 7);
  		Constraints.setTextFieldMaxLength(passwordConfirmaLog, 7);
  	}

 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
 //  string value of p/ casting int p/ string 		
 		textNumeroLog.setText(String.valueOf(entity.getNumeroLog()));
 		textNomeLog.setText("");
 		textNivelLog.setText(String.valueOf(0));
 		passwordSenhaLog.setText("");
 		passwordConfirmaLog.setText("");
   	}
 	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
  		labelErrorNomeLog.setText(fields.contains("nome") ? erros.get("nome") : "");		
  		labelErrorNivelLog.setText(fields.contains("nivel") ? erros.get("nivel") : "");	
  		labelErrorSenhaLog.setText(fields.contains("senha") ? erros.get("senha") : "");		
  		labelErrorConfirmaLog.setText(fields.contains("confirma") ? erros.get("confirma") : "");		
	}

	public void subscribeDataChangeListenerLog(DataChangeListener loginFormController) {
		dataChangeListeners.add(loginFormController);
		
	}
}	
