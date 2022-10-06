package gui.util;
public class FormataGabarito {

  	public static String formataString37(Integer tam) {
			String str = "";
   			int t = 37 - tam;
  			for (int i = 1; i < t + 1 ; i ++)
  			{	str =  str + " ";	
  			}
   		return str;
	}
 	
  	public static String formataStringQualquer(Integer espaco, Integer tamItem) {
			String str = "";
   			int preenche = espaco - tamItem;
 			for (int i = 1; i < preenche + 1 ; i ++)
  			{	str =  str + " ";	
  			}
   		return str;
	}
 	
  	public static String formataString(Integer tam) {
			String str = "";
   			int t = 19 - tam;
 			for (int i = 1; i < t + 1 ; i ++)
  			{	str =  str + " ";	
  			}
   		return str;
	}
 	
 	public static String formataNnf(Integer nnf) {
 		String str  = "";
 		int tam = 0;
		if (nnf > 99999)
		{	tam = 6;	
		}	else
			{	if (nnf > 9999)
				{	tam = 5;	
				}	else
					{	if (nnf > 999)
						{	tam = 4;	
						}	else
							{	if (nnf > 99)
								{	tam = 3;	
								}	else
									{	if (nnf > 9)
										{	tam = 2;	
										}	else
										{	if (nnf < 10)
											{	tam = 1;	
											}
										}
									}
							}
					}
			}
 		int t = 6 - tam;
		for (int i = 1; i < t + 1; i ++)
		{	str =  str + " ";	
 		}	
 	return str; 
	}

 	public static String formataParc(Integer par) {
 		String str  = "";
 		int tam = 0;
   		if (par > 9)
		{	tam = 2;	
		}	else
			{	if (par < 10)
				{	tam = 1;	
				}
			}
		int t = 4 - tam;
		for (int i = 1; i < t + 1; i ++)
		{	str =  str + " ";
		}
	return str; 
 	}
 	
 	public static String formataDouble(Double vlr) {
 		String str  = "";
 		int tam = 0;
 		if (vlr > 999999999.99) {
 			tam = 16;
 		} else {
 	 		if (vlr > 99999999.99) {
 	 			tam = 14;
 	 		} else {
 	 	 		if (vlr > 9999999.99) {
 	 	 			tam = 13;
 	 	 		} else {
 	 	 			if (vlr > 999999.99) {
 	 	 				tam = 12;
 	 	 			} else {
 	 	 				if (vlr > 99999.99) {
 	 	 					tam = 10;
 	 	 				} else {
 	 	 					if (vlr > 9999.99) {
 	 	 						tam = 9;	
 	 	 					} else {	  
 	 	 						if (vlr > 999.99) {
 	 	 							tam = 8;	
 	 	 						} else	{
 	 	 							if (vlr > 99.99) {
 	 	 								tam = 6;	
 	 	 							} else {	
 	 	 								if (vlr > 9.99) {
 	 	 									tam = 5;	
 	 	 								} else {
 	 	 									tam = 4; 											
 	 	 								}
 	 	 							}
 	 	 						}
 	 	 					}
 	 	 				}
 	 	 			}
 	 	 		}
 	 		}
 		}
 		double t = 12 - tam;
 		for (int i = 1; i < t + 1; i ++)
 		{	str =  str + " ";
 		}
 	return str; 
	}

 	public static String formataQtd5(Double vlr) {
 		String str  = "";
 		int tam = 0;
 		if (vlr > 9.99) {
 			tam = 05;
 		} else {
 	 		tam = 4;
 	 		}
 		double t = 5 - tam;
 		for (int i = 1; i < t + 1; i ++)
 		{	str =  str + " ";
 		}
 	return str; 
	}
}
