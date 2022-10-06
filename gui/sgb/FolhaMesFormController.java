package gui.sgb;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Anos;
import gui.sgbmodel.entities.FolhaMes;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Meses;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.AnosService;
import gui.sgbmodel.service.FolhaMesService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.MesesService;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.exception.ValidationException;

public class FolhaMesFormController implements Initializable {

	private FolhaMes entity;
/*
 *  dependencia service com metodo set
 */
	private FolhaMesService service;
	private AdiantamentoService adService;
	private FuncionarioService funService;
	private MesesService mesService;
	private AnosService anoService;

	private Meses objMes;
	private Anos objAno;
	private Funcionario objFun;

 	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private ComboBox<Meses>  comboBoxMeses; 
	
	@FXML
	private ComboBox<Anos>  comboBoxAnos; 
	
  	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Label labelErrorComboBoxMeses;

	@FXML
	private Label labelErrorComboBoxAnos;

 	private ObservableList<Meses> obsListMes;
 	private ObservableList<Anos> obsListAno;
 	
//auxiliar
 	String classe = "";
 
 	public void setFolhaMes(FolhaMes entity,
 							Meses objMes,
 							Anos objAno,
 							Funcionario objFun) {		
		this.entity = entity;
		this.objMes = objMes;
		this.objAno = objAno;
		this.objFun = objFun;
	}

 // 	 * metodo set /p service
 	public void setServices(FolhaMesService service, 
 							AdiantamentoService adService,
 							FuncionarioService funService,
 							MesesService mesService,
 							AnosService anoService) {
 		this.service = service;
 		this.adService = adService;
 		this.funService = funService;
 		this.mesService = mesService;
 		this.anoService = anoService;
	}
  	
//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtOkAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula");
		}
		try {
     		entity = getFormData();
     		montaFolha(entity);
   	    	notifyDataChangeListerners();
	    	Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
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
	private FolhaMes getFormData() {
		FolhaMes obj = new FolhaMes();
 // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");

		obj.setMeses(comboBoxMeses.getValue());
 		if (obj.getMeses() == null) {
 		 	exception.addErros("meses", "mes inválido");
		}

		obj.setAnos(comboBoxAnos.getValue());
 		if (obj.getAnos() == null) {
 		 	exception.addErros("anos", "ano inválido");
		}

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

	private void montaFolha(FolhaMes entity2) {
 		if (adService == null) {
			throw new IllegalStateException("Serviço Adiantamento está vazio");
 		}
 		if (funService == null) {
			throw new IllegalStateException("Serviço Funcionarios está vazio");
 		}
 		if (service == null) { 
			throw new IllegalStateException("Serviço FolhaFolha está vazio");
 		}
 		if (mesService == null) {
			throw new IllegalStateException("Serviço Meses está vazio");
 		}
 		if (anoService == null) {
			throw new IllegalStateException("Serviço Anos está vazio");
 		}
 		try {
			classe = "FolhaMes 1 ";
			zeraMes();
			int segue = 0;

			classe = "Meses Folha ";
			objMes = mesService.findId(entity2.getMeses().getNumeroMes());
			FolhaMesListController.mes = ": " + objMes.getNomeMes();
			classe = "Anos Folha ";
			objAno = anoService.findAno(entity2.getAnos().getAnoAnos());
			
			classe = "Funcionario Folha 1 ";
 			List<Funcionario> fun = funService.findAll(objAno.getAnoAnos(), objMes.getNumeroMes());
 			fun.removeIf(x -> x.getNomeFun().contains("Consumo Próprio"));
 			if (fun.size() > 0) {
 				fun.add(objFun);
 				for (Funcionario f : fun) {
 					if (f.getCodigoFun() != null) {
 						objFun = funService.findById(f.getCodigoFun());
 						objFun.setComissaoFun(0.00);
 						objFun.setAdiantamentoFun(0.00);
 						objFun.setSalarioFun(0.00);
 						funService.saveOrUpdate(objFun);
 					}	
 				}	
 			} else {
 				segue = 1;
 			}
 			
// confere salario
 			AdiantamentoUpdate.updateSemSalario(objMes.getNumeroMes(), objAno.getAnoAnos());
 			
 			classe = "Adiantamento Folha 1";
 			List<Adiantamento> adianto = adService.findMes(objMes.getNumeroMes(), objAno.getAnoAnos());
			for (Funcionario f : fun) {
				for (Adiantamento a : adianto) {
					if (a.getNumeroAdi() != null) {
						if (a.getNomeAdi().equals(f.getNomeFun())) {
							classe = "Funcionario Folha 2 ";
							objFun = funService.findById(f.getCodigoFun());
							objFun.totalComissaoFun(a.getComissaoAdi());
							objFun.totalAdiantamentoFun(a.getValeAdi());
							if (a.getSalarioAdi() > 0) {
								objFun.setSalarioFun(a.getSalarioAdi());
							}	
							funService.saveOrUpdate(objFun);
						}	
					}
				}
			}

 			if (segue == 0) {
 				Double tot = 0.0;			
 				classe = "Funcionario 3";
 				fun = funService.findByAtivo("Ativo", objAno.getAnoAnos(), objMes.getNumeroMes());
 				fun.removeIf(x -> x.getNomeFun().contains("Consumo Próprio"));
 				classe = "Folha Folha 2 ";
 				for (Funcionario f : fun) {
					entity.setNumeroFolha(null);
					if (!f.getNomeFun().contains("Consumo Próprio")) {
						entity.setFuncionarioFolha(f.getNomeFun());
						entity.setCargoFolha(f.getCargoFun());
						entity.setSituacaoFolha(f.getSituacaoFun());
						entity.setSalarioFolha(Mascaras.formataValor(f.getSalarioFun())); 
						entity.setComissaoFolha(Mascaras.formataValor(f.getComissaoFun()));
						entity.setValeFolha(Mascaras.formataValor(f.getAdiantamentoFun()));
						entity.setReceberFolha(Mascaras.formataValor(f.totalMesFun()));
						tot += f.totalMesFun();
						entity.setTotalFolha(Mascaras.formataValor(tot));
						entity.setMeses(objMes);
						entity.setAnos(objAno);					
						service.insert(entity);
					}	
 				}
 			}	
 		}
 		catch (ParseException p) {
 			p.getStackTrace();
 		}
	}
	
	public void zeraMes() {
		service.zeraAll();
	}

/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeComboBoxMeses();
		initializeComboBoxAnos();
    	}

	private void initializeComboBoxMeses() {
		Callback<ListView<Meses>, ListCell<Meses>> factory = lv -> new ListCell<Meses>() {
			@Override
			protected void updateItem(Meses item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeMes());
 			}
		};
		
		comboBoxMeses.setCellFactory(factory);
		comboBoxMeses.setButtonCell(factory.call(null));
	}		
   	
	private void initializeComboBoxAnos() {
		Callback<ListView<Anos>, ListCell<Anos>> factory = lv -> new ListCell<Anos>() {
			@Override
			protected void updateItem(Anos item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getAnoStrAnos());
 			}
		};
		
		comboBoxAnos.setCellFactory(factory);
		comboBoxAnos.setButtonCell(factory.call(null));
	}		
   	
 /*
  * transforma string da tela p/ o tipo no bco de Folha 
  */
 	public void updateFormData() {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
 		if (entity.getMeses() == null) {
			comboBoxMeses.getSelectionModel().selectFirst();
		} else {
 			comboBoxMeses.setValue(entity.getMeses());
		}
 		if (entity.getAnos() == null) {
			comboBoxAnos.getSelectionModel().selectFirst();
		} else {
 			comboBoxAnos.setValue(entity.getAnos());
		}
     }
 	
//	carrega Folha do bco  dentro obslist
	public void loadAssociatedObjects() {
		if (mesService == null) {
			throw new IllegalStateException("MesesServiço esta nulo");
		}
// buscando (carregando) os forn q estão no bco de Folha		
		List<Meses> listMes = mesService.findAll();
 		obsListMes = FXCollections.observableArrayList(listMes);
		comboBoxMeses.setItems(obsListMes);
		List<Anos> listAno = anoService.findAll();
 		obsListAno = FXCollections.observableArrayList(listAno);
		comboBoxAnos.setItems(obsListAno);
  	}
  	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
		labelErrorComboBoxMeses.setText((fields.contains("meses") ? erros.get("meses") : ""));
 		labelErrorComboBoxAnos.setText((fields.contains("anos") ? erros.get("anos") : ""));
  	}
}	