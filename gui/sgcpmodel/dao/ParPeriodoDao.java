package gui.sgcpmodel.dao;

import java.util.List;

import gui.sgcpmodel.entites.consulta.ParPeriodo;

public interface ParPeriodoDao {

	void update(ParPeriodo obj);
  	void deleteByAll();
  	ParPeriodo findById(Integer cod);
   	List<ParPeriodo> findAll();
 }
