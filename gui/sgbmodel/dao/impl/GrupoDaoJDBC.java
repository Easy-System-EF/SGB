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
import gui.sgbmodel.dao.GrupoDao;
import gui.sgbmodel.entities.Grupo;
  
public class GrupoDaoJDBC implements GrupoDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public GrupoDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	String classe = "Grupo ";

	@Override
	public void insert(Grupo obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO grupo " +
				      "(NomeGru)" + 
  				      "VALUES " +
				      "(?)",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setString(1, obj.getNomeGru());
			
// 			st.executeUpdate();

 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setCodigoGru(codigo);;
					System.out.println("New key inserted: " + obj.getCodigoGru());
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
	public void update(Grupo obj) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"UPDATE grupo " +  
							"SET NomeGru = ? " +
   					"WHERE (CodigoGru = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

 			st.setString(1, obj.getNomeGru());
 			st.setInt(2, obj.getCodigoGru());
    			
			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 		throw new DbException (classe + "Erro!!! sem atualização " + e.getMessage()); }

  		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigoGru) {
		PreparedStatement st = null;
//		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM grupo WHERE CodigoGru = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigoGru);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! naõ excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public Grupo findById(Integer codigo) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM grupo " +
 				 "WHERE CodigoGru = ?");
 			
			st.setInt(1, codigo);
			rs = st.executeQuery();
			if (rs.next())
			{	if (rs != null)
				{	Grupo obj = instantiateGrupos(rs);
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
	public List<Grupo> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM grupo " +
					"ORDER BY NomeGru");
			
			rs = st.executeQuery();
			
			List<Grupo> list = new ArrayList<>();
			
			while (rs.next())
			{	if (rs != null)
				{	Grupo obj = instantiateGrupos(rs);
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
	
	private Grupo instantiateGrupos(ResultSet rs) throws SQLException {
 		Grupo grupo = new Grupo();
 		
 		grupo.setCodigoGru(rs.getInt("CodigoGru"));
 		grupo.setNomeGru(rs.getString("NomeGru"));	
         return grupo;
	}
}
