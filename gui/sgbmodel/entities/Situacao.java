package gui.sgbmodel.entities;

import java.io.Serializable;

public class Situacao implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroSit;
	private String nomeSit;
	
	public Situacao() {
	}

	public Situacao(Integer numeroSit, String nomeSit) {
		this.numeroSit = numeroSit;
		this.nomeSit = nomeSit;
	}

	public Integer getNumeroSit() {
		return numeroSit;
	}

	public void setNumeroSit(Integer numeroSit) {
		this.numeroSit = numeroSit;
	}

	public String getNomeSit() {
		return nomeSit;
	}

	public void setNomeSit(String nomeSit) {
		this.nomeSit = nomeSit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroSit == null) ? 0 : numeroSit.hashCode());
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
		Situacao other = (Situacao) obj;
		if (numeroSit == null) {
			if (other.numeroSit != null)
				return false;
		} else if (!numeroSit.equals(other.numeroSit))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Situacao [numeroSit = " + numeroSit + ", nomeSit = " + nomeSit + "]";
	}
	
	
}
