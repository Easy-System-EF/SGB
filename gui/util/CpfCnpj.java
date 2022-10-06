package gui.util;

/*
 * crie um vetor com os dados cpf/cnpj
 *  String [] cnpjV/cpfV = new String[14]/[11] (-1);
 *  for (int i = 0 ; i < cnpjV/cpfV.length ; i ++ )
 *  {	char b = xyz.getCnpjCli().charAt(i);
 * 	{	char b = yxz.getCpfCli().charAt(i);
 *		String c = "" + b;
 *		cnpjV[i] // cpfV[i] = (c);
 *  }
 *  flagv = 0;
 *  CpfCnpj.Cpf(flagv, cpfV);
 *  CpfCnpj.Cnpj(flagv, cnpjV);
 *  if (CpfCnpj.Cpf(flagv, cpfV) == false) => INVÁLIDO 
 *  if (CpfCnpj.Cnpj(flagv, cpfV) == false) => INVÁLIDO 
 */		
 
public class CpfCnpj {

	static int i = 0;
	static int ii = 0;
	static int soma = 0;
	static int resto = 0;
	static int d1 = 0;
	static int d2 = 0;
	static int dig1 = 0; 
	static int dig2 = 0;
 	static String[] vetor12 = new String[12];
	static String[] vetor13 = new String[13];

 	public static boolean Cnpj(Integer flagvf, String[] cnpjV) {
  		dig1 = Integer.parseInt(cnpjV[12]);
  		dig2 = Integer.parseInt(cnpjV[13]);
  		d1 = 0;
  		d2 = 0;
  		inverteCnpj12(cnpjV);
 		ii = 2;
 		soma = 0;
 		int flag = 0;
  		for (i = 0 ; i < vetor12.length ; i++ )
		{	int n = Integer.parseInt(vetor12[i]);
 			soma = soma + (n * ii);
 			int a = n * ii;
   			if	(ii == 9 )
 			{	ii = 1;
 				flag = 1;
 			}				
  			ii += 1;
 			if  (flag == 1 && ii > 5)
 			{	resto = soma % 11; 				
 				if (resto > 10 || resto == 0)
 				{	d1 = 0;
 				}	else	 
 					{	d1 = 11 - resto;
 					}
 				if (d1 > 9)
 				{	d1 = 0;
 				}
   				if (dig1 == d1)
 				{	inverteCnpj13(vetor12);
 					n = 0;
 					ii = 2;
 					soma = 0;
  					flag = 0;
  					for (i = 0 ; i < vetor13.length ; i ++)
 					{	n = Integer.parseInt(vetor13[i]);
 						soma = soma + (n * ii);
  			 			if (ii == 9)
 			 			{	ii = 1;
 			 				flag = 1;
  			 			}
 			 			ii += 1;
 			 			if (flag == 1 && ii > 6)
  						{	resto = soma % 11;
  							if (resto > 10 || resto == 0)
 							{	d2 = 0;
 							}	else	 
 								{	d2 = 11 - resto;
 								}
 								if (d2 > 9)
 								{	d2 = 0;
 								}
  							if (d2 == dig2 )
 							{	flagvf = 1;  								
 							}
  						}
 					}	
 				}	
 			}
		}
  		return flagvf > 0;			
 	}
 	
 	public static boolean Cpf(Integer flagvf, String[] cpfV) {
   		dig1 = Integer.parseInt(cpfV[9]);
		dig2 = Integer.parseInt(cpfV[10]);
		d1 = 0;
		d2 = 0;
		ii = 10;
		soma = 0;
		resto = 0;
		
  		for (i = 0 ; i < 9 ; i++ )
		{	int n = Integer.parseInt(cpfV[i]);
 			soma = soma + (n * ii);
 			int a = n * ii;
  			ii -= 1;
 			if (ii == 1)
 			{	resto = soma % 11;
 				if (resto > 10 || resto == 0)
 				{	d1 = 0;
 				}	else	 
 					{	d1 = 11 - resto;
 					}
 				if (d1 > 9)
 				{	d1 = 0;
 				}
   				if (dig1 == d1)
 				{	n = 0;
 					ii = 11;
 					soma = 0;
   					for (i = 0 ; i < 9 ; i ++)
 					{	n = Integer.parseInt(cpfV[i]);
 						soma = soma + (n * ii);
   						ii -= 1;
 						if (ii == 2)
 						{	soma = soma + (d1 * ii);
 							resto = soma % 11;
 							if (resto > 10 || resto == 0)
 							{	d2 = 0;
 							}	else	 
 								{	d2 = 11 - resto;
 								}
 							if (d2 > 9)
 							{	d2 = 0;
 							}
 							if (d1 == dig1 && d2 == dig2 )
 							{	flagvf = 1;  								
 							}
  						}
 					}	
 				}	
 			}
		}
 		return flagvf > 0;
 	}
 	
 	private static void inverteCnpj12(String[] cnpjV) {
 		i = 0;
		ii = 11;
		for (i = i ; i < 12 ; i ++)
		{	vetor12[i] = cnpjV[ii]; 
			ii -= 1;
		}
//		0 11; 1 10; 2 9; 3 8; 4 7; 5 6; 6 5; 7 4; 8 3; 9 2; 10 1; 11 0 
  	}	
	
 	private static void inverteCnpj13(String[] vetor12) {
 		i = 0;
  		for (i = i ; i < vetor12.length + 1 ; i ++)
		{	if (i == 0)
			{	vetor13[i] = String.valueOf(d1);
			}	else
				{	vetor13[i] = vetor12[i - 1];
				}
 		}
   	}	
 }
