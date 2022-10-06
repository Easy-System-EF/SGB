package gui.util;

import java.util.Calendar;
import java.util.Date;

public class CalculaParcela {

	public static Date CalculaVencimentoDia(Date data, int parcela, int dia) {
   		Calendar cal = Calendar.getInstance(); 
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, (dia * parcela));
 		Date dataVen = cal.getTime();
		return dataVen;
	}
	
	public static Date CalculaVencimentoMes(Date data, int mes) {
   		Calendar cal = Calendar.getInstance(); 
		cal.setTime(data);
		cal.add(Calendar.MONTH, (mes));
 		Date dataVen = cal.getTime();
		return dataVen;
	}
	
	public static Double calculaParcelas(Double valor, int qtdParcelas, int numParcela) {
			double a = valor % qtdParcelas;
			double b = valor - a;
			double c = b / qtdParcelas;
			double d = c + a;
			if (numParcela == 1)
		{ 	valor = d;
		}	else
			{	valor = c;
			}
		return valor;
	}
}
