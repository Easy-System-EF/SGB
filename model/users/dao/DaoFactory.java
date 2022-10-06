package model.users.dao;

import db.DB;
import model.users.jdbc.UsersDaoJDBC;

public class DaoFactory {

/*
 * é obrigatório passar uma conexão como argumento (db.getCon...)
 */
 	public static UsersDao createUsersDao() {
		return new UsersDaoJDBC(DB.getConnection());
	}
	
}
