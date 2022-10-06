package gui.sgcpmodel.entites;

import java.io.Serializable;
import java.util.Date;

import gui.sgcpmodel.entites.consulta.ParPeriodo;

public class Parcela implements Serializable {

 	private static final long serialVersionUID = 1L;

 	private Integer codigoFornecedorPar;
 	private String nomeFornecedorPar;
 	private Integer nnfPar;
	private Integer numeroPar;
	private Date  dataVencimentoPar;
 	private Double valorPar;
	private Double descontoPar;
	private Double jurosPar;
	private Double totalPar;
	private Double pagoPar;
	private Date dataPagamentoPar;
 	private Double resultadoPar;
 	private String resultadoParStr;
 
	public Fornecedor fornecedor;
	public TipoConsumo tipoFornecedor;
	public ParPeriodo periodo;

	public Parcela() {
	}

	public Parcela(Integer codigoFornecedorPar, String nomeFornecedorPar, Integer nnfPar, Integer numeroPar,
			Date dataVencimentoPar, Double valorPar, Double descontoPar, Double jurosPar, Double totalPar,
			Double pagoPar, Date dataPagamentoPar, Double resultadoPar, Fornecedor fornecedor,
			TipoConsumo tipoFornecedor, ParPeriodo periodo) {
 		this.codigoFornecedorPar = codigoFornecedorPar;
		this.nomeFornecedorPar = nomeFornecedorPar;
		this.nnfPar = nnfPar;
		this.numeroPar = numeroPar;
		this.dataVencimentoPar = dataVencimentoPar;
		this.valorPar = valorPar;
		this.descontoPar = descontoPar;
		this.jurosPar = jurosPar;
		this.totalPar = totalPar;
		this.pagoPar = pagoPar;
		this.dataPagamentoPar = dataPagamentoPar;
		this.resultadoPar = resultadoPar;
 		this.fornecedor = fornecedor;
		this.tipoFornecedor = tipoFornecedor;
		this.periodo = periodo;
	}
	
	public Integer getCodigoFornecedorPar() {
		return codigoFornecedorPar;
	}

	public void setCodigoFornecedorPar(Integer codigoFornecedorPar) {
		this.codigoFornecedorPar = codigoFornecedorPar;
	}

	public String getNomeFornecedorPar() {
		return nomeFornecedorPar;
	}

	public void setNomeFornecedorPar(String nomeFornecedorPar) {
		this.nomeFornecedorPar = nomeFornecedorPar;
	}

	public Integer getNnfPar() {
		return nnfPar;
	}

	public void setNnfPar(Integer nnfPar) {
		this.nnfPar = nnfPar;
	}

	public Integer getNumeroPar() {
		return numeroPar;
	}

	public void setNumeroPar(Integer numeroPar) {
		this.numeroPar = numeroPar;
	}

	public Date getDataVencimentoPar() {
		return dataVencimentoPar;
	}

	public void setDataVencimentoPar(Date dataVencimentoPar) {
		this.dataVencimentoPar = dataVencimentoPar;
	}

 	public Double getValorPar() {
		return valorPar;
	}

	public void setValorPar(Double valorPar) {
		this.valorPar = valorPar;
	}

	public Double getDescontoPar() {
		return descontoPar;
	}

	public void setDescontoPar(Double descontoPar) {
		this.descontoPar = descontoPar;
	}

	public Double getJurosPar() {
		return jurosPar;
	}

	public void setJurosPar(Double jurosPar) {
		this.jurosPar = jurosPar;
	}

	public Double getTotalPar() {
		return totalPar;
	}

	public void setTotalPar(Double totalPar) {
		this.totalPar = totalPar;
	}
	
//	public void setTotalPar(Double vPar) {
//		this.totalPar = valorPar + jurosPar - descontoPar;
//	}

	public Double getPagoPar() {
		return pagoPar;
	}

	public void setPagoPar(Double pagoPar) {
		this.pagoPar = pagoPar;
	}

	public Date getDataPagamentoPar() {
		return dataPagamentoPar;
	} 

	public void setDataPagamentoPar(Date dataPagamentoPar) {
		this.dataPagamentoPar = dataPagamentoPar;
	}

 	public Double getResultadoPar() {
		return resultadoPar;
	}

	public void setResultadoPar(Double resultadoPar) {
		this.resultadoPar = resultadoPar;
	}

 	public String getResultadoParStr() {
		return resultadoParStr;
	}

	public void setResultadoParStr(String resultadoParStr) {
		this.resultadoParStr = resultadoParStr;
	}

   	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public TipoConsumo getTipoFornecedor() {
		return tipoFornecedor;
	}

	public void setTipoFornecedor(TipoConsumo tipoFornecedor) {
		this.tipoFornecedor = tipoFornecedor;
	}

	public ParPeriodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(ParPeriodo periodo) {
		this.periodo = periodo;
	}
	
	public Double calculaParcelas(Double valor, int num, int i) {
  			double a = valor % num;
 			double b = valor - a;
 			double c = b / num;
 			double d = c + a;
 			if (i == 1)
			{ 	valor = d;
			}	else
				{	valor = c;
				}
 		return valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoFornecedorPar == null) ? 0 : codigoFornecedorPar.hashCode());
		result = prime * result + ((nnfPar == null) ? 0 : nnfPar.hashCode());
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
		Parcela other = (Parcela) obj;
		if (codigoFornecedorPar == null) {
			if (other.codigoFornecedorPar != null)
				return false;
		} else if (!codigoFornecedorPar.equals(other.codigoFornecedorPar))
			return false;
		if (nnfPar == null) {
			if (other.nnfPar != null)
				return false;
		} else if (!nnfPar.equals(other.nnfPar))
			return false; 
		return true;
	}

	@Override
	public String toString() {
		return "Parcela [codigoFornecedorPar=" + codigoFornecedorPar + ", nomeFornecedorPar=" + nomeFornecedorPar
				+ ", nnfPar=" + nnfPar + ", numeroPar=" + numeroPar + ", dataVencimentoPar=" + dataVencimentoPar
				+ ", valorPar=" + valorPar + ", descontoPar=" + descontoPar + ", jurosPar=" + jurosPar + ", totalPar="
				+ totalPar + ", pagoPar=" + pagoPar + ", dataPagamentoPar=" + dataPagamentoPar + ", resultadoPar="
				+ resultadoPar + "]";
	}

 }

 