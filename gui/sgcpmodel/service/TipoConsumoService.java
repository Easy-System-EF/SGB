package gui.sgcpmodel.service;

import java.util.List;

import gui.sgcpmodel.dao.DaoFactory;
import gui.sgcpmodel.dao.TipoConsumidorDao;
import gui.sgcpmodel.entites.TipoConsumo;
 
public class TipoConsumoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private TipoConsumidorDao dao = DaoFactory.createTipoFornecedorDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<TipoConsumo> findAll() {
   		return dao.findAll();
	} 
	
	public TipoConsumo findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(TipoConsumo obj) {
		if (obj.getCodigoTipo() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(TipoConsumo obj) {
		dao.deleteById(obj.getCodigoTipo());
	}
}
