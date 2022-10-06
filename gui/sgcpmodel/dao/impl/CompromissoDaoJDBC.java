package gui.sgcpmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import db.DB;
import db.DbException;
import gui.sgcpmodel.dao.CompromissoDao;
import gui.sgcpmodel.entites.Compromisso;
import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.entites.TipoConsumo;
import gui.sgcpmodel.entites.consulta.ParPeriodo;

public class CompromissoDaoJDBC implements CompromissoDao {
 
// aq entra um construtor declarando a conexão	
	private Connection conn;
	
	public CompromissoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
 
 	@Override
	public void insert(Compromisso obj) {
  		Locale ptBR = new Locale("pt", "BR");
  		PreparedStatement st = null;
 		try {
			st = conn.prepareStatement(
					"INSERT INTO compromisso " + 
					"(CodigoFornecedorCom, NomeFornecedorCom, NnfCom, DataCom, "
					+ "DataVencimentoCom, ValorCom, ParcelaCom, PrazoCom, "
					+ "FornecedorIdCom, TipoIdCom, PeriodoIdCom) " 
					+ "VALUES " +  
					"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
 
					Statement.RETURN_GENERATED_KEYS); 

			st.setInt(1, obj.getFornecedor().getCodigo());
			st.setString(2, obj.getFornecedor().getRazaoSocial()); 
   			st.setInt(3, obj.getNnfCom());
			st.setDate(4, new java.sql.Date(obj.getDataCom().getTime()));
			st.setDate(5, new java.sql.Date(obj.getDataVencimentoCom().getTime()));
 			st.setDouble(6, obj.getValorCom());
			st.setInt(7, obj.getParcelaCom());
			st.setInt(8, obj.getPrazoCom());
 			st.setInt(9, obj.getFornecedor().getCodigo());
 			st.setInt(10, obj.getTipoFornecedor().getCodigoTipo());
 			st.setInt(11, obj.getParPeriodo().getIdPeriodo());
  			
			 st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException ("Erro compromisso !!! no insert " + e.getMessage());  
		}
		finally {
 			DB.closeStatement(st);
		}
	}

