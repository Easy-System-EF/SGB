package gui.sgcpmodel.dao;

import java.util.List;

import gui.sgcpmodel.entites.TipoConsumo;

public interface TipoConsumidorDao {

	void insert(TipoConsumo obj);
	void update(TipoConsumo obj);
	void deleteById(Integer codigo);
	TipoConsumo findById(Integer codigo); 
 	List<TipoConsumo> findAll();

}
