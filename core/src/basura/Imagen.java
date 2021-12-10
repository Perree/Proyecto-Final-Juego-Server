package basura;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.garaperree.guazoserver.utiles.Render;

public class Imagen {
	private Texture tex;
	private Sprite spr;
	
	public Imagen(String ruta){
		tex = new Texture(ruta);
		spr = new Sprite(tex);
	}
	
	public void dibujar(){
	spr.draw(Render.sb);
	}
	
	public void setTransparencia(float a) {
		spr.setAlpha(a);
	}
	
	public void setSize(float ancho, float alto) {
		spr.setSize(ancho, alto);
	}
	
	
}
