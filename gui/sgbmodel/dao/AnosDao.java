package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Anos;

public interface AnosDao {

 	List<Anos> findAll();
 	Anos findAno(Integer ano);
}
