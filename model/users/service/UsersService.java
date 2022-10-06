package model.users.service;

import java.util.List;

import model.users.Users;
import model.users.dao.DaoFactory;
import model.users.dao.UsersDao;
  
public class UsersService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private UsersDao dao = DaoFactory.createUsersDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Users> findAll() {
   		return dao.findAll();
	} 
	
	public Users findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Users obj) {
		if (obj.getNumeroUser() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Users obj) {
		dao.deleteById(obj.getNumeroUser());
	}
}
