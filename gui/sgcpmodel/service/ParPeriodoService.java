package gui.sgcpmodel.service;

import java.util.List;

import gui.sgcpmodel.dao.DaoFactory;
import gui.sgcpmodel.dao.ParPeriodoDao;
import gui.sgcpmodel.entites.consulta.ParPeriodo;
 
public class ParPeriodoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ParPeriodoDao dao = DaoFactory.createConsultaDao();
 
	public List<ParPeriodo> findAll(){
 		return dao.findAll();
	}
	
	public ParPeriodo findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
 // * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void update(ParPeriodo obj) {
		dao.update(obj);
 	}
 
// removendo
	public void remove() {
   			dao.deleteByAll();
 	}
}
