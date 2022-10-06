package gui.sgbmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import gui.sgbmodel.dao.EntradaDao;
import gui.sgbmodel.entities.Entrada;
import gui.sgbmodel.entities.Produto;
import gui.sgcpmodel.entites.Fornecedor;
  
public class EntradaDaoJDBC implements EntradaDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public EntradaDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Entrada";
	
	@Override
	public void insert(Entrada obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO entrada " 
				      + "(NnfEnt, DataEnt, NomeFornEnt, NomeProdEnt, QuantidadeProdEnt, "
				      + "ValorProdEnt, FornecedorIdEnt, ProdutoIdEnt )" 
   				      + "VALUES " 
				      + "(?, ?, ?, ?, ?, ?, ?, ?)",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setInt(1, obj.getNnfEnt());
			st.setDate(2, new java.sql.Date(obj.getDataEnt().getTime()));
			st.setString(3, obj.getForn().getRazaoSocial());
			st.setString(4, obj.getProd().getNomeProd());
 			st.setDouble(5, obj.getQuantidadeProdEnt());
			st.setDouble(6, obj.getValorProdEnt());
			st.setInt(7,  obj.getForn().getCodigo());
			st.setInt(8, obj.getProd().getCodigoProd());
 
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroEnt(codigo);
//					System.out.println("Novo inserido: " + classe + " " + obj.getNumeroEnt());
				}
				else
				{	throw new DbException("Erro!!! sem inclusão" + classe);
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
	public void update(Entrada obj) {
		PreparedStatement st = null;
  		try {
  			conn.setAutoCommit(false);
			st = conn.prepareStatement(
					"UPDATE entrada " +  
							"SET NnfEnt = ?, "
							+ "DataEnt = ?, "
							+ "NomeFornEnt = ?, "
							+ "NomeProdEnt = ?, "
							+ "QuantidadeProdEnt = ?, "
							+ "ValorProdEnt = ?, "
 							+ "FornecedorIdEnt = ?, "
 							+ "ProdutoIdEnt = ? "
 					+ "WHERE (NumeroEnt = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

			st.setInt(1,  obj.getNnfEnt());
			st.setDate(2, new java.sql.Date(obj.getDataEnt().getTime()));
			st.setString(3,  obj.getForn().getRazaoSocial());
			st.setString(4, obj.getProd().getNomeProd());
 			st.setDouble(5, obj.getQuantidadeProdEnt());
			st.setDouble(6, obj.getValorProdEnt());
			st.setInt(7,  obj.getForn().getCodigo());
			st.setInt(8, obj.getProd().getCodigoProd());
			st.setInt(9, obj.getNumeroEnt()); 
 			st.executeUpdate();
 			conn.commit();
   		} 
 		catch (SQLException e) {
 				throw new DbException ( "Erro!!! sem atualização " +  classe + " " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM entrada WHERE NumeroEnt = ?", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigo);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! naõ excluído " +  classe + " " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public List<Entrada> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try 
		{	st = conn.prepareStatement( 
					 "SELECT *, fornecedor.Codigo, produto.CodigoProd "
						 + "FROM entrada " 
					 		+ "INNER JOIN fornecedor " 
					 			+ "on entrada.FornecedorIdEnt = fornecedor.Codigo " 
					 		+ "INNER JOIN produto " 
								+ "on entrada.ProdutoIdEnt = produto.CodigoProd "
						+ "ORDER BY - DataEnt");
  
			rs = st.executeQuery();
			
			List<Entrada> list = new ArrayList<>();
			Produto prod = new Produto();
			Map<Integer, Fornecedor> mapForn = new HashMap<>();
  			Map<Integer, Produto> mapProd = new HashMap<>();
   			
			while (rs.next()) {
  				Fornecedor forn = mapForn.get(rs.getInt("FornecedorIdEnt"));
  				if (forn == null)
  				{	forn = instantiateFornecedor(rs);
  					mapForn.put(rs.getInt("FornecedorIdEnt"), forn);  				
  				}
 				prod =	mapProd.get(rs.getInt("ProdutoIdEnt")); 
 				if (prod == null) {	
 					prod = instantiateProduto(rs);
 					mapProd.put(rs.getInt("ProdutoIdEnt"), prod);
 				}	
  			    Entrada obj = instantiateEntrada(rs, prod, forn);
  			    list.add(obj);
  			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException( classe + " " + e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	private Entrada instantiateEntrada(ResultSet rs, Produto prod, Fornecedor forn) throws SQLException {
 		Entrada ent = new Entrada();
 		ent.setNumeroEnt(rs.getInt("NumeroEnt"));
 		ent.setNnfEnt(rs.getInt("NnfEnt"));
  		ent.setDataEnt(new java.util.Date(rs.getTimestamp("DataEnt").getTime()));
 		ent.setNomeFornEnt(rs.getString("NomeFornEnt"));
 		ent.setNomeProdEnt(rs.getString("NomeProdEnt"));
  		ent.setQuantidadeProdEnt(rs.getDouble("QuantidadeProdEnt"));
 		ent.setValorProdEnt(rs.getDouble("ValorProdEnt"));
 		ent.setProd(prod);
 		ent.setForn(forn);
        return ent;
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
 		forn.setObservacao(rs.getString("Observacao"));
        return forn;
	}
	
 	private Produto instantiateProduto(ResultSet rs) throws SQLException {
 		Produto produto = new Produto();
   		produto.setCodigoProd(rs.getInt("CodigoProd"));
   		produto.setGrupoProd(rs.getInt("GrupoProd"));
  		produto.setNomeProd(rs.getString("NomeProd"));
  		produto.setEntradaProd(rs.getDouble("EntradaProd"));
  		produto.setSaidaProd(rs.getDouble("SaidaProd"));
  		produto.setPrecoProd(rs.getDouble("PrecoProd"));
  		produto.setVendaProd(rs.getDouble("VendaProd"));
  		produto.setEstMinProd(rs.getDouble("EstMinProd"));
      	return produto;
	}

	@Override
	public Entrada findById(Integer codigo) {
		return null;
	}
}
