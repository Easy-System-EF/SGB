package gui.sgb;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Cargo;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Situacao;
import gui.sgbmodel.service.CargoService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.SituacaoService;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.CpfCnpj;
import gui.util.Mascaras;
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

public class FuncionarioFormController implements Initializable {

	private Funcionario entity;
	
/*
 *  dependencia service com metodo set
 */
	private FuncionarioService service;
	private CargoService cargoService;
	private SituacaoService sitService;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField textCodigoFun;
	
	@FXML
	private TextField textNomeFun;
	
	@FXML
	private TextField textEnderecoFun;
	
	@FXML
	private TextField textBairroFun;
	
	@FXML
	private TextField textCidadeFun;
	
	@FXML
	private TextField textUfFun;
	
 	@FXML
	private TextField textCepFun;
	
	@FXML
	private TextField textDddFun;
	
	@FXML
	private TextField textTelefoneFun;
	
	@FXML
	private TextField textCpfFun;
	
  	@FXML
	private TextField textPixFun;

  	@FXML
 	private ComboBox<Cargo> comboBoxCargo;
 	
  	@FXML
 	private ComboBox<Situacao> comboBoxSituacao;
 	
   	@FXML
	private Button btSaveFun;
	
	@FXML
	private Button btCancelFun;
	
	@FXML
	private Label labelErrorNomeFun;

	@FXML
	private Label labelErrorEnderecoFun; 
	
	@FXML
	private Label labelErrorBairroFun; 
	
	@FXML
	private Label labelErrorCidadeFun;
	
	@FXML
	private Label labelErrorUfFun; 

	@FXML
	private Label labelErrorTelefoneFun; 

 	@FXML
	private Label labelErrorCpfFun; 

 	@FXML
 	private Label labelUser;

 // auxiliar
  	String classe = "Funcionário ";
 	public String user = "usuário";		
// flag p/ verificar UfFun	
	int flag = 0;
// compara cpf de registro novo	
	Integer codAnt = 999999;

	private ObservableList<Cargo>  obsListCargo ;
	private ObservableList<Situacao>  obsListSituacao ;

 	public void setFuncionario(Funcionario entity) {
		this.entity = entity;
	} 

