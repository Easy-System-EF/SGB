package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgb;
import gui.listerneres.DataChangeListener;
import gui.sgb.AdiantamentoListController;
import gui.sgb.CargoListController;
import gui.sgb.CartelaListAbertoController;
import gui.sgb.CartelaListController;
import gui.sgb.CartelaListPagoController;
import gui.sgb.ComissaoListController;
import gui.sgb.EntradaListController;
import gui.sgb.FechamentoMesListController;
import gui.sgb.FolhaMesListController;
import gui.sgb.FuncionarioListController;
import gui.sgb.GrupoListController;
import gui.sgb.LoginFormController;
import gui.sgb.ProdutoListController;
import gui.sgbmodel.entities.Adiantamento;
import gui.sgbmodel.entities.Login;
import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.CargoService;
import gui.sgbmodel.service.CartelaPaganteService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.EntradaService;
import gui.sgbmodel.service.FechamentoMesService;
import gui.sgbmodel.service.FolhaMesService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.GrupoService;
import gui.sgbmodel.service.LoginService;
import gui.sgbmodel.service.ProdutoService;
import gui.sgcp.CompromissoListController;
import gui.sgcp.FornecedorListController;
import gui.sgcp.ParcelaListAbertoController;
import gui.sgcp.ParcelaListPagoController;
import gui.sgcp.ParcelaPrintAbertoController;
import gui.sgcp.ParcelaPrintPagoController;
import gui.sgcp.TipoConsumoListController;
import gui.sgcpmodel.service.CompromissoService;
import gui.sgcpmodel.service.FornecedorService;
import gui.sgcpmodel.service.ParPeriodoService;
import gui.sgcpmodel.service.ParcelaService;
import gui.sgcpmodel.service.TipoConsumoService;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainViewSgbController implements Initializable, DataChangeListener {
 
	@FXML
	private MenuItem menuItemLogin;
	
	@FXML
	private MenuItem menuItemCargo;
	
	@FXML
	private MenuItem menuItemFornecedor;

	@FXML
	private MenuItem menuItemCompromisso;

	@FXML
	private MenuItem menuItemTipoConsumo;

	@FXML
	private MenuItem menuItemGrupo;

	@FXML
	private MenuItem menuItemProduto;

	@FXML
	private MenuItem menuItemFuncionario;
	
	@FXML
	private MenuItem menuItemAdiantamento;
	
	@FXML
	private MenuItem menuItemEntrada;
	
	@FXML
	private MenuItem menuItemCartela;
	
	@FXML
	private MenuItem menuItemRelatorioParcelaAberto;

	@FXML
	private MenuItem menuItemRelatorioParcelaPago;

	@FXML
	private MenuItem menuItemConsultaPago;

	@FXML
	private MenuItem menuItemConsultaAberto;
	
	@FXML
	private MenuItem menuItemConsultaCartelaAberto;
	
	@FXML
	private MenuItem menuItemConsultaCartelaPago;
	
	@FXML
	private MenuItem menuItemConsultaComissao;
	
	@FXML
	private MenuItem menuItemConsultaFolhaMes;
	
	@FXML
	private MenuItem menuItemConsultaFechamentoMes;
	
	@FXML
	private MenuItem menuItemSobre;

	@FXML
	private Button btLogin;

	@FXML
	private Label labelUser;

//auxiliares	
	String classe = "";
	public static String senha = null;
	public static int nivel = 0;
	public static String user = "usuário";
//código da empresa
//  1 = Easy; 2 = WS; 3 = Black Beer........	
	public static int numEmp = 0;
	
	@FXML
	private void onBtLoginAction() {
		classe = "Login ";
		Login log = new Login();
		senha = "null";
		createDialogForm(log, "/gui/sgb/LoginForm.fxml");
		if (senha == "null") {
			temLogar();
		}
		initializeNodes();
	}
	
	/*
	 * função e inicialização do FornecedorController antes na loadview 2 -
	 * expressão lambda inicialização como parametro da função loadView aqui
	 */
	
	@FXML
	public void onMenuItemCargoAction() {
		classe = "Cargo ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel > 1 && nivel < 9) {
				Alerts.showAlert("Nível de Acesso", "Atenção!!!", "nível sem Acesso", AlertType.ERROR);
			} else {
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgb/CargoList.fxml", (CargoListController controller) -> {
					controller.user = user;
					controller.setCargoService(new CargoService());
					controller.updateTableView();
		});
			}
		}
			}
		}
	}
 	
	@FXML
	public void onMenuItemFornecedorAction() {
		classe = "Fornecedor ";
		if (senha != "Ok") {
			temLogar();
		} else {	
		loadView("/gui/sgcp/FornecedorList.fxml", (FornecedorListController controller) -> {
			controller.user = user;
			controller.setFornecedorService(new FornecedorService());
			controller.updateTableView();
			// view2 p/ funcionar mock
		});}
	}
	
	// criou um expressão lambda como par ametros para atz tableview =>
	// initializingAction
	@FXML
	public void onMenuItemCompromissoAction() {
		classe = "Compromisso ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/gui/sgcp/CompromissoList.fxml", (CompromissoListController controller) -> {
					controller.user = user;
					controller.setCompromissoService(new CompromissoService());
					controller.setFornecedorService(new FornecedorService());
					controller.setParcelaService(new ParcelaService());
					controller.updateTableView();
		});
			}
		}	
	}

	@FXML
	public void onMenuItemTipoConsumoAction() {
		classe = "Tipo Consumo ";
		if (senha != "Ok") {
			temLogar();
		} else {	
		loadView("/gui/sgcp/TipoConsumoList.fxml", (TipoConsumoListController controller) -> {
			controller.user = user;
			controller.setTipoConsumoService(new TipoConsumoService());
			controller.updateTableView();
			// view2 p/ funcionar mock
		});}
	}


	@FXML
	public void onMenuItemGrupoAction() {
		classe = "Grupo ";
		if (senha != "Ok") {
			temLogar();
		} else {	
		loadView("/gui/sgb/GrupoList.fxml", (GrupoListController controller) -> {
			controller.user = user;
			controller.setGrupoService(new GrupoService());
			controller.updateTableView();
		});}
	}
 
	@FXML
	public void onMenuItemProdutoAction() {
		classe = "Produto ";
		if (senha != "Ok") {
			temLogar();
		} else {	
 		loadView("/gui/sgb/ProdutoList.fxml", (ProdutoListController controller) -> {
			controller.user = user;
  			controller.setProdutoService(new ProdutoService());
  			controller.setGrupoService(new GrupoService());
   			controller.updateTableView();
		});}
	}
 
	@FXML
	public void onMenuItemFuncionarioAction() {
		classe = "Funcionario ";
		if (senha != "Ok") {
			temLogar();
		} else {	
 		loadView("/gui/sgb/FuncionarioList.fxml", (FuncionarioListController controller) -> {
			controller.user = user;
  			controller.setFuncionarioService(new FuncionarioService());
   			controller.updateTableView();
		});}
	}
  
	@FXML
	public void onMenuItemAdiantamentoAction() {
		classe = "Adiantamento ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel > 1 && nivel < 9) {
				Alerts.showAlert("Nível de Acesso", "Atenção!!!", "nível sem Acesso", AlertType.ERROR);
			} else {
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/gui/sgb/AdiantamentoList.fxml", (AdiantamentoListController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.setAdiantamentoService(new AdiantamentoService());
					controller.setTipo("A");
					controller.updateTableView();
		});
			}
		}
			}
		}
	}
  
	@FXML
	public void onMenuItemEntradaAction() {
		classe = "Entrada ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/gui/sgb/EntradaList.fxml", (EntradaListController controller) -> {
					controller.user = user;
					controller.setProdutoService(new ProdutoService());
					controller.setFornecedorService(new FornecedorService());
					controller.setEntradaService(new EntradaService());
					controller.setGrupoService(new GrupoService());
					controller.updateTableView();
		});
			}
		}	
	}
  
	@FXML
	public void onMenuItemCartelaAction() {
		classe = "Cartela ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/gui/sgb/CartelaList.fxml", (CartelaListController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.nivel = nivel;
					controller.setCartelaService(new CartelaService());
					controller.updateTableView();
		});
			}
		}	
	}
  
	@FXML  
	public void onMenuItemRelatorioParcelaAbertoAction() {
		classe = "Compromisso Aberto ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel > 1 && nivel < 9) {
				Alerts.showAlert("Nível de Acesso", "Atenção!!!", "nível sem Acesso", AlertType.ERROR);
			} else {
				loadView("/gui/sgcp/ParcelaPrintAberto.fxml", (ParcelaPrintAbertoController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.setFornecedorService(new FornecedorService());
					controller.setParPeriodoService(new ParPeriodoService());
					controller.setParcelaService(new ParcelaService());
					controller.updateTableViewAberto();
		}); 
		   }
		}	
	}

	@FXML
	public void onMenuItemRelatorioParcelaPagoAction() {
		classe = "Compromisso Pago ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel > 1 && nivel < 9) {
				Alerts.showAlert("Nível de Acesso", "Atenção!!!", "nível sem Acesso", AlertType.ERROR);
			} else {
				loadView("/gui/sgcp/ParcelaPrintPago.fxml", (ParcelaPrintPagoController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.setFornecedorService(new FornecedorService());
					controller.setParPeriodoService(new ParPeriodoService());
					controller.setParcelaService(new ParcelaService());
					controller.updateTableViewPago();
				}); 
			}		
		   }
		}

	@FXML
	public void onMenuItemConsultaAbertoAction() {
		classe = "Contas a Pagar ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel > 1 && nivel < 9) {
				Alerts.showAlert("Nível de Acesso", "Atenção!!!", "nível sem Acesso", AlertType.ERROR);
		} else {
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgcp/ParcelaListAberto.fxml", (ParcelaListAbertoController controller) -> {
					controller.user = user;
					controller.setFornecedorService(new FornecedorService());
					controller.setParPeriodoService(new ParPeriodoService());
					controller.setParcelaService(new ParcelaService());
					controller.updateTableViewAberto();
		});
			}
		}	
			}
		}	
	}

	@FXML
	public void onMenuItemConsultaPagoAction() {
		classe = "Contas Pagas ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel > 1 && nivel < 9) {
				Alerts.showAlert("Nível de Acesso", "Atenção!!!", "nível sem Acesso", AlertType.ERROR);
		} else {
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgcp/ParcelaListPago.fxml", (ParcelaListPagoController controller) -> {
					controller.user = user;
					controller.setFornecedorService(new FornecedorService());
					controller.setParPeriodoService(new ParPeriodoService());
					controller.setParcelaService(new ParcelaService());
					controller.updateTableViewPago();
		});
			}
		}
			}
		}	
	}

	@FXML
	public void onMenuItemConsultaCartelaAbertoAction() {
		classe = "Con Receber";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel > 1 && nivel < 9) {
				Alerts.showAlert("Nível de Acesso", "Atenção!!!", "nível sem Acesso", AlertType.ERROR);
		} else {
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgb/CartelaListAberto.fxml", (CartelaListAbertoController controller) -> {
					controller.user = user;
					controller.mm = 0;
					controller.aa = 0;
					controller.setCartelaService(new CartelaService());
					controller.updateTableView();
		});
			}
		}	
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaCartelaPagoAction() {
		classe = "Con Recebido ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel > 1 && nivel < 9) {
				Alerts.showAlert("Nível de Acesso", "Atenção!!!", "nível sem Acesso", AlertType.ERROR);
		} else {
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgb/CartelaListPago.fxml", (CartelaListPagoController controller) -> {
					controller.user = user;
					controller.mm = 0;
					controller.aa = 0;
					controller.setCartelaPaganteService(new CartelaPaganteService());
					controller.updateTableView();
		});
			}
		}	
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaComissaoAction() {
		classe = "Con Comissão ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel > 1 && nivel < 9) {
				Alerts.showAlert("Nível de Acesso", "Atenção!!!", "nível sem Acesso", AlertType.ERROR);
			} else {
				Adiantamento adiantamento = new Adiantamento(); 
				loadView("/gui/sgb/ComissaoList.fxml", (ComissaoListController controller) -> {
					controller.user = user;
					controller.setAdiantamento(adiantamento);
					controller.setServices(new AdiantamentoService());
					controller.updateTableView();
		});
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaFolhaMesAction() {
		classe = "Con FolhaMes ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel > 1 && nivel < 9) {
				Alerts.showAlert("Nível de Acesso", "Atenção!!!", "nível sem Acesso", AlertType.ERROR);
			} else {
				loadView("/gui/sgb/FolhaMesList.fxml", (FolhaMesListController controller) -> {
					controller.user = user;
					controller.setServices(new FolhaMesService());
					controller.zeraMes();
					controller.updateTableView();
		});
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaFechamentoMesAction() {
		classe = "Con Fechamnto Mes";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel > 1 && nivel < 9) {
				Alerts.showAlert("Nível de Acesso", "Atenção!!!", "nível sem Acesso", AlertType.ERROR);
			} else {
				loadView("/gui/sgb/FechamentoMesList.fxml", (FechamentoMesListController controller) -> {
					controller.user = user;
					controller.setServices(new FechamentoMesService());
		});
			}
		}	
	}
  
	// obrigatoria função x , sem nada; mesmo sem "nada" repassado para atender
	// parametros <consumer> do loadView
	@FXML
	public void onMenuItemSobreAction() {
		if (senha != "Ok") {
			temLogar();
		} else {	
		loadView("/gui/Sobre.fxml", x -> {
		});}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		labelUser.setText(user);
	}

	private void temLogar() {
		Alerts.showAlert("Erro login!!!", null, "Tem que logar ", AlertType.ERROR);
	}
	
	/*
	 * interface consumer <T>, passa a ser função 
	 * generica synchronized garante processo inteiro sem interrupção
	 */
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
//	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

