package com.garaperree.guazoserver.servidor;


public class Servidor {
	private HiloServidor hiloServidor;
		
		public Servidor() {
			hiloServidor = new HiloServidor();
			hiloServidor.start();
		}
	
		public void enviarATodos(String msg) {
			hiloServidor.enviarMensajeATodos(msg);
		}
}
