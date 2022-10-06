package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Cartela;

public interface CartelaDao {

	void insert(Cartela obj);
	void update(Cartela obj);
	void deleteById(Integer codigo);
	Cartela findById(Integer codigo); 
	List<Cartela> findByMesAno(Integer mm, Integer aa); 
 	List<Cartela> findAll();
 	List<Cartela> findSituacao(String sit);
	List<Cartela> findSituacaoAberto(Integer mm, Integer aa, String strA, String strC); 
	
}
  