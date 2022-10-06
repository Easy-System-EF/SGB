package gui.sgbmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import gui.sgbmodel.dao.LoginDao;
import gui.sgbmodel.entities.Login;
  
public class LoginDaoJDBC implements LoginDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public LoginDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Login ";
	
	@Override
	public void insert(Login obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO login " +
				      "(SenhaLog, NomeLog, NivelLog, AlertaLog, DataLog, VencimentoLog, " +
							"MaximaLog, AcessoLog, EmpresaLog )" + 
  				      "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setString(1, obj.getSenhaLog());
			st.setString(2, obj.getNomeLog());
			st.setInt(3, obj.getNivelLog());
			st.setInt(4, obj.getAlertaLog());
			st.setDate(5, new java.sql.Date(obj.getDataLog().getTime()));
			st.setDate(6, new java.sql.Date(obj.getVencimentoLog().getTime()));
			st.setDate(7, new java.sql.Date(obj.getMaximaLog().getTime()));
			st.setDate(8, new java.sql.Date(obj.getAcessoLog().getTime()));
			st.setInt(9, obj.getEmpresaLog());
			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	Integer num = rs.getInt(1);
					obj.setNumeroLog(num);
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
	public void update(Login obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE login " +
				      "SET SenhaLog = ?, NomeLog = ?, NivelLog = ?, AlertaLog = ?, " +
							"DataLog = ?, VencimentoLog = ?, MaximaLog = ?, AcessoLog = ?, " +
							"EmpresaLog = ? " +
  				      "WHERE NumeroLog = ? ",
  				      		Statement.RETURN_GENERATED_KEYS); 

			st.setString(1, obj.getSenhaLog());
			st.setString(2, obj.getNomeLog());
			st.setInt(3, obj.getNivelLog());
			st.setInt(4, obj.getAlertaLog());
			st.setDate(5, new java.sql.Date(obj.getDataLog().getTime()));
			st.setDate(6, new java.sql.Date(obj.getVencimentoLog().getTime()));
			st.setDate(7, new java.sql.Date(obj.getMaximaLog().getTime()));
			st.setDate(8, new java.sql.Date(obj.getAcessoLog().getTime()));
			st.setInt(9, obj.getEmpresaLog());
			st.setInt(10, obj.getNumeroLog());

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
	public Login findBySenha(String senha) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM login " +
 				 "WHERE SenhaLog = ? ");
 			
			st.setString(1, senha);
			
			rs = st.executeQuery();
			if (rs.next())
			{	if (rs != null)
				{	Login obj = instantiateLog(rs);
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
	public List<Login> findAll() {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM login " );
 			
			rs = st.executeQuery();
			
			List<Login> list = new ArrayList<>();
			
			while (rs.next())
			{	if (rs != null)
				{	Login obj = instantiateLog(rs);
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
	
	@Override
	public Login findById(Integer cod) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM login " +
 				 "WHERE NumeroLog = ? ");
 			
			st.setInt(1, cod);
			
			rs = st.executeQuery();
			if (rs.next())
			{	if (rs != null)
				{	Login obj = instantiateLog(rs);
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
	
	private Login instantiateLog(ResultSet rs) throws SQLException {
 		Login log = new Login();
 		log.setNumeroLog(rs.getInt("NumeroLog"));
 		log.setSenhaLog(rs.getString("SenhaLog"));
 		log.setNomeLog(rs.getString("NomeLog"));
 		log.setNivelLog(rs.getInt("NivelLog"));
 		log.setAlertaLog(rs.getInt("AlertaLog"));
 		log.setDataLog(new java.util.Date(rs.getTimestamp("DataLog").getTime()));
 		log.setDataVencimentoLog(new java.util.Date(rs.getTimestamp("VencimentoLog").getTime()));
 		log.setDataMaximaLog(new java.util.Date(rs.getTimestamp("MaximaLog").getTime()));
 		log.setAcessoLog(new java.util.Date(rs.getTimestamp("AcessoLog").getTime()));
 		log.setEmpresaLog(rs.getInt("EmpresaLog"));
        return log;
	}
}