 	@Override
	public void deleteById(Integer nnf, Integer forn) {
 		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM compromisso WHERE NnfCom = ? AND codigoFornecedorCom = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, nnf);
			st.setInt(2, forn);
 			st.executeUpdate();
      		}
  			catch (SQLException e) {
  				throw new DbException ( "Erro compromisso !!! sem exclusão " + e.getMessage()); }
  			finally {
  				DB.closeStatement(st);
  			}
		}

	@Override
	public List<Compromisso> findPesquisa(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM Compromisso.*, Fornecedor.*, TipoFornecedor.*, ParPeriodo.* " +
 						"INNER JOIN fornecedor " +
 							"ON compromisso.FornecedorIdCom = fornecedor.Codigo " +
 						"INNER JOIN tipoFornecedor " +
 							"ON compromisso.TipoIdCom = tipoFornecedor.CodigoTipo " +
 						"INNER JOIN periodo " +
 							"ON compromisso.PeriodoIdCom = parPeriodo.IdPeriodo " +
						"WHERE NomeFornecedorCom like ? " +	
					"ORDER BY NomeFornecedorCom ");
			
			st.setString(1, str + "%");
			rs = st.executeQuery();
			
			List<Compromisso> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapFor = new HashMap();
			Map<Integer, TipoConsumo> mapTp = new HashMap();
			Map<Integer, ParPeriodo> mapPer = new HashMap();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdCom"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdCom"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoFornecedorIdCom"));
				if (objTp == null) {
					objTp = instantiateTipoFornecedor(rs);
					mapTp.put(rs.getInt("TipoFornecedorIdCom"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("ParPeriodoIdCom"));
				if (objPer == null) {
					objPer = instantiatePeriodo(rs, objFor, objTp);
					mapPer.put(rs.getInt("ParPeriodoIdCom"), objPer);
				}	
				Compromisso obj = instantiateCompromisso(rs, objFor, objTp, objPer);
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
	
 	private Compromisso instantiateCompromisso(ResultSet rs, Fornecedor forn, TipoConsumo tipoForn, ParPeriodo periodo) throws SQLException {
 		Compromisso obj = new Compromisso();
		obj.setCodigoFornecedorCom(rs.getInt("CodigoFornecedorCom"));
		obj.setNomeFornecedorCom(rs.getString("NomeFornecedorCom"));
   		obj.setNnfCom(rs.getInt("NnfCom"));
		obj.setDataCom(new java.util.Date(rs.getTimestamp("DataCom").getTime()));
		obj.setDataVencimentoCom(new java.util.Date(rs.getTimestamp("DataVencimentoCom").getTime()));
 		obj.setValorCom(rs.getDouble("ValorCom"));
  		obj.setParcelaCom(rs.getInt("ParcelaCom"));
		obj.setPrazoCom(rs.getInt("PrazoCom"));
//objetos montados (sem id e com a associação la da classe Fornrecedor fornecedor...				
		obj.setFornecedor(forn);
		obj.setTipoFornecedor(tipoForn);
		obj.setParPeriodo(periodo);
  		return obj;
	}

	private Compromisso instantiateCompromissoOne(ResultSet rs) throws SQLException {
 		Compromisso obj = new Compromisso();
		obj.setCodigoFornecedorCom(rs.getInt("CodigoFornecedorCom"));
		obj.setNomeFornecedorCom(rs.getString("NomeFornecedorCom"));
   		obj.setNnfCom(rs.getInt("NnfCom"));
		obj.setDataCom(new java.util.Date(rs.getTimestamp("DataCom").getTime()));
		obj.setDataVencimentoCom(new java.util.Date(rs.getTimestamp("DataVencimentoCom").getTime()));
 		obj.setValorCom(rs.getDouble("ValorCom"));
  		obj.setParcelaCom(rs.getInt("ParcelaCom"));
		obj.setPrazoCom(rs.getInt("PrazoCom"));
//objetos montados (sem id e com a associação la da classe Fornrecedor fornecedor...				
//		obj.setFornecedor(forn);
//		obj.setTipoFornecedor(tipoForn);
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
  	
	private TipoConsumo instantiateTipoFornecedor(ResultSet rs) throws SQLException {
		TipoConsumo tipoForn = new TipoConsumo();
		tipoForn.setCodigoTipo(rs.getInt("CodigoTipo"));
		tipoForn.setNomeTipo(rs.getString("NomeTipo"));
 		return tipoForn;
	}

	private ParPeriodo instantiatePeriodo(ResultSet rs, Fornecedor objFor, TipoConsumo objTp) throws SQLException {
		ParPeriodo periodo = new ParPeriodo();
		periodo.setIdPeriodo(rs.getInt("IdPeriodo"));
		periodo.setDtiPeriodo(rs.getDate("DtiPeriodo"));
		periodo.setDtfPeriodo(rs.getDate("DtfPeriodo"));
		periodo.setFornecedor(objFor);
		periodo.setTipoConsumo(objTp);
  		return periodo;
	}

	@Override
	public List<Compromisso> findAll() {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
// nome do fornecedor(apelido aqui) = ForId					
			st = conn.prepareStatement(
 // nome das entities fornecedor(apelido aqui) = ForName / TipoName

					"SELECT *, fornecedor.Codigo, tipoFornecedor.CodigoTipo, parPeriodo.IdPeriodo " + 
					 	"FROM Compromisso " +
					 		"INNER JOIN fornecedor " +
					 			"on compromisso.FornecedorIdCom = fornecedor.Codigo " +
					 				"INNER JOIN Tipofornecedor " +
					 			"on compromisso.TipoIdCom = tipoFornecedor.CodigoTipo " +
					 				"INNER JOIN ParPeriodo " +
					 			"on compromisso.PeriodoIdCom = parPeriodo.IdPeriodo " +
    								"ORDER BY DataCom ");
 			
 			rs = st.executeQuery();
 
 			List<Compromisso> list = new ArrayList();
 			
			while (rs.next()) 
 			{ 	Fornecedor forn = instantiateFornecedor(rs);
 			    TipoConsumo tipoForn = instantiateTipoFornecedor(rs);
 			    ParPeriodo periodo = instantiatePeriodo(rs, forn, tipoForn);
 			    Compromisso obj = instantiateCompromisso(rs, forn, tipoForn, periodo);
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
	public List<Compromisso> findByFornecedor(Fornecedor fornecedor) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
// nome da coluna na tabela - (apelido aqui) = ForId					
					"SELECT compromisso.*, fornecedor.Codigo " +  
						"FROM compromisso INNER JOIN fornecedor "  +
							"ON compromisso.FornecedorIdCom = fornecedor.Codigo " + 
								"WHERE compromisso.codigoFornecedorCom = ? " + 
									"ORDER BY DataCom");
			
  			st.setInt(1, fornecedor.getCodigo());
			rs = st.executeQuery();
			
			List<Compromisso> list = new ArrayList<>();
			Map<Integer, Fornecedor> map = new HashMap<>();

/* se map for nulo, instanciamos o forn e salvamos o forn no map
 * map put salva o vlr da chave (acima rs.getInt e o fornecedor da variavel forn			
 */
			while (rs.next()) 
			{	Fornecedor forn = map.get(rs.getInt("fornecedor.Codigo"));
 				if (forn == null) 
				{	forn = instantiateFornecedor(rs);
 					map.put(rs.getInt("fornecedor.Codigo"), forn);
 				}
 				TipoConsumo tipoForn = instantiateTipoFornecedor(rs); 
 			    ParPeriodo periodo = instantiatePeriodo(rs, forn, tipoForn);
  				Compromisso obj = instantiateCompromisso(rs, forn, tipoForn, periodo);
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
	public List<Compromisso> findByTipo(int tp) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
// nome do tipo(apelido aqui) = TipoName					
					"SELECT compromisso.*, tipoFornecedor.CodigoTipo " +  
						"FROM compromisso " +
							"INNER JOIN tipoFornecedor "  +
							"ON compromisso.TipoIdCom = tipoFornecedor.CodigoTipo " + 
								"WHERE compromisso.TipoId = ? " + 
									"ORDER BY TipoId");

  			st.setInt(1, tp);
 			rs = st.executeQuery();
			
			List<Compromisso> list = new ArrayList<>();
			Map<Integer, TipoConsumo> map = new HashMap<>();
			
			while (rs.next()) 
			{	TipoConsumo tipoForn = map.get(rs.getInt("tipoFornecedor.CodigoTipo"));
				if (tipoForn == null) 
				{	tipoForn = instantiateTipoFornecedor(rs);
					map.put(rs.getInt("tipoFornecedor.CodigoTipo"), tipoForn);
 				}
 				Fornecedor forn = instantiateFornecedor(rs); 
 			    ParPeriodo periodo = instantiatePeriodo(rs, forn, tipoForn);
  				Compromisso obj = instantiateCompromisso(rs, forn, tipoForn, periodo);
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
