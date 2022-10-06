package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.FolhaMes;

public interface FolhaMesDao {

	void insert(FolhaMes obj);
 	List<FolhaMes> findAll();
	void zeraAll();
}
