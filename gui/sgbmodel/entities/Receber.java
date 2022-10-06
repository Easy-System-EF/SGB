package gui.sgbmodel.entities;

import java.io.Serializable;
import java.util.Date;

import gui.sgcpmodel.entites.consulta.ParPeriodo;
 
public class Receber implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroRec;
	private Integer cartelaRec;
	private Date dataCartelaRec;
	private String formaPagamentoRec;
	private Double valorRec;
	private Date dataPagamentoRec;

	/* forma pgto
	 * 1 = Dinheiro
	 * 2 = Pix
	 * 3 = Débito
	 * 4 = CC	
	 */

	public ParPeriodo periodo;
	
	public ParPeriodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(ParPeriodo periodo) {
		this.periodo = periodo;
	}

	public Receber() {
	}

	public Receber(Integer numeroRec, Integer cartelaRec, Date dataCartelaRec, String formaPagamentoRec,
			Double valorRec, Date dataPagamentoRec, ParPeriodo periodo) {
		this.numeroRec = numeroRec;
		this.cartelaRec = cartelaRec;
		this.dataCartelaRec = dataCartelaRec;
		this.formaPagamentoRec = formaPagamentoRec;
		this.valorRec = valorRec;
		this.dataPagamentoRec = dataPagamentoRec;
		this.periodo = periodo;
	}

	public Integer getNumeroRec() {
		return numeroRec;
	}

	public void setNumeroRec(Integer numeroRec) {
		this.numeroRec = numeroRec;
	}

	public Integer getCartelaRec() {
		return cartelaRec;
	}

	public void setCartelaRec(Integer cartelaRec) {
		this.cartelaRec = cartelaRec;
	}

	public Date getDataCartelaRec() {
		return dataCartelaRec;
	}

	public void setDataCartelaRec(Date dataCartelaRec) {
		this.dataCartelaRec = dataCartelaRec;
	}

	public String getFormaPagamentoRec() {
		return formaPagamentoRec;
	}

	public void setFormaPagamentoRec(String formaPagamentoRec) {
		this.formaPagamentoRec = formaPagamentoRec;
	}

	public Double getValorRec() {
		return valorRec;
	}

	public void setValorRec(Double valorRec) {
		this.valorRec = valorRec;
	}

	public Date getDataPagamentoRec() {
		return dataPagamentoRec;
	}

	public void setDataPagamentoRec(Date dataPagamentoRec) {
		this.dataPagamentoRec = dataPagamentoRec;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cartelaRec == null) ? 0 : cartelaRec.hashCode());
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
		Receber other = (Receber) obj;
		if (cartelaRec == null) {
			if (other.cartelaRec != null)
				return false;
		} else if (!cartelaRec.equals(other.cartelaRec))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Receber [numeroRec=" + numeroRec + ", cartelaRec=" + cartelaRec + ", dataCartelaRec=" + dataCartelaRec
				+ ", formaPagamentoRec=" + formaPagamentoRec + ", valorRec=" + valorRec + ", dataPagamentoRec="
				+ dataPagamentoRec + ", periodo=" + periodo + "]";
	}
}
