package model.users.dao;

import java.util.List;

import model.users.Users;

	public interface UsersDao {

		void insert(Users obj);
		void update(Users obj);
		void deleteById(Integer codigo);
		Users findById(Integer codigo); 
	 	List<Users> findAll();

	}
