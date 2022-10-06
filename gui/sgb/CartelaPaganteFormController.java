package gui.sgb;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.sgbmodel.entities.Cartela;
import gui.sgbmodel.entities.CartelaPagante;
import gui.sgbmodel.service.CartelaPaganteService;
import gui.sgbmodel.service.CartelaService;
import gui.util.Alerts;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import model.exception.ValidationException;

public class CartelaPaganteFormController implements Initializable {

	private CartelaPagante entity;
	private Cartela cartela;

	/*
	 * dependencia service com metodo set
	 */
	private CartelaPaganteService service;
	private CartelaService carService;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textPaganteCartelaPag;

	@FXML
	private TextField textCartela;

	@FXML
	private TextField textDataCartelaPag;

	@FXML
	private TextField textLocalCartelaPag;

	@FXML
	private TextField textConsumoCartela;

	@FXML
	private TextField textValorCartelaPag;

	@FXML
	private RadioButton rbFormaDinheiroCartelaPag;

	@FXML
	private RadioButton rbFormaPixCartelaPag;

	@FXML
	private RadioButton rbFormaDebitoCartelaPag;

	@FXML
	private RadioButton rbFormaCartaoCartelaPag;

	@FXML
	private Button btSaveCartelaPag;

	@FXML
	private Button btCancelCartelaPag;

	@FXML
	private Label labelErrorValorCartelaPag;

	@FXML
	private Label labelErrorFormaCartelaPag;

	@FXML
	private Label labelUser;

// auxiliares
	String classe = "CartelaPagante ";
	public String user = "usuário";
	double parcela = 0.00;
	double diferenca = 0.00;
	String dif = "";
	double valorPago = 0.00;
	int dd = 0;
	int mm = 0;
	int aa = 0;
	Calendar cal = Calendar.getInstance();

	public static Integer numPagante = null;
	public static Integer numCar = null;

	public void setPagantes(CartelaPagante entity, Cartela cartela) {
		this.entity = entity;
		this.cartela = cartela;
	}

	// * metodo set /p service
	public void setServices(CartelaPaganteService service, CartelaService carService) {
		this.service = service;
		this.carService = carService;
	}

//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	/*
	 * vamos instanciar um tipoforn e salvar no bco de dados meu obj entity (lá em
	 * cima) vai receber uma chamada do getformdata metodo q busca dados do
	 * formulario convertidos getForm (string p/ int ou string) pegou no formulario
	 * e retornou (convertido) p/ jogar na variavel entity chamo o service na rotina
	 * saveupdate e mando entity vamos tst entity e service = null -> não foi
	 * injetado para fechar a janela, pego a referencia para janela atual (event) e
	 * close DataChangeListner classe subjetc - q emite o evento q muda dados, vai
	 * guardar uma lista qdo ela salvar obj com sucesso, é só notificar (juntar)
	 * recebe lá no listController
	 */
	@FXML
	public void onBtSaveCartelaPaganteAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		ValidationException exception = new ValidationException("Validation exception");
		try {
    		 entity = getFormData();
    		 service.insert(entity);
    	 	 if (numPagante == cartela.getNumeroPaganteCar()) {
    		 	 diferenceII();
    		 	 if (diferenca > 0.00) {
					dif = Mascaras.formataValor(diferenca);
				 	Alerts.showAlert("Erro!!!", "Valor pago diferente do total", 
 					 			"Refaça - diferença R$" + dif, AlertType.ERROR);
				 	service.remove(numCar);
   				 } else {
    				CartelaFormController.situacao = "P";
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
		catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se não for atz tb verificamos se cpos
	 * obrigatórios estão preenchidos, para informar erro(s) para cpos string não
	 * precisa tryParse
	 */
	private CartelaPagante getFormData() {
		CartelaPagante obj = new CartelaPagante();
		obj = entity;
		// instanciando uma exceção, mas não lançado - validation exc....
		ValidationException exception = new ValidationException("Validation exception");
// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro
		if (textValorCartelaPag.getText() == null || textValorCartelaPag.getText().contains("0.00")
				|| textValorCartelaPag.getText().trim().contentEquals("")) {
			exception.addErros("valor", "valor é obrigatório");
		} else {
			obj.setValorCartelaPag(Utils.formatDecimalIn(textValorCartelaPag.getText().replace(".", "")));
		}
		if (obj.getValorCartelaPag() == 0.00)
		exception.addErros("valor", "valor é obrigatório");

		obj.setPaganteCartelaPag(Utils.tryParseToInt(textPaganteCartelaPag.getText()));

		int flag3 = 0;
		if (rbFormaDinheiroCartelaPag.isSelected()) {
			obj.setFormaCartelaPag("Dinheiro");
			flag3 += 1;
		}
		if (rbFormaPixCartelaPag.isSelected()) {
			obj.setFormaCartelaPag("Pix");
			flag3 += 1;
		}
		if (rbFormaDebitoCartelaPag.isSelected()) {
			obj.setFormaCartelaPag("Débito");
			flag3 += 1;
		}
		if (rbFormaCartaoCartelaPag.isSelected()) {
			obj.setFormaCartelaPag("Cartão");
			flag3 += 1;
		}
		if (obj.getFormaCartelaPag() == null || flag3 == 0) {
			exception.addErros("forma", "Pagamento obrigatório");
		}
		if (flag3 > 1) {
			exception.addErros("forma", "Só pode uma opção");
		}

		obj.setSituacaoCartelaPag("P");

		// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

// msm processo save p/ fechar	
	@FXML
	public void onBtCancelCartelaPaganteAction(ActionEvent event) {
		CartelaFormController.numPag = 99;
		CartelaFormController.situacao = "N";
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
//***		Utils.formatTableColumnDate(textDataCartelaPag, "dd/MM/yyyy");
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		parcela = 0.00;
		cartela = carService.findById(numCar);
		entity.setNumeroCartelaPag(null);
		entity.setPaganteCartelaPag(numPagante);
		parcela = cartela.getTotalCar() / cartela.getNumeroPaganteCar();
		if (cartela.getNumeroPaganteCar() > 1) { 
			if (numPagante == cartela.getNumeroPaganteCar()) {
				diference();
				parcela = diferenca;
			}	
		}	
		entity.setDataCartelaPag(cartela.getDataCar());
		entity.setLocalCartelaPag(cartela.getLocalCar());
		entity.setValorCartelaPag(parcela);
		valorPago = entity.getValorCartelaPag();
		entity.setSituacaoCartelaPag(cartela.getSituacaoCar());
		entity.setCartelaIdOrigemPag(cartela.getNumeroCar());
		
		cal.setTime(entity.getDataCartelaPag());
		dd = cal.get(Calendar.DAY_OF_MONTH);
		mm = 1 + cal.get(Calendar.MONTH);
		aa = cal.get(Calendar.YEAR);
		String data = (dd + "/" + mm + "/" + aa);
		labelUser.setText(user);
		entity.setMesCartelaPag(mm);
		entity.setAnoCartelaPag(aa);
		// string value of p/ casting int p/ string
		textPaganteCartelaPag.setText(String.valueOf(entity.getPaganteCartelaPag()));
		textCartela.setText(String.valueOf(entity.getCartelaIdOrigemPag()));

		if (entity.getDataCartelaPag() != null) {
			textDataCartelaPag.setText(data);
		}

		textLocalCartelaPag.setText(entity.getLocalCartelaPag());

		String valor = Mascaras.formataValor(cartela.getTotalCar());
		textConsumoCartela.setText(String.valueOf(valor));
		String receber = Mascaras.formataValor(entity.getValorCartelaPag());
		textValorCartelaPag.setText(receber);

		if (entity.getFormaCartelaPag() == "Dinheiro") {
			rbFormaDinheiroCartelaPag.setSelected(true);
		}	else { 	
			rbFormaDinheiroCartelaPag.setSelected(false);
		}
		
		if (entity.getFormaCartelaPag() == "Pix") {
			rbFormaPixCartelaPag.setSelected(true);
		} else {	
			rbFormaPixCartelaPag.setSelected(false);
		}	
		
		if (entity.getFormaCartelaPag() == "Débito") {
			rbFormaDebitoCartelaPag.setSelected(true);
		} else {	
			rbFormaDebitoCartelaPag.setSelected(false);
		}
		
		if (entity.getFormaCartelaPag() == "Cartão") {
			rbFormaCartaoCartelaPag.setSelected(true);
		} else {	
			rbFormaCartaoCartelaPag.setSelected(false);
		}	
	}

	private void diference() {
		List<CartelaPagante> listPag = service.findByCartela(numCar);
		if (listPag.size() > 0) {
			for (CartelaPagante cp : listPag) {
				CartelaPagante carPag = service.findById(cp.getNumeroCartelaPag());
				valorPago += carPag.getValorCartelaPag();
			}
	 		diferenca = cartela.getTotalCar() - (valorPago);
	 		parcela = diferenca;
			diferenca = parcela;;
    	 }
	}

	private void diferenceII() {
		valorPago = 0.00;
		List<CartelaPagante> listPag = service.findByCartela(numCar);
		if (listPag.size() > 0) {
			for (CartelaPagante cp : listPag) {
				CartelaPagante carPag = service.findById(cp.getNumeroCartelaPag());
				valorPago += carPag.getValorCartelaPag();
			}
	 		diferenca = valorPago - cartela.getTotalCar();
    	 }
	}

	// mandando a msg de erro para o labelErro correspondente
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		if (fields.contains("valor")) {
			labelErrorValorCartelaPag.setText(erros.get("valor"));
		}
		labelErrorFormaCartelaPag.setText(fields.contains("forma") ? erros.get("forma") : "");
	}
}
