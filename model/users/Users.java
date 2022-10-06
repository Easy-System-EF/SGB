package model.users;

import java.io.Serializable;
import java.util.Date;

public class Users implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroUser;
	private String nomeUser;
	private String enderecoUser;
	private Integer numeroEnderecoUser;
	private String complementoUser;
	private String bairroUser;
	private String cidadeUser;
	private String ufUser;
	private String cepUser;
	private String cpfUser;
	private String cnpjUser;
	private String contatoUser;
	private String loginUser;
	private String senhaUser;
	private Date dataCadastroUser;
	private Date dataVencimentoUser;
	private Date dataAcessoUser;

	public Users() {
	}

	public Users(Integer numeroUser, String nomeUser, String enderecoUser, Integer numeroEnderecoUser,
			String complementoUser, String bairroUser, String cidadeUser, String ufUser, String cepUser, String cpfUser,
			String cnpjUser, String contatoUser, String loginUser, String senhaUser, Date dataCadastroUser,
			Date dataVencimentoUser, Date dataAcessoUser) {
		this.numeroUser = numeroUser;
		this.nomeUser = nomeUser;
		this.enderecoUser = enderecoUser;
		this.numeroEnderecoUser = numeroEnderecoUser;
		this.complementoUser = complementoUser;
		this.bairroUser = bairroUser;
		this.cidadeUser = cidadeUser;
		this.ufUser = ufUser;
		this.cepUser = cepUser;
		this.cpfUser = cpfUser;
		this.cnpjUser = cnpjUser;
		this.contatoUser = contatoUser;
		this.loginUser = loginUser;
		this.senhaUser = senhaUser;
		this.dataCadastroUser = dataCadastroUser;
		this.dataVencimentoUser = dataVencimentoUser;
		this.dataAcessoUser = dataAcessoUser;
	}

	public Integer getNumeroUser() {
		return numeroUser;
	}

	public void setNumeroUser(Integer numeroUser) {
		this.numeroUser = numeroUser;
	}

	public String getNomeUser() {
		return nomeUser;
	}

	public void setNomeUser(String nomeUser) {
		this.nomeUser = nomeUser;
	}

	public String getEnderecoUser() {
		return enderecoUser;
	}

	public void setEnderecoUser(String enderecoUser) {
		this.enderecoUser = enderecoUser;
	}

	public Integer getNumeroEnderecoUser() {
		return numeroEnderecoUser;
	}

	public void setNumeroEnderecoUser(Integer numeroEnderecoUser) {
		this.numeroEnderecoUser = numeroEnderecoUser;
	}

	public String getComplementoUser() {
		return complementoUser;
	}

	public void setComplementoUser(String complementoUser) {
		this.complementoUser = complementoUser;
	}

	public String getBairroUser() {
		return bairroUser;
	}

	public void setBairroUser(String bairroUser) {
		this.bairroUser = bairroUser;
	}

	public String getCidadeUser() {
		return cidadeUser;
	}

	public void setCidadeUser(String cidadeUser) {
		this.cidadeUser = cidadeUser;
	}

	public String getUfUser() {
		return ufUser;
	}

	public void setUfUser(String ufUser) {
		this.ufUser = ufUser;
	}

	public String getCepUser() {
		return cepUser;
	}

	public void setCepUser(String cepUser) {
		this.cepUser = cepUser;
	}

	public String getCpfUser() {
		return cpfUser;
	}

	public void setCpfUser(String cpfUser) {
		this.cpfUser = cpfUser;
	}

	public String getCnpjUser() {
		return cnpjUser;
	}

	public void setCnpjUser(String cnpjUser) {
		this.cnpjUser = cnpjUser;
	}

	public String getContatoUser() {
		return contatoUser;
	}

	public void setContatoUser(String contatoUser) {
		this.contatoUser = contatoUser;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public String getSenhaUser() {
		return senhaUser;
	}

	public void setSenhaUser(String senhaUser) {
		this.senhaUser = senhaUser;
	}

	public Date getDataCadastroUser() {
		return dataCadastroUser;
	}

	public void setDataCadastroUser(Date dataCadastroUser) {
		this.dataCadastroUser = dataCadastroUser;
	}

	public Date getDataVencimentoUser() {
		return dataVencimentoUser;
	}

	public void setDataVencimentoUser(Date dataVencimentoUser) {
		this.dataVencimentoUser = dataVencimentoUser;
	}

	public Date getDataAcessoUser() {
		return dataAcessoUser;
	}

	public void setDataAcessoUser(Date dataAcessoUser) {
		this.dataAcessoUser = dataAcessoUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroUser == null) ? 0 : numeroUser.hashCode());
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
		Users other = (Users) obj;
		if (numeroUser == null) {
			if (other.numeroUser != null)
				return false;
		} else if (!numeroUser.equals(other.numeroUser))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Users [numeroUser = " + numeroUser + ", nomeUser = " + nomeUser + ", enderecoUser = " + enderecoUser
				+ ", numeroEnderecoUser = " + numeroEnderecoUser + ", complementoUser = " + complementoUser
				+ ", bairroUser = " + bairroUser + ", cidadeUser = " + cidadeUser + ", ufUser = " + ufUser + ", cepUser = "
				+ cepUser + ", cpfUser = " + cpfUser + ", cnpjUser = " + cnpjUser + ", contatoUser = " + contatoUser
				+ ", loginUser = " + loginUser + ", senhaUser = " + senhaUser + ", dataCadastroUser = " + dataCadastroUser
				+ ", dataVencimentoUser = " + dataVencimentoUser + ", dataAcessoUser = " + dataAcessoUser + "]";
	}
}
