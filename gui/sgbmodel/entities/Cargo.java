package gui.sgbmodel.entities;

import java.io.Serializable;

public class Cargo implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer codigoCargo;
	private String nomeCargo;
	private Double salarioCargo;
	private Double comissaoCargo;
	
	public Cargo() {
	}

	public Cargo(Integer codigoCargo, String nomeCargo, Double salarioCargo, Double comissaoCargo) {
 		this.codigoCargo = codigoCargo;
		this.nomeCargo = nomeCargo;
		this.salarioCargo = salarioCargo;
		this.comissaoCargo = comissaoCargo;
	}

	public Integer getCodigoCargo() {
		return codigoCargo;
	}

	public void setCodigoCargo(Integer codigoCargo) {
		this.codigoCargo = codigoCargo;
	}

	public String getNomeCargo() {
		return nomeCargo;
	}

	public void setNomeCargo(String nomeCargo) {
		this.nomeCargo = nomeCargo;
	}

	public Double getSalarioCargo() {
		return salarioCargo;
	}

	public void setSalarioCargo(Double salarioCargo) {
		this.salarioCargo = salarioCargo;
	}

	public Double getComissaoCargo() {
		return comissaoCargo;
	}

	public void setComissaoCargo(Double comissaoCargo) {
		this.comissaoCargo = comissaoCargo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoCargo == null) ? 0 : codigoCargo.hashCode());
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
		Cargo other = (Cargo) obj;
		if (codigoCargo == null) {
			if (other.codigoCargo != null)
				return false;
		} else if (!codigoCargo.equals(other.codigoCargo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cargo [codigoCar=" + codigoCargo + ", nomeCargo=" + nomeCargo + ", salarioCargo=" + salarioCargo
				+ ", comissaoCargo=" + comissaoCargo + "]";
	}
	
}
