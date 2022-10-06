package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.FechamentoMes;

public interface FechamentoMesDao {

	void insert(FechamentoMes obj);
 	List<FechamentoMes> findAll();
	void zeraAll();
}
