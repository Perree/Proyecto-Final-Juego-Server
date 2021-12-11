package com.garaperree.guazoserver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.garaperree.guazoserver.diseños.Texto;
import com.garaperree.guazoserver.pantallas.PantallaJuego;
import com.garaperree.guazoserver.servidor.HiloServidor;
import com.garaperree.guazoserver.utiles.Global;
import com.garaperree.guazoserver.utiles.Render;

public class GuazoServer extends Game {

	// Dimensiones pixeles por metros
	public static final int V_WIDTH = 1024;
	public static final int V_HEIGHT = 768;
	public static final float PPM = 100;
	
	//Box2D Collision Bits
	public static final short NOTHING_BIT = 0;
	public static final short DEFAULT_BIT = 1;
	public static final short FUMIKO_BIT = 2;
	public static final short META_BIT = 4;
	public static final short PINCHES_BIT = 8;
	public static final short LAVA_BIT = 16;
	public static final short DESTROYED_BIT = 32;
	public static final short OBJECT_BIT = 64;
	public static final short DERECHA_BIT = 128;
	public static final short IZQUIERDA_BIT = 256;
	public static final short POR_DEBAJO_BIT = 512;
	
	public SpriteBatch batch;
	
	// Diseños
	private Texto espera;
	
	// Red
	private HiloServidor hs;
	
	private PantallaJuego app;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		// Texto para la conexion
//		espera = new Texto(Recursos.FUENTE, 100, Color.WHITE, false);
//		espera.setTexto("Esperando jugadores...");
//		espera.setPosition((Config.ANCHO/2)-(espera.getAncho()/2), (Config.ALTO/2)+(espera.getAlto()/2));
		
		// Hilo servidor
//		hs = new HiloServidor();
//		hs.start();
		
		setScreen(new PantallaJuego(this, hs));
	}

	@Override
	public void render () {
		Render.limpiarPantalla();
		if(!Global.empieza) {
			System.out.println("Esperando a que empiece la partida");
//			Render.begin();
//			espera.dibujar();
//			Render.end();
		}else {
			// delegar el metodo de render para la pantalla del juego
			super.render(); 
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}
}
