package gui.sgcpmodel.entites;

import java.io.Serializable;

public class Fornecedor implements Serializable {

	private static final long serialVersionUID  =  1L;

	private Integer codigo;
	private String razaoSocial;
	private String rua;
	private Integer numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String uf;
	private Integer cep;
	private Integer ddd01;
	private Integer telefone01;
	private Integer ddd02;
	private Integer telefone02;
	private String contato;
	private Integer dddContato;
	private Integer telefoneContato;
	private String email;
	private String pix;
	private String Observacao;
 
	public Fornecedor() {
	}

	public Fornecedor(Integer codigo, String razaoSocial, String rua, Integer numero, String complemento, String bairro,
			String cidade, String uf, Integer cep, Integer ddd01, Integer telefone01, Integer ddd02, Integer telefone02,
			String contato, Integer dddContato, Integer telefoneContato, String email, String pix, String observacao)
			{
 		this.codigo  =  codigo;
		this.razaoSocial  =  razaoSocial;
		this.rua  =  rua;
		this.numero  =  numero;
		this.complemento  =  complemento;
		this.bairro  =  bairro;
		this.cidade  =  cidade;
		this.uf  =  uf;
		this.cep  =  cep;
		this.ddd01  =  ddd01;
		this.telefone01  =  telefone01;
		this.ddd02  =  ddd02;
		this.telefone02  =  telefone02;
		this.contato  =  contato;
		this.dddContato  =  dddContato;
		this.telefoneContato  =  telefoneContato;
		this.email  =  email;
		this.pix  =  pix;
		Observacao  =  observacao;
 	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo  =  codigo;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial  =  razaoSocial;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua  =  rua;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero  =  numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento  =  complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro  =  bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade  =  cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf  =  uf;
	}

	public Integer getCep() {
		return cep;
	}

	public void setCep(Integer cep) {
		this.cep  =  cep;
	}

	public Integer getDdd01() {
		return ddd01;
	}

	public void setDdd01(Integer ddd01) {
		this.ddd01  =  ddd01;
	}

	public Integer getTelefone01() {
		return telefone01;
	}

	public void setTelefone01(Integer telefone01) {
		this.telefone01  =  telefone01;
	}

	public Integer getDdd02() {
		return ddd02;
	}

	public void setDdd02(Integer ddd02) {
		this.ddd02  =  ddd02;
	}

	public Integer getTelefone02() {
		return telefone02;
	}

	public void setTelefone02(Integer telefone02) {
		this.telefone02  =  telefone02;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato  =  contato;
	}

	public Integer getDddContato() {
		return dddContato;
	}

	public void setDddContato(Integer dddContato) {
		this.dddContato  =  dddContato;
	}

	public Integer getTelefoneContato() {
		return telefoneContato;
	}

	public void setTelefoneContato(Integer telefoneContato) {
		this.telefoneContato  =  telefoneContato;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email  =  email;
	}

	public String getPix() {
		return pix;
	}

	public void setPix(String pix) {
		this.pix  =  pix;
	}

	public String getObservacao() {
		return Observacao;
	}

	public void setObservacao(String observacao) {
		Observacao  =  observacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Fornecedor other = (Fornecedor) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Fornecedor [codigo = " + codigo + ", razaoSocial = " + razaoSocial + ", rua = " + rua + ", numero = " + numero
				+ ", complemento = " + complemento + ", bairro = " + bairro + ", cidade = " + cidade + ", uf = " + uf + ", cep = "
				+ cep + ", ddd01 = " + ddd01 + ", telefone01 = " + telefone01 + ", ddd02 = " + ddd02 + ", telefone02 = "
				+ telefone02 + ", contato = " + contato + ", dddContato = " + dddContato + ", telefoneContato = "
				+ telefoneContato + ", email = " + email + ", pix = " + pix + ", Observacao = " + Observacao + "]";
	}
}