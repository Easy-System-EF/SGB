package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.AnosDao;
import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.entities.Anos;
  
public class AnosService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private AnosDao dao = DaoFactory.createAnosDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Anos> findAll() {
   		return dao.findAll();
	} 

	public Anos findAno(Integer ano) {
   		return dao.findAno(ano);
	} 	
}
