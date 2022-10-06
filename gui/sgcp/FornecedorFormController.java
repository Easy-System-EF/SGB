package gui.sgcp;

import java.net.URL;
import java.text.ParseException;
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

public class FornecedorFormController implements Initializable {

	private Fornecedor entity;
	
/*
 *  dependencia service com metodo set
 */
	private FornecedorService service;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField textCodigo;
	
	@FXML
	private TextField textRazaoSocial;
	
	@FXML
	private TextField textRua;
	
	@FXML
	private TextField textNumero;
	
	@FXML
	private TextField textComplemento;
	
	@FXML
	private TextField textCep;
	
	@FXML
	private TextField textBairro;
	
	@FXML
	private TextField textCidade;
	
	@FXML
	private TextField textUF;
	
	@FXML
	private TextField textDdd01;
	
	@FXML
	private TextField textTelefone01;
	
	@FXML
	private TextField textDdd02;
	
	@FXML
	private TextField textTelefone02;
	
	@FXML
	private TextField textContato;
	
	@FXML
	private TextField textDddContato;
	
	@FXML
	private TextField textTelefoneContato;
	
	@FXML
	private TextField textEmail;
	
	@FXML
	private TextField textPix;
	
	@FXML
	private TextField textObservacao;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Label labelErrorRazaoSocial;

	@FXML
	private Label labelErrorRua; 
	
	@FXML
	private Label labelErrorBairro; 
	
	@FXML
	private Label labelErrorCidade;
	
	@FXML
	private Label labelErrorCep; 
	
	@FXML
	private Label labelErrorUF; 

	@FXML
	private Label labelErrorTelefone01; 

	@FXML
	private Label labelErrorEmail; 

	@FXML
	private Label labelUser;

	String classe = "Fornecedor ";
	public String user = "usuário";	
	 	
// flag p/ verificar UF	
		int flag = 0;
 