 // 	 * metodo set /p service
 	public void setServices(FuncionarioService service,
 							CargoService cargoService,
 							SituacaoService sitService) {
		this.service = service;
		this.cargoService = cargoService;
		this.sitService = sitService;
	}
	
//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

/* 
 * vamos instanciar um forn e salvar no bco de dados
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
	public void onBtSaveFunAction(ActionEvent event) {
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
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (ParseException p) {
			p.printStackTrace();
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
	private Funcionario getFormData() throws ParseException {
		Funcionario obj = new Funcionario();
		Date data = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		int mm = cal.get(Calendar.MONTH) + 1;
		int aa = cal.get(Calendar.YEAR);
		List<Funcionario> listFun = service.findAll(aa, mm);
 // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setCodigoFun(Utils.tryParseToInt(textCodigoFun.getText()));
// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro
		if (textNomeFun.getText() == null || textNomeFun.getText().trim().contentEquals("")) {
			exception.addErros("nome", "Nome é obrigatório");
		}
		else {
			obj.setNomeFun(textNomeFun.getText());
			if (textNomeFun.getText().length() < 4) {
				exception.addErros("nome", "Nome muito curto");
			}	
		}
		
		if (textEnderecoFun.getText() == null || textEnderecoFun.getText().trim().contentEquals("")) {
			exception.addErros("endereco", "Endereço é obrigatório");
		}
		else {
			obj.setEnderecoFun(textEnderecoFun.getText());
			if (textEnderecoFun.getText().length() < 3) {
				exception.addErros("endereco", "Endereço muito curto");
			}
		}
		
 		if (textBairroFun.getText() == null || textBairroFun.getText().trim().contentEquals("")) {
			exception.addErros("bairro", "Bairro é obrigatório");
		}
 		else {
 			obj.setBairroFun(textBairroFun.getText());
			if (textBairroFun.getText().length() < 3) {
 				exception.addErros("bairro", "Bairro muito curto");
 			}	
		}
 		
		if (textCidadeFun.getText() == null || textCidadeFun.getText().trim().contentEquals("")) {
			exception.addErros("cidade", "Cidade é obrigatório");
		}
		else {
			obj.setCidadeFun(textCidadeFun.getText());
			if (textCidadeFun.getText().length() < 3) {
				exception.addErros("cidade", "Cidade muito curta");
			}	
		}
		
		if (textUfFun.getText() == null || textUfFun.getText().trim().contentEquals("")) {
			exception.addErros("uf", "UF é obrigatório");
		}
		else {
			obj.setUfFun(textUfFun.getText().toUpperCase());
  			verificaUfFun(obj);
  			if (obj.getUfFun()  == "XX") {
  				exception.addErros("uf", "UF não existe ");
  			}
		}	

		if (textCepFun.getText() == null || textCepFun.getText().trim().contentEquals("")) {
			exception.addErros("cep", "Cep é obrigatório");
		} else {
			String cep = "";
			cep = textCepFun.getText().replace("-", "");
			obj.setCepFun(cep);
			if (textCepFun.getText().length() < 8) {
				exception.addErros("cep", "Cep inválido");
			}	
		}
		
   		obj.setDddFun(Utils.tryParseToInt(textDddFun.getText()));
   		if (obj.getDddFun() != null) {
   			if (obj.getDddFun() < 11) {
				exception.addErros("telefone", "DDD inválido");
			}	
		}

		if (textTelefoneFun.getText() == null || textTelefoneFun.getText().trim().contentEquals("")) {
			exception.addErros("telefone", "Telefone é obrigatório");
		}
		else {
			obj.setTelefoneFun(Utils.tryParseToInt(textTelefoneFun.getText()));
			if (obj.getTelefoneFun() < 20000000) {
	  			exception.addErros("telefone", "Número telefone não existe");
	  		}	
		}
		
  		obj.setPixFun(textPixFun.getText());
 		
  	//validação cpf / nomeCargoFun  	 	
		String cpf = "";
		if (textCpfFun.getText() != null) {
			cpf = textCpfFun.getText().replace(".", "");
			cpf = cpf.replace("-", "");
		}	
		if (cpf == null) { 
			exception.addErros("cpf", "Cpf é obrigatório");
		} 
		obj.setCpfFun(cpf);
		if (obj.getCpfFun().length() < 11) {
  			exception.addErros("cpf", "Cpf incompleto");
  		} else {
  			String [] cpfV = new String[11];
  			for (int i = 0 ; i < cpfV.length ; i ++ ) {
  				char b = cpf.charAt(i);
  				String c = "" + b;
  				cpfV[i] = (c);
  			}	
  			int flagv = 0;
  			CpfCnpj.Cpf(flagv, cpfV);
  			if (CpfCnpj.Cpf(flagv, cpfV) == false) { 
  				exception.addErros("cpf", "Cpf inválido");
  			}
  			else { 				
  				if (codAnt != obj.getCodigoFun()) {
  					for (Funcionario f : listFun) {
  						if (f.getCpfFun().equals(obj.getCpfFun())) {
  							exception.addErros("telefone", "Cpf já existe");
  							exception.addErros("cpf", f.getCodigoFun() + " " + f.getNomeFun());
  						}	
  					}	
  				}
  			}
  		}
		
 		obj.setCargo(comboBoxCargo.getValue());
 		obj.setCargoFun(comboBoxCargo.getValue().getNomeCargo()); 
 		
 		obj.setSituacao(comboBoxSituacao.getValue());
 		obj.setSituacaoFun(comboBoxSituacao.getValue().getNomeSit());
 		obj.setComissaoFun(0.00);
 		obj.setAdiantamentoFun(0.00);
 		obj.setSalarioFun(comboBoxCargo.getValue().getSalarioCargo());
 		if (entity.getMesFun() == null) {
 			obj.setMesFun(mm);
 			obj.setAnoFun(aa);
 		}	else {
 			obj.setMesFun(entity.getMesFun());
 			obj.setAnoFun(entity.getAnoFun());
 		}
 			

  // tst se houve algum (erro com size > 0)		
		if (exception.getErros().size() > 0){
			throw exception;
		}
		return obj;
	} 
	
	private static Funcionario verificaUfFun(Funcionario obj) {
   		int flag = 0;
  		int i = 0;
  		String vuf = obj.getUfFun();
  		String tabelaUfFun = "AC, AL, AM, AP, AC, BA, CE, DF, GO, MA, MG, MT, MS, PB, PE, PI, PR, "
       		         	+ "RJ, RN, RO, RR, RS, SE, SC, SP, TO";
  		String[] tabuf = tabelaUfFun.split(",");
	  	
  		for ( i = 0 ; i < tabuf.length ; i ++)
  			{	Boolean fv = tabuf[i].contains(vuf) ;
  				if (fv == true )
  				{	flag = 1;	}
  			}	
  		if (flag == 0)
  		{	obj.setUfFun("XX"); 	}
  		return obj;
  	}
  	
// msm processo save p/ fechar	
	@FXML
	public void onBtCancelFunAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {  		
 		Constraints.setTextFieldInteger(textCodigoFun);
// 		Constraints.setTextFieldInteger(textCepFun);
 		Constraints.setTextFieldInteger(textDddFun);
 		Constraints.setTextFieldInteger(textTelefoneFun);
// 		Constraints.setTextFieldInteger(textCpfFun);
 		Constraints.setTextFieldMaxLength(textNomeFun, 40);
 		Constraints.setTextFieldMaxLength(textCepFun, 9);
 		Constraints.setTextFieldMaxLength(textUfFun, 2);
 		Constraints.setTextFieldMaxLength(textDddFun, 2);
 		Constraints.setTextFieldMaxLength(textTelefoneFun, 9);
   		Constraints.setTextFieldMaxLength(textPixFun, 40);
   		Constraints.setTextFieldMaxLength(textCpfFun, 14);
		initializeComboBoxCargo();
		initializeComboBoxSituacao();
  	}
  	
	private void initializeComboBoxCargo() {
		Callback<ListView<Cargo>, ListCell<Cargo>> factory = lv -> new ListCell<Cargo>() {
			@Override
			protected void updateItem(Cargo item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeCargo());
 			}
		};
		
		comboBoxCargo.setCellFactory(factory);
		comboBoxCargo.setButtonCell(factory.call(null));
	}		
 
	private void initializeComboBoxSituacao() {
		Callback<ListView<Situacao>, ListCell<Situacao>> factory = lv -> new ListCell<Situacao>() {
			@Override
			protected void updateItem(Situacao item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeSit());
 			}
		};
		
		comboBoxSituacao.setCellFactory(factory);
		comboBoxSituacao.setButtonCell(factory.call(null));
	}		
 
 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
  	public void updateFormData() throws ParseException {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
 		String cep = null;
 		String cepM = null;
 		String cpf = null;
  		String cpfM = null;
  		labelUser.setText(user);
//  string value of p/ casting int p/ string 		
 		textCodigoFun.setText(String.valueOf(entity.getCodigoFun()));
 		textNomeFun.setText(entity.getNomeFun());
 		textEnderecoFun.setText(entity.getEnderecoFun());
 
 		textCepFun.setText(entity.getCepFun());
 		if (entity.getCepFun() != null)
 		{	cep = textCepFun.getText();
   			cepM = Mascaras.formataString(cep, "Cep");
			textCepFun.setText(cepM);
 		}
 		textBairroFun.setText(entity.getBairroFun());
 		
 		textCidadeFun.setText(entity.getCidadeFun());
 		
 		textUfFun.setText(entity.getUfFun());
 		
 		if (entity.getDddFun() == null) {
 			entity.setDddFun(0);
 		}
 		textDddFun.setText(String.valueOf(entity.getDddFun()));
 		
 		if (entity.getTelefoneFun() == null) {
 			entity.setTelefoneFun(0);
 		}
 		textTelefoneFun.setText(String.valueOf(entity.getTelefoneFun()));
 		
   	 	textPixFun.setText(entity.getPixFun());
 
	 	textCpfFun.setText(entity.getCpfFun());
   	 	if (entity.getCpfFun() != null ) {
  	 		cpf = textCpfFun.getText();
  	 		cpfM = Mascaras.formataString(cpf, "Cpf");
   	 		textCpfFun.setText(cpfM);
   	 		codAnt = entity.getCodigoFun();
  	 	}	
  	 	
 		if (entity.getCargo() == null) {
			comboBoxCargo.getSelectionModel().selectFirst();
		} else {
 			comboBoxCargo.setValue(entity.getCargo());
		}

 		if (entity.getSituacao() == null) {
			comboBoxSituacao.getSelectionModel().selectFirst();
		} else {
 			comboBoxSituacao.setValue(entity.getSituacao());
		}
 		
   	}

	public void loadAssociatedObjects() {
		if (cargoService == null) {
			throw new IllegalStateException("Cargo Serviço esta nulo");
		}
		if (sitService == null) {
			throw new IllegalStateException("Situação Serviço esta nulo");
		}
// buscando (carregando) os cargos q estão no bco de dados via Dialogform		
		List<Cargo> list = cargoService.findAll();
		List<Situacao> listSit = sitService.findAll();
// transf p/ obslist		
		obsListCargo = FXCollections.observableArrayList(list);
		obsListSituacao = FXCollections.observableArrayList(listSit);
		comboBoxCargo.setItems(obsListCargo);
		comboBoxSituacao.setItems(obsListSituacao);
 	}
  	
// mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
 		if (fields.contains("nome")) 
 		{	labelErrorNomeFun.setText(erros.get("nome"));	
 		}
 		labelErrorNomeFun.setText((fields.contains("nome") ? erros.get("nome") : ""));		
 		labelErrorEnderecoFun.setText((fields.contains("endereco") ? erros.get("endereco") : ""));		
		labelErrorBairroFun.setText((fields.contains("bairro") ? erros.get("bairro") : ""));
		labelErrorCidadeFun.setText((fields.contains("cidade") ? erros.get("cidade") : ""));
		labelErrorUfFun.setText((fields.contains("uf") ? erros.get("uf") : ""));
 		labelErrorTelefoneFun.setText((fields.contains("telefone") ? erros.get("telefone") : ""));		
   		labelErrorCpfFun.setText((fields.contains("cpf") ? erros.get("cpf") : ""));		
   	}	
 } 
