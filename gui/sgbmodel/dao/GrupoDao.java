package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Grupo;

public interface GrupoDao {

	void insert(Grupo obj);
	void update(Grupo obj);
	void deleteById(Integer codigo);
	Grupo findById(Integer codigo); 
 	List<Grupo> findAll();

}
