package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.dao.ProdutoDao;
import gui.sgbmodel.entities.Produto;
  
public class ProdutoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ProdutoDao dao = DaoFactory.createProdutoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Produto> findAll() {
   		return dao.findAll();
	} 
	
	public List<Produto> findPesquisa(String str) {
   		return dao.findPesquisa(str);
	} 
	
	public Produto findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Produto obj) {
		if (obj.getCodigoProd() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Produto obj) {
		dao.deleteById(obj.getCodigoProd());
	}
}
