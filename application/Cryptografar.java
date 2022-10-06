package application;

import java.util.Scanner;

public class Cryptografar {

	public static void main(String[] args) {

		int key = 3;
		
		Scanner sc = new Scanner(System.in);
		String senha = sc.nextLine();
		String senhaCryp = "";
		String original = "";
	
		sc.close();
		
		char[] charsC = senha.toCharArray();
		for (char c : charsC) {
			c += key;
			senhaCryp += c;
			System.out.println(c + " " + charsC + " " + senhaCryp);
		}
		
		System.out.println("-------------------------");
		System.out.println();
		
		char[] charsD = senhaCryp.toCharArray();
		for (char d : charsD) {
			d -= key;
			original += d;
			System.out.println(d + " " + charsD + " " + original);
		}
	}

}
