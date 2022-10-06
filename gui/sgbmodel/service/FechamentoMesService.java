package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.dao.FechamentoMesDao;
import gui.sgbmodel.entities.FechamentoMes;
  
public class FechamentoMesService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private FechamentoMesDao dao = DaoFactory.createFechamentoMesDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<FechamentoMes> findAll() {
   		return dao.findAll();
	} 
	
	public void zeraAll() {
   		dao.zeraAll();
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void insert(FechamentoMes obj) {
			dao.insert(obj);
	}	
}
