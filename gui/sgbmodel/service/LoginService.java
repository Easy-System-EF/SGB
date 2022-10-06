package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.dao.LoginDao;
import gui.sgbmodel.entities.Login;
  
public class LoginService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private LoginDao dao = DaoFactory.createLoginDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public void insertOrUpdate(Login obj) {
		if (obj.getNumeroLog() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	} 

	public Login findBySenha(String senha) {
   		return dao.findBySenha(senha);
	}

	public Login findById(Integer cod) {
   		return dao.findById(cod);
	}

	public List<Login> findAll() {
   		return dao.findAll();
	}
}
