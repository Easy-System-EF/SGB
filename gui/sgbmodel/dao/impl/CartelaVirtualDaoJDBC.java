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
import gui.sgbmodel.dao.CartelaVirtualDao;
import gui.sgbmodel.entities.CartelaVirtual;
import gui.sgbmodel.entities.Funcionario;
import gui.sgbmodel.entities.Produto;

public class CartelaVirtualDaoJDBC implements CartelaVirtualDao {

// tb entra construtor p/ conexão
	private Connection conn;

	public CartelaVirtualDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	String classe = "CartelaVirtual ";

	@Override
	public void insert(CartelaVirtual obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO CartelaVirtual "
					+ "(localVir, situacaoVir, nomeFuncVir, nomeProdVir, quantidadeProdVir, "
					+ "precoProdVir, vendaProdVir, totalProdVir, origemIdCarVir, FuncionarioIdVir, "
					+ "ProdutoIdVir )" 
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )", 
						Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getLocalVir());
			st.setString(2, obj.getSituacaoVir());
			st.setString(3, obj.getFuncionario().getNomeFun());
			st.setString(4, obj.getProduto().getNomeProd());
			st.setDouble(5, obj.getQuantidadeProdVir());
			st.setDouble(6, obj.getPrecoProdVir());
			st.setDouble(7, obj.getVendaProdVir());
			st.setDouble(8, obj.getTotalProdVir());
			st.setInt(9, obj.getOrigemIdCarVir());
			st.setInt(10, obj.getFuncionario().getCodigoFun());
			st.setInt(11, obj.getProduto().getCodigoProd());

			int rowsaffectad = st.executeUpdate();

			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setNumeroVir(codigo);
					;
