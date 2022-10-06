package gui.sgb;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgcpmodel.entites.consulta.ParPeriodo;
import gui.sgcpmodel.service.ParPeriodoService;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import model.exception.ValidationException;

public class RecPeriodoFormController implements Initializable {

	private ParPeriodo entity;
 
/*
 *  dependencia service com metodo set
 */
	private ParPeriodoService perService;
 
// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private DatePicker dpInicial; 
	
	@FXML
	private DatePicker dpFinal;
	
 	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Label labelErrorDataInicial;

	@FXML
	private Label labelErrorDataFinal;

	private ObservableList<ParPeriodo> obsListPer;
 
 	public void setPeriodo(ParPeriodo entity) {
		this.entity = entity;
 	}	
		
  // 	 * metodo set /p service
 	public void setPeriodoService(ParPeriodoService perService) {
		this.perService = perService;
 	}
 	
// auxiliar
 	String classe = "Periodo";
 	
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
	public void onBtOkAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula");
		}
		if (perService == null)
		{	throw new IllegalStateException("Serviço nulo");
		}
		try {
			List<ParPeriodo> listPer = perService.findAll();
     		entity = getFormData();
 			for (ParPeriodo p : listPer) {
 			 	entity.setIdPeriodo(p.getIdPeriodo());
 			 	entity.setFornecedor(p.getFornecedor());
    		 	entity.setTipoConsumo(p.getTipoConsumo());
 			} 
 			perService.update(entity);
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
	private ParPeriodo getFormData() {
		ParPeriodo obj = new ParPeriodo();
 // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
 
		if (dpInicial.getValue() == null) {
			exception.addErros("dtiPeriodo", "data inicial é obrigatória");
		}
		else {
			Instant instant = Instant.from(dpInicial.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDtiPeriodo(Date.from(instant));
		}

		if (dpFinal.getValue() == null) {
			exception.addErros("dtfPeriodo", "data final é obrigatória");
		}
		else {
			Instant instant = Instant.from(dpFinal.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDtfPeriodo(Date.from(instant));
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
		Utils.formatDatePicker(dpInicial, "dd/MM/yyyy");
		Utils.formatDatePicker(dpFinal, "dd/MM/yyyy");
   	}

 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
 		
 //  string value of p/ casting int p/ string 		
 
   		if (entity.getDtiPeriodo() != null) {
			dpInicial.setValue(LocalDate.ofInstant(entity.getDtiPeriodo().toInstant(), ZoneId.systemDefault()));
 		}

   		if (entity.getDtfPeriodo() != null) {
			dpFinal.setValue(LocalDate.ofInstant(entity.getDtfPeriodo().toInstant(), ZoneId.systemDefault()));
 		}
   	}
 	
//	carrega dados do bco  dentro obslist
	public void loadAssociatedObjects() {
		if (perService == null) {
			throw new IllegalStateException("FornecedorServiço esta nulo");
		}
// buscando (carregando)  q estão no bco de dados		
		List<ParPeriodo> list = perService.findAll();
 // transf p/ obslist		
		obsListPer = FXCollections.observableArrayList(list);
  	}
  	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
		labelErrorDataInicial.setText((fields.contains("dtiPeriodo") ? erros.get("dtiPeriodo") : ""));
		labelErrorDataFinal.setText((fields.contains("dtfPeriodo") ? erros.get("dtfPeriodo") : ""));
 	}
}	

