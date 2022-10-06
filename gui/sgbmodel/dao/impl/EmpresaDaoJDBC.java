package gui.sgbmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;
import gui.sgbmodel.dao.EmpresaDao;
import gui.sgbmodel.entities.Empresa;
  
public class EmpresaDaoJDBC implements EmpresaDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public EmpresaDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Empresa ";
	
	@Override
	public void insert(Empresa obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO empresa " +
				      "(NomeEmp, EnderecoEmp, TelefoneEmp, EmailEmp, PixEmp )" + 
  				      "VALUES " +
				      "(?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setString(1, obj.getNomeEmp());
			st.setString(2, obj.getEnderecoEmp());
			st.setString(3, obj.getTelefoneEmp());
			st.setString(4, obj.getEmailEmp());
			st.setString(5, obj.getPixEmp());
			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	Integer num = rs.getInt(1);
					obj.setNumeroEmp(num);
//					System.out.println("New key inserted: " + obj.getNumeroSit());
				}
				else
				{	throw new DbException(classe + "Erro!!! sem inclusão" );
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
	public void update(Empresa obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE empresa " +
				      "SET NomeEmp = ?, EnderecoEmp = ?, TelefoneEmp = ?, " +
							"EmailEmp = ?, PixEmp = ? " + 
  				      "WHERE NumeroEmp = ? ",
  				      		Statement.RETURN_GENERATED_KEYS); 

			st.setString(1, obj.getNomeEmp());
			st.setString(2, obj.getEnderecoEmp());
			st.setString(3, obj.getTelefoneEmp());
			st.setString(4, obj.getEmailEmp());
			st.setString(5, obj.getPixEmp());
			st.setInt(6, obj.getNumeroEmp());

 			st.executeUpdate();
			
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
	public Empresa findById(Integer cod) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM empresa " +
 				 "WHERE NumeroEmp = ? ");
 			
			st.setInt(1, cod);
			
			rs = st.executeQuery();
			if (rs.next())
			{	if (rs != null)
				{	Empresa obj = instantiateEmp(rs);
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
	
	private Empresa instantiateEmp(ResultSet rs) throws SQLException {
 		Empresa tab = new Empresa();
 		tab.setNumeroEmp(rs.getInt("NumeroEmp"));
 		tab.setNomeEmp(rs.getString("NomeEmp"));
 		tab.setEnderecoEmp(rs.getString("EnderecoEmp"));
 		tab.setTelefoneEmp(rs.getString("TelefoneEmp"));
 		tab.setEmailEmp(rs.getString("EmailEmp"));
 		tab.setPixEmp(rs.getString("PixEmp"));
        return tab;
	}
}
