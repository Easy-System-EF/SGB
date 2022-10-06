package gui.sgbmodel.service;

import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.dao.EmpresaDao;
import gui.sgbmodel.entities.Empresa;
  
public class EmpresaService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private EmpresaDao dao = DaoFactory.createEmpresaDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public void insertOrUpdate(Empresa obj) {
		if (obj.getNumeroEmp() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	} 

	public Empresa findById(Integer cod) {
   		return dao.findById(cod);
	}
}
