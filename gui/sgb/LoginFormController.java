package gui.sgb;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.MainViewSgbController;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Login;
import gui.sgbmodel.service.LoginService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Cryptograf;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.exception.ValidationException;

public class LoginFormController implements Initializable, DataChangeListener {

/*
 *  dependencia service com metodo set
 */
	private LoginService service;
	private Login entity;
	Login login = new Login(); 

//auxiliar
	Date data = new Date();
	String classe = "Login F ";
	int nivel = 0;
	private Date dataV;
	private Date dataM;
	
	public static Integer numEmp = null;
	
// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private PasswordField passwordLoginSenha;
	
 	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Button btNew;
	
	@FXML
	private Label labelErrorSenha;

  
 	public void setLogin(Login entity) {
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setLoginService(LoginService service) {
		this.service = service;
	}
	
//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(MainViewSgbController mainViewController) {
		dataChangeListeners.add(mainViewController);
	}

	@FXML
	public void onBtOkAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		try {
			entity = getFormData();
			if (MainViewSgbController.senha == "Ok") {
				if (login.getNivelLog() != 9) {
					if (data.after(login.getMaximaLog())) {
						Alerts.showAlert("Fim do processo", null, 
								"Limite do vencimento, por favor faça contato", AlertType.INFORMATION);
						mainNo();
					}
					if (data.after(login.getVencimentoLog())) {
						Alerts.showAlert("Atenção", null, "Mensalidade vencida", AlertType.INFORMATION);
					}	
					if (login.getAcessoLog().after(data)) {
						Alerts.showAlert("Atenção", null, "Hora e/ou data desconfigurada", 
							AlertType.INFORMATION);
						int alerta = login.getAlertaLog() + 1;
						login.setAlertaLog(alerta);
						service.insertOrUpdate(login);
					}	
					if (login.getAlertaLog() > 5) {
						Alerts.showAlert("Fim do processo", null, "Limite de avisos ", AlertType.INFORMATION);
						mainNo();
					}
				}
			}	
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
	public Login getFormData() {
		Login obj = new Login();
 // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null

		String senha = "";
		if (passwordLoginSenha.getText() == null || passwordLoginSenha.getText().trim().contentEquals("")) {
			exception.addErros("senha", "Senha é obrigatória");
		} else {	
			senha = Cryptograf.criptografa(passwordLoginSenha.getText().toLowerCase());
			obj.setSenhaLog(senha);
		}
		login = service.findBySenha(senha);
		if (login == null) {
			exception.addErros("senha", "Senha inválida");
			mainNo();
		} else {
			mainOk();
			if (login.getNivelLog() == 9) {
				updateDados();
			}
			if (login.getNivelLog() == 1 || login.getNivelLog() == 9) {
				Optional<ButtonType> result = Alerts.showConfirmation("Usuário", "Deseja incluir novo usuário?");
				if (result.get() == ButtonType.OK) {
					dataV = login.getVencimentoLog();
					dataM = login.getMaximaLog();
					onbtNewAction();
				}
			}	
		}
		
		if (exception.getErros().size() > 0)
		{	throw exception;
		}
		return obj;
	} 
	
	private void updateDados() {
		login.updateVencimento(new Date());
		login.updateMaxima();
		login.setAlertaLog(0);
		nivel = login.getNivelLog();
		Login logAll = new Login();
		List<Login> listLog = new ArrayList<>();
		listLog = service.findAll();
		for (Login l : listLog) {
			if (l.getNumeroLog() != null) {
				logAll = service.findById(l.getNumeroLog());
				logAll.updateVencimento(data);
				logAll.updateMaxima();
				logAll.setAlertaLog(0);
				logAll.setAcessoLog(data);
				service.insertOrUpdate(logAll);
			}	
		}	
	}

	private void mainOk() {
		MainViewSgbController.senha = "Ok";
		MainViewSgbController.nivel = login.getNivelLog();
		MainViewSgbController.user = login.getNomeLog();
	}

	private void mainNo() {
		MainViewSgbController.senha = "null";
		MainViewSgbController.nivel = 0;
		MainViewSgbController.user = "usuário";
	}

	@FXML
	private void onbtNewAction() {
		classe = "Login ";
		Login log = new Login();
		createDialogForm(log, "/gui/sgb/LoginUserForm.fxml");
	}

	private void createDialogForm(Login log, String absoluteName) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			LoginUsertFormController controller = loader.getController();
			controller.dataMx = dataM;
			controller.dataVc = dataV;
			controller.numEmp = numEmp;
// injetando passando parametro obj 			
			controller.setLogin(log);
// injetando tb o forn service vindo da tela de formulario fornform
			controller.setLoginService(new LoginService());
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Novo login                                             ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
 	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		mainNo();
		Utils.currentStage(event).close();
	}
	
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setPasswordFieldMaxLength(passwordLoginSenha, 7);
  	}

 	public void updateFormData() {
 		if (entity == null) {
 			throw new IllegalStateException("Entidade esta nula");
 		}
 		String senha = null; 		
		entity.setSenhaLog(senha);
		passwordLoginSenha.setText(entity.getSenhaLog());
   	}
 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
  		labelErrorSenha.setText(fields.contains("senha") ? erros.get("senha") : "");		
	}

	public void subscribeDataChangeListenerMain(MainViewSgbController mainViewController) {
		dataChangeListeners.add(mainViewController);		
	}

	@Override
	public void onDataChanged() {
	}

}	

