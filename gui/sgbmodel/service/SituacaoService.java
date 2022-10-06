package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.dao.SituacaoDao;
import gui.sgbmodel.entities.Situacao;
  
public class SituacaoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private SituacaoDao dao = DaoFactory.createSituacaoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Situacao> findAll() {
   		return dao.findAll();
	} 
	
	public Situacao findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Situacao obj) {
		if (obj.getNumeroSit() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Situacao obj) {
		dao.deleteById(obj.getNumeroSit());
	}
}
