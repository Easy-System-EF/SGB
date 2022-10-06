package gui.sgbmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import db.DB;
import db.DbException;
import gui.sgbmodel.dao.ReceberDao;
import gui.sgbmodel.entities.Receber;
import gui.sgcpmodel.entites.consulta.ParPeriodo;

public class ReceberDaoJDBC implements ReceberDao {

// aq entra um construtor declarando a conexão	
	private Connection conn;
	
	public ReceberDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	Locale ptBR = new Locale("pt", "BR");
	String classe = "Receber ";

	@Override
	public void insert(Receber obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					"INSERT INTO receber " + 
						"(CartelaRec, DataCartelaRec, FormaPagamentoRec, " +
						"ValorRec, DataPagamentoRec, PeriodoIdRec) " +
					"VALUES " +  
						"(?, ?, ?, ?, ?, ?)",

// retorna o o novo Id inserido 					
						Statement.RETURN_GENERATED_KEYS); 

			st.setInt(1, obj.getCartelaRec());
			st.setDate(2, new java.sql.Date(obj.getDataCartelaRec().getTime()));
			st.setString(3, obj.getFormaPagamentoRec());
			st.setDouble(4, obj.getValorRec());
			st.setDate(5, new java.sql.Date(obj.getDataPagamentoRec().getTime()));
			st.setInt(6, obj.getPeriodo().getIdPeriodo());
   			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroRec(codigo);
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
	public void removeCartela(Integer idCar) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM receber WHERE CartelaRec = ? ", 
						Statement.RETURN_GENERATED_KEYS);
 			st.setInt(1, idCar);
 						
 			st.executeUpdate();
 			
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro receber !!! sem exclusão " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}
 
	private Receber instantiateReceber(ResultSet rs, ParPeriodo per) throws SQLException {
		Receber obj = new Receber();
		obj.setNumeroRec(rs.getInt("NumeroRec"));
		obj.setCartelaRec(rs.getInt("CartelaRec"));
		obj.setDataCartelaRec(new java.util.Date(rs.getTimestamp("DataCartelaRec").getTime()));
		obj.setFormaPagamentoRec(rs.getString("FormaPagamentoRec"));
		obj.setValorRec(rs.getDouble("ValorRec"));
		obj.setDataPagamentoRec(new java.util.Date(rs.getTimestamp("DataPagamentoRec").getTime()) );
//objetos montados (sem id e com a associação la da classe Fornrecedor fornecedor...				
		obj.setPeriodo(per);
  		return obj;
	}

	private ParPeriodo instantiateParPeriodo(ResultSet rs) throws SQLException {
		ParPeriodo per = new ParPeriodo();
		per.setIdPeriodo(rs.getInt("IdPeriodo"));
		per.setDtiPeriodo(new java.util.Date(rs.getTimestamp("DtiPeriodo").getTime()));
		per.setDtfPeriodo(new java.util.Date(rs.getTimestamp("DtfPeriodo").getTime()));
   		return per;
	}
 
 	@Override
	public List<Receber> findAllAberto() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
 					"SELECT *, parPeriodo.* " +  
				    	"FROM receber "  +
				    		"INNER JOIN parPeriodo " +
				    			"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " + 
						"WHERE DataPagamentoRec = 0000-00-00 " +
							"ORDER BY CartelaRec ");

 			rs = st.executeQuery();
 			 
 			List<Receber> list = new ArrayList();
 			
			while (rs.next()) 
 			{ 	ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
   				list.add(obj);
  			}
 			return list;
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
	public List<Receber> findAllPago() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
 					"SELECT * " +  
				    	"FROM receber "  +
				    		"INNER JOIN parPeriodo " +
				    			"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " + 
						"WHERE DataPagamentoRec > 0000-00-00 " +
							"ORDER BY CartelaRec ");

 			rs = st.executeQuery();
 			 
 			List<Receber> list = new ArrayList();
 			
			while (rs.next()) 
 			{ 	ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
   				list.add(obj);
  			}
 			return list;
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
	public List<Receber> findPeriodoAberto() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
 			st = conn.prepareStatement(
  					"SELECT * " +  
  						"FROM receber "  +
  							"INNER JOIN parPeriodo " +
  								"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " +
						"WHERE DataPagamentoRec == 0 " +
						"ORDER BY CartelaRec ");
 				
 			rs = st.executeQuery();
			
			List<Receber> list = new ArrayList<>();
 			
			while (rs.next()) 
 			{ 	ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
   				list.add(obj);
  			}	
  			return list;
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
	public List<Receber> findPeriodoPago() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
 			st = conn.prepareStatement(

 					"SELECT * " +  
 		  				"FROM receber "  +
 		  					"INNER JOIN parPeriodo " +
 		  						"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " +
						"WHERE DataPagamentoRec > 0 " +
						"ORDER BY CartelaRec ");
    
 			rs = st.executeQuery();
			
			List<Receber> list = new ArrayList<>();
 			
			while (rs.next()) 
 			{ 	ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
   				list.add(obj);
  			}
 			return list;
  		}
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
 	} 
 }