//					System.out.println("New key inserted: " + obj.getCodigoCartelaVirtual());
				} else {
					throw new DbException(classe + "Erro!!! sem inclusão");
				}
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(CartelaVirtual obj) {
		PreparedStatement st = null;
		try {
 			st = conn.prepareStatement(
					"UPDATE CartelaVirtual " 
							+ "SET localVir = ?, situacaoVir = ?, nomeFuncVir = ?, nomeProdVir = ?, " 
							+ "quantidadeProdVir = ?, precoProdVir = ?, vendaProdVir = ?, "
							+ "totalProdVir = ?, origemIdCarVir = ?, FuncionarioIdVir = ?, "
							+ "ProdutoIdVir = ? " 
							+ "WHERE (NumeroVir = ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getLocalVir());
			st.setString(2, obj.getSituacaoVir());
			st.setString(3, obj.getFuncionario().getNomeFun());
			st.setString(4, obj.getProduto().getNomeProd());
			st.setDouble(5, obj.getQuantidadeProdVir());
			st.setDouble(6, obj.getPrecoProdVir());
			st.setDouble(7, obj.getVendaProdVir());
			st.setDouble(8, obj.getTotalProdVir());
			st.setInt(9, obj.getOrigemIdCarVir());
			st.setInt(10, obj.getFuncionario().getCodigoFun());
			st.setInt(11, obj.getProduto().getCodigoProd());
			st.setInt(12, obj.getNumeroVir());

			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException("Erro!!! sem atualização " + classe + " " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer idCar) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM CartelaVirtual WHERE NumeroVir = ? ",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, idCar);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(classe + "Erro!!! naõ excluído " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public List<CartelaVirtual> findCartela(Integer idCar) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT CartelaVirtual.*, funcionario.*, produto.* " + 
							"FROM CartelaVirtual "
							+ "INNER JOIN Funcionario " 
								+ "ON cartelaVirtual.FuncionarioIdVir = funcionario.codigoFun "
							+ "INNER JOIN Produto " 
								+ "ON cartelaVirtual.ProdutoIdVir = produto.codigoProd "
							+ "WHERE OrigemIdCarVir = ? " 
								+ "ORDER BY NumeroVir ");

			st.setInt(1, idCar);
			rs = st.executeQuery();

			List<CartelaVirtual> listVir = new ArrayList<>();
			Funcionario fun = new Funcionario();
			Produto prod = new Produto();
			CartelaVirtual vir = new CartelaVirtual();
			Map<Integer, Produto> mapProd = new HashMap<Integer, Produto>();
			Map<Integer, Funcionario> mapFunc = new HashMap<Integer, Funcionario>();

			while (rs.next()) {
				fun = mapFunc.get(rs.getInt("FuncionarioIdVir"));
				if (fun == null) {
					fun = instantiateFuncionario(rs);
					mapFunc.put(rs.getInt("FuncionarioIdVir"), fun);
				}
				prod = mapProd.get(rs.getInt("ProdutoIdVir"));
				if (prod == null) {
					prod = instantiateProduto(rs);
					mapProd.put(rs.getInt("ProdutoIdVir"), prod);
				}
				vir = instantiateVirtual(fun, prod, rs);
				listVir.add(vir);
			}
			return listVir;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<CartelaVirtual> findSituacao(String local, String situacao) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT CartelaVirtual.*, funcionario.*, produto.* " + 
							"FROM CartelaVirtual "
							+ "INNER JOIN Funcionario " 
								+ "ON cartelaVirtual.FuncionarioIdVir = funcionario.codigoFun "
							+ "INNER JOIN Produto " 
								+ "ON cartelaVirtual.ProdutoIdVir = produto.codigoProd "
							+ "WHERE LocalVir = ? AND SituacaoVir = ? " 
								+ "ORDER BY NumeroVir ");

			st.setString(1, local);
			st.setString(2, situacao);
			rs = st.executeQuery();

			List<CartelaVirtual> listVir = new ArrayList<>();
			Funcionario fun = new Funcionario();
			Produto prod = new Produto();
			CartelaVirtual vir = new CartelaVirtual();
			Map<Integer, Produto> mapProd = new HashMap<Integer, Produto>();
			Map<Integer, Funcionario> mapFunc = new HashMap<Integer, Funcionario>();

			while (rs.next()) {
				fun = mapFunc.get(rs.getInt("FuncionarioIdVir"));
				if (fun == null) {
					fun = instantiateFuncionario(rs);
					mapFunc.put(rs.getInt("FuncionarioIdVir"), fun);
				}
				prod = mapProd.get(rs.getInt("ProdutoIdVir"));
				if (prod == null) {
					prod = instantiateProduto(rs);
					mapProd.put(rs.getInt("ProdutoIdVir"), prod);
				}
				vir = instantiateVirtual(fun, prod, rs);
				listVir.add(vir);
			}
			return listVir;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<CartelaVirtual> findPesquisaProd(String str) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, funcionario.*, produto.* " + 
						"FROM CartelaVirtual " + 
							"INNER JOIN funcionario " +
								"ON cartelaVirtual.FuncionarioIdVir = funcionario.CodigoFun " + 
							"INNER JOIN produto " +
								"ON cartelaVirtual.ProdutoIdVir = produto.CodigoProd " + 
						"WHERE NomeProdVir like ? " +
							"ORDER BY NomeProdVir");

			st.setString(1, str);
			rs = st.executeQuery();

			List<CartelaVirtual> listVir = new ArrayList<>();
			Produto prod = new Produto();
			Funcionario fun = new Funcionario();
			CartelaVirtual vir = new CartelaVirtual();
			Map<Integer, Produto> mapProd = new HashMap<Integer, Produto>();
			Map<Integer, Funcionario> mapFunc = new HashMap<Integer, Funcionario>();

			while (rs.next()) {
				if (fun == null) {
					fun = instantiateFuncionario(rs);
					mapFunc.put(rs.getInt("FuncionarioIdVir"), fun);
				}
				prod = mapProd.get(rs.getInt("ProdutoIdVir"));
				if (prod == null) {
					prod = instantiateProduto(rs);
					mapProd.put(rs.getInt("ProdutoIdVir"), prod);
				}
				vir = instantiateVirtual(fun, prod, rs);
				listVir.add(vir);
			}
			return listVir;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public CartelaVirtual findByProduto(String str) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, funcionario.*, produto.* " + 
						"FROM CartelaVirtual " + 
							"INNER JOIN funcionario " +
								"ON cartelaVirtual.FuncionarioIdVir = funcionario.CodigoFun " + 
							"INNER JOIN produto " +
								"ON cartelaVirtual.ProdutoIdVir = produto.CodigoProd " + 
						"WHERE NomeProdVir = ? " +
							"ORDER BY NomeProdVir");

			st.setString(1, str);
			rs = st.executeQuery();

			List<CartelaVirtual> listVir = new ArrayList<>();
			Produto prod = new Produto();
			Funcionario fun = new Funcionario();
			CartelaVirtual vir = new CartelaVirtual();
			Map<Integer, Produto> mapProd = new HashMap<Integer, Produto>();
			Map<Integer, Funcionario> mapFunc = new HashMap<Integer, Funcionario>();

			while (rs.next()) {
				if (fun == null) {
					fun = instantiateFuncionario(rs);
					mapFunc.put(rs.getInt("FuncionarioIdVir"), fun);
				}
				prod = mapProd.get(rs.getInt("ProdutoIdVir"));
				if (prod == null) {
					prod = instantiateProduto(rs);
					mapProd.put(rs.getInt("ProdutoIdVir"), prod);
				}
				vir = instantiateVirtual(fun, prod, rs);
				return vir;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<CartelaVirtual> findPesquisaFunc(String str) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, funcionario.*, produto.* " + 
						"FROM CartelaVirtual " + 
							"INNER JOIN funcionario " +
								"ON cartelaVirtual.FuncionarioIdVir = funcionario.CodigoFun " + 
							"INNER JOIN produto " +
								"ON cartelaVirtual.ProdutoIdVir = produto.CodigoProd " + 
						"WHERE NomeFuncVir like ? " +
							"ORDER BY NomeProdVir");

			st.setString(1, str);
			rs = st.executeQuery();

			List<CartelaVirtual> listVir = new ArrayList<>();
			Produto prod = new Produto();
			Funcionario fun = new Funcionario();
			CartelaVirtual vir = new CartelaVirtual();
			Map<Integer, Produto> mapProd = new HashMap<Integer, Produto>();
			Map<Integer, Funcionario> mapFunc = new HashMap<Integer, Funcionario>();

			while (rs.next()) {
				if (fun == null) {
					fun = instantiateFuncionario(rs);
					mapFunc.put(rs.getInt("FuncionarioIdVir"), fun);
				}
				prod = mapProd.get(rs.getInt("ProdutoIdVir"));
				if (prod == null) {
					prod = instantiateProduto(rs);
					mapProd.put(rs.getInt("ProdutoIdVir"), prod);
				}
				vir = instantiateVirtual(fun, prod, rs);
				listVir.add(vir);
			}
			return listVir;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private CartelaVirtual instantiateVirtual(Funcionario fun, Produto prod, 
					ResultSet rs) throws SQLException {
		CartelaVirtual car = new CartelaVirtual();		car.setLocalVir(rs.getString("LocalVir"));

		car.setNumeroVir(rs.getInt("NumeroVir"));
		car.setLocalVir(rs.getString("LocalVir"));
		car.setSituacaoVir(rs.getString("SituacaoVir"));
		car.setNomeFuncVir(rs.getString("NomeFuncVir"));
		car.setNomeProdVir(rs.getString("NomeProdVir"));
		car.setQuantidadeProdVir(rs.getDouble("QuantidadeProdVir"));
		car.setPrecoProdVir(rs.getDouble("PrecoProdVir"));
		car.setVendaProdVir(rs.getDouble("VendaProdVir"));
		car.setTotalProdVir(rs.getDouble("TotalProdVir"));
		car.setOrigemIdCarVir(rs.getInt("OrigemIdCarVir"));
		car.setFuncionario(fun);
		car.setProduto(prod);
		return car;
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

	private Funcionario instantiateFuncionario(ResultSet rs) throws SQLException {
		Funcionario fun = new Funcionario();

		fun.setCodigoFun(rs.getInt("CodigoFun"));
		fun.setNomeFun(rs.getString("NomeFun"));
		fun.setEnderecoFun(rs.getString("EnderecoFun"));
		fun.setBairroFun(rs.getString("BairroFun"));
		fun.setCidadeFun(rs.getString("CidadeFun"));
		fun.setUfFun(rs.getString("UfFun"));
		fun.setCepFun(rs.getString("CepFun"));
		fun.setDddFun(rs.getInt("DddFun"));
		fun.setTelefoneFun(rs.getInt("TelefoneFun"));
		fun.setCpfFun(rs.getString("CpfFun"));
		fun.setPixFun(rs.getString("PixFun"));
		fun.setCargoFun(rs.getString("CargoFun"));
		fun.setSituacaoFun(rs.getString("SituacaoFun"));
		fun.setComissaoFun(rs.getDouble("ComissaoFun"));
		fun.setAdiantamentoFun(rs.getDouble("AdiantamentoFun"));
		return fun;
	}
}
