package gui.sgbmodel.entities;

import java.io.Serializable;
import java.util.Date;

public class AdiantamentoAnt implements Serializable{

	private static final long serialVersionUID = 1L;
 
	private Integer numeroAdi;
	private Date dataAdi;
	private Integer codFunAdi;
	private String nomeFunAdi;
	private String cargoAdi;
	private String situacaoAdi;
	private Double valeAdi;
	private Integer mesAdi;
	private Integer anoAdi;
	private Double valorCartelaAdi;
	private Integer cartelaAdi;
	private Double comissaoAdi;
	private String tipoAdi;
	private Double salarioAdi;  

	private Funcionario funcionario;
	private Cargo cargo;
	private Situacao situacao;
	
	public AdiantamentoAnt() {		
	}

	public AdiantamentoAnt(Integer numeroAdi, Date dataAdi, Integer codFunAdi, String nomeFunAdi, 
			String cargoAdi, String situacaoAdi, Double valeAdi, Integer mesAdi, 
			Integer anoAdi, Double valorCartelaAdi, Integer cartelaAdi, Double comissaoAdi, 
			String tipoAdi, Double salarioAdi, Funcionario funcionario, Cargo cargo, 
			Situacao situacao) {
		this.numeroAdi = numeroAdi;
		this.dataAdi = dataAdi;
		this.codFunAdi = codFunAdi;
		this.nomeFunAdi = nomeFunAdi;
		this.cargoAdi = cargoAdi;
		this.situacaoAdi = situacaoAdi;
		this.valeAdi = valeAdi;
		this.mesAdi = mesAdi;
		this.anoAdi = anoAdi;
		this.valorCartelaAdi = valorCartelaAdi;
		this.cartelaAdi = cartelaAdi;
		this.comissaoAdi = comissaoAdi;
		this.tipoAdi = tipoAdi;
		this.salarioAdi = salarioAdi;
		this.funcionario = funcionario;
		this.cargo = cargo;
		this.situacao = situacao;
	}

	public Integer getNumeroAdi() {
		return numeroAdi;
	}

	public void setNumeroAdi(Integer numeroAdi) {
		this.numeroAdi = numeroAdi;
	}

	public Date getDataAdi() {
		return dataAdi;
	}

	public void setDataAdi(Date dataAdi) {
		this.dataAdi = dataAdi;
	}

	public Integer getCodFunAdi() {
		return codFunAdi;
	}

	public void setCodFunAdi(Integer codFunAdi) {
		this.codFunAdi = codFunAdi;
	}

	public String getNomeFunAdi() {
		return nomeFunAdi;
	}

	public void setNomeFunAdi(String nomeFunAdi) {
		this.nomeFunAdi = nomeFunAdi;
	}

	public String getCargoAdi() {
		return cargoAdi;
	}

	public void setCargoAdi(String cargoAdi) {
		this.cargoAdi = cargoAdi;
	}

	public String getSituacaoAdi() {
		return situacaoAdi;
	}

	public void setSituacaoAdi(String situacaoAdi) {
		this.situacaoAdi = situacaoAdi;
	}

	public Double getValeAdi() {
		return valeAdi;
	}

	public void setValeAdi(Double valeAdi) {
		this.valeAdi = valeAdi;
	}

	public Integer getMesAdi() {
		return mesAdi;
	}

	public void setMesAdi(Integer mesAdi) {
		this.mesAdi = mesAdi;
	}

	public Integer getAnoAdi() {
		return anoAdi;
	}

	public void setAnoAdi(Integer anoAdi) {
		this.anoAdi = anoAdi;
	}

	public Double getValorCartelaAdi() {
		return valorCartelaAdi;
	}

	public void setValorCartelaAdi(Double valorCartelaAdi) {
		this.valorCartelaAdi = valorCartelaAdi;
	}

	public Integer getCartelaAdi() {
		return cartelaAdi;
	}

	public void setCartelaAdi(Integer cartelaAdi) {
		this.cartelaAdi = cartelaAdi;
	}

	public Double getComissaoAdi() {
		comissaoAdi = 0.00;
		if (cartelaAdi != null) {
			comissaoAdi = (valorCartelaAdi * funcionario.getCargo().getComissaoCargo()) / 100;
		}
		return comissaoAdi;
	}

	public String getTipoAdi() {
		return tipoAdi;
	}

	public void setTipoAdi(String tipoAdi) {
		this.tipoAdi = tipoAdi;
	}

	public Double getSalarioAdi() {
		return salarioAdi;
	}

	public void setSalarioAdi(Double salarioAdi) {
		this.salarioAdi = salarioAdi;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroAdi == null) ? 0 : numeroAdi.hashCode());
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
		AdiantamentoAnt other = (AdiantamentoAnt) obj;
		if (numeroAdi == null) {
			if (other.numeroAdi != null)
				return false;
		} else if (!numeroAdi.equals(other.numeroAdi))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Adiantamento [numeroAdi=" + numeroAdi + ", dataAdi=" + dataAdi + ", codFunAdi=" + codFunAdi
				+ ", nomeFunAdi=" + nomeFunAdi + ", cargoAdi=" + cargoAdi + ", situacaoAdi=" + situacaoAdi
				+ ", valeAdi=" + valeAdi + ", mesAdi=" + mesAdi + ", anoAdi=" + anoAdi + ", valorCartelaAdi="
				+ valorCartelaAdi + ", cartelaAdi=" + cartelaAdi + ", comissaoAdi=" + comissaoAdi + ", tipoAdi="
				+ tipoAdi + ", salarioAdi=" + salarioAdi + ", funcionario=" + funcionario + ", cargo=" + cargo
				+ ", situacao=" + situacao + "]";
	}	
}