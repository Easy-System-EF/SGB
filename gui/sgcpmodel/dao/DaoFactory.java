package gui.sgcpmodel.dao;

import db.DB;
import gui.sgcpmodel.dao.impl.CompromissoDaoJDBC;
import gui.sgcpmodel.dao.impl.FornecedorDaoJDBC;
import gui.sgcpmodel.dao.impl.ParPeriodoDaoJDBC;
import gui.sgcpmodel.dao.impl.ParcelaDaoJDBC;
import gui.sgcpmodel.dao.impl.TipoConsumidorDaoJDBC;

public class DaoFactory {

/*
 * é obrigatório passar uma conexão como argumento (db.getCon...)
 */
	public static CompromissoDao createCompromissoDao() {
		return new CompromissoDaoJDBC(DB.getConnection());
	}
	
	public static FornecedorDao createFornecedorDao() {
		return new FornecedorDaoJDBC(DB.getConnection());
	}
	
	public static TipoConsumidorDao createTipoFornecedorDao() {
		return new TipoConsumidorDaoJDBC(DB.getConnection());
	}
	
	public static ParcelaDao createParcelaDao() {
		return new ParcelaDaoJDBC(DB.getConnection());
	}
	
	public static ParPeriodoDao createConsultaDao() {
		return new ParPeriodoDaoJDBC(DB.getConnection());
	}
}
