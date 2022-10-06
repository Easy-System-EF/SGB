package gui.sgcpmodel.service;

import java.util.List;

import gui.sgcpmodel.dao.DaoFactory;
import gui.sgcpmodel.dao.ParcelaDao;
import gui.sgcpmodel.entites.Parcela;

public class ParcelaService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ParcelaDao dao = DaoFactory.createParcelaDao();

//    criar no parcelalist uma dependencia na parc controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Parcela> findAllAberto() {
   		return dao.findAllAberto();
	} 
	
	public List<Parcela> findAllPago() {
   		return dao.findAllPago();
	} 
	
	public List<Parcela> findPeriodoAberto() {
   		return dao.findPeriodoAberto();
	} 

	public List<Parcela> findPeriodoPago() {
   		return dao.findPeriodoPago();
	} 

	public List<Parcela> findByIdFornecedorAberto(Integer cod) {
   		return dao.findByIdFornecedorAberto(cod);
	} 

	public List<Parcela> findByIdFornecedorPago(Integer cod) {
   		return dao.findByIdFornecedorPago(cod);
	} 
	
	public List<Parcela> findByIdTipoAberto(Integer cod) {
   		return dao.findByIdTipoAberto(cod);
	} 

	public List<Parcela> findByIdTipoPago(Integer cod) {
   		return dao.findByIdTipoPago(cod);
	} 
	
 // * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void insert(Parcela obj) {
		dao.insert(obj);
 	}

	public void update(Parcela obj) {
		dao.update(obj);
 	}

// removendo
	public void removeNnf(Parcela obj) {
			dao.deleteByNnf(obj.getNnfPar(), obj.getCodigoFornecedorPar());
		
	}
	
	public void removeNumPar(Parcela obj) {
			dao.deleteByNumPar(obj.getNomeFornecedorPar(), obj.getNnfPar(), obj.getNumeroPar());
	}
}