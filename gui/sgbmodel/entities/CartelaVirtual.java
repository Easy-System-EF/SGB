package gui.sgbmodel.entities;

import java.io.Serializable;

/*
 * orçamento virtual para mater preço número de materiais
 */

public class CartelaVirtual implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroVir;
	private String localVir;
	private String situacaoVir;
	private String nomeFuncVir;
	private String nomeProdVir;
	private Double quantidadeProdVir;
	private Double precoProdVir;
	private Double vendaProdVir;
	private Double totalProdVir;
	private Integer origemIdCarVir;

	private Funcionario funcionario;
	private Produto produto;
	
	public CartelaVirtual() {
	}

	public CartelaVirtual(Integer numeroVir, String localVir, String situacaoVir, 
			String nomeFuncVir, String nomeProdVir, Double quantidadeProdVir, 
			Double precoProdVir, Double vendaProdVir, Double totalProdVir, 
			Integer origemIdCarVir, Integer numeroBalVir, Funcionario funcionario, 
			Produto produto) {
		this.numeroVir = numeroVir;
		this.situacaoVir = situacaoVir;
		this.localVir = localVir;
		this.nomeFuncVir = nomeFuncVir;
		this.nomeProdVir = nomeProdVir;
		this.quantidadeProdVir = quantidadeProdVir;
		this.precoProdVir = precoProdVir;
		this.vendaProdVir = vendaProdVir;
		this.totalProdVir = totalProdVir;
		this.origemIdCarVir = origemIdCarVir;
		this.funcionario = funcionario;
		this.produto = produto;
	}

	public Integer getNumeroVir() {
		return numeroVir;
	}

	public void setNumeroVir(Integer numeroVir) {
		this.numeroVir = numeroVir;
	}

	public String getLocalVir() {
		return localVir;
	}

	public void setLocalVir(String localVir) {
		this.localVir = localVir;
	}

	public String getSituacaoVir() {
		return situacaoVir;
	}

	public void setSituacaoVir(String situacaoVir) {
		this.situacaoVir = situacaoVir;
	}

	public String getNomeProdVir() {
		return nomeProdVir;
	}

	public void setNomeFuncVir(String nomeFuncVir) {
		this.nomeFuncVir = nomeFuncVir;
	}

	public String getNomeFuncVir() {
		return nomeFuncVir;
	}

	public void setNomeProdVir(String nomeProdVir) {
		this.nomeProdVir = nomeProdVir;
	}

	public Double getQuantidadeProdVir() {
		return quantidadeProdVir;
	}

	public void setQuantidadeProdVir(Double quantidadeProdVir) {
		this.quantidadeProdVir = quantidadeProdVir;
	}

	public Double getPrecoProdVir() {
		return precoProdVir;
	}

	public void setPrecoProdVir(Double precoProdVir) {
		this.precoProdVir = precoProdVir;
	}

	public Double getVendaProdVir() {
		return vendaProdVir;
	}

	public void setVendaProdVir(Double vendaProdVir) {
		this.vendaProdVir = vendaProdVir;
	}

	public Double getTotalProdVir() {
		return totalProdVir = totalProdVir;
	}

	public void setTotalProdVir(Double totalProdVir) {
		this.totalProdVir = totalProdVir;
	}

	public Integer getOrigemIdCarVir() {
		return origemIdCarVir;
	}

	public void setOrigemIdCarVir(Integer origemIdCarVir) {
		this.origemIdCarVir = origemIdCarVir;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Produto getProduto() {
		return produto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((localVir == null) ? 0 : localVir.hashCode());
		result = prime * result + ((situacaoVir == null) ? 0 : situacaoVir.hashCode());
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
		CartelaVirtual other = (CartelaVirtual) obj;
		if (localVir == null) {
			if (other.localVir != null)
				return false;
		} else if (!localVir.equals(other.localVir))
			return false;
		if (situacaoVir == null) {
			if (other.situacaoVir != null)
				return false;
		} else if (!situacaoVir.equals(other.situacaoVir))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CartelaVirtual [numeroVir=" + numeroVir + ", localVir=" + localVir + ", situacaoVir=" + situacaoVir
				+ ", nomeFuncVir=" + nomeFuncVir + ", nomeProdVir=" + nomeProdVir + ", quantidadeProdVir="
				+ quantidadeProdVir + ", vendaProdVir=" + vendaProdVir + ", totalProdVir=" + totalProdVir
				+ ", origemIdCarVir=" + origemIdCarVir + ", funcionario=" + funcionario + ", produto=" + produto + "]";
	}

}
