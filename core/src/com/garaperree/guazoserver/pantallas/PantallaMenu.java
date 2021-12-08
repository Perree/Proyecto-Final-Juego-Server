package com.garaperree.guazoserver.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch.Config;
import com.garaperree.guazoserver.diseños.Imagen;
import com.garaperree.guazoserver.diseños.Recursos;
import com.garaperree.guazoserver.diseños.Texto;
import com.garaperree.guazoserver.io.Entradas;
import com.garaperree.guazoserver.utiles.*;


public class PantallaMenu extends InputAdapter implements Screen {
	Imagen fondo;
	SpriteBatch b;
	
	Texto opciones[] = new Texto[4];
	String textos[] = {"Nueva partida","Online","Opciones","salir"};

	
	int opc = 1;
	boolean mouseArriba = false;
	public float tiempo = 0;
	


	Entradas entradas = new Entradas(this);
	
	@Override
	public void show() {
			
		fondo = new Imagen(Recursos.FONDOMENU);
		fondo.setSize(Config.ANCHO, Config.ALTO);
		b = Render.batch;
		
		
		Gdx.input.setInputProcessor(entradas);
		int avance = 50;
		
		

		for (int i = 0; i < opciones.length; i++) {
			opciones[i] = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, true);
			opciones[i].setTexto(textos[i]);
			opciones[i].setPosition((Config.ANCHO / 2) - (opciones[i].getAncho() / 2),
					((Config.ALTO / 2) + (opciones[0].getAlto() / 2)) - ((opciones[i].getAlto()+(avance * i))));
		}
		
	}
	
	@Override
	public void render(float delta) { // Se llama 60 veces por seg
		b.begin();
		fondo.dibujar();
		for (int i = 0; i < opciones.length; i++) {
			opciones[i].dibujar();
		}
		
		b.end();
		
	
		tiempo += delta;
		
		if (entradas.isAbajo()) {
			if (tiempo > 0.1f) {
				tiempo = 0;
				opc++;
				if (opc > 4) {
					opc = 1;
				}
			}

		}
		if (entradas.isArriba()) {
			if (tiempo > 0.1f) {
				tiempo = 0;
				opc--;
				if (opc < 1) {
					opc = 4;
				}
			}

		}
		
		int cont = 0;
		for (int i = 0; i < opciones.length; i++) {
			if((entradas.getMouseX()>=opciones[i].getX()) && (entradas.getMouseX()<=(opciones[i].getX()+opciones[i].getAncho()))) {
				if((entradas.getMouseY()>=opciones[i].getY()-opciones[i].getAlto()) && (entradas.getMouseY()<=(opciones[i].getY()))) {
					opc=i+1;
					cont++;
				}
				
			}
			
		}
		
		if(cont>0) {
			mouseArriba = true;
		}else {
			mouseArriba = false;
		}
		
		for (int i = 0; i < opciones.length; i++) {
			if(i==(opc-1)) {
				opciones[i].setColor(Color.YELLOW);
			}else {
			opciones[i].setColor(Color.WHITE);	
			}
			
		}
		
		if(entradas.isEnter() || (entradas.isClick())) {
			if(((opc==1) && (entradas.isEnter())) || ((opc==1) && (entradas.isClick())&&(mouseArriba))) {
				Render.app.setScreen(new PantallaJuego());
			}else if(((opc==4) && (entradas.isEnter())) || ((opc==4) && (entradas.isClick())&&(mouseArriba))) {
				Gdx.app.exit();
			}
		}
		
			
	}
	
	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
//		stage.getViewport().update(width, height, true); //se actualiza el stage si el userinterface cambia de tamaño
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
