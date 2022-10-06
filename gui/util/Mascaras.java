package gui.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.text.MaskFormatter;

import javafx.scene.control.TableColumn;

public class Mascaras {

	Locale ptBR = new Locale("pt", "BR");
	
	static String cepMask = ("#####-###");
 	static String cpfMask = ("###.###.###-##");
	static String cnpjMask = ("##.###.###/####-##");
	static String comMask = ("##,#");
	static String mask = " ";

	static DecimalFormat dfVlr = new DecimalFormat("##,##0.00");
	static DecimalFormat dfMil = new DecimalFormat("###,###");
	static DecimalFormat dfCep = new DecimalFormat("#####-###");
	static DecimalFormat dfCom = new DecimalFormat("#0.0");

	public static String formataString(String txt, String tipo) throws ParseException {
 		switch (tipo)
		{	case "Cep":
				mask = cepMask;
				break;
			case "Cpf":
				mask = cpfMask;	
				break;
			case "Cnpj":
				mask = cnpjMask;
				break;
			case "Com":
				mask = comMask;
				break;
			default:
				mask = null;
				break;
 		}
 		MaskFormatter mf = new MaskFormatter(mask);
 		mf.setValueContainsLiteralCharacters(false);
 		return txt = mf.valueToString(txt);
	}
	
	public static String formataValor(double vlr) throws ParseException {
  	   	String resultado = ""; 
   	   	resultado = dfVlr.format(vlr);
  		return resultado;   
	}

	public static String formataCom(double vlr) throws ParseException {
  	   	String resultado = ""; 
   	   	resultado = dfCom.format(vlr);
  		return resultado;   
	}

	public static String formataMillhar(Integer vlr) throws ParseException {
  	   	String resultado = ""; 
   	   	resultado = dfMil.format(vlr);
  		return resultado;   
	}

	public static String formataValorStr(String vlr) throws ParseException {
  	   	String resultado; 
   	   	resultado = dfVlr.format(vlr);
   	   	System.out.println(resultado);
  		return resultado;   
	}

//	public static String formataCep(Integer cep) throws ParseException {
//  	   	String resultado = ""; 
//   	   	resultado = dfCep.format(cep);
//  		return resultado;   
//	}

 }

 