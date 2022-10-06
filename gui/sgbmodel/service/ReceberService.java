package gui.sgbmodel.service;

import java.util.List;

import gui.sgbmodel.dao.DaoFactory;
import gui.sgbmodel.dao.ReceberDao;
import gui.sgbmodel.entities.Receber;
 
public class ReceberService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ReceberDao dao = DaoFactory.createReceberDao();

//    criar no parcelalist uma dependencia na parc controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Receber> findAllAberto() {
   		return dao.findAllAberto();
	} 
	
	public List<Receber> findAllPago() {
   		return dao.findAllPago();
	} 
	
	public List<Receber> findPeriodoAberto() {
   		return dao.findPeriodoAberto();
	} 

	public List<Receber> findPeriodoPago() {
   		return dao.findPeriodoPago();
	} 

// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void insert(Receber obj) {
		if (obj.getNumeroRec() == null) {
			dao.insert(obj);
		}
 	}

// removendo
	public void removeCartela(Receber obj) {
			dao.removeCartela(obj.getCartelaRec());
	}
}