package gui.sgcpmodel.entites.consulta;

import java.io.Serializable;
import java.util.Date;

import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.entites.TipoConsumo;

public class ParPeriodo implements Serializable {

 	private static final long serialVersionUID = 1L;

 	private Integer idPeriodo;
 	private Date dtiPeriodo;
 	private Date dtfPeriodo;
 	
 	private Fornecedor fornecedor;
 	private TipoConsumo tipoConsumo;
 	
 	public ParPeriodo() {
 	}

	public ParPeriodo(Integer IdPeriodo, Date dtiPeriodo, Date dtfPeriodo, Fornecedor fornecedor, TipoConsumo tipoConsumo) {
 		this.idPeriodo = idPeriodo;
 		this.dtiPeriodo = dtiPeriodo;
		this.dtfPeriodo = dtfPeriodo;
		this.fornecedor = fornecedor;
		this.tipoConsumo = tipoConsumo;
	}
 	
	public Integer getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public Date getDtiPeriodo() {
		return dtiPeriodo;
	}

	public void setDtiPeriodo(Date dtiPeriodo) {
		this.dtiPeriodo = dtiPeriodo;
	}

	public Date getDtfPeriodo() {
		return dtfPeriodo;
	}

	public void setDtfPeriodo(Date dtfPeriodo) {
		this.dtfPeriodo = dtfPeriodo;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public TipoConsumo getTipoConsumo() {
		return tipoConsumo;
	}

	public void setTipoConsumo(TipoConsumo tipoConsumo) {
		this.tipoConsumo = tipoConsumo;
	}

	@Override
	public String toString() {
		return "ParPeriodo [idPeriodo = " + idPeriodo + ", dtiPeriodo = " + dtiPeriodo + ", dtfPeriodo = " + dtfPeriodo
				+ ", fornecedor = " + fornecedor + ", tipoConsumo = " + tipoConsumo + "]";
	}
	
 }