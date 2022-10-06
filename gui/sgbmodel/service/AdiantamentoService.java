package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.AdiantamentoDao;
import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.entities.Adiantamento;

public class AdiantamentoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private AdiantamentoDao dao = DaoFactory.createAdiantamentoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Adiantamento> findMes(Integer mes, Integer ano) {
		return dao.findMes(mes, ano);
	}

	public List<Adiantamento> findMesTipo(Integer mes, Integer ano, String tipo) {
		return dao.findMesTipo(mes, ano, tipo);
	}

	public Adiantamento findMesIdFun(Integer cod, Integer mes, Integer ano, String tipo) {
		return dao.findMesIdFun(cod, mes, ano, tipo);
	}

	public List<Adiantamento> findPesquisaFun(String str) {
		return dao.findPesquisaFun(str);
	}

	public List<Adiantamento> findByCartela(Integer idCar) {
		return dao.findByCartela(idCar);
	}

// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Adiantamento obj) {
		if (obj.getNumeroAdi() == null) {
			dao.insert(obj);
		}
	}

// removendo
	public void remove(Integer cod) {
		dao.deleteById(cod);
	}
}
