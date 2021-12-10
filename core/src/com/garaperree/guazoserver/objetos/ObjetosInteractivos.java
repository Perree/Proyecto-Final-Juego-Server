package com.garaperree.guazoserver.objetos;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.garaperree.guazoserver.GuazoServer;
import com.garaperree.guazoserver.pantallas.PantallaJuego;
import com.garaperree.guazoserver.sprites.Fumiko;

public abstract class ObjetosInteractivos {
	protected World world;
	protected TiledMap map;
	protected TiledMapTile tile;
	protected Rectangle bounds;
	protected Body body;
	protected Fixture fixture;
	protected PantallaJuego screen;
	protected MapObject object;
	
	// Definimos los objetos con los cuales el personaje va a interactuar
	public ObjetosInteractivos(PantallaJuego screen, MapObject object) {
		this.object = object;
        this.screen = screen;
		this.world = screen.getWorld();
		this.map = screen.getMap();
		this.bounds = ((RectangleMapObject) object).getRectangle();
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bounds.getX() + bounds.getWidth()/2)/GuazoServer.PPM, (bounds.getY() + bounds.getHeight()/2)/GuazoServer.PPM);
		
		body = world.createBody(bdef);
		
		shape.setAsBox(bounds.getWidth()/2/GuazoServer.PPM, bounds.getHeight()/2/GuazoServer.PPM);
		fdef.shape = shape;
		fixture = body.createFixture(fdef);
	}
	
	// Metodo que detecta cuando hay colisions
	public abstract void contactColision(Fumiko fumiko);
	
	// Seteamos el filtro
	public void setCategoryFilter(short filterBit) {
		Filter filter = new Filter();
		filter.categoryBits = filterBit;
		fixture.setFilterData(filter);
	}
	
}
