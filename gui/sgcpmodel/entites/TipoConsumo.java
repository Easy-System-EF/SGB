package gui.sgcpmodel.entites;

import java.io.Serializable;

public class TipoConsumo implements Serializable {

 	private static final long serialVersionUID = 1L;

 	private Integer codigoTipo;
	private String nomeTipo;
	
	public TipoConsumo() {
	}

	public TipoConsumo(Integer codigoTipo, String nomeTipo) {
 		this.codigoTipo = codigoTipo;
		this.nomeTipo = nomeTipo;
	}

	public Integer getCodigoTipo() {
		return codigoTipo;
	}

	public void setCodigoTipo(Integer codigoTipo) {
		this.codigoTipo = codigoTipo;
	}

	public String getNomeTipo() {
		return nomeTipo;
	}

	public void setNomeTipo(String nomeTipo) {
		this.nomeTipo = nomeTipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoTipo == null) ? 0 : codigoTipo.hashCode());
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
		TipoConsumo other = (TipoConsumo) obj;
		if (codigoTipo == null) {
			if (other.codigoTipo != null)
				return false;
		} else if (!codigoTipo.equals(other.codigoTipo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TipoFornecedor [codigoTipo = " + codigoTipo + ", nomeTipo = " + nomeTipo + "]";
	}
}
