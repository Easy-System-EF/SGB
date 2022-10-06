package gui.sgb;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Grupo;
import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.GrupoService;
import gui.sgbmodel.service.ProdutoService;
import gui.util.Alerts;
import gui.util.Constraints;
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

public class ProdutoFormController implements Initializable {

	private Produto entity;
	
/*
 *  dependencia service com metodo set
 */
	private ProdutoService service;
	private GrupoService gruService;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField textCodigoProd;
	
	@FXML
	private ComboBox<Grupo> comboBoxGrupo;
	
	@FXML
	private TextField textNomeProd;
	
 	@FXML
	private TextField textVendaProd;
	
	@FXML
	private TextField textEstMinProd;
	
   	@FXML
	private Button btSaveProd;
	
	@FXML
	private Button btCancelProd;
	
	@FXML
	private Label labelErrorNomeProd;

	@FXML
	private Label labelErrorGrupoProd; 
	
	@FXML
	private Label labelErrorVendaProd; 

	private ObservableList<Grupo>  obsListGru;

	@FXML
	private Label labelUser;

	// auxiliar
	Date dataAnt = null;
	String classe = "Produto ";
	public String user = "usuário";		
	
  	public void setProduto(Produto entity) {
		this.entity = entity;
	}

  	// 	 * metodo set /p service
 	public void setProdutoService(ProdutoService service) {
		this.service = service;
	}
	
 	public void setGrupoService(GrupoService gruService) {
		this.gruService = gruService;
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
	public void onBtSaveProdAction(ActionEvent event) {
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
	private Produto getFormData() throws ParseException {
		Locale ptBR = new Locale("pt", "BR");
		Produto obj = new Produto();
		
  // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setCodigoProd(Utils.tryParseToInt(textCodigoProd.getText()));
// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro
		if (textNomeProd.getText() == null || textNomeProd.getText().trim().contentEquals("")) {
			exception.addErros("nome", "Nome é obrigatório");
		} 
		obj.setNomeProd(textNomeProd.getText());
		if (textNomeProd.getText() != null) {
			if (textNomeProd.getText().length() < 3) {
				exception.addErros("nome", "Nome muito curto");
			}	
		}
		
		if (textVendaProd.getText() == null || textVendaProd.getText().trim().contentEquals("")) {
			exception.addErros("venda", "Valor é obrigatório");
			obj.setVendaProd(0.0);
		}
		obj.setVendaProd(Utils.formatDecimalIn(textVendaProd.getText().replace(".", "")));
		if (obj.getVendaProd() == 0.00) {
			exception.addErros("venda", "Valor é obrigatório");
		}
		
		obj.setEntradaProd(0.00);
		obj.setSaidaProd(0.00);
		obj.setPrecoProd(0.00);

		if (textEstMinProd.getText() == null || textEstMinProd.getText().trim().contentEquals("")) {
			obj.setEstMinProd(0.00);
		}
		obj.setEstMinProd(Utils.formatDecimalIn(textEstMinProd.getText().replace(".", "")));
		
 		obj.setGrupo(comboBoxGrupo.getValue());
 		obj.setGrupoProd(comboBoxGrupo.getValue().getCodigoGru());

 		if (dataAnt == null) {
 			obj.setDataCadastroProd(new Date());
 		}
 		else {
 			obj.setDataCadastroProd(dataAnt);
 		}
 		// tst se houve algum (erro com size > 0)		
		if (exception.getErros().size() > 0)
		{	throw exception;
		}
		return obj;
	} 
	
  // msm processo save p/ fechar	
	@FXML
	public void onBtCancelProdAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
 		Constraints.setTextFieldInteger(textCodigoProd);
  		Constraints.setTextFieldMaxLength(textNomeProd, 40);
		initializeComboBoxGrupo();
	}

	private void initializeComboBoxGrupo() {
		Callback<ListView<Grupo>, ListCell<Grupo>> factory = lv -> new ListCell<Grupo>() {
			@Override
			protected void updateItem(Grupo item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeGru());
 			}
		};
		
		comboBoxGrupo.setCellFactory(factory);
		comboBoxGrupo.setButtonCell(factory.call(null));
	}		


 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
  	public void updateFormData() throws ParseException {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
		ValidationException exception = new ValidationException("Validation exception");
		labelUser.setText(user);
//  string value of p/ casting int p/ string 		
 		textCodigoProd.setText(String.valueOf(entity.getCodigoProd()));
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
 		if (entity.getGrupo() == null) {
			comboBoxGrupo.getSelectionModel().selectFirst();
		} else {
 			comboBoxGrupo.setValue(entity.getGrupo());
		}

 		if (textNomeProd.getText() != null) {
 			textNomeProd.setText(entity.getNomeProd());
  		}
 			
 		if (entity.getVendaProd() == null) {
 			entity.setVendaProd(0.00);
 		}
 		String venda = Mascaras.formataValor(entity.getVendaProd());	
		textVendaProd.setText(venda); 
 		
		if (entity.getEstMinProd() == null) {
			entity.setEstMinProd(0.00);
		}
		String estMin = Mascaras.formataValor(entity.getEstMinProd());
		textEstMinProd.setText(estMin);

		if (entity.getDataCadastroProd() != null) {
			dataAnt = entity.getDataCadastroProd();
		}
    }
  	
//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (gruService == null) {
			throw new IllegalStateException("GrupoServiço esta nulo");
		}
// buscando (carregando) os forn q estão no bco de dados		
		List<Grupo> list = gruService.findAll();
// transf p/ obslist		
		obsListGru = FXCollections.observableArrayList(list);
		comboBoxGrupo.setItems(obsListGru);
 	}
  
// mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
  		labelErrorNomeProd.setText((fields.contains("nome") ? erros.get("nome") : ""));		
 		labelErrorGrupoProd.setText((fields.contains("grupo") ? erros.get("grupo") : ""));		
		labelErrorVendaProd.setText((fields.contains("venda") ? erros.get("venda") : ""));
   	}	
 } 
