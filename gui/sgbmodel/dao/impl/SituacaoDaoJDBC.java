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
import gui.sgbmodel.dao.SituacaoDao;
import gui.sgbmodel.entities.Situacao;
  
public class SituacaoDaoJDBC implements SituacaoDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public SituacaoDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Situacao ";
	
	@Override
	public void insert(Situacao obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO login " +
				      "(NomeSit)" + 
  				      "VALUES " +
				      "(?)",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setString(1, obj.getNomeSit());
			
// 			st.executeUpdate();

 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroSit(codigo);;
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
	public void update(Situacao obj) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"UPDATE situacao " +  
							"SET NomeSit = ? " +
   					"WHERE (NumeroSit = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

 			st.setString(1, obj.getNomeSit());
 			st.setInt(2, obj.getNumeroSit());
    			
			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 		throw new DbException (classe + "Erro!!! sem atualização " + e.getMessage()); }

  		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer numeroSit) {
		PreparedStatement st = null;
//		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM situacao WHERE NumeroSit = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, numeroSit);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! naõ excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public Situacao findById(Integer numeroSit) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM situacao " +
 				 "WHERE NumeroSit = ?");
 			
			st.setInt(1, numeroSit);
			rs = st.executeQuery();
			if (rs.next())
			{	if (rs != null)
				{	Situacao obj = instantiateSituacao(rs);
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
	public List<Situacao> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM situacao " +
					"ORDER BY NomeSit");
			
			rs = st.executeQuery();
			
			List<Situacao> list = new ArrayList<>();
			
			while (rs.next())
			{	if (rs != null)
				{	Situacao obj = instantiateSituacao(rs);
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
	
	private Situacao instantiateSituacao(ResultSet rs) throws SQLException {
 		Situacao situacao = new Situacao();
 		
 		situacao.setNumeroSit(rs.getInt("NumeroSit"));
 		situacao.setNomeSit(rs.getString("NomeSit"));	
         return situacao;
	}
}