 	public void setFornecedor(Fornecedor entity) {
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setFornecedorService(FornecedorService service) {
		this.service = service;
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
	public void onBtSaveAction(ActionEvent event) {
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
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setCodigo(Utils.tryParseToInt(textCodigo.getText()));
// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro
		if (textRazaoSocial.getText() == null || textRazaoSocial.getText().trim().contentEquals("")) {
			exception.addErros("razaoSocial", "Razão Social é obrigatório");
		}
		obj.setRazaoSocial(textRazaoSocial.getText());
		if (obj.getRazaoSocial() != null) {
			if (obj.getRazaoSocial().length() < 5) {
				exception.addErros("razaoSocial", "Razão Social muito curta");
			}	
		}	
			
		if (textRua.getText() == null || textRua.getText().trim().contentEquals("")) {
			exception.addErros("rua", "Rua é obrigatório");
		}
		obj.setRua(textRua.getText());
		if (obj.getRua() != null) {
			if (textRua.getText().length() < 3) {
				exception.addErros("rua", "Rua muito curta");
			}	
		}

		obj.setNumero(Utils.tryParseToInt(textNumero.getText()));

		obj.setComplemento(textComplemento.getText());
		
  		String cep = "";
		if (textCep.getText() != null) {
			cep = textCep.getText().replace("-", "");
			obj.setCep(Utils.tryParseToInt(cep));
			if (textCep.getText().length() < 8) {
				exception.addErros("cep", " CEP inválido");
			}	
		}
		 
		if (textBairro.getText() != null) {
			if (textBairro.getText().length() < 3) {
				exception.addErros("bairro", "Bairro de ser inválido");
			}
		}
		obj.setBairro(textBairro.getText());
			
		if (textCidade.getText() != null) {
			if (textCidade.getText().length() < 3) {
				exception.addErros("cidade", "Cidade de ser inválida");
			}
		}
		obj.setCidade(textCidade.getText());

		obj.setUf(textUF.getText());
// verifica se UF existe
  		if (obj.getUf() != null) {
  			verificaUf(obj);
  		}
		if (obj.getUf()  == "XX") {
			exception.addErros("uf", "UF não existe ");
		}
		
   		obj.setDdd01(Utils.tryParseToInt(textDdd01.getText()));
   		obj.setTelefone01(Utils.tryParseToInt(textTelefone01.getText()));
		if (obj.getTelefone01()  == null || obj.getTelefone01() <  10000001) {
			exception.addErros("telefone01", "1º Telefone é obrigatório");
		}
		
  		obj.setDdd02(Utils.tryParseToInt(textDdd02.getText()));
  		obj.setTelefone02(Utils.tryParseToInt(textTelefone02.getText()));
		obj.setContato(textContato.getText());
  		obj.setDddContato(Utils.tryParseToInt(textDddContato.getText()));
 		obj.setTelefoneContato(Utils.tryParseToInt(textTelefoneContato.getText()));

 		if (textEmail.getText() != null) {
 	 		if (textEmail.getText().length() < 5) {
 			exception.addErros("email", "Email deve ser inválido");
 	 		}
 		}	
		obj.setEmail(textEmail.getText());

		obj.setPix(textPix.getText());
		obj.setObservacao(textObservacao.getText());
 		
// tst se houve algum (erro com size > 0)		
		if (exception.getErros().size() > 0)
		{	throw exception;
		}
		return obj;
	} 
	
	private static Fornecedor verificaUf(Fornecedor obj) {
   		int flag = 0;
  		int i = 0;
  		String vuf = obj.getUf();
  		String tabelaUF = "AC,AL,AM, AP,AC, BA, CE, DF, GO, MA, MG, MT, MS, PB, PE, PI, PR, "
       		         	+ "RJ, RN, RO, RR, RS, SE, SC, SP, TO, '  ', 'null'";
  		String[] tabuf = tabelaUF.split(",");
	  	
  		for ( i = 0 ; i < tabuf.length ; i ++)
  			{	Boolean fv = tabuf[i].contains(vuf) ;
  				if (fv == true )
  				{	flag = 1;	}
  			}	
  		if (flag == 0)
  		{	obj.setUf("XX"); 	}
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
 		Constraints.setTextFieldInteger(textCodigo);
 		Constraints.setTextFieldInteger(textNumero);
// 		Constraints.setTextFieldInteger(textCep);
 		Constraints.setTextFieldInteger(textDdd01);
 		Constraints.setTextFieldInteger(textTelefone01);
 		Constraints.setTextFieldInteger(textDdd02);
 		Constraints.setTextFieldInteger(textTelefone02);
 		Constraints.setTextFieldInteger(textDddContato);
 		Constraints.setTextFieldInteger(textTelefoneContato);

 		Constraints.setTextFieldMaxLength(textRazaoSocial, 50);
 		Constraints.setTextFieldMaxLength(textCep, 9);
 		Constraints.setTextFieldMaxLength(textUF, 2);
 		Constraints.setTextFieldMaxLength(textDdd01, 2);
 		Constraints.setTextFieldMaxLength(textTelefone01, 9);
 		Constraints.setTextFieldMaxLength(textDdd02, 2);
 		Constraints.setTextFieldMaxLength(textTelefone02, 9);
 		Constraints.setTextFieldMaxLength(textDddContato, 2);
 		Constraints.setTextFieldMaxLength(textTelefoneContato, 9);
 		Constraints.setTextFieldMaxLength(textObservacao, 50);
 	}

 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() throws ParseException {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
 		limpaNumeros(entity);
 		String cep = null;
 		String cepM = null;
 		labelUser.setText(user);
 //  string value of p/ casting int p/ string 		
 		textCodigo.setText(String.valueOf(entity.getCodigo()));
 		textRazaoSocial.setText(entity.getRazaoSocial());
 		textRua.setText(entity.getRua());
 		textNumero.setText(String.valueOf(entity.getNumero()));
 		textComplemento.setText(entity.getComplemento());
 		if (entity.getCep() != null)
 		{	cep = (String.valueOf(entity.getCep()));
   			cepM = Mascaras.formataString(cep, "Cep");
   			textCep.setText(cepM);
 		}
  		textBairro.setText(entity.getBairro());
 		textCidade.setText(entity.getCidade());
 		textUF.setText(entity.getUf());
 		textDdd01.setText(String.valueOf(entity.getDdd01()));
 		textTelefone01.setText(String.valueOf(entity.getTelefone01()));
 		textDdd02.setText(String.valueOf(entity.getDdd02()));
 		textTelefone02.setText(String.valueOf(entity.getTelefone02()));
 		textContato.setText(entity.getContato());
 		textDddContato.setText(String.valueOf(entity.getDddContato()));
 		textTelefoneContato.setText(String.valueOf(entity.getTelefoneContato()));
 	 	textEmail.setText(entity.getEmail());
 		textPix.setText(entity.getPix());
 		textObservacao.setText(entity.getObservacao());
  	}
 	
 	private void limpaNumeros(Fornecedor entity) {
 		if (entity.getNumero() == null)
 		{ entity.setNumero(0); 	}	
  		if (entity.getDdd01() == null)
 		{ entity.setDdd01(0); 	}	
 		if (entity.getDdd02() == null)
 		{ entity.setDdd02(0); 	}	
 		if (entity.getDddContato() == null)
 		{ entity.setDddContato(0); 	}	
 		if (entity.getTelefone01() == null)
 		{ entity.setTelefone01(0); 	}	
 		if (entity.getTelefone02() == null)
 		{ entity.setTelefone02(0); 	}	
 		if (entity.getTelefoneContato() == null)
 		{ entity.setTelefoneContato(0); 	}	
  	}
 	
// mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
 		if (fields.contains("razaoSocial")) 
 		{	labelErrorRazaoSocial.setText(erros.get("razaoSocial"));	
 		}
		labelErrorRua.setText((fields.contains("rua") ? erros.get("rua") : ""));
		labelErrorUF.setText((fields.contains("uf") ? erros.get("uf") : ""));
		labelErrorCep.setText((fields.contains("cep") ? erros.get("cep") : ""));
		labelErrorBairro.setText((fields.contains("bairro") ? erros.get("bairro") : ""));
		labelErrorCidade.setText((fields.contains("cidade") ? erros.get("cidade") : ""));
		labelErrorEmail.setText((fields.contains("email") ? erros.get("email") : ""));
  		if (fields.contains("telefone01")) 
 		{	labelErrorTelefone01.setText(erros.get("telefone01"));	
 		}
 	}	
 } 
