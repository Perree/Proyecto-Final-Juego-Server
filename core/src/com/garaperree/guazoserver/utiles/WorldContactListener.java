package com.garaperree.guazoserver.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.garaperree.guazoserver.GuazoServer;
import com.garaperree.guazoserver.objetos.ObjetosInteractivos;
import com.garaperree.guazoserver.sprites.Fumiko;

public class WorldContactListener implements ContactListener {
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		
		switch (cDef){
	        case GuazoServer.POR_DEBAJO_BIT | GuazoServer.META_BIT:
	        case GuazoServer.POR_DEBAJO_BIT | GuazoServer.PINCHES_BIT:
	        case GuazoServer.POR_DEBAJO_BIT | GuazoServer.LAVA_BIT:
	        	if(fixA.getFilterData().categoryBits == GuazoServer.POR_DEBAJO_BIT)
	        		((ObjetosInteractivos) fixB.getUserData()).contactColision((Fumiko) fixA.getUserData());
	            else
	            	((ObjetosInteractivos) fixA.getUserData()).contactColision((Fumiko) fixB.getUserData());
	            break;
	        case GuazoServer.DERECHA_BIT| GuazoServer.META_BIT:
	        case GuazoServer.DERECHA_BIT| GuazoServer.PINCHES_BIT:
	        case GuazoServer.DERECHA_BIT| GuazoServer.LAVA_BIT: 
	        	if(fixA.getFilterData().categoryBits == GuazoServer.DERECHA_BIT)
	        		((ObjetosInteractivos) fixB.getUserData()).contactColision((Fumiko) fixA.getUserData());
	            else
	            	((ObjetosInteractivos) fixA.getUserData()).contactColision((Fumiko) fixB.getUserData());
	            break;
	        case GuazoServer.IZQUIERDA_BIT | GuazoServer.META_BIT:
	        case GuazoServer.IZQUIERDA_BIT | GuazoServer.PINCHES_BIT:
	        case GuazoServer.IZQUIERDA_BIT | GuazoServer.LAVA_BIT:
	            if(fixA.getFilterData().categoryBits == GuazoServer.IZQUIERDA_BIT)
	            	((ObjetosInteractivos) fixB.getUserData()).contactColision((Fumiko) fixA.getUserData());
	            else
	            	((ObjetosInteractivos) fixA.getUserData()).contactColision((Fumiko) fixB.getUserData());
	            break;
		}
	}

	@Override
	public void endContact(Contact contact) {
		Gdx.app.log("End Contact", "");
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
