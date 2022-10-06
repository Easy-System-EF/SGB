package gui.sgbmodel.entities;

import java.io.Serializable;

public class Meses implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroMes;
	private String nomeMes;
	
	public Meses () {
	}

	public Meses(Integer numeroMes, String nomeMes) {
		this.numeroMes = numeroMes;
		this.nomeMes = nomeMes;
	}

	public Integer getNumeroMes() {
		return numeroMes;
	}

	public void setNumeroMes(Integer numeroMes) {
		this.numeroMes = numeroMes;
	}

	public String getNomeMes() {
		return nomeMes;
	}

	public void setNomeMes(String nomeMes) {
		this.nomeMes = nomeMes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroMes == null) ? 0 : numeroMes.hashCode());
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
		Meses other = (Meses) obj;
		if (numeroMes == null) {
			if (other.numeroMes != null)
				return false;
		} else if (!numeroMes.equals(other.numeroMes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Meses [numeroMes = " + numeroMes + ", nomeMes = " + nomeMes + "]";
	}
}
