package gui.sgbmodel.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Produto implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer codigoProd;
	private Integer grupoProd;
	private String nomeProd;
	private Double entradaProd;
	private Double saidaProd;
	private Double saldoProd;
	private Double estMinProd;
	private Double precoProd;
	private Double vendaProd;
	private Double cmmProd;
	private Date dataCadastroProd;

  	private Grupo grupo;

	public Produto() {
	}

	public Produto(Integer codigoProd, Integer grupoProd, String nomeProd, Double entradaProd, Double saidaProd,
			Double saldoProd, Double estMinProd, Double precoProd, Double vendaProd, Double cmmProd,
			Date dataCadastroProd, Grupo grupo) {
		this.codigoProd = codigoProd;
		this.grupoProd = grupoProd;
		this.nomeProd = nomeProd;
		this.entradaProd = entradaProd;
		this.saidaProd = saidaProd;
		this.saldoProd = saldoProd;
		this.estMinProd = estMinProd;
		this.precoProd = precoProd;
		this.vendaProd = vendaProd;
		this.cmmProd = cmmProd;
		this.dataCadastroProd = dataCadastroProd;
		this.grupo = grupo;
	}

	public Integer getCodigoProd() {
		return codigoProd;
	}

	public void setCodigoProd(Integer codigoProd) {
		this.codigoProd = codigoProd;
	}

	public Integer getGrupoProd() {
		return grupoProd;
	}

	public void setGrupoProd(Integer grupoProd) {
		this.grupoProd = grupoProd;
	}

	public String getNomeProd() {
		return nomeProd;
	}

	public void setNomeProd(String nomeProd) {
		this.nomeProd = nomeProd;
	}

	public Double getEntradaProd() {
		return entradaProd;
	}

	public void setEntradaProd(Double entradaProd) {
		if (entradaProd == 0.00) {
			this.entradaProd = 0.00;
		}
		if (this.entradaProd == null) {
			this.entradaProd = 0.00;
		}
		this.entradaProd += entradaProd;
	}

	public Double getSaidaProd() {
		return saidaProd;
	}

	public void setSaidaProd(Double saidaProd) {
		if (saidaProd == 0.00) {
			this.saidaProd = 0.00;
		}
		if (this.saidaProd == null) {
			this.saidaProd = 0.00;
		}
		this.saidaProd += saidaProd;
	}

	public Double getSaldoProd() {
		return saldoProd = entradaProd - saidaProd;
	}

	public Double getEstMinProd() {
		return estMinProd;
	}

	public void setEstMinProd(Double estMinProd) {
		if (this.estMinProd == null) {
			this.estMinProd = 0.00;
		}
		this.estMinProd = estMinProd;
	}

	public Double getPrecoProd() {
		return precoProd;
	}

	public void setPrecoProd(Double precoProd) {
		if (this.precoProd == null) {
			this.precoProd = 0.00;
		}
		this.precoProd = precoProd;
	}

	public Double getVendaProd() {
		return vendaProd;
	}

	public void setVendaProd(Double vendaProd) {
		if (this.vendaProd == null) {
			this.vendaProd = 0.00;
		}
		this.vendaProd = vendaProd;
	}

	public Double getCmmProd() {
		return cmmProd = calculaCmm();
	}

	public Date getDataCadastroProd() {
		return dataCadastroProd;
	}

	public void setDataCadastroProd(Date dataCadastroProd) {
		this.dataCadastroProd = dataCadastroProd;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Double calculaCmm() {
		Double result = 0.00;
		Date dataHoje = new Date();
		int meses = 0;
  		long dif = dataHoje.getTime() - dataCadastroProd.getTime();
  		long  dias = TimeUnit.DAYS.convert(dif, TimeUnit.MILLISECONDS);
  		if (dias > 30) {
  			meses = (int) (dias / 30);
  			result = saidaProd / meses;
  		}
  		else {
  			result = saidaProd;
  		}
  			
			return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoProd == null) ? 0 : codigoProd.hashCode());
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
		Produto other = (Produto) obj;
		if (codigoProd == null) {
			if (other.codigoProd != null)
				return false;
		} else if (!codigoProd.equals(other.codigoProd))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Produto [codigoProd=" + codigoProd + ", grupoProd=" + grupoProd + ", nomeProd=" + nomeProd
				+ ", entradaProd=" + entradaProd + ", saidaProd=" + saidaProd + ", saldoProd=" + saldoProd
				+ ", estMinProd=" + estMinProd + ", precoProd=" + precoProd + ", vendaProd=" + vendaProd + ", cmmProd="
				+ cmmProd + ", dataCadastroProd=" + dataCadastroProd + ", grupo=" + grupo + "]";
	}

// 		Date dt  = new Date();
// 		Calendar cal = Calendar.getInstance();
// 		cal.setTime(dt);
// 		int aa = cal.get(Calendar.YEAR);
// 		int mm = cal.get(Calendar.MONTH);
}
