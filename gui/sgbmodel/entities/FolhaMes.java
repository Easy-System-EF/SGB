package gui.sgbmodel.entities;

public class FolhaMes {

	private Integer numeroFolha;
	private String funcionarioFolha;
	private String cargoFolha;
	private String situacaoFolha;
	private String salarioFolha;
	private String comissaoFolha;
	private String valeFolha;
	private String receberFolha;
	private String totalFolha;
	
	private Meses mes;
	private Anos ano;

	public FolhaMes() {
		
	}

	public FolhaMes(Integer numeroFolha, String funcionarioFolha, String cargoFolha, String situacaoFolha, String salarioFolha,
			String comissaoFolha, String valeFolha, String receberFolha, String totalFolha, 
			Meses mes, Anos ano) {
		this.numeroFolha = numeroFolha;
		this.funcionarioFolha = funcionarioFolha;
		this.cargoFolha = cargoFolha;
		this.situacaoFolha = situacaoFolha;
		this.salarioFolha = salarioFolha;
		this.comissaoFolha = comissaoFolha;
		this.valeFolha = valeFolha;
		this.receberFolha = receberFolha;
		this.totalFolha = totalFolha;
		this.mes = mes;
		this.ano = ano;
	}

	public Integer getNumeroFolha() {
		return numeroFolha;
	}

	public void setNumeroFolha(Integer numeroFolha) {
		this.numeroFolha = numeroFolha;
	}

	public String getFuncionarioFolha() {
		return funcionarioFolha;
	}

	public void setFuncionarioFolha(String funcionarioFolha) {
		this.funcionarioFolha = funcionarioFolha;
	}

	public String getCargoFolha() {
		return cargoFolha;
	}

	public void setCargoFolha(String cargoFolha) {
		this.cargoFolha = cargoFolha;
	}

	public String getSituacaoFolha() {
		return situacaoFolha;
	}

	public void setSituacaoFolha(String situacaoFolha) {
		this.situacaoFolha = situacaoFolha;
	}

	public String getSalarioFolha() {
		return salarioFolha;
	}

	public void setSalarioFolha(String salarioFolha) {
		this.salarioFolha = salarioFolha;
	}

	public String getComissaoFolha() {
		return comissaoFolha;
	}

	public void setComissaoFolha(String comissaoFolha) {
		this.comissaoFolha = comissaoFolha;
	}

	public String getValeFolha() {
		return valeFolha;
	}

	public void setValeFolha(String valeFolha) {
		this.valeFolha = valeFolha;
	}

	public String getReceberFolha() {
		return receberFolha;
	}

	public void setReceberFolha(String receberFolha) {
		this.receberFolha = receberFolha;
	}

	public String getTotalFolha() {
		return totalFolha;
	}

	public void setTotalFolha(String totalFolha) {
		this.totalFolha = totalFolha;
	}

	public Meses getMeses() {
		return mes;
	}

	public void setMeses(Meses mes) {
		this.mes = mes;
	}

	public Anos getAnos() {
		return ano;
	}

	public void setAnos(Anos ano) {
		this.ano = ano;
	}

	@Override
	public String toString() {
		return "FolhaMes [numeroFolha = " + numeroFolha + ", funcionarioFolha = " + funcionarioFolha + ", cargoFolha = " + cargoFolha + ", situacaoFolha = "
				+ situacaoFolha + ", salarioFolha = " + salarioFolha + ", comissaoFolha = " + comissaoFolha + ", valeFolha = "
				+ valeFolha + ", receberFolha = " + receberFolha + ", totalFolha = " + totalFolha + ", mes = " + mes + ", ano = " + ano + "]";
	}
}
