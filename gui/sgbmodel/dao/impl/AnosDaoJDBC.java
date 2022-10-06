package gui.sgbmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import gui.sgbmodel.dao.AnosDao;
import gui.sgbmodel.entities.Anos;
  
public class AnosDaoJDBC implements AnosDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public AnosDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public List<Anos> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM anos " +
					"ORDER BY AnoAnos");
			
			rs = st.executeQuery();
			
			List<Anos> list = new ArrayList<>();
			
			while (rs.next())
			{	if (rs != null)
				{	Anos obj = instantiateAnos(rs);
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
	public Anos findAno(Integer ano) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * " +
						"From Anos " +	
					"Where AnoAnos = ? " );
			
			st.setInt(1, ano);
			
			rs = st.executeQuery();
						
			if (rs.next()) {
				Anos obj = instantiateAnos(rs);
 				return obj;
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
	
	private Anos instantiateAnos(ResultSet rs) throws SQLException {
 		Anos ano = new Anos();
 		ano.setNumeroAnos(rs.getInt("NumeroAnos"));
 		ano.setAnoAnos(rs.getInt("AnoAnos"));
 		ano.setAnoStrAnos(rs.getString("AnoStrAnos"));
        return ano;
	}
}
