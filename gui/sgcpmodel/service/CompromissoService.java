package gui.sgcpmodel.service;

import java.util.List;

import gui.sgcpmodel.dao.CompromissoDao;
import gui.sgcpmodel.dao.DaoFactory;
import gui.sgcpmodel.entites.Compromisso;

public class CompromissoService {

// dependencia - injeta com padrao factory 
	private CompromissoDao dao = DaoFactory.createCompromissoDao();
	
// criar no compromissolist uma dependencia no compromisso controlador para esse metodo, carregando e mostrando na view	
	public List<Compromisso> findAll(){
 		return dao.findAll();
	}
	
	public List<Compromisso> findPesquisa(String str) {
   		return dao.findPesquisa(str);
	} 
	
	public List<Compromisso> findByTipo(int tp){
 		return dao.findByTipo(tp);
	}
	
 // inserindo ou atualizando	
	public void saveOrUpdate(Compromisso obj) {
 			dao.insert(obj);
  	}
		
// removendo 	
	public void remove(Compromisso obj) {
 		dao.deleteById(obj.getNnfCom(), obj.getCodigoFornecedorCom());	
	}
}		
