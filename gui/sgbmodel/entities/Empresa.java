package gui.sgbmodel.entities;

import java.io.Serializable;

public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroEmp;
	private String nomeEmp;
	private String enderecoEmp;
	private String telefoneEmp;
	private String emailEmp;
	private String pixEmp;
	
	public Empresa() {
	}

	public Empresa(Integer numeroEmp, String nomeEmp, String enderecoEmp, String telefoneEmp, String emailEmp,
			String pixEmp) {
		this.numeroEmp = numeroEmp;
		this.nomeEmp = nomeEmp;
		this.enderecoEmp = enderecoEmp;
		this.telefoneEmp = telefoneEmp;
		this.emailEmp = emailEmp;
		this.pixEmp = pixEmp;
	}

	public Integer getNumeroEmp() {
		return numeroEmp;
	}

	public void setNumeroEmp(Integer numeroEmp) {
		this.numeroEmp = numeroEmp;
	}

	public String getNomeEmp() {
		return nomeEmp;
	}

	public void setNomeEmp(String nomeEmp) {
		this.nomeEmp = nomeEmp;
	}

	public String getEnderecoEmp() {
		return enderecoEmp;
	}

	public void setEnderecoEmp(String enderecoEmp) {
		this.enderecoEmp = enderecoEmp;
	}

	public String getTelefoneEmp() {
		return telefoneEmp;
	}

	public void setTelefoneEmp(String telefoneEmp) {
		this.telefoneEmp = telefoneEmp;
	}

	public String getEmailEmp() {
		return emailEmp;
	}

	public void setEmailEmp(String emailEmp) {
		this.emailEmp = emailEmp;
	}

	public String getPixEmp() {
		return pixEmp;
	}

	public void setPixEmp(String pixEmp) {
		this.pixEmp = pixEmp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroEmp == null) ? 0 : numeroEmp.hashCode());
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
		Empresa other = (Empresa) obj;
		if (numeroEmp == null) {
			if (other.numeroEmp != null)
				return false;
		} else if (!numeroEmp.equals(other.numeroEmp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Empend [numeroEmp= " + numeroEmp + ", nomeEmp= " + nomeEmp + ", enderecoEmp= " + enderecoEmp
				+ ", telefoneEmp= " + telefoneEmp + ", emailEmp= " + emailEmp + ", pixEmp= " + pixEmp + "]";
	}	
}
