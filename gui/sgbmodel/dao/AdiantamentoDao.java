package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Adiantamento;

public interface AdiantamentoDao {

	void insert(Adiantamento obj);
	void deleteById(Integer codigo);
 	Adiantamento findMesIdFun(Integer cod, Integer mes, Integer Ano, String tipo);
 	List<Adiantamento> findMesTipo(Integer mes, Integer Ano, String tipo);
 	List<Adiantamento> findMes(Integer mes, Integer Ano);
 	List<Adiantamento> findPesquisaFun(String str);
 	List<Adiantamento> findByCartela(Integer idCar);
}
  