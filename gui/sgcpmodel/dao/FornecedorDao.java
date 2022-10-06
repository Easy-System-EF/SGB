package gui.sgcpmodel.dao;

import java.util.List;

import gui.sgcpmodel.entites.Fornecedor;

public interface FornecedorDao {

	void insert(Fornecedor obj);
	void update(Fornecedor obj);
	void deleteById(Integer codigo);
	Fornecedor findById(Integer codigo); 
 	List<Fornecedor> findAll();
 	List<Fornecedor> findPesquisa(String str);

}
