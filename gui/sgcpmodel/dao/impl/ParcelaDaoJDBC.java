package gui.sgcpmodel.dao.impl;

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
import gui.sgcpmodel.dao.ParcelaDao;
import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.entites.Parcela;
import gui.sgcpmodel.entites.TipoConsumo;
import gui.sgcpmodel.entites.consulta.ParPeriodo;

public class ParcelaDaoJDBC implements ParcelaDao {

// aq entra um construtor declarando a conexão	
	private Connection conn;
	
	public ParcelaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	Locale ptBR = new Locale("pt", "BR");

	@Override
	public void insert(Parcela obj) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"INSERT INTO parcela " + 
					"(CodigoFornecedorPar, NomeFornecedorPar, NnfPar, "
						+ "NumeroPar, DataVencimentoPar, ValorPar, DescontoPar, "
						+ "JurosPar, TotalPar, PagoPar, DataPagamentoPar, "
						+ "FornecedorIdPar, TipoIdPar, PeriodoIdPar) " +  
							"VALUES " +  
								"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
// retorna o o novo Id inserido 					
									Statement.RETURN_GENERATED_KEYS); 
			
 			st.setInt(1, obj.getFornecedor().getCodigo());
			st.setString(2, obj.getFornecedor().getRazaoSocial());
 			st.setInt(3, obj.getNnfPar());
 			st.setInt(4, obj.getNumeroPar());
			st.setDate(5, new java.sql.Date(obj.getDataVencimentoPar().getTime()));
  			st.setDouble(6, obj.getValorPar());
			st.setDouble(7, obj.getDescontoPar());
			st.setDouble(8, obj.getJurosPar());
			st.setDouble(9, obj.getTotalPar());
			st.setDouble(10, obj.getPagoPar());
			st.setDate(11, new java.sql.Date(obj.getDataPagamentoPar().getTime()));
  			st.setInt(12, obj.getFornecedor().getCodigo());
  			st.setInt(13, obj.getTipoFornecedor().getCodigoTipo());
  			st.setInt(14, obj.getPeriodo().getIdPeriodo());
   			
 			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Parcela obj) {
		PreparedStatement st = null;
 		try {
			st = conn.prepareStatement(
					
 					"UPDATE parcela " +
 						"INNER JOIN fornecedor " +
 							"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
 						"INNER JOIN tipoFornecedor " +
 							"ON parcela.TipoIdPar = tipoFornecedor.CodigoTipo " +
 						"INNER JOIN periodo " +
 							"ON parcela.PeriodoIdPar = periodo.IdPeriodo " +
 							"SET NomeFornecedorPar = ?, " +
 								"DataVencimentoPar = ?, " +
 								"ValorPar = ?, " +
 								"DescontoPar = ?, " +
 								"JurosPar = ?, " +
 								"TotalPar = ?, " +
 								"PagoPar = ?, " +
 								"DataPagamentoPar = ?, " +
 								"FornecedorIdPar = ?, " +
 								"TipoIdPar = ?, " +
 								"PeriodoIdPar = ? " +
  									"WHERE (CodigoFornecedorPar = ?, NnfPar = ?, NumeroPar = ? ",
  										Statement.RETURN_GENERATED_KEYS); 

			st.setString(1, obj.getNomeFornecedorPar());
			st.setDate(2, new java.sql.Date(obj.getDataVencimentoPar().getTime()));
 			st.setDouble(3, obj.getValorPar());
			st.setDouble(4, obj.getDescontoPar());
			st.setDouble(5, obj.getJurosPar());
			st.setDouble(6, obj.getTotalPar());
			st.setDouble(7, obj.getPagoPar());
			st.setDate(8, new java.sql.Date(obj.getDataPagamentoPar().getTime()));
  			st.setInt(9, obj.getFornecedor().getCodigo());
  			st.setInt(10, obj.getTipoFornecedor().getCodigoTipo());
  			st.setInt(11,  obj.getPeriodo().getIdPeriodo());
			st.setInt(12, obj.getFornecedor().getCodigo());
 			st.setInt(13, obj.getNnfPar());
 			st.setInt(14, obj.getNumeroPar());
   			
			st.executeUpdate();
			
    		}
 		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteByNumPar(String nome, Integer nnf, Integer num) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM parcela WHERE NomeFornecedorPar = ? AND " +
							"NnfPar = ? AND " +
							"NumeroPar = ? "
							, Statement.RETURN_GENERATED_KEYS);
 			st.setString(1, nome);
 			st.setInt(2, nnf);
 			st.setInt(3, num);
 						
 			st.executeUpdate();
 			
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro parcela !!! sem exclusão " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}
 
	@Override
	public void deleteByNnf(Integer nnfCom, Integer forn) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM parcela WHERE NnfPar = ? AND CodigoFornecedorPar = ? ", Statement.RETURN_GENERATED_KEYS);
 			st.setInt(1, nnfCom);
 			st.setInt(2, forn);
 						
 			st.executeUpdate();
 			
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro parcela !!! sem exclusão " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}
 
	private Parcela instantiateParcela(ResultSet rs, Fornecedor forn, TipoConsumo tipo, ParPeriodo per) throws SQLException {
		Parcela obj = new Parcela();
		obj.setCodigoFornecedorPar(forn.getCodigo());
		obj.setNomeFornecedorPar(forn.getRazaoSocial());
   		obj.setNnfPar(rs.getInt("NnfPar"));
   		obj.setNumeroPar(rs.getInt("NumeroPar"));
		obj.setDataVencimentoPar(new java.util.Date(rs.getTimestamp("DataVencimentoPar").getTime()));
 		obj.setValorPar(rs.getDouble("ValorPar"));
  		obj.setDescontoPar(rs.getDouble("DescontoPar"));
 		obj.setJurosPar(rs.getDouble("JurosPar"));
		obj.setTotalPar(rs.getDouble("TotalPar"));
		obj.setPagoPar(rs.getDouble("PagoPar"));
		obj.setDataPagamentoPar(new java.util.Date(rs.getTimestamp("DataPagamentoPar").getTime()));
 //objetos montados (sem id e com a associação la da classe Fornrecedor fornecedor...				
		obj.setFornecedor(forn);
		obj.setTipoFornecedor(tipo);
		obj.setPeriodo(per);
  		return obj;
	}

	private Parcela instantiateParcelaOne(ResultSet rs) throws SQLException {
		Parcela obj = new Parcela();
   		obj.setNnfPar(rs.getInt("NnfPar"));
   		obj.setNumeroPar(rs.getInt("NumeroPar"));
		obj.setDataVencimentoPar(new java.util.Date(rs.getTimestamp("DataVencimentoPar").getTime()));
 		obj.setValorPar(rs.getDouble("ValorPar"));
  		obj.setDescontoPar(rs.getDouble("DescontoPar"));
 		obj.setJurosPar(rs.getDouble("JurosPar"));
		obj.setTotalPar(rs.getDouble("TotalPar"));
		obj.setPagoPar(rs.getDouble("PagoPar"));
		obj.setDataPagamentoPar(new java.util.Date(rs.getTimestamp("DataPagamentoPar").getTime()));
  		return obj;
	}

	private Fornecedor instantiateFornecedor(ResultSet rs) throws SQLException {
		Fornecedor forn = new Fornecedor();
		forn.setCodigo(rs.getInt("Codigo"));
		forn.setRazaoSocial(rs.getString("RazaoSocial"));
 		forn.setRua(rs.getString("Rua"));
		forn.setNumero(rs.getInt("Numero"));
		forn.setComplemento(rs.getString("Complemento"));
		forn.setBairro(rs.getString("Bairro"));
		forn.setCidade(rs.getString("Cidade"));
		forn.setUf(rs.getString("UF"));
		forn.setCep(rs.getInt("Cep"));
		forn.setDdd01(rs.getInt("Ddd01"));
		forn.setTelefone01(rs.getInt("Telefone01"));
		forn.setDdd02(rs.getInt("Ddd02"));
		forn.setTelefone02(rs.getInt("Telefone02"));
		forn.setContato(rs.getString("Contato"));
		forn.setDddContato(rs.getInt("DddContato"));
		forn.setTelefoneContato(rs.getInt("TelefoneContato"));
		forn.setEmail(rs.getString("Email"));
		forn.setPix(rs.getString("Pix"));
 		return forn;
 	}


	private TipoConsumo instantiateTipo(ResultSet rs) throws SQLException {
		TipoConsumo tipo = new TipoConsumo();
		tipo.setCodigoTipo(rs.getInt("CodigoTipo"));
		tipo.setNomeTipo(rs.getString("NomeTipo"));
 		return tipo;
	}

	private ParPeriodo instantiateParPeriodo(ResultSet rs, Fornecedor forn) throws SQLException {
		ParPeriodo per = new ParPeriodo();
		per.setIdPeriodo(rs.getInt("IdPeriodo"));
		per.setDtiPeriodo(new java.util.Date(rs.getTimestamp("DtiPeriodo").getTime()));
		per.setDtfPeriodo(new java.util.Date(rs.getTimestamp("DtfPeriodo").getTime()));
		per.setFornecedor(forn);
		
//		per.setTipoFornecedor(tpForn);
   		return per;
	}
 
 	@Override
	public List<Parcela> findAllAberto() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
 					"SELECT * " +  
				    	"FROM parcela "  +
				    		"INNER JOIN fornecedor " +
				    			"ON parcela.FornecedorIdPar = fornecedor.codigo " + 
				    		"INNER JOIN parPeriodo " +
				    			"ON parcela.PeriodoIdPar = parPeriodo.idPeriodo " + 
	  						"INNER JOIN TipoFornecedor " +
	  							"ON parcela.TipoIdPar = tipoFornecedor.CodigoTipo " +
	  								"WHERE PagoPar = 0 " +
 									"ORDER BY DataVencimentoPar ");

 			rs = st.executeQuery();
 			 
 			List<Parcela> list = new ArrayList();
 			
			while (rs.next()) 
 			{ 	Fornecedor forn = instantiateFornecedor(rs);
				TipoConsumo tipo = instantiateTipo(rs);
				ParPeriodo per = instantiateParPeriodo(rs, forn);
				Parcela obj = instantiateParcela(rs, forn, tipo, per);
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
	public List<Parcela> findAllPago() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
 					"SELECT * " +  
				    	"FROM parcela "  +
				    		"INNER JOIN fornecedor " +
				    			"ON parcela.FornecedorIdPar = fornecedor.codigo " + 
				    		"INNER JOIN parPeriodo " +
				    			"ON parcela.PeriodoIdPar = parPeriodo.idPeriodo " + 
	  						"INNER JOIN TipoFornecedor " +
	  							"ON parcela.TipoIdPar = tipoFornecedor.CodigoTipo " +
	  								"WHERE PagoPar > 0 " +
 									"ORDER BY DataVencimentoPar ");

 			rs = st.executeQuery();
 			 
 			List<Parcela> list = new ArrayList();
 			
			while (rs.next()) 
 			{ 	Fornecedor forn = instantiateFornecedor(rs);
				TipoConsumo tipo = instantiateTipo(rs);
				ParPeriodo per = instantiateParPeriodo(rs, forn);
				Parcela obj = instantiateParcela(rs, forn, tipo, per);
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
	public List<Parcela> findPeriodoAberto() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
 			st = conn.prepareStatement(
  					"SELECT * " +  
  						"FROM parcela "  +
  							"INNER JOIN parPeriodo " +
  								"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
  							"INNER JOIN fornecedor " +
  								"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
	  						"INNER JOIN TipoFornecedor " +
	  							"ON parcela.TipoIdPar = tipoFornecedor.CodigoTipo " +
  									"WHERE  PagoPar = 0 AND " +
  									"parcela.DataVencimentoPar >= parPeriodo.DtiPeriodo AND " +
  									"parcela.DataVencimentoPar <= parPeriodo.DtfPeriodo " +
 										"ORDER BY DataVencimentoPar, NumeroPar ");
 				
 			rs = st.executeQuery();
			
			List<Parcela> list = new ArrayList<>();
 			
			while (rs.next()) 
 			{ 	Fornecedor forn = instantiateFornecedor(rs);
				TipoConsumo tipo = instantiateTipo(rs);
				ParPeriodo per = instantiateParPeriodo(rs, forn);
				Parcela obj = instantiateParcela(rs, forn, tipo, per);
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
	public List<Parcela> findPeriodoPago() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
 			st = conn.prepareStatement(

 					"SELECT * " +  
 		  				"FROM parcela "  +
 		  					"INNER JOIN parPeriodo " +
 		  						"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
 		  					"INNER JOIN fornecedor " +
 		  						"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
	  						"INNER JOIN TipoFornecedor " +
	  							"ON parcela.TipoIdPar = tipoFornecedor.CodigoTipo " +
 		  							"WHERE  PagoPar > 0 AND " +
 		  							"parcela.DataVencimentoPar >= parPeriodo.DtiPeriodo AND " +
 		  							"parcela.DataVencimentoPar <= parPeriodo.DtfPeriodo " +
 										"ORDER BY DataVencimentoPar, NumeroPar ");
    
 			rs = st.executeQuery();
			
			List<Parcela> list = new ArrayList<>();
 			
			while (rs.next()) 
 			{ 	Fornecedor forn = instantiateFornecedor(rs);
				TipoConsumo tipo = instantiateTipo(rs);
				ParPeriodo per = instantiateParPeriodo(rs, forn);
				Parcela obj = instantiateParcela(rs, forn, tipo, per);
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
	public List<Parcela> findByIdFornecedorAberto(Integer cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
 
					"SELECT * " +  
		  				"FROM parcela "  +
		  					"INNER JOIN parPeriodo " +
		  						"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
		  					"INNER JOIN fornecedor " +
		  						"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
	  						"INNER JOIN TipoFornecedor " +
	  							"ON parcela.TipoIdPar = tipoFornecedor.CodigoTipo " +
		  							"WHERE codigoFornecedorPar = ? AND " +
		  							" PagoPar = 0 " +
										"ORDER BY DataVencimentoPar, Numeropar");

  			st.setInt(1, cod);
			rs = st.executeQuery();
			
			List<Parcela> list = new ArrayList<>();
  
  			while (rs.next()) 
 			{ 	Fornecedor forn = instantiateFornecedor(rs);
				TipoConsumo tipo = instantiateTipo(rs);
				ParPeriodo per = instantiateParPeriodo(rs, forn);
				Parcela obj = instantiateParcela(rs, forn, tipo, per);
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
	public List<Parcela> findByIdFornecedorPago(Integer cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
 
					"SELECT * " +  
		  				"FROM parcela "  +
		  					"INNER JOIN parPeriodo " +
		  						"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
		  					"INNER JOIN fornecedor " +
		  						"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
	  						"INNER JOIN TipoFornecedor " +
	  							"ON parcela.TipoIdPar = tipoFornecedor.CodigoTipo " +
		  							"WHERE codigoFornecedorPar = ? AND " +
		  							" PagoPar > 0 " +
										"ORDER BY DataVencimentoPar, Numeropar");
 			
  			st.setInt(1, cod);
			rs = st.executeQuery();
			
			List<Parcela> list = new ArrayList<>();
  
  			while (rs.next()) 
 			{ 	Fornecedor forn = instantiateFornecedor(rs);
				TipoConsumo tipo = instantiateTipo(rs);
				ParPeriodo per = instantiateParPeriodo(rs, forn);
				Parcela obj = instantiateParcela(rs, forn, tipo, per);
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
	public List<Parcela> findByIdTipoAberto(Integer cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
 
					"SELECT * " +  
		  				"FROM parcela "  +
		  					"INNER JOIN parPeriodo " +
		  						"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
			  						"INNER JOIN fornecedor " +
		  						"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
			  						"INNER JOIN TipoFornecedor " +
		  						"ON parcela.TipoIdPar = tipoFornecedor.CodigoTipo " +
		  							"WHERE TipoIdPar = ? AND " +
		  							"PagoPar = 0 " +
										"ORDER BY DataVencimentoPar, Numeropar");
 			
  			st.setInt(1, cod);
			rs = st.executeQuery();
			
			List<Parcela> list = new ArrayList<>();
  
  			while (rs.next()) 
 			{ 	Fornecedor forn = instantiateFornecedor(rs);
 				TipoConsumo tipo = instantiateTipo(rs);
				ParPeriodo per = instantiateParPeriodo(rs, forn);
			    Parcela obj = instantiateParcela(rs, forn, tipo, per);
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
	public List<Parcela> findByIdTipoPago(Integer cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
 
					"SELECT * " +  
		  				"FROM parcela "  +
		  					"INNER JOIN parPeriodo " +
		  						"ON parcela.PeriodoIdPar = parPeriodo.IdPeriodo " +
			  						"INNER JOIN fornecedor " +
		  						"ON parcela.FornecedorIdPar = fornecedor.Codigo " +
			  						"INNER JOIN TipoFornecedor " +
		  						"ON parcela.TipoIdPar = tipoFornecedor.CodigoTipo " +
		  							"WHERE TipoIdPar = ? AND " +
		  							"PagoPar > 0 " +
										"ORDER BY DataVencimentoPar, Numeropar");
 			
  			st.setInt(1, cod);
			rs = st.executeQuery();
			
			List<Parcela> list = new ArrayList<>();
  
  			while (rs.next()) 
 			{ 	Fornecedor forn = instantiateFornecedor(rs);
 				TipoConsumo tipo = instantiateTipo(rs);
				ParPeriodo per = instantiateParPeriodo(rs, forn);
			    Parcela obj = instantiateParcela(rs, forn, tipo, per);
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
