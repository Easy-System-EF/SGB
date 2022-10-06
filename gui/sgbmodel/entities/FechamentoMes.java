package gui.sgbmodel.entities;

import java.io.Serializable;

public class FechamentoMes implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroFechamentoMes;
	private String cartelaFechamentoMes;
	private String dataFechamentoMes;
	private String situacaoFechamentoMes;
	private String valorCartelaFechamentoMes;
	private String valorProdutoFechamentoMes;
	private String valorComissaoFechamentoMes;
	private String valorResultadoFechamentoMes;
	private String valorAcumuladoFechamentoMes;

	private Meses mes;
	private Anos ano;
		
	public FechamentoMes() {
	}

	public FechamentoMes(Integer numeroFechamentoMes, String cartelaFechamentoMes, 
			String dataFechamentoMes, String situacaoFechamentoMes, 
			String valorCartelaFechamentoMes, String valorProdutoFechamentoMes, 
			String valorComissaoFechamentoMes, 	String valorResultadoFechamentoMes, 
			String valorAcumuladoFechamentoMes, Meses mes, Anos ano) {
		this.numeroFechamentoMes = numeroFechamentoMes;
		this.cartelaFechamentoMes = cartelaFechamentoMes;
		this.dataFechamentoMes = dataFechamentoMes;
		this.situacaoFechamentoMes = situacaoFechamentoMes;
		this.valorCartelaFechamentoMes = valorCartelaFechamentoMes;
		this.valorProdutoFechamentoMes = valorProdutoFechamentoMes;
		this.valorComissaoFechamentoMes = valorComissaoFechamentoMes;
		this.valorResultadoFechamentoMes = valorResultadoFechamentoMes;
		this.valorAcumuladoFechamentoMes = valorAcumuladoFechamentoMes;
		this.mes = mes;
		this.ano = ano;
	}

	public Integer getNumeroFechamentoMes() {
		return numeroFechamentoMes;
	}

	public void setNumeroFechamentoMes(Integer numeroFechamentoMes) {
		this.numeroFechamentoMes = numeroFechamentoMes;
	}

	public String getCartelaFechamentoMes() {
		return cartelaFechamentoMes;
	}

	public void setCartelaFechamentoMes(String cartelaFechamentoMes) {
		this.cartelaFechamentoMes = cartelaFechamentoMes;
	}

	public String getDataFechamentoMes() {
		return dataFechamentoMes;
	}

	public void setDataFechamentoMes(String dataFechamentoMes) {
		this.dataFechamentoMes = dataFechamentoMes;
	}

	public String getSituacaoFechamentoMes() {
		return situacaoFechamentoMes;
	}

	public void setSituacaoFechamentoMes(String situacaoFechamentoMes) {
		this.situacaoFechamentoMes = situacaoFechamentoMes;
	}

	public String getValorCartelaFechamentoMes() {
		return valorCartelaFechamentoMes;
	}

	public void setValorCartelaFechamentoMes(String valorCartelaFechamentoMes) {
		this.valorCartelaFechamentoMes = valorCartelaFechamentoMes;
	}

	public String getValorProdutoFechamentoMes() {
		return valorProdutoFechamentoMes;
	}

	public void setValorProdutoFechamentoMes(String valorProdutoFechamentoMes) {
		this.valorProdutoFechamentoMes = valorProdutoFechamentoMes;
	}

	public String getValorComissaoFechamentoMes() {
		return valorComissaoFechamentoMes;
	}

	public void setValorComissaoFechamentoMes(String valorComissaoFechamentoMes) {
		this.valorComissaoFechamentoMes = valorComissaoFechamentoMes;
	}

	public String getValorResultadoFechamentoMes() {
		return valorResultadoFechamentoMes;
	}

	public void setValorResultadoFechamentoMes(String valorResultadoFechamentoMes) {
		this.valorResultadoFechamentoMes = valorResultadoFechamentoMes;
	}

	public String getValorAcumuladoFechamentoMes() {
		return valorAcumuladoFechamentoMes;
	}

	public void setValorAcumuladoFechamentoMes(String valorAcumuladoFechamentoMes) {
		this.valorAcumuladoFechamentoMes = valorAcumuladoFechamentoMes;
	}

	public Meses getMes() {
		return mes;
	}

	public void setMes(Meses mes) {
		this.mes = mes;
	}

	public Anos getAno() {
		return ano;
	}

	public void setAno(Anos ano) {
		this.ano = ano;
	}

	@Override
	public String toString() {
		return "FechamentoMes [numeroFechamentoMes=" + numeroFechamentoMes + ", cartelaFechamentoMes="
				+ cartelaFechamentoMes + ", dataFechamentoMes=" + dataFechamentoMes + ", situacaoFechamentoMes="
				+ situacaoFechamentoMes + ", valorCartelaFechamentoMes=" + valorCartelaFechamentoMes
				+ ", valorProdutoFechamentoMes=" + valorProdutoFechamentoMes + ", valorComissaoFechamentoMes="
				+ valorComissaoFechamentoMes + ", valorResultadoFechamentoMes=" + valorResultadoFechamentoMes
				+ ", valorAcumuladoFechamentoMes=" + valorAcumuladoFechamentoMes + ", mes=" + mes + ", ano=" + ano
				+ "]";
	}
}
