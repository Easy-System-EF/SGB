package gui.sgcpmodel.service;

import java.util.List;

import gui.sgcpmodel.dao.DaoFactory;
import gui.sgcpmodel.dao.FornecedorDao;
import gui.sgcpmodel.entites.Fornecedor;
 
public class FornecedorService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private FornecedorDao dao = DaoFactory.createFornecedorDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Fornecedor> findAll() {
   		return dao.findAll();
	} 
	
	public List<Fornecedor> findPesquisa(String str) {
   		return dao.findPesquisa(str);
	} 
	
	public Fornecedor findById(Integer cod) {
    		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Fornecedor obj) {
		if (obj.getCodigo() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Fornecedor obj) {
		dao.deleteById(obj.getCodigo());
	}
}
