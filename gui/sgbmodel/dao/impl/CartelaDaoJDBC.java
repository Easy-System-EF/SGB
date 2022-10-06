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
import gui.sgbmodel.dao.CartelaDao;
import gui.sgbmodel.entities.Cartela;
 
public class CartelaDaoJDBC implements CartelaDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public CartelaDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Cartela ";
	
	@Override
	public void insert(Cartela obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO cartela " +
				      "(DataCar, LocalCar, DescontoCar, TotalCar, SituacaoCar, " +
				       "NumeroPaganteCar, ValorPaganteCar, MesCar, AnoCar, ObsCar, " +
				       "ServicoCar, ValorServicoCar, SubTotalCar, NomeSituacaoCar ) " +
   				       "VALUES " +
				       "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setDate(1, new java.sql.Date(obj.getDataCar().getTime()));
			st.setString(2, obj.getLocalCar());
			st.setDouble(3, obj.getDescontoCar());
			st.setDouble(4,  obj.getTotalCar());
			st.setString(5, obj.getSituacaoCar());
			st.setInt(6,  obj.getNumeroPaganteCar());
			st.setDouble(7, obj.getValorPaganteCar());
			st.setInt(8, obj.getMesCar());
			st.setInt(9, obj.getAnoCar());
			st.setString(10, obj.getObsCar());
			st.setString(11, obj.getServicoCar());
			st.setDouble(12, obj.getValorServicoCar());
			st.setDouble(13, obj.getSubTotalCar());
			st.setString(14, obj.getNomeSituacaoCar());
			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroCar(codigo);
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
	public void update(Cartela obj) {
		PreparedStatement st = null;
  		try {
  			conn.setAutoCommit(false);
			st = conn.prepareStatement(
					"UPDATE cartela " +  
							"SET DataCar = ?, " +
							 "LocalCar = ?, " +
							 "DescontoCar = ?, " +
							 "TotalCar = ?, " +
							 "SituacaoCar = ?, " +
							 "NumeroPaganteCar = ?, " +
							 "ValorPaganteCar = ?, " +
							 "MesCar = ?, " +
							 "AnoCar = ?, " +
							 "ObsCar = ?, " +
							 "ServicoCar = ?, " +
							 "ValorServicoCar = ?, " +
							 "SubTotalCar = ?, " +
							 "NomeSituacaoCar = ? " +
   					 "WHERE NumeroCar = ? ",
        			 Statement.RETURN_GENERATED_KEYS);
			
			st.setDate(1, new java.sql.Date(obj.getDataCar().getTime()));
			st.setString(2, obj.getLocalCar());
			st.setDouble(3, obj.getDescontoCar());
			st.setDouble(4,  obj.getTotalCar());
			st.setString(5, obj.getSituacaoCar());
			st.setInt(6,  obj.getNumeroPaganteCar());
			st.setDouble(7, obj.getValorPaganteCar());
			st.setInt(8, obj.getMesCar());
			st.setInt(9, obj.getAnoCar());
			st.setString(10, obj.getObsCar());
			st.setString(11, obj.getServicoCar());
			st.setDouble(12, obj.getValorServicoCar());
			st.setDouble(13, obj.getSubTotalCar());
			st.setString(14, obj.getNomeSituacaoCar());
			st.setInt(15, obj.getNumeroCar());
			
 			st.executeUpdate();
 			conn.commit();
   		} 
 		catch (SQLException e) {
 				throw new DbException (classe + "Erro!!! sem atualização " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM cartela WHERE NumeroCar = ? ", 
						Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigo);
			st.executeUpdate();
   		}
  		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! naõ excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public Cartela findById(Integer codigo) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT cartela.* " +
				   "FROM cartela " +
				 "WHERE NumeroCar = ?");
 			
			st.setInt(1, codigo);
			rs = st.executeQuery();
			
			while (rs.next()) {
 			    Cartela obj = instantiateCartela(rs);
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

	@Override
	public List<Cartela> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					 "SELECT cartela.* " +
							   "FROM cartela " +
								 "ORDER BY - NumeroCar");
			
			rs = st.executeQuery();
			
 			List<Cartela> list = new ArrayList();
 			
			while (rs.next()) {
 			    Cartela obj = instantiateCartela(rs);
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
	public List<Cartela> findByMesAno(Integer mm, Integer aa) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					 "SELECT cartela.* " +
							   "FROM cartela " +
								 "WHERE MesCar = ? AND AnoCar = ? ");
			
			st.setInt(1, mm);
			st.setInt(2, aa);
			rs = st.executeQuery();
			
 			List<Cartela> list = new ArrayList();
 			
			while (rs.next()) {
 			    Cartela obj = instantiateCartela(rs);
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
	public List<Cartela> findSituacaoAberto(Integer mm, Integer aa, String strA, String strC) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					 "SELECT cartela.* " +
							   "FROM cartela " +
			 "WHERE MesCar = ? AND AnoCar = ? AND (SituacaoCar = ? OR SituacaoCar = ?) " +
							   "ORDER BY NumeroCar ");
			
			st.setInt(1, mm);
			st.setInt(2, aa);
			st.setString(3, strA);
			st.setString(4, strC);
			rs = st.executeQuery();
			
 			List<Cartela> list = new ArrayList();
 			
			while (rs.next()) {
 			    Cartela obj = instantiateCartela(rs);
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
	public List<Cartela> findSituacao(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					 "SELECT cartela.* " +
						"FROM cartela " +
							"WHERE SituacaoCar = ? " +
								 "ORDER BY NumeroCar AND LocalCar");

			st.setString(1, str);
			rs = st.executeQuery();
			
 			List<Cartela> list = new ArrayList();
 			
			while (rs.next()) {
 			    Cartela obj = instantiateCartela(rs);
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

	private Cartela instantiateCartela(ResultSet rs) throws SQLException {
 		Cartela car = new Cartela();
 		car.setNumeroCar(rs.getInt("NumeroCar"));
		car.setDataCar(new java.util.Date(rs.getTimestamp("DataCar").getTime()));
		car.setLocalCar(rs.getString("LocalCar"));
		car.setDescontoCar(rs.getDouble("DescontoCar"));
		car.setTotalCar(rs.getDouble("TotalCar"));
 		car.setSituacaoCar(rs.getString("SituacaoCar"));
		car.setNumeroPaganteCar(rs.getInt("NumeroPaganteCar"));
		car.setValorPaganteCar(rs.getDouble("ValorPaganteCar"));
		car.setMesCar(rs.getInt("MesCar"));
		car.setAnoCar(rs.getInt("AnoCar"));
		car.setObsCar(rs.getString("ObsCar"));
		car.setServicoCar(rs.getString("ServicoCar"));
		car.setValorServicoCar(rs.getDouble("ValorServicoCar"));
		car.setSubTotalCar(rs.getDouble("SubTotalCar"));
		car.setNomeSituacaoCar(rs.getString("NomeSituacaoCar"));
        return car;
	}
}
