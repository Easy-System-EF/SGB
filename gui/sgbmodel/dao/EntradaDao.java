package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Entrada;
import gui.sgbmodel.entities.Grupo;

public interface EntradaDao {

	void insert(Entrada obj);
	void update(Entrada obj);
	void deleteById(Integer codigo);
	Entrada findById(Integer codigo); 
 	List<Entrada> findAll();
}
  