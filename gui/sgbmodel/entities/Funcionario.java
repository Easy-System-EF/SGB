package gui.sgbmodel.entities;

import java.io.Serializable;

public class Funcionario implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer codigoFun;
	private String nomeFun; 
	private String enderecoFun;
	private String bairroFun;
	private String cidadeFun;
	private String ufFun;
	private String cepFun;
	private Integer dddFun;
	private Integer telefoneFun;
	private String cpfFun;
	private String pixFun;
	private String cargoFun;
	private String situacaoFun;
	private Double comissaoFun;
	private Double adiantamentoFun;
	private Double salarioFun;
	private Integer mesFun;
	private Integer anoFun;
	
	private static Double totalMesFun;
	private static Double totalComissaoFun;
	private static Double totalAdiantamentoFun;
	
/*
 *  associando orçto (varios valores) para calculo da comissão
 *  não entra no construtor e não posso setar uma nova lista
 *  eu vou adicionar
 */

	private static Cargo cargo;
	private Situacao situacao;
	
	public Funcionario() {
	}

	public Funcionario(Integer codigoFun, String nomeFun, String enderecoFun, String bairroFun, 
			String cidadeFun, String ufFun, String cepFun, Integer dddFun, Integer telefoneFun, 
			String cpfFun, String pixFun, String cargoFun, String situacaoFun, Double comissaoFun,
			Double adiantamentoFun, Double salarioFun, Integer mesFun, Integer anoFun, 
			Cargo cargo, Situacao situacao) {
		this.codigoFun = codigoFun;
		this.nomeFun = nomeFun;
		this.enderecoFun = enderecoFun;
		this.bairroFun = bairroFun;
		this.cidadeFun = cidadeFun;
		this.ufFun = ufFun;
		this.cepFun = cepFun;
		this.dddFun = dddFun;
		this.telefoneFun = telefoneFun;
		this.cpfFun = cpfFun;
		this.pixFun = pixFun;
		this.cargoFun = cargoFun; 
		this.situacaoFun = situacaoFun;
		this.comissaoFun = comissaoFun;
		this.adiantamentoFun = adiantamentoFun;
		this.salarioFun = salarioFun;
		this.mesFun = mesFun;
		this.anoFun = anoFun;
		Funcionario.cargo = cargo;
		this.situacao = situacao;
	}

	public Integer getCodigoFun() {
		return codigoFun;
	}

	public void setCodigoFun(Integer codigoFun) {
		this.codigoFun = codigoFun;
	}

	public String getNomeFun() {
		return nomeFun;
	}

	public void setNomeFun(String nomeFun) {
		this.nomeFun = nomeFun;
	}

	public String getEnderecoFun() {
		return enderecoFun;
	}

	public void setEnderecoFun(String enderecoFun) {
		this.enderecoFun = enderecoFun;
	}

	public String getBairroFun() {
		return bairroFun;
	}

	public void setBairroFun(String bairroFun) {
		this.bairroFun = bairroFun;
	}

	public String getCidadeFun() {
		return cidadeFun;
	}

	public void setCidadeFun(String cidadeFun) {
		this.cidadeFun = cidadeFun;
	}

	public String getUfFun() {
		return ufFun;
	}

	public void setUfFun(String ufFun) {
		this.ufFun = ufFun;
	}

	public String getCepFun() {
		return cepFun;
	}

	public void setCepFun(String cepFun) {
		this.cepFun = cepFun;
	}

	public Integer getDddFun() {
		return dddFun;
	}

	public void setDddFun(Integer dddFun) {
		this.dddFun = dddFun;
	}

	public Integer getTelefoneFun() {
		return telefoneFun;
	}

	public void setTelefoneFun(Integer telefoneFun) {
		this.telefoneFun = telefoneFun;
	}

	public String getCpfFun() {
		return cpfFun;
	}

	public void setCpfFun(String cpfFun) {
		this.cpfFun = cpfFun;
	}

	public String getPixFun() {
		return pixFun;
	}

	public void setPixFun(String pixFun) {
		this.pixFun = pixFun;
	}

	public void setCargoFun(String cargoFun) {
		this.cargoFun = cargoFun;
	}

	public String getCargoFun() {
		return cargoFun;
	}

	public String getSituacaoFun() {
		return situacaoFun;
	}

	public void setSituacaoFun(String situacaoFun) {
		this.situacaoFun = situacaoFun;
	}

	public Double getComissaoFun() {
		return comissaoFun;
	}

	public void setComissaoFun(Double comissaoFun) { 
		this.comissaoFun = comissaoFun;
	}

	public Double getAdiantamentoFun() {
		return adiantamentoFun;
	}

	public void setAdiantamentoFun(Double adiantamentoFun) {
		this.adiantamentoFun = adiantamentoFun;
	}

	public Double getSalarioFun() {
		return salarioFun;
	}

	public void setSalarioFun(Double salarioFun) {
		this.salarioFun = salarioFun;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
		

	public Integer getMesFun() {
		return mesFun;
	}

	public void setMesFun(Integer mesFun) {
		this.mesFun = mesFun;
	}

	public Integer getAnoFun() {
		return anoFun;
	}

	public void setAnoFun(Integer anoFun) {
		this.anoFun = anoFun;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		Funcionario.cargo = cargo;
	}

	public Double totalMesFun() {
		return totalMesFun = (salarioFun + comissaoFun) - adiantamentoFun;
	}
	
	public void totalComissaoFun(Double com) {
		comissaoFun += com;
	}
	
	public void totalAdiantamentoFun(Double vale) {
		adiantamentoFun += vale;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoFun == null) ? 0 : codigoFun.hashCode());
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
		Funcionario other = (Funcionario) obj;
		if (codigoFun == null) {
			if (other.codigoFun != null)
				return false;
		} else if (!codigoFun.equals(other.codigoFun))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Funcionario [codigoFun=" + codigoFun + ", nomeFun=" + nomeFun + ", enderecoFun=" + enderecoFun
				+ ", bairroFun=" + bairroFun + ", cidadeFun=" + cidadeFun + ", ufFun=" + ufFun + ", cepFun=" + cepFun
				+ ", dddFun=" + dddFun + ", telefoneFun=" + telefoneFun + ", cpfFun=" + cpfFun + ", pixFun=" + pixFun
				+ ", cargoFun=" + cargoFun + ", situacaoFun=" + situacaoFun + ", comissaoFun=" + comissaoFun
				+ ", adiantamentoFun=" + adiantamentoFun + ", salarioFun=" + salarioFun + ", cargo=" + cargo
				+ ", situacao=" + situacao + "]";
	}
}
