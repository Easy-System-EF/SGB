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
import gui.sgbmodel.dao.CartelaPaganteDao;
import gui.sgbmodel.entities.CartelaPagante;
  
public class CartelaPaganteDaoJDBC implements CartelaPaganteDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public CartelaPaganteDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "CartelaPagante ";
	
	@Override
	public void insert(CartelaPagante obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO cartelaPagante " +
				      "(paganteCartelaPag, dataCartelaPag, localCartelaPag, valorCartelaPag, " +
					  "formaCartelaPag, situacaoCartelaPag, cartelaIdOrigemPag, mesCartelaPag, " +
				      "anoCartelaPag )" +		
  				      "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 
 
			st.setInt(1, obj.getPaganteCartelaPag());
			st.setDate(2, new java.sql.Date(obj.getDataCartelaPag().getTime()));
			st.setString(3, obj.getLocalCartelaPag());
			st.setDouble(4, obj.getValorCartelaPag());
			st.setString(5, obj.getFormaCartelaPag());
			st.setString(6, obj.getSituacaoCartelaPag());
			st.setInt(7, obj.getCartelaIdOrigemPag());
			st.setInt(8, obj.getMesCartelaPag());
			st.setInt(9, obj.getAnoCartelaPag());
			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroCartelaPag(codigo);
//					System.out.println("New key inserted: " + obj.getCodigoCartelaPagante());
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
	public void deleteById(Integer idCar) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM cartelaPagante WHERE cartelaIdOrigemPag = ? ", 
						Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, idCar);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! naõ excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override 
	public List<CartelaPagante> findBySituacao(String local, String situacao) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM cartelaPagante " +
				 "WHERE LocalCartelaPag = ? AND SituacaoCartelaPag = ? ");

//	ex:		st.setDate(1, new java.sql.Date(rs.getTimestamp("data").getTime()));
			st.setString(1, local);
			st.setString(2, situacao);
			rs = st.executeQuery();
			
			List<CartelaPagante> list = new ArrayList<>();
			
			while (rs.next()) {
				CartelaPagante obj = instantiateCartelaPagantes(rs);
				list.add(obj);
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
	public List<CartelaPagante> findByMesAno(int mm, int aa, String str) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM cartelaPagante " +
				 "WHERE MesCartelaPag = ? AND AnoCartelaPag = ? AND SituacaoCartelaPag = ? " +
					"ORDER BY NumeroCartelaPag ") ;
			

//	ex:		st.setDate(1, new java.sql.Date(rs.getTimestamp("data").getTime()));
			st.setInt(1, mm);
			st.setInt(2, aa);
			st.setString(3, str);
			rs = st.executeQuery();
			
			List<CartelaPagante> list = new ArrayList<>();
			
			while (rs.next()) {
				CartelaPagante obj = instantiateCartelaPagantes(rs);
				list.add(obj);
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
	public List<CartelaPagante> findByCartela(Integer idCar) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * from cartelaPagante " +
					"WHERE CartelaIdOrigemPag = ? ");
			
			st.setInt(1, idCar);
			rs = st.executeQuery();
			
			List<CartelaPagante> listPag = new ArrayList<>();
			
			while(rs.next()) {
				CartelaPagante carPag = instantiateCartelaPagantes(rs);
				listPag.add(carPag);
			}
			return listPag;
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
	public CartelaPagante findById(Integer codigo) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * from cartelaPagante " +
					"WHERE NumeroCartelaPag = ? ");
			
			st.setInt(1, codigo);
			rs = st.executeQuery();
			
			while(rs.next()) {
				CartelaPagante carPag = instantiateCartelaPagantes(rs);
				return carPag;
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
	
	private CartelaPagante instantiateCartelaPagantes(ResultSet rs) throws SQLException {
 		CartelaPagante car = new CartelaPagante(); 		
 		car.setNumeroCartelaPag(rs.getInt("NumeroCartelaPag"));
 		car.setPaganteCartelaPag(rs.getInt("PaganteCartelaPag"));
 		car.setDataCartelaPag(new java.util.Date(rs.getTimestamp("DataCartelaPag").getTime()));	
 		car.setLocalCartelaPag(rs.getString("LocalCartelaPag"));
 		car.setValorCartelaPag(rs.getDouble("ValorCartelaPag"));
 		car.setFormaCartelaPag(rs.getString("FormaCartelaPag"));
 		car.setSituacaoCartelaPag(rs.getString("SituacaoCartelaPag"));
 		car.setCartelaIdOrigemPag(rs.getInt("CartelaIdOrigemPag"));
 		car.setMesCartelaPag(rs.getInt("MesCartelaPag"));
 		car.setAnoCartelaPag(rs.getInt("AnoCartelaPag"));
        return car;
	}
}
