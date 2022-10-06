package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Produto;

public interface ProdutoDao {

	void insert(Produto obj);
	void update(Produto obj);
	void deleteById(Integer codigo);
	Produto findById(Integer codigo); 
 	List<Produto> findAll();
 	List<Produto> findPesquisa(String str);
 
}
