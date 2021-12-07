package com.garaperree.guazoserver.utiles;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Utiles {
	
	public static Random r = new Random();
	public static Scanner s = new Scanner(System.in);
	
	//Delay de espera
	
	public static void delay(long delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//Valido los ENTEROS
	
	public static int ingresarEntero(int min, int max) {
			// TODO Auto-generated method stub
			boolean error = false;
			int opc = 0;
			
			do {
				error = false;
				try {
					opc = s.nextInt();
					if(opc < min || opc > max) {
						error = true;
						System.out.println("El valor está fuera de los parametros. "
								+ "Ingrese un valor entre "
		                         + min + " y " + max);
					}
				}
				catch(InputMismatchException e) {
					System.out.println("tipo de dato mal ingresado. Ingrese un número");
					error = true;
					s.nextLine();
				}
				catch(Exception e) {
					System.out.println("Error inesperado "+ e);
					error = true;
					s.nextLine();
				}
			}while(error);
			s.nextLine();
			return opc;
		}
		
	//Valido los decimales 
		
	public static float ingresarDecimal(float min, float max) {
				// TODO Auto-generated method stub
				boolean error = false;
				float opc = 0;
				
				do {
					error = false;
					try {
						opc = s.nextFloat();
						if(opc < min || opc > max) {
							error = true;
							System.out.println("El valor está fuera de los parametros. "
									+ "Ingrese un valor entre "
			                         + min + " y " + max);
						}
					}
					catch(InputMismatchException e) {
						System.out.println("tipo de dato mal ingresado. Ingrese un número");
						error = true;
						s.nextLine();
					}
					catch(Exception e) {
						System.out.println("Error inesperado "+ e);
						error = true;
						s.nextLine();
					}
				}while(error);
				s.nextLine();
				return opc;
			}
	
	//Salir del programa o reiniciarlo
	
	public static void opcSalirReiniciar() {
		System.out.println("Inicia el juego para volver intentarlo!");
		System.out.println("Saliendo del juego...");
		Utiles.delay(3999);
		System.exit(0);

	}
}