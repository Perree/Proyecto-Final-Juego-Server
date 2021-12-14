 package com.garaperree.guazoserver.servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.garaperree.guazoserver.utiles.Utiles;

public class HiloServidor extends Thread{
	
	private DatagramSocket conexion;
	private boolean fin = false;
	private int cantClientes = 0;
	private int maxClientes = 2;
	private DireccionRed[] clientes = new DireccionRed[maxClientes];
	
	public HiloServidor() {
		try {
			conexion = new DatagramSocket(8080);
		} catch (SocketException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void run() {
		do {
			byte[] data = new byte[1024];
			DatagramPacket dp = new DatagramPacket(data, data.length);
			try {
				conexion.receive(dp);
				procesarMensaje(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		} while(!fin);
	}
	
	private void procesarMensaje(DatagramPacket dp) { 
		String msg = (new String(dp.getData())).trim(); 
		
		System.out.println("Mensaje = "+ msg);
		
			if(msg.equals("Conexion")) { 
				
				System.out.println("Llega msg conexion cliente " + cantClientes);
				
				if(cantClientes<2) {
					this.clientes[cantClientes] = new DireccionRed(dp.getAddress(), dp.getPort());
					enviarMensaje("ConexionAceptada!"+(cantClientes+1), dp.getAddress(), dp.getPort());
					cantClientes++;  
					
					if(cantClientes==2) {
						Utiles.listener.empezar();
						enviarATodos("Empieza");
					}
				} 
			}  
			
			if(cantClientes==2) {
				int nroPlayer = obtenerNroPlayer(dp.getAddress(), dp.getPort());
				
				System.out.println(nroPlayer);
				
				if(msg.equals("ApretoArriba")) {
					Utiles.listener.apretoTecla(nroPlayer, "Arriba");
				}
				
				if(msg.equals("ApretoIzquierda")) {
					Utiles.listener.apretoTecla(nroPlayer, "Izquierda");
				}
				
				if(msg.equals("ApretoDerecha")) {
					Utiles.listener.apretoTecla(nroPlayer, "Derecha");
				}
				
				if(msg.equals("NoApretoArriba")) {
					Utiles.listener.soltoTecla(nroPlayer, "Arriba");
				}
				
				if(msg.equals("NoApretoIzquierda")) {
					Utiles.listener.soltoTecla(nroPlayer, "Izquierda");
				}
				
				if(msg.equals("NoApretoDerecha")) {
					Utiles.listener.soltoTecla(nroPlayer, "Derecha");
				}
			}
	}
	
	private int obtenerNroPlayer(InetAddress address, int port) {
		boolean fin = false;
		int i = 0;
		do {
			if(address.equals(this.clientes[i].getIp())&&(port == this.clientes[i].getPuerto())) {
				fin = true;
			}
			i++;
			if(i==this.clientes.length) {
				fin = true;
			}
		}while(!fin);
		return i;
	}

	public void enviarMensaje(String msg, InetAddress ip, int puerto) {
		byte[] data = msg.getBytes();
		DatagramPacket dp = new DatagramPacket(data, data.length, ip, puerto);
		try {
			conexion.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Envia mensaje a todos los clientes
	public void enviarATodos(String msg) {
		for (int i = 0; i < clientes.length; i++) {
			enviarMensaje(msg, clientes[i].getIp(), clientes[i].getPuerto());
		}
	}

	
	
}
