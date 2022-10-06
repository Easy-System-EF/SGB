package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.Receber;

public interface ReceberDao {

	void insert(Receber obj);
 	void removeCartela(Integer idCar);
  	List<Receber> findAllAberto();
  	List<Receber> findAllPago();
  	List<Receber> findPeriodoAberto();
  	List<Receber> findPeriodoPago();
 }
