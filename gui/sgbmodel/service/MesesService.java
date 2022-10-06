package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.dao.MesesDao;
import gui.sgbmodel.entities.Meses;
  
public class MesesService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private MesesDao dao = DaoFactory.createMesesDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Meses> findAll() {
   		return dao.findAll();
	} 
	
	public Meses findId(Integer mes) {
   		return dao.findId(mes);
	} 	
}
