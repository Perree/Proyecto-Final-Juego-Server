package com.garaperree.guazoserver.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.garaperree.guazoserver.GuazoServer;
import com.garaperree.guazoserver.pantallas.PantallaJuego;
import com.garaperree.guazoserver.servidor.Servidor;

public class Fumiko extends Sprite{
	
	public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD};
	
	public boolean fin = false;
	public boolean mueveArriba, mueveIzquierda, mueveDerecha;

	public State currentState;
	public State previousState;
	
	public World world;
	public Body b2body;
	
	private Animation<?> fumikoStand;
	private Animation<?> fumikoRun;
	private Animation<?> fumikoJump;
	
	private float stateTimer;
	
	private Servidor servidor;
	
	private int nroJugador;
	
	private boolean runningRight;
	private boolean fumikoIsDead;
	
	public Fumiko(PantallaJuego screen, int nroJugador, Servidor servidor) {
		super(screen.getAtlas().findRegion("fumiko"));
		this.nroJugador = nroJugador;
		this.servidor = servidor;
		this.world = screen.getWorld();
//		currentState = State.STANDING;
//		previousState = State.STANDING;
		stateTimer = 0;
		runningRight = true;
		
		// Metemos los frames de la animacion a un array
		Array<TextureRegion> frames = new Array<TextureRegion>();
		
		// Animacion Correr
		for (int i = 0; i < 6; i++) 
			frames.add(new TextureRegion(getTexture(), i * 48, 14, 52, 52));
			fumikoRun = new Animation<Object>(0.1f, frames);
			frames.clear();
		
		// Animacion Saltar
		for (int i = 6; i < 10; i++) 
			frames.add(new TextureRegion(getTexture(), i * 48, 14, 52, 52));
			fumikoJump = new Animation<Object>(0.1f, frames);
			frames.clear();
		
		// Animacion Parado
		for (int i = 10; i < 14; i++) 
			frames.add(new TextureRegion(getTexture(), i * 48, 14, 52, 52));	
			fumikoStand = new Animation<Object>(0.1f, frames);
			frames.clear();
		
		// Definimos a fumiko en box2d
		defineFumiko();
		
		// Seteamos los valores de posicion para fumiko
		setBounds(0, 0, 52 / GuazoServer.PPM, 52 / GuazoServer.PPM);
//		setRegion(fumikoStand);
	}
	
	// Con este metodo sabemos que esta haciendo el jugador (correr, saltar, etc)
	public State getState() {

		if(fumikoIsDead) 
			return State.DEAD;
		
		if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
			return State.JUMPING;
		
		else if(b2body.getLinearVelocity().y < 0) 
			return State.FALLING;
		
		else if(b2body.getLinearVelocity().x != 0)
			return State.RUNNING;
		
		else
			return State.STANDING;
	}
	
	// Se va actualizando la posicion del personaje
	public void update(float dt) {
		setPosition(b2body.getPosition().x - getWidth() /2,b2body.getPosition().y - getHeight() /2);
		this.setOriginCenter();
		setRegion(getFrame(dt));
		if(nroJugador==1) {
			servidor.enviarATodos("Animacion!1!"+ getState());
		}else {
			servidor.enviarATodos("Animacion!2!"+ getState());
		}
	}
	
	// Obtenego el frame exacto dependiendo lo que el jugador este haciendo
	public TextureRegion getFrame(float dt) {
		currentState = getState();
		
		TextureRegion region;
		switch(currentState) {
		case JUMPING:
			region = (TextureRegion) fumikoJump.getKeyFrame(stateTimer);
			break;
			
		case RUNNING:
			region = (TextureRegion) fumikoRun.getKeyFrame(stateTimer, true);
			break;
			
		default:
			region = (TextureRegion) fumikoStand.getKeyFrame(stateTimer);
			break;
		}
		
		// Controlar los lados del jugador 
		if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
			region.flip(true, false);
			runningRight = false;
		}
		else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
			region.flip(true, false);
			runningRight = true;
		}
		
		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
		
	}
	
	public void jump(){
        if (currentState != State.JUMPING) {
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }
	
	public void right(){
		b2body.applyLinearImpulse(new Vector2(0.1f, 0),b2body.getWorldCenter(), true);
    }
	
	public void left(){
		b2body.applyLinearImpulse(new Vector2(-0.1f, 0),b2body.getWorldCenter(), true);
    }

	private void defineFumiko() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(128/GuazoServer.PPM, 512/GuazoServer.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		
		// Cuan grande es el circulo
		shape.setRadius(12/GuazoServer.PPM);
		
		// Filtros
		fdef.filter.categoryBits = GuazoServer.FUMIKO_BIT;
		fdef.filter.maskBits = 
				GuazoServer.DEFAULT_BIT | 
				GuazoServer.META_BIT | 
				GuazoServer.PINCHES_BIT | 
				GuazoServer.LAVA_BIT | 
				GuazoServer.OBJECT_BIT;
		
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
		
		// Colision derecha
		EdgeShape derecha = new EdgeShape();
		derecha.set(new Vector2(11/ GuazoServer.PPM, -10/GuazoServer.PPM), 
				new Vector2(11/ GuazoServer.PPM, 10/GuazoServer.PPM));
		fdef.filter.categoryBits = GuazoServer.DERECHA_BIT;
		fdef.shape = derecha;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData(this);
		
		// Colision izquierda
		EdgeShape izquierda = new EdgeShape();
		izquierda.set(new Vector2(-11/ GuazoServer.PPM, 10/GuazoServer.PPM), 
				new Vector2(-11/ GuazoServer.PPM, -10/GuazoServer.PPM));
		fdef.filter.categoryBits = GuazoServer.IZQUIERDA_BIT;
		fdef.shape = izquierda;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData(this);
		
		// Colision abajo
		EdgeShape porDeBajo = new EdgeShape();
		porDeBajo.set(new Vector2(-2/ GuazoServer.PPM, -12/GuazoServer.PPM), 
				new Vector2(2/ GuazoServer.PPM, -12/GuazoServer.PPM));
		fdef.filter.categoryBits = GuazoServer.POR_DEBAJO_BIT;
		fdef.shape = porDeBajo;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData(this);
	}

	public float getStateTimer() {
		return stateTimer;
	}
	
	public void llegoSalida() {
		fin = true;
	}
	
	public Boolean isPuedeSalir() {
		return fin;
	}
	
	public boolean isDead() {
		return fumikoIsDead;
	}

}
