package gui.sgbmodel.entities;

import java.io.Serializable;

public class Grupo implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer codigoGru;
	private String nomeGru;
	
	public Grupo() {
	}

	public Grupo(Integer codigoGru, String nomeGru) {
 		this.codigoGru = codigoGru;
		this.nomeGru = nomeGru;
	}

	public Integer getCodigoGru() {
		return codigoGru;
	}

	public void setCodigoGru(Integer codigoGru) {
		this.codigoGru = codigoGru;
	}

	public String getNomeGru() {
		return nomeGru;
	}

	public void setNomeGru(String nomeGru) {
		this.nomeGru = nomeGru;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoGru == null) ? 0 : codigoGru.hashCode());
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
		Grupo other = (Grupo) obj;
		if (codigoGru == null) {
			if (other.codigoGru != null)
				return false;
		} else if (!codigoGru.equals(other.codigoGru))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Grupo [codigoGru=" + codigoGru + ", nomeGru=" + nomeGru + "]";
	}
}
