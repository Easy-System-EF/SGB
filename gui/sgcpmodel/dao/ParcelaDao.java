package gui.sgcpmodel.dao;

import java.util.List;

import gui.sgcpmodel.entites.Parcela;

public interface ParcelaDao {

	void insert(Parcela obj);
	void update(Parcela obj);
 	void deleteByNnf(Integer nnfPar, Integer forn);
 	void deleteByNumPar(String nome, Integer forn, Integer num);
  	List<Parcela> findAllAberto();
  	List<Parcela> findAllPago();
  	List<Parcela> findPeriodoAberto();
  	List<Parcela> findPeriodoPago();
   	List<Parcela> findByIdFornecedorAberto(Integer cod);
   	List<Parcela> findByIdFornecedorPago(Integer cod);
  	List<Parcela> findByIdTipoAberto(Integer cod);
  	List<Parcela> findByIdTipoPago(Integer cod);
 }
