package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.dao.FolhaMesDao;
import gui.sgbmodel.entities.FolhaMes;

public class FolhaMesService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private FolhaMesDao dao = DaoFactory.createFolhaMesDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<FolhaMes> findAll() {
   		return dao.findAll();
	} 
	
	public void zeraAll() {
   		dao.zeraAll();
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void insert(FolhaMes obj) {
		if (obj.getNumeroFolha() == null) {
			dao.insert(obj);
		}
	}	
}
