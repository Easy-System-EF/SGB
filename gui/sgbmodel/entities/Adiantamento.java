package gui.sgbmodel.entities;

import java.io.Serializable;
import java.util.Date;

public class Adiantamento extends Funcionario implements Serializable{

	private static final long serialVersionUID = 1L;
 
	private Integer numeroAdi;
	private Date dataAdi;
	private Double valeAdi;
	private Integer mesAdi;
	private Integer anoAdi;
	private Double valorCartelaAdi;
	private Integer cartelaAdi;
	private Double comissaoAdi;
	private String tipoAdi;
	private Double salarioAdi;
	private String nomeAdi;
	private String cargoAdi;
	private String situacaoAdi;

	public Adiantamento() {
		super();
	}

	public Adiantamento(Integer numeroAdi, Date dataAdi, Double valeAdi, Integer mesAdi, Integer anoAdi,
			Double valorCartelaAdi, Integer cartelaAdi, Double comissaoAdi, String tipoAdi, Double salarioAdi,
			String nomeAdi, String cargoAdi, String situacaoAdi) {
		super();
		this.numeroAdi = numeroAdi;
		this.dataAdi = dataAdi;
		this.valeAdi = valeAdi;
		this.mesAdi = mesAdi;
		this.anoAdi = anoAdi;
		this.valorCartelaAdi = valorCartelaAdi;
		this.cartelaAdi = cartelaAdi;
		this.comissaoAdi = comissaoAdi;
		this.tipoAdi = tipoAdi;
		this.salarioAdi = salarioAdi;
		this.nomeAdi = nomeAdi;
		this.cargoAdi = cargoAdi;
		this.situacaoAdi = situacaoAdi;
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
		if (cartelaAdi != null) {
			comissaoAdi = (valorCartelaAdi * getCargo().getComissaoCargo()) / 100;
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

	public String getNomeAdi() {
		return nomeAdi;
	}

	public void setNomeAdi(String nomeAdi) {
		this.nomeAdi = nomeAdi;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dataAdi == null) ? 0 : dataAdi.hashCode());
		result = prime * result + ((nomeAdi == null) ? 0 : nomeAdi.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adiantamento other = (Adiantamento) obj;
		if (dataAdi == null) {
			if (other.dataAdi != null)
				return false;
		} else if (!dataAdi.equals(other.dataAdi))
			return false;
		if (nomeAdi == null) {
			if (other.nomeAdi != null)
				return false;
		} else if (!nomeAdi.equals(other.nomeAdi))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Adiantamento [numeroAdi=" + numeroAdi + ", dataAdi=" + dataAdi + ", valeAdi=" + valeAdi + ", mesAdi="
				+ mesAdi + ", anoAdi=" + anoAdi + ", valorCartelaAdi=" + valorCartelaAdi + ", cartelaAdi=" + cartelaAdi
				+ ", comissaoAdi=" + comissaoAdi + ", tipoAdi=" + tipoAdi + ", salarioAdi=" + salarioAdi + ", nomeAdi="
				+ nomeAdi + ", cargoAdi=" + cargoAdi + ", situacaoAdi=" + situacaoAdi + "]";
	}
}