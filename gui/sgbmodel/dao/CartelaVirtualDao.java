package gui.sgbmodel.dao;

import java.util.List;

import gui.sgbmodel.entities.CartelaVirtual;

public interface CartelaVirtualDao {

	void insert(CartelaVirtual obj);
	void update(CartelaVirtual obj);
	void deleteById(Integer numero);
 	List<CartelaVirtual> findCartela(Integer idCar);
 	List<CartelaVirtual> findSituacao(String local, String situacao);
 	List<CartelaVirtual> findPesquisaFunc(String str);
 	List<CartelaVirtual> findPesquisaProd(String str);
 	CartelaVirtual findByProduto(String str);
}
