package com.garaperree.guazoserver.objetos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.garaperree.guazoserver.GuazoServer;
import com.garaperree.guazoserver.pantallas.PantallaJuego;
import com.garaperree.guazoserver.sprites.Fumiko;

public class Lava extends ObjetosInteractivos {

	public Lava(PantallaJuego screen, MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(GuazoServer.LAVA_BIT);
	}

	@Override
	public void contactColision(Fumiko fumiko) {
		Gdx.app.log("Lava", "Collision");
		setCategoryFilter(GuazoServer.DESTROYED_BIT);
	}

}
