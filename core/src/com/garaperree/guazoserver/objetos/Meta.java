package com.garaperree.guazoserver.objetos;

import com.badlogic.gdx.maps.MapObject;
import com.garaperree.guazoserver.GuazoServer;
import com.garaperree.guazoserver.pantallas.PantallaJuego;
import com.garaperree.guazoserver.sprites.Fumiko;

public class Meta extends ObjetosInteractivos {

	public Meta(PantallaJuego screen, MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(GuazoServer.META_BIT);
	}

	@Override
	public void contactColision(Fumiko fumiko) {
		setCategoryFilter(GuazoServer.DESTROYED_BIT);
	}

}
