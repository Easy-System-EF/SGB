package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Cargo;

public interface CargoDao {

	void insert(Cargo obj);
	void update(Cargo obj);
	void deleteById(Integer codigo);
	Cargo findById(Integer codigo); 
 	List<Cargo> findAll();

}
