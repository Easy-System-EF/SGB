package gui.sgbmodel.dao;

import java.util.Date;
import java.util.List;

import gui.sgbmodel.entities.CartelaPagante;

public interface CartelaPaganteDao {

	void insert(CartelaPagante obj);
	void deleteById(Integer numero);
 	List<CartelaPagante> findBySituacao(String local, String situacao);
 	List<CartelaPagante> findByCartela(Integer idCar);
 	List<CartelaPagante> findByMesAno(int mm, int aa, String str);
 	CartelaPagante findById(Integer codigo);
}
