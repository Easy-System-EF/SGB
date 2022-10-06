package gui.sgbmodel.entities;

import java.io.Serializable;

public class MesAno implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroMesAno;
	private Integer mesMesAno;
	private Integer anoMesAno;
	
	private Meses mes;
	private Anos ano;
		
	public MesAno () {
	}

	public MesAno(Integer numeroMesAno, Integer mesMesAno, Integer anoMesAno) {
		this.numeroMesAno = numeroMesAno;
		this.mesMesAno = mesMesAno;
		this.anoMesAno = anoMesAno;
	}

	public Integer getNumeroMesAno() {
		return numeroMesAno;
	}

	public void setNumeroMesAno(Integer numeroMesAno) {
		this.numeroMesAno = numeroMesAno;
	}

	public Integer getMesMesAno() {
		return mesMesAno;
	}

	public void setMesMesAno(Integer mesMesAno) {
		this.mesMesAno = mesMesAno;
	}

	public Integer getAnoMesAno() {
		return anoMesAno;
	}

	public void setAnoMesAno(Integer anoMesAno) {
		this.anoMesAno = anoMesAno;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroMesAno == null) ? 0 : numeroMesAno.hashCode());
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
		MesAno other = (MesAno) obj;
		if (numeroMesAno == null) {
			if (other.numeroMesAno != null)
				return false;
		} else if (!numeroMesAno.equals(other.numeroMesAno))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MesAno [numeroMesAno=" + numeroMesAno + ", mesMesAno=" + mesMesAno + ", anoMesAno=" + anoMesAno
				+ ", mes=" + mes + ", ano=" + ano + "]";
	}
}
