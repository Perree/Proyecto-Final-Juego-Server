package com.garaperree.guazoserver.servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.garaperree.guazoserver.utiles.Global;

public class HiloServidor extends Thread{
	
	private DatagramSocket conexion;
	private boolean fin = false;
	private DireccionRed[] clientes = new DireccionRed[2];
	private int cantClientes = 0;
	
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
				clientes[cantClientes] = new DireccionRed(dp.getAddress(), dp.getPort());
				enviarMensaje("Ok", clientes[cantClientes].getIp(), clientes[cantClientes++].getPuerto());
				if(cantClientes==2) {
					Global.empieza = true;
					for(int i = 0; i < clientes.length; i++) {
						enviarMensaje("Empieza", clientes[i].getIp(), clientes[i].getPuerto());
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

	
	
}
