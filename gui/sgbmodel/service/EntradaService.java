package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.dao.EntradaDao;
import gui.sgbmodel.entities.Entrada;
 
public class EntradaService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private EntradaDao dao = DaoFactory.createEntradaDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Entrada> findAll() {
   		return dao.findAll();
	} 
	
	public Entrada findById(Integer cod) {
    		return dao.findById(cod);	
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Entrada obj) {
		if (obj.getNumeroEnt() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Entrada obj) {
		dao.deleteById(obj.getNumeroEnt());
	}
}
