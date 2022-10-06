package gui.sgbmodel.entities;

import java.io.Serializable;

public class CartelaVirtualImprime implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer codigoVirImp;
	private String localVirImp;
	private String nomeProdVirImp;
	private Double quantidadeProdVirImp;
	private Double vendaProdVirImp;
	private Double totalProdVirImp;

	public CartelaVirtualImprime () {
	}

	public CartelaVirtualImprime(Integer codigoVirImp, String localVirImp, String nomeProdVirImp,
			Double quantidadeProdVirImp, Double vendaProdVirImp, Double totalProdVirImp) {
		this.codigoVirImp = codigoVirImp;
		this.localVirImp = localVirImp;
		this.nomeProdVirImp = nomeProdVirImp;
		this.quantidadeProdVirImp = quantidadeProdVirImp;
		this.vendaProdVirImp = vendaProdVirImp;
		this.totalProdVirImp = totalProdVirImp;
	}

	public Integer getCodigoVirImp() {
		return codigoVirImp;
	}

	public void setCodigoVirImp(Integer codigoVirImp) {
		this.codigoVirImp = codigoVirImp;
	}

	public String getLocalVirImp() {
		return localVirImp;
	}

	public void setLocalVirImp(String localVirImp) {
		this.localVirImp = localVirImp;
	}

	public String getNomeProdVirImp() {
		return nomeProdVirImp;
	}

	public void setNomeProdVirImp(String nomeProdVirImp) {
		this.nomeProdVirImp = nomeProdVirImp;
	}

	public Double getQuantidadeProdVirImp() {
		return quantidadeProdVirImp;
	}

	public void setQuantidadeProdVirImp(Double quantidadeProdVirImp) {
		this.quantidadeProdVirImp = quantidadeProdVirImp;
	}

	public Double getVendaProdVirImp() {
		return vendaProdVirImp;
	}

	public void setVendaProdVirImp(Double vendaProdVirImp) {
		this.vendaProdVirImp = vendaProdVirImp;
	}

	public Double getTotalProdVirImp() {
		return totalProdVirImp;
	}

	public void setTotalProdVirImp(Double totalProdVirImp) {
		this.totalProdVirImp = totalProdVirImp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoVirImp == null) ? 0 : codigoVirImp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartelaVirtualImprime other = (CartelaVirtualImprime) obj;
		if (codigoVirImp == null) {
			if (other.codigoVirImp != null)
				return false;
		} else if (!codigoVirImp.equals(other.codigoVirImp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CartelaVirtualImprime [codigoVirImp=" + codigoVirImp + ", localVirImp=" + localVirImp
				+ ", nomeProdVirImp=" + nomeProdVirImp + ", quantidadeProdVirImp=" + quantidadeProdVirImp
				+ ", vendaProdVirImp=" + vendaProdVirImp + ", totalProdVirImp=" + totalProdVirImp + "]";
	}	
}
