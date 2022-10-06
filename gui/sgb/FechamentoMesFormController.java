package gui.sgb;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Anos;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.FechamentoMes;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Meses;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.AnosService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.CartelaVirtualService;
import gui.sgbmodel.service.FechamentoMesService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.MesesService;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.service.ParcelaService;
import gui.sgcpmodel.service.TipoConsumoService;
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

public class FechamentoMesFormController implements Initializable {

	private FechamentoMes entity;
	private FechamentoMes fechMes = new FechamentoMes();
	private Meses objMes;
	private Anos objAno;
	private Funcionario objFun;
/*
 *  dependencia service com metodo set
 */
	private FechamentoMesService service;
	private AdiantamentoService adiService;
	private MesesService mesService;
	private AnosService anoService;
	private CartelaService carService;
	private CartelaVirtualService virService;
	private FuncionarioService funService;
	private TipoConsumoService tipoService;
	private ParcelaService parService;

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
 	int mm = 0;
 	int aa = 0;
 
 	public void setFechMesEntityes(FechamentoMes entity,
 								 Funcionario objFun,
 								 Meses objMes,
 								 Anos objAno ) {		
		this.entity = entity;
		this.objFun = objFun;
		this.objMes = objMes;
		this.objAno = objAno;
	}

 // 	 * metodo set /p service
 	public void setServices(FechamentoMesService service,
							AdiantamentoService adiService,
 							MesesService mesService,
 							AnosService anoService,
 							CartelaService carService,
 							CartelaVirtualService virService,
 							FuncionarioService funService,
 							TipoConsumoService tipoService,
 							ParcelaService parService) {
 		this.service = service;
 		this.adiService = adiService;
 		this.mesService = mesService;
 		this.anoService = anoService;
 		this.carService = carService;
 		this.virService = virService;
 		this.funService = funService;
 		this.tipoService = tipoService;
 		this.parService = parService;
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
     		montafechMesMensal();
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
	private FechamentoMes getFormData() {
		FechamentoMes obj = new FechamentoMes();
 // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");

		obj.setMes(comboBoxMeses.getValue());
		mm = comboBoxMeses.getValue().getNumeroMes();
 		if (obj.getMes() == null) {
 		 	exception.addErros("meses", "mes inválido");
		}

		obj.setAno(comboBoxAnos.getValue());
		aa = comboBoxAnos.getValue().getAnoAnos();
 		if (obj.getAno() == null) {
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

	private void montafechMesMensal() {
 		if (service == null) { 
			throw new IllegalStateException("Serviço FechamentoMesMensal está vazio");
 		}
 		if (adiService == null) {
			throw new IllegalStateException("Serviço Adiantamento está vazio");
 		}
 		if (mesService == null) {
			throw new IllegalStateException("Serviço Meses está vazio");
 		}
 		if (anoService == null) {
			throw new IllegalStateException("Serviço Anos está vazio");
 		}
 		if (carService == null) {
			throw new IllegalStateException("Serviço Cartela está vazio");
 		}
 		if (virService == null) {
			throw new IllegalStateException("Serviço Virtual está vazio");
 		}
 		if (funService == null) {
			throw new IllegalStateException("Serviço Funcionário está vazio");
 		}
 		if (parService == null) {
			throw new IllegalStateException("Serviço Parcela está vazio");
 		}
 		if (tipoService == null) {
			throw new IllegalStateException("Serviço TipoConsumo está vazio");
 		}

 		classe = "Fechamento Mes 1 ";
		service.zeraAll();

		classe = "Meses ";
		objMes = mesService.findId(mm);
		classe = "Anos fechMes ";
		objAno = anoService.findAno(aa);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		int mmCom = 0;
		int aaCom = 0;
		double vlrImposto = 0.00;
		double vlrTaxa = 0.00;
		double vlrFolha = 0.00;
// monta fechMes tributos	
		
		classe = "Parcela 1 imposto ";
		List<Parcela> listImp = parService.findAllAberto();
		for (Parcela par : listImp) {
			if (par.getTipoFornecedor().getNomeTipo().contains("Impostos")) {
				cal.setTime(par.getDataVencimentoPar());
				mmCom = cal.get(Calendar.MONTH) + 1;
				aaCom = cal.get(Calendar.YEAR);
				if (mmCom <= mm && aaCom <= aa) {
					vlrImposto += par.getTotalPar();
				}	
			}
			if (par.getTipoFornecedor().getNomeTipo().contains("Taxas")) {
				cal.setTime(par.getDataVencimentoPar());
				mmCom = cal.get(Calendar.MONTH) + 1;
				aaCom = cal.get(Calendar.YEAR);
				if (mmCom <= mm && aaCom <= aa) {
					vlrTaxa += par.getTotalPar();
				}	
			}
		}

// zera comissão no funcionario		
		classe = "Adiantamento 1 ";
		List<Adiantamento> adiZera = adiService.findMes(mm, aa);
		for (Adiantamento a : adiZera) {
			if (a.getNomeFun() != null) {
				if (a.getComissaoAdi() > 0 || a.getValeAdi() > 0) {
					classe = "Funcionario fechMes 1 ";
					objFun = funService.findById(a.getCodigoFun());
					objFun.setComissaoFun(0.00);
					objFun.setAdiantamentoFun(0.00);
					funService.saveOrUpdate(objFun);
				}	
			}	
		}

// confere salario
		AdiantamentoUpdate.updateSemSalario(mm, aa);
		
// atualiza comissao e vale		
		classe = "Adiantamento 2 ";
		List<Funcionario> listFun = funService.findByAtivo("Ativo", aa, mm);
		listFun.removeIf(x -> x.getNomeFun().contains("Consumo Próprio"));
		List<Adiantamento> adiAtz = adiService.findMes(mm, aa);
		for (Adiantamento a : adiAtz) {
			if (a.getTipoAdi() != null) {
				for (Funcionario f : listFun) {
					if (f.getNomeFun().equals(a.getNomeAdi())) {
						objFun = f;
						if (a.getTipoAdi().contains("C")) {
							objFun.totalComissaoFun(a.getComissaoAdi());
						}
						if (a.getTipoAdi().contains("A")) {
							objFun.totalAdiantamentoFun(a.getValeAdi());
						}	
						if (a.getTipoAdi().contains("S")) {
							objFun.setSalarioFun(a.getSalarioAdi());
							vlrFolha += a.getSalarioAdi();
						}
						objFun.totalMesFun();
						funService.saveOrUpdate(objFun);
					}	
				}
			}
		}
		
		
// monta fechMes OS		
		classe = "Cartela ";
		int qtdCar = 0;
		int idCar = 0;
		double acumulado = 0.00;
		List<Adiantamento> adi3 = new ArrayList<>(); 
		List<CartelaVirtual> listVir = new ArrayList<>();
		List<Cartela> listCar = carService.findByMesAno(mm, aa);
		qtdCar = listCar.size();
		try {
			if (listCar.size() > 0) {
				for (Cartela c : listCar) {
					double produto = 0.00;
					double vlrCartela = 0.00;
					if (c.getNumeroCar() != null) {
						idCar = c.getNumeroCar();
						fechMes.setCartelaFechamentoMes(Mascaras.formataMillhar(c.getNumeroCar()));
						fechMes.setDataFechamentoMes(sdf.format(c.getDataCar()));
						fechMes.setValorCartelaFechamentoMes(Mascaras.formataValor(c.getTotalCar()));
						vlrCartela = c.getTotalCar();
						classe = "Virtual ";
						produto = 0.00;
						listVir = virService.findCartela(c.getNumeroCar());
						for (CartelaVirtual cv : listVir) {
							if (cv.getOrigemIdCarVir() == c.getNumeroCar()) {
								produto += (cv.getPrecoProdVir() * cv.getQuantidadeProdVir());
							}
						}
						double comissao = 0.00;
						adi3 = adiService.findByCartela(idCar);
						for (Adiantamento ad3 : adi3) {
							if (ad3.getCartelaAdi() == idCar && ad3.getTipoAdi() == "C") {
								comissao += ad3.getComissaoAdi();
							}
						}
						fechMes.setValorProdutoFechamentoMes(Mascaras.formataValor(produto));
						fechMes.setValorComissaoFechamentoMes(Mascaras.formataValor(comissao));
						if (fechMes.getValorResultadoFechamentoMes() == null) {
							fechMes.setValorResultadoFechamentoMes(Mascaras.formataValor(0.00));
						}
						double resultado = 0.00;
						if (c.getSituacaoCar().equals("P")) {
							resultado = vlrCartela;
						}
						fechMes.setSituacaoFechamentoMes(c.getNomeSituacaoCar());
						resultado -= (produto + comissao);
						fechMes.setValorResultadoFechamentoMes(Mascaras.formataValor(resultado));
						if (fechMes.getValorAcumuladoFechamentoMes() == null) {
							fechMes.setValorAcumuladoFechamentoMes(Mascaras.formataValor(0.00));
						}
						acumulado += resultado;
						fechMes.setValorAcumuladoFechamentoMes(Mascaras.formataValor(acumulado));
						fechMes.setMes(objMes);
						fechMes.setAno(objAno);	
						classe = "fechMes Fechamento ";
						service.insert(fechMes);
					}
				}
			} 
		}	
		catch (ParseException p) {
			p.getStackTrace();
		}
	

// monta tributos		
		if (vlrFolha > 0) {
			try {
				double impTaxaFolha = (vlrImposto + vlrTaxa + vlrFolha) - acumulado;
				double porCartela = (impTaxaFolha / qtdCar);
				Date data = new Date();
				fechMes.setNumeroFechamentoMes(null);
				fechMes.setCartelaFechamentoMes("");
				fechMes.setDataFechamentoMes("Total ===");
				fechMes.setSituacaoFechamentoMes("====>");
				fechMes.setValorCartelaFechamentoMes("Impostos +");
				fechMes.setValorProdutoFechamentoMes("Taxas +");
				fechMes.setValorComissaoFechamentoMes("Folha =");
				fechMes.setValorResultadoFechamentoMes("Total - Acumulado");
				fechMes.setValorAcumuladoFechamentoMes("p/ Cartela");
				fechMes.setMes(objMes);
				fechMes.setAno(objAno);	
				service.insert(fechMes);			

				fechMes.setNumeroFechamentoMes(null);
				fechMes.setCartelaFechamentoMes("");
				fechMes.setDataFechamentoMes(sdf.format(data));
				fechMes.setValorCartelaFechamentoMes(Mascaras.formataValor(vlrImposto));
				fechMes.setValorProdutoFechamentoMes(Mascaras.formataValor(vlrTaxa));
				fechMes.setValorComissaoFechamentoMes(Mascaras.formataValor(vlrFolha));
				fechMes.setValorResultadoFechamentoMes(Mascaras.formataValor(impTaxaFolha));
				fechMes.setValorAcumuladoFechamentoMes(Mascaras.formataValor(porCartela));
/* tarjado para conferir no -> push git hub
				fechMes.setMes(objMes);
*/				
				fechMes.setMes(objMes);
				fechMes.setAno(objAno);	
				service.insert(fechMes);			
			} catch (ParseException e) {
				e.printStackTrace();
			}	
		}	
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
  * transforma string da tela p/ o tipo no bco de fechMes 
  */
 	public void updateFormData() {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
 		if (entity.getMes() == null) {
			comboBoxMeses.getSelectionModel().selectFirst();
		} else {
 			comboBoxMeses.setValue(entity.getMes());
		}
 		if (entity.getAno() == null) {
			comboBoxAnos.getSelectionModel().selectFirst();
		} else {
 			comboBoxAnos.setValue(entity.getAno());
		}
     }
 	
//	carrega fechMes do bco  dentro obslist
	public void loadAssociatedObjects() {
		if (mesService == null) {
			throw new IllegalStateException("MesesServiço esta nulo");
		}
// buscando (carregando) os forn q estão no bco de fechMes		
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