package gui.sgbmodel.dao;

import gui.sgbmodel.entities.Empresa;

public interface EmpresaDao {

 	void insert(Empresa tabend);
 	void update(Empresa tabend);
	Empresa findById(Integer cod);
}