// busca o conteudo da variavel no Main via metodo getScene			
			Scene mainScene = MainSgb.getMainScene();
			/*
			 * os filhos da janela Vbox (sobre) inseridos no scrollpane casting vbox
			 * scrollpane trazendo o primeiro reg content getRoot pega o primeiro elemento
			 * da view principal - scrollpane casting scrollpane - getContent
			 */
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

//	busca o primeiro children do vbox da janela principal			
			Node mainMenu = mainVBox.getChildren().get(0);
// limpa os filhos do main vbox
			mainVBox.getChildren().clear();
// adiciona o mainMenu			
			mainVBox.getChildren().add(mainMenu);
// adiciona os filhos do newVbox
			mainVBox.getChildren().addAll(newVBox.getChildren());

// ativar essa função ; retorna o controlador <T> do tipo chamado aqui em cima 
//			na função(Fornecedor, compromis....			
			T controller = loader.getController();
// para executar a ação -> função accept do consumer			
			initializingAction.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", classe + "Erro carregando a página", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void createDialogForm(Login log, String absoluteName) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			LoginFormController controller = loader.getController();
// injetando passando parametro obj 			
			controller.setLogin(log);
			controller.numEmp = numEmp;
// injetando tb o forn service vindo da tela de formulario fornform
			controller.setLoginService(new LoginService());
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListenerMain(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Logar                                             ");
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

	@Override
	public void onDataChanged() {
		// TODO Auto-generated method stub
		
	}
}