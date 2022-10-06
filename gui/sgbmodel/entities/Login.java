package gui.sgbmodel.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Login implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroLog;
	private String senhaLog;
	private String nomeLog;
	private Integer nivelLog;
	private Integer alertaLog;
	private Date dataLog;
	private Date vencimentoLog;
	private Date maximaLog;
	private Date acessoLog;
	private Integer empresaLog;

	public Login() {		
	}

	public Login(Integer numeroLog, String senhaLog, String nomeLog, Integer nivelLog, Integer alertaLog,  
			Date dataLog, Date vencimentoLog, Date maximaLog, Date acessoLog, Integer empresaLog) {
		this.numeroLog = numeroLog;
		this.senhaLog = senhaLog;
		this.nomeLog = nomeLog;
		this.nivelLog = nivelLog;
		this.alertaLog = alertaLog;
		this.dataLog = dataLog;
		this.vencimentoLog = vencimentoLog;
		this.maximaLog = maximaLog;
		this.acessoLog = acessoLog;
		this.empresaLog = empresaLog;
	}

	public Integer getNumeroLog() {
		return numeroLog;
	}

	public void setNumeroLog(Integer numeroLog) {
		this.numeroLog = numeroLog;
	}

	public String getSenhaLog() {
		return senhaLog;
	}

	public void setSenhaLog(String senhaLog) {
		this.senhaLog = senhaLog;
	}

	public String getNomeLog() {
		return nomeLog;
	}

	public void setNomeLog(String nomeLog) {
		this.nomeLog = nomeLog;
	}

	public Integer getNivelLog() {
		return nivelLog;
	}

	public void setNivelLog(Integer nivelLog) {
		this.nivelLog = nivelLog;
	}

	public Integer getAlertaLog() {
		return alertaLog;
	}

	public void setAlertaLog(Integer alertaLog) {
		this.alertaLog = alertaLog;
	}

	public Date getDataLog() {
		return dataLog;
	}

	public void setDataLog(Date dataLog) {
		this.dataLog = dataLog;
	}

	public void setDataVencimentoLog(Date vencimentoLog) {
		this.vencimentoLog = vencimentoLog;
	}

	public Date getVencimentoLog() {
		return vencimentoLog;
	}

	public void setDataMaximaLog(Date maximaLog) {
		this.maximaLog = maximaLog;
	}

	public Date getMaximaLog() {
		return maximaLog;
	}

	public Date getAcessoLog() {
		return acessoLog;
	}

	public void setAcessoLog(Date acessoLog) {
		this.acessoLog = acessoLog;
	}

	public Integer getEmpresaLog() {
		return empresaLog;
	}

	public void setEmpresaLog(Integer empresaLog) {
		this.empresaLog = empresaLog;
	}

	public Date updateVencimento(Date dataHj) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataHj);
		int dd = 0;
		int mm = cal.get(Calendar.MONTH) + 1;
		int aa = cal.get(Calendar.YEAR);
		int resto = aa % 4;
		if (mm == 1 || mm == 3 || mm == 5 || mm == 7 || mm == 8 || mm == 10 || mm == 12) {
			dd = 31;
		}
		if (mm == 4 || mm == 6 || mm == 9 || mm == 11) {
			dd = 30;
		}
		if (mm == 2 && resto > 0) {
			dd = 28;
		} else {
			if (mm == 2 && resto == 0) {
				dd = 29;
			}
		}	
		cal.set(Calendar.DAY_OF_MONTH, dd);
		vencimentoLog = cal.getTime();
		updateMaxima();
		return vencimentoLog;		
	}

	public Date updateMaxima() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(vencimentoLog);
		cal.add(Calendar.DAY_OF_MONTH, 10);
		maximaLog = cal.getTime();
		return maximaLog;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroLog == null) ? 0 : numeroLog.hashCode());
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
		Login other = (Login) obj;
		if (numeroLog == null) {
			if (other.numeroLog != null)
				return false;
		} else if (!numeroLog.equals(other.numeroLog))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Login [numeroLog= " + numeroLog + ", senhaLog= " + senhaLog + ", nomeLog= " + nomeLog + ", nivelLog= "
				+ nivelLog + ", alertaLog= " + alertaLog + ", dataLog= " + dataLog + ", vencimentoLog= " + vencimentoLog
				+ ", maximaLog= " + maximaLog + ", acessoLog= " + acessoLog + ", empresaLog= " + empresaLog + "]";
	}
}
