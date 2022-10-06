package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Login;

public interface LoginDao {

 	void insert(Login login);
 	void update(Login login);
	Login findBySenha(String senha);
	Login findById(Integer cod);
	List<Login> findAll();
}
