package com.garaperree.guazoserver.servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.garaperree.guazoserver.pantallas.PantallaJuego;
import com.garaperree.guazoserver.utiles.Global;

public class HiloServidor extends Thread{
	
	private DatagramSocket conexion;
	private boolean fin = false;
	private DireccionRed[] clientes = new DireccionRed[2];
	private int cantClientes = 0;
	private PantallaJuego app;
	
	public HiloServidor(PantallaJuego app) {
		this.app = app;
		
		try {
			conexion = new DatagramSocket(8080);
		} catch (SocketException e) {
			e.printStackTrace();
		} 
	}
	
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
		int nroCliente = -1;
		
		if(cantClientes>1) {
			for (int i = 0; i < clientes.length; i++) {
				if(dp.getPort()==clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())) {
					nroCliente=i;
				}
			}
		}
		
		if(cantClientes<2) {
			if(msg.equals("Conexion")) {
				System.out.println("Llega msg conexion cliente " + cantClientes);
				if(cantClientes<2) {
					clientes[cantClientes] = new DireccionRed(dp.getAddress(), dp.getPort());
					enviarMensaje("Ok"+(nroCliente+1), clientes[cantClientes].getIp(), clientes[cantClientes++].getPuerto());
					if(cantClientes==2) {
						Global.empieza = true;
						for(int i = 0; i < clientes.length; i++) {
							enviarMensaje("Empieza", clientes[i].getIp(), clientes[i].getPuerto());
						}
					}
				} 
			} 
		} else {
			if(nroCliente!=-1) {
				if (msg.equals("ApreteArriba")){
					if(nroCliente==0) {
						app.isUp1 = true;
					} else {
						app.isUp2 = true;
					}
					
				} else if(msg.equals("ApreteDerecha")) {
					if(nroCliente==0) {
						app.isRight1 = true;
					} else {
						app.isRight2 = true;
					}
					
				} else if(msg.equals("ApreteIzquierda")) {
					if(nroCliente==0) {
						app.isLeft1 = true;
					} else {
						app.isLeft2 = true;
					}	
				}
				
				else if(msg.equals("NoApreteIzquierda")) {
					if(nroCliente==0) {
						app.isLeft1 = false;
					} else {
						app.isLeft2 = false;
					}	
				}
			
				else if(msg.equals("NoApreteDerecha")) {
					if(nroCliente==0) {
						app.isRight1 = false;
					} else {
						app.isRight2 = false;
					}
				} 
				
				else if (msg.equals("NoApreteArriba")){
					if(nroCliente==0) {
						app.isUp1 = false;
					} else {
						app.isUp2 = false;
					}
				}
			} 
		}
	}
	
	public void enviarMensaje(String msg, InetAddress ip, int puerto) {
		byte[] data = msg.getBytes();
		try {
			DatagramPacket dp = new DatagramPacket(data, data.length, ip, puerto);
			conexion.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enviarMensajeATodos(String msg) {
		for (int i = 0; i < clientes.length; i++) {
			enviarMensaje(msg, clientes[i].getIp(), clientes[i].getPuerto());
		}
	}

	
	
}
