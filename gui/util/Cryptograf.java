package gui.util;

public class Cryptograf {

	static final int key = 3;
	
	static String senhaCryp = "";
	static String senhaDesCryp = "";
	
	
	public static String criptografa(String str) {
		senhaCryp = "";
		char[] charsC = str.toCharArray();
		for (char c : charsC) {
			c += key;
			senhaCryp += c;
		}
		return senhaCryp;
	}
	
	public static String desCriptografa(String str) {
		senhaCryp = "";
		char[] charsD = str.toCharArray();
		for (char d : charsD) {
			d -= key;
			senhaDesCryp += d;
		}
		return senhaDesCryp;
	}
}
