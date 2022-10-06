package gui.sgbmodel.entities;

public class Anos {

	private Integer numeroAnos;
	private Integer anoAnos;
	private String anoStrAnos;
	
	public Anos() {
	}

	public Anos(Integer numeroAnos, Integer anoAnos, String anoStrAnos) {
		this.numeroAnos = numeroAnos;
		this.anoAnos = anoAnos;
	}

	public void setNumeroAnos(Integer numeroAnos) {
		this.numeroAnos = numeroAnos;
	}

	public Integer getNumeroAnos() {
		return numeroAnos;
	}

	public Integer getAnoAnos() {
		return anoAnos;
	}

	public void setAnoAnos(Integer anoAnos) {
		this.anoAnos = anoAnos;
	}

	public String getAnoStrAnos() {
		return anoStrAnos;
	}

	public void setAnoStrAnos(String anoStrAnos) {
		this.anoStrAnos = anoStrAnos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroAnos == null) ? 0 : numeroAnos.hashCode());
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
		Anos other = (Anos) obj;
		if (numeroAnos == null) {
			if (other.numeroAnos != null)
				return false;
		} else if (!numeroAnos.equals(other.numeroAnos))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Anos [numeroAnos = " + numeroAnos + ", anoAnos = " + anoAnos + ", anoStrAnos = " + anoStrAnos + "]";
	}


}
