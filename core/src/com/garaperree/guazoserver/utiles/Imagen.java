package com.garaperree.guazoserver.utiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Imagen {
	private Texture tex;
	private Sprite spr;
	
	public Imagen(String ruta){
		tex = new Texture(ruta);
		spr = new Sprite(tex);
	}
	
	public void dibujar(){
	spr.draw(Render.batch);
	}
	
	public void setTransparencia(float a) {
		spr.setAlpha(a);
	}
	
	public void setSize(float ancho, float alto) {
		spr.setSize(ancho, alto);
	}
	
	
}
