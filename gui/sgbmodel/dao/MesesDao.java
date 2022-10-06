package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Meses;

public interface MesesDao {

 	List<Meses> findAll();
 	Meses findId(Integer mes);
}
