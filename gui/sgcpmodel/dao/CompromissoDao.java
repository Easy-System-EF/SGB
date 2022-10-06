package gui.sgcpmodel.dao;

import java.util.List;

import gui.sgcpmodel.entites.Compromisso;
import gui.sgcpmodel.entites.Fornecedor;

public interface CompromissoDao {

	void insert(Compromisso obj);
 	void deleteById(Integer nnf, Integer fornId);
  	List<Compromisso> findAll();
  	List<Compromisso> findPesquisa(String str);
 	List<Compromisso> findByFornecedor(Fornecedor fornecedor);
	List<Compromisso> findByTipo(int tp);
}
