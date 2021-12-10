package com.garaperree.guazoserver.objetos;

import com.badlogic.gdx.maps.MapObject;
import com.garaperree.guazoserver.GuazoServer;
import com.garaperree.guazoserver.pantallas.PantallaJuego;
import com.garaperree.guazoserver.sprites.Fumiko;

public class Pinches extends ObjetosInteractivos{

	public Pinches(PantallaJuego screen, MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(GuazoServer.PINCHES_BIT);
	}

	@Override
	public void contactColision(Fumiko fumiko) {
		setCategoryFilter(GuazoServer.DESTROYED_BIT);
	}

}
