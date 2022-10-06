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
import gui.sgbmodel.dao.FolhaMesDao;
import gui.sgbmodel.entities.Anos;
import gui.sgbmodel.entities.FolhaMes;
import gui.sgbmodel.entities.Meses;
  
public class FolhaMesDaoJDBC implements FolhaMesDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public FolhaMesDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(FolhaMes obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO FolhaMes " +
				      "(FuncionarioFolha, CargoFolha, SituacaoFolha, SalarioFolha, " +
				      "ComissaoFolha, ValeFolha, ReceberFolha, TotalFolha , " +
				      "MesesIdFolha, AnosIdFolha ) " +
  				      "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setString(1, obj.getFuncionarioFolha());
 			st.setString(2, obj.getCargoFolha());
 			st.setString(3, obj.getSituacaoFolha());
 			st.setString(4, obj.getSalarioFolha());
 			st.setString(5, obj.getComissaoFolha());
 			st.setString(6, obj.getValeFolha());
 			st.setString(7, obj.getReceberFolha());
 			st.setString(8, obj.getTotalFolha());
			st.setInt(9,  obj.getMeses().getNumeroMes());
			st.setInt(10, obj.getAnos().getNumeroAnos());
 			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroFolha(codigo);
				}
				else
				{	throw new DbException("Erro!!! sem inclusão" );
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
	public List<FolhaMes> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, meses.*, anos.* " +
						"From FolhaMes " +	
							"INNER JOIN meses " +
								"ON FolhaMes.MesesIdFolha = meses.NumeroMes " + 
							"INNER JOIN anos " +
								"ON FolhaMes.AnosIdFolha = anos.NumeroAnos " + 
					"ORDER BY FuncionarioFolha");
			
			rs = st.executeQuery();
			
			List<FolhaMes> list = new ArrayList<>();
			Map<Integer, Meses> mapMes = new HashMap();
			Map<Integer, Anos> mapAno = new HashMap();
			
			while (rs.next()) {
				Meses objMes = mapMes.get(rs.getInt("MesesIdFolha"));
				if (objMes == null) {
					objMes = instantiateMeses(rs);
					mapMes.put(rs.getInt("MesesIdFolha"), objMes);
				}	
				Anos objAno = mapAno.get(rs.getInt("AnosIdFolha"));
				if (objAno == null) {
					objAno = instantiateAnos(rs);
					mapAno.put(rs.getInt("AnosIdFolha"), objAno);
				}	
				FolhaMes obj = instantiateFolhaMes(rs, objMes, objAno);
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
	public void zeraAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"TRUNCATE TABLE sgb.FolhaMes " );

			st.executeUpdate();

		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	private FolhaMes instantiateFolhaMes(ResultSet rs, Meses mes, Anos ano) throws SQLException {
		FolhaMes Folha = new FolhaMes();
 		Folha.setNumeroFolha(rs.getInt("NumeroFolha"));
 		Folha.setFuncionarioFolha(rs.getString("FuncionarioFolha"));	
 		Folha.setCargoFolha(rs.getString("CargoFolha"));
 		Folha.setSituacaoFolha(rs.getString("SituacaoFolha"));
 		Folha.setSalarioFolha(rs.getString("SalarioFolha"));
 		Folha.setComissaoFolha(rs.getString("ComissaoFolha"));
 		Folha.setValeFolha(rs.getString("ValeFolha"));
 		Folha.setReceberFolha(rs.getString("ReceberFolha"));
 		Folha.setTotalFolha(rs.getString("TotalFolha"));
 		Folha.setMeses(mes);
 		Folha.setAnos(ano);
        return Folha;
	}

	private Meses instantiateMeses(ResultSet rs) throws SQLException {
		Meses meses = new Meses();
		meses.setNumeroMes(rs.getInt("NumeroMes"));
		meses.setNomeMes(rs.getString("NomeMes"));
		return meses;
	}

	
	private Anos instantiateAnos(ResultSet rs) throws SQLException {
		Anos anos = new Anos();
		anos.setNumeroAnos(rs.getInt("NumeroAnos"));
		anos.setAnoAnos(rs.getInt("AnoAnos"));
		return anos;
	} 	
}
