package model.users.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.users.Users;
import model.users.dao.UsersDao;

public class UsersDaoJDBC implements UsersDao {
	
	// tb entra construtor p/ conexão
		private Connection conn;
		
		public UsersDaoJDBC (Connection conn) {
			this.conn = conn;
		}

		@Override
		public void insert(Users obj) {
			PreparedStatement st = null;
			ResultSet rs = null;
	  		try {
				st = conn.prepareStatement(
						"INSERT INTO user " +
					      "(NomeUser, SenhaUser )" + 
	  				      "VALUES " +
					      "(?)",
	 					 Statement.RETURN_GENERATED_KEYS); 
	 
	 			st.setString(1, obj.getNomeUser());
	 			st.setString(2, obj.getSenhaUser());
				
//	 			st.executeUpdate();

	 			int rowsaffectad = st.executeUpdate();
				
				if (rowsaffectad > 0)
				{	rs = st.getGeneratedKeys();
					if (rs.next())
					{	int codigo = rs.getInt(1);
						obj.setNumeroUser(codigo);;
						System.out.println("New key inserted: " + obj.getNumeroUser());
					}
					else
					{	throw new DbException("Erro!!! sem inclusão" );
					}
				}	
	  		}
	 		catch (SQLException e) {
				throw new DbException (e.getMessage());
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(st);
			}
		}
	 
		@Override
		public void update(Users obj) {
			PreparedStatement st = null;
	   		try {
				st = conn.prepareStatement(
						"UPDATE user " +  
								"SET NomeUser = ?, " +
								"SET SenhaUser = ? " +
	   					"WHERE (NumeroUser = ?)",
	        			 Statement.RETURN_GENERATED_KEYS);

	 			st.setString(1, obj.getNomeUser());
	 			st.setString(2, obj.getSenhaUser());
	 			st.setInt(3, obj.getNumeroUser());
	    			
				st.executeUpdate();
	   		} 
	 		catch (SQLException e) {
	 		throw new DbException ( "Erro!!! sem atualização " + e.getMessage()); }

	  		finally {
	 			DB.closeStatement(st);
			}
		}

		@Override
		public void deleteById(Integer num) {
			PreparedStatement st = null;
//			ResultSet rs = null;
	  		try {
				st = conn.prepareStatement(
						"DELETE FROM user WHERE NumeroUser = ? ", Statement.RETURN_GENERATED_KEYS);
					  
				st.setInt(1, num);
				st.executeUpdate();
	   		}
	 		catch (SQLException e) {
				throw new DbException ( "Erro!!! naõ excluído " + e.getMessage()); }
	 		finally {
	 			DB.closeStatement(st);
			}
		}

		@Override
		public Users findById(Integer num) {
	 		PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = conn.prepareStatement(
					 "SELECT * FROM user " +
	 				 "WHERE NumeroUser = ?");
	 			
				st.setInt(1, num);
				rs = st.executeQuery();
				if (rs.next())
				{	if (rs != null)
					{	Users obj = instantiateUsers(rs);
	 					return obj;
					}	
				}
				return null;
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
			finally {
				DB.closeStatement(st);
				DB.closeResultSet(rs);
			}
	 	}
		@Override
		public List<Users> findAll() {
			PreparedStatement st = null; 
			ResultSet rs = null;
			try {
				st = conn.prepareStatement( 
						"SELECT * FROM user " +
						"ORDER BY NomeUser");
				
				rs = st.executeQuery();
				
				List<Users> list = new ArrayList<>();
				
				while (rs.next())
				{	if (rs != null)
					{	Users obj = instantiateUsers(rs);
	 					list.add(obj);
					}	
	 			}
				return list;
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
			finally {
				DB.closeStatement(st);
				DB.closeResultSet(rs);
			}
		} 
		
		private Users instantiateUsers(ResultSet rs) throws SQLException {
	 		model.users.Users user = new Users();
	 		
	 		user.setNumeroUser(rs.getInt("NumeroUser"));
	 		user.setNomeUser(rs.getString("NomeUser"));	
	 		user.setSenhaUser(rs.getString("SenhaUser"));
	         return user;
		}
	}
