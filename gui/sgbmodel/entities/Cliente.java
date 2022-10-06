 
package gui.sgbmodel.entities;

import java.io.Serializable;

public class Cliente implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer codigoCli;
	private String nomeCli;
	private String ruaCli;
	private Integer numeroCli;
	private String complementoCli;
	private String bairroCli;
	private String cidadeCli;
	private String ufCli;
	private String cepCli;
	private Integer ddd01Cli;
	private Integer telefone01Cli;
	private Integer ddd02Cli;
	private Integer telefone02Cli;
	private String emailCli;
 	private char pessoaCli;
	private String cpfCli;
 	private String cnpjCli;

	public Cliente( ) {
	}

	public Cliente(Integer codigoCli, String nomeCli, String ruaCli, Integer numeroCli, String complementoCli,
			String bairroCli, String cidadeCli, String ufCli, String cepCli, Integer ddd01Cli, Integer telefone01Cli,
			Integer ddd02Cli, Integer telefone02Cli, String emailCli, char pessoaCli, String cpfCli,
			String cnpjCli) {
 		this.codigoCli = codigoCli;
		this.nomeCli = nomeCli;
		this.ruaCli = ruaCli;
		this.numeroCli = numeroCli;
		this.complementoCli = complementoCli;
		this.bairroCli = bairroCli;
		this.cidadeCli = cidadeCli;
		this.ufCli = ufCli;
		this.cepCli = cepCli;
		this.ddd01Cli = ddd01Cli;
		this.telefone01Cli = telefone01Cli;
		this.ddd02Cli = ddd02Cli;
		this.telefone02Cli = telefone02Cli;
		this.emailCli = emailCli;
 		this.pessoaCli = pessoaCli;
		this.cpfCli = cpfCli;
 		this.cnpjCli = cnpjCli;
	}

	public Integer getCodigoCli() {
		return codigoCli;
	}

	public void setCodigoCli(Integer codigoCli) {
		this.codigoCli = codigoCli;
	}

	public String getNomeCli() {
		return nomeCli;
	}

	public void setNomeCli(String nomeCli) {
		this.nomeCli = nomeCli;
	}

	public String getRuaCli() {
		return ruaCli;
	}

	public void setRuaCli(String ruaCli) {
		this.ruaCli = ruaCli;
	}

	public Integer getNumeroCli() {
		return numeroCli;
	}

	public void setNumeroCli(Integer numeroCli) {
		this.numeroCli = numeroCli;
	}

	public String getComplementoCli() {
		return complementoCli;
	}

	public void setComplementoCli(String complementoCli) {
		this.complementoCli = complementoCli;
	}

	public String getBairroCli() {
		return bairroCli;
	}

	public void setBairroCli(String bairroCli) {
		this.bairroCli = bairroCli;
	}

	public String getCidadeCli() {
		return cidadeCli;
	}

	public void setCidadeCli(String cidadeCli) {
		this.cidadeCli = cidadeCli;
	}

	public String getUfCli() {
		return ufCli;
	}

	public void setUfCli(String ufCli) {
		this.ufCli = ufCli;
	}

	public String getCepCli() {
		return cepCli;
	}

	public void setCepCli(String cepCli) {
		this.cepCli = cepCli;
	}

	public Integer getDdd01Cli() {
		return ddd01Cli;
	}

	public void setDdd01Cli(Integer ddd01Cli) {
		this.ddd01Cli = ddd01Cli;
	}

	public Integer getTelefone01Cli() {
		return telefone01Cli;
	}

	public void setTelefone01Cli(Integer telefone01Cli) {
		this.telefone01Cli = telefone01Cli;
	}

	public Integer getDdd02Cli() {
		return ddd02Cli;
	}

	public void setDdd02Cli(Integer ddd02Cli) {
		this.ddd02Cli = ddd02Cli;
	}

	public Integer getTelefone02Cli() {
		return telefone02Cli;
	}

	public void setTelefone02Cli(Integer telefone02Cli) {
		this.telefone02Cli = telefone02Cli;
	}

	public String getEmailCli() {
		return emailCli;
	}

	public void setEmailCli(String emailCli) {
		this.emailCli = emailCli;
	}

 	public char getPessoaCli() {
		return pessoaCli;
	}

	public void setPessoaCli(char pessoaCli) {
		this.pessoaCli = pessoaCli;
	}

	public String getCpfCli() {
		return cpfCli;
	}

	public void setCpfCli(String cpfCli) {
		this.cpfCli = cpfCli;
	}

 	public String getCnpjCli() {
		return cnpjCli;
	}

	public void setCnpjCli(String cnpjCli) {
		this.cnpjCli = cnpjCli;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnpjCli == null) ? 0 : cnpjCli.hashCode());
		result = prime * result + ((codigoCli == null) ? 0 : codigoCli.hashCode());
		result = prime * result + ((cpfCli == null) ? 0 : cpfCli.hashCode());
		result = prime * result + pessoaCli;
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
		Cliente other = (Cliente) obj;
		if (cnpjCli == null) {
			if (other.cnpjCli != null)
				return false;
		} else if (!cnpjCli.equals(other.cnpjCli))
			return false;
		if (codigoCli == null) {
			if (other.codigoCli != null)
				return false;
		} else if (!codigoCli.equals(other.codigoCli))
			return false;
		if (cpfCli == null) {
			if (other.cpfCli != null)
				return false;
		} else if (!cpfCli.equals(other.cpfCli))
			return false;
		if (pessoaCli != other.pessoaCli)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cliente [codigoCli=" + codigoCli + ", nomeCli=" + nomeCli + ", ruaCli=" + ruaCli
				+ ", numeroCli=" + numeroCli + ", complementoCli=" + complementoCli + ", bairroCli=" + bairroCli
				+ ", cidadeCli=" + cidadeCli + ", ufCli=" + ufCli + ", cepCli=" + cepCli + ", ddd01Cli=" + ddd01Cli
				+ ", telefone01Cli=" + telefone01Cli + ", ddd02Cli=" + ddd02Cli + ", telefone02Cli=" + telefone02Cli
				+ ", emailCli=" + emailCli + ", pessoaCli=" + pessoaCli + ", cpfCli=" + cpfCli
				+ ", cnpjCli=" + cnpjCli + "]";
	}
   }
