package com.garaperree.guazoserver.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.garaperree.guazoserver.GuazoServer;
import com.garaperree.guazoserver.diseños.Recursos;
import com.garaperree.guazoserver.diseños.Texto;
import com.garaperree.guazoserver.escenas.Hud;
import com.garaperree.guazoserver.sprites.Fumiko;
import com.garaperree.guazoserver.utiles.B2WorldCreator;
import com.garaperree.guazoserver.utiles.Global;
import com.garaperree.guazoserver.utiles.Render;
import com.garaperree.guazoserver.utiles.WorldContactListener;

public class PantallaJuego implements Screen{

	//Referenciar a nuestro Juego, para setear las pantallas
	private GuazoServer game;
	private TextureAtlas atlas;
	
	private OrthographicCamera gamecam;
	private Viewport gamePort;
	public Hud hud;
	
	// Variables del Tiled map
	private TmxMapLoader maploader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer; // dibuja el mapa
	
	//Variables de Box2d
	private World world;
	private Box2DDebugRenderer b2dr;
	
	//Referenciar a nuestro personaje principal (sprites)
	private Fumiko jugador1, jugador2;
	
	private Music music;
	
	public PantallaJuego(GuazoServer game) {
		
		atlas = new TextureAtlas("fumiko/personaje.atlas");
		
		this.game = game;
		
		// Crea una camara para seguir al personaje a travez del mundo
		gamecam = new OrthographicCamera(); 
		
		// Crea un FitViewport para mantenar el aspecto virtual
		gamePort = new FitViewport(GuazoServer.V_WIDTH/GuazoServer.PPM, GuazoServer.V_HEIGHT/GuazoServer.PPM, gamecam);
		
		// Crea una hud para puntos/tiempos/niveles
		hud = new Hud(game.batch); 
		
		//cargando el mapa
		maploader = new TmxMapLoader();
		map = maploader.load("mapas/nivel1/nivel1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1/GuazoServer.PPM);	
	
		// inicializando la camara del juego para poder estar centrado al comienzo
		gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		
		// Crea el box2d world, sin gravedad en el eje x, -10 en el eje, permitiendo que los cuerpos descansen
		world = new World(new Vector2(0, -15), true);
		
		// Permite hacer los debugs de nuestro box2d world
		b2dr = new Box2DDebugRenderer();
		
		new B2WorldCreator(this);
		
		// crear personajes en nuestro juego
		jugador1 = new Fumiko(this);
		
		jugador2 = new Fumiko(this);
		
		world.setContactListener(new WorldContactListener());
		
		music = GuazoServer.manager.get("audio/music/MatWyre_Deep_Dawn.mp3", Music.class);
		music.setLooping(true);
		music.setVolume(0.1f);
//		music.play();
		
	}

	
	public TextureAtlas getAtlas() {
		return atlas;
	}
	
	@Override
	public void show() {
		
		
	}
	
	//mover la posicion de la camara hacia la derecha
	private void handleInput(float dt) {
		
		// controlar a nuestro jugador mediante impulsos
		if(jugador1.currentState != Fumiko.State.DEAD) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
				jugador1.jump();
			
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && jugador1.b2body.getLinearVelocity().x <=2)
				jugador1.b2body.applyLinearImpulse(new Vector2(0.1f, 0),jugador1.b2body.getWorldCenter(), true);
			
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && jugador1.b2body.getLinearVelocity().x >=-2)
				jugador1.b2body.applyLinearImpulse(new Vector2(-0.1f, 0),jugador1.b2body.getWorldCenter(), true);
		}
		
		if(jugador2.currentState != Fumiko.State.DEAD) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.W))
				jugador2.jump();
			
			if(Gdx.input.isKeyPressed(Input.Keys.D) && jugador2.b2body.getLinearVelocity().x <=2)
				jugador2.b2body.applyLinearImpulse(new Vector2(0.1f, 0),jugador2.b2body.getWorldCenter(), true);
			
			if(Gdx.input.isKeyPressed(Input.Keys.A) && jugador2.b2body.getLinearVelocity().x >=-2)
				jugador2.b2body.applyLinearImpulse(new Vector2(-0.1f, 0),jugador2.b2body.getWorldCenter(), true);
		}
		
	}
	
	public void update(float dt) {
		// maneja la entradas del usuario
		handleInput(dt);
		 
		// toma 1 paso en fisicas (60 veces por segundo)
		world.step(1/60f, 6, 2);
		
		jugador1.update(dt);
		jugador2.update(dt);
		hud.update(dt);
		
		//jugador 1
		
		//Cuando el personaje se cae en la lava
		if (jugador1.getY() < 0) {
			//sonido
			GuazoServer.manager.get("audio/music/MatWyre_Deep_Dawn.mp3", Music.class).stop();
//			GuazoServer.manager.get("audio/sfx/muere.wav", Sound.class).play();
			jugador1.currentState = Fumiko.State.DEAD;
		}
		
		// Pinches 1
		if((jugador1.getX() > 2.42f && jugador1.getY() >= 4.50f) && 
				(jugador1.getX() <= 2.81f && jugador1.getY() <= 5.15f)) {
			GuazoServer.manager.get("audio/music/MatWyre_Deep_Dawn.mp3", Music.class).stop();
//			GuazoServer.manager.get("audio/sfx/muere.wav", Sound.class).play();
			jugador1.currentState = Fumiko.State.DEAD;
		}
		
		// Pinches 2
		if((jugador1.getX() >= 4.9895763 && jugador1.getY() >= 4.98f) && 
				(jugador1.getX() <= 6.335001 && jugador1.getY() <= 4.99f)) {
			GuazoServer.manager.get("audio/music/MatWyre_Deep_Dawn.mp3", Music.class).stop();
//			GuazoServer.manager.get("audio/sfx/muere.wav", Sound.class).play();
			jugador1.currentState = Fumiko.State.DEAD;
		}
		
		// Pinches 3
		if((jugador1.getX() >= 5.12f && jugador1.getY() <= 1.5f) && 
				(jugador1.getX() <= 5.55f && jugador1.getY() >= 1.46f)) {
			GuazoServer.manager.get("audio/music/MatWyre_Deep_Dawn.mp3", Music.class).stop();
//			GuazoServer.manager.get("audio/sfx/muere.wav", Sound.class).play();
			jugador1.currentState = Fumiko.State.DEAD;
		}

		System.out.println("X: "+ jugador1.getX()+"Y: "+jugador1.getY());
		
		// Usamos la ubicacion del personaje para poder determinar la meta
		if ((jugador1.getX() <= 1.64f && jugador1.getY() >= 1.46f) &&
				(jugador1.getX() >= 1.32f && jugador1.getY() <= 1.6f)) {
			GuazoServer.manager.get("audio/music/MatWyre_Deep_Dawn.mp3", Music.class).stop();
//			GuazoServer.manager.get("audio/sounds/next_level.wav", Sound.class).play();
			jugador1.llegoSalida();
			
		}
		
		//jugador 2
		
		//Cuando el personaje se cae en la lava
		if (jugador2.getY() < 0) {
			GuazoServer.manager.get("audio/music/MatWyre_Deep_Dawn.mp3", Music.class).stop();
//			GuazoServer.manager.get("audio/sfx/muere.wav", Sound.class).play();
			jugador2.currentState = Fumiko.State.DEAD;
		}
		
		// Pinches 1
		if((jugador2.getX() > 2.42f && jugador2.getY() >= 4.50f) && 
				(jugador2.getX() <= 2.81f && jugador2.getY() <= 5.15f)) {
			GuazoServer.manager.get("audio/music/MatWyre_Deep_Dawn.mp3", Music.class).stop();
			GuazoServer.manager.get("audio/sfx/muere.wav", Sound.class).play();
			jugador2.currentState = Fumiko.State.DEAD;
		}
		
		// Pinches 2
		if((jugador2.getX() >= 4.9895763 && jugador2.getY() >= 4.98f) && 
				(jugador2.getX() <= 6.335001 && jugador2.getY() <= 4.99f)) {
			GuazoServer.manager.get("audio/music/MatWyre_Deep_Dawn.mp3", Music.class).stop();
//			GuazoServer.manager.get("audio/sfx/muere.wav", Sound.class).play();
			jugador2.currentState = Fumiko.State.DEAD;
		}
		
		// Pinches 3
		if((jugador2.getX() >= 5.12f && jugador2.getY() <= 1.5f) && 
				(jugador2.getX() <= 5.55f && jugador2.getY() >= 1.46f)) {
			GuazoServer.manager.get("audio/music/MatWyre_Deep_Dawn.mp3", Music.class).stop();
//			GuazoServer.manager.get("audio/sfx/muere.wav", Sound.class).play();
			jugador2.currentState = Fumiko.State.DEAD;
		}

		System.out.println("X: "+ jugador2.getX()+"Y: "+jugador2.getY());
		
		// Usamos la ubicacion del personaje para poder determinar la meta
		if ((jugador2.getX() <= 1.64f && jugador2.getY() >= 1.46f) &&
				(jugador2.getX() >= 1.32f && jugador2.getY() <= 1.6f)) {
			//sonido
			GuazoServer.manager.get("audio/music/MatWyre_Deep_Dawn.mp3", Music.class).stop();
//			GuazoServer.manager.get("audio/sounds/next_level.wav", Sound.class).play();
			jugador2.llegoSalida();
		}
		
		//Meta
		//X: 1.649973Y: 1.4649998
		//X: 1.3461664Y: 1.465
		
		//Pinches 1
		//X: 2.4297383Y: 4.9849987
		//X: 2.8150024Y: 4.984998
		
		//Pinches 2
		//X: 4.9849987 Y: 4.9849997
		//X: 6.335001 Y: 4.9849997

		//Pinches 3
		//X: 5.1250024Y: 4.9849987
		//X: 5.5542116Y: 1.4649998
		
		//Sigue la camara del jugador (no lo necesitamos)
//		gamecam.position.x = fumiko.b2body.getPosition().x;
		
		//actualiza la camara del juego con las coordenadas correctas despues de hacer los cambios
		gamecam.update();
		
		//renderiza lo que la camara puede ver
		renderer.setView(gamecam);
	}


	@Override
	public void render(float delta) {
		
		//separa la actualizacion logica del renderizado
		update(delta);
		
		// Limpear pantalla con negro
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
		
		//renderizar el mapa del juego
		renderer.render();
		
		//renderizar Box2DDebugRenderer
		b2dr.render(world, gamecam.combined);
		
		game.batch.setProjectionMatrix(gamecam.combined);
		game.batch.begin();
		jugador1.draw(game.batch);
		jugador2.draw(game.batch);
		game.batch.end();
		
		// setea el batch para dibujar el hud
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		
		// si el tiempo se acaba, se termina el juego
		if(hud.getWorldTimer()==0) {
			finishing();
		}
		
		// Llego a la meta GANO!
		if(jugador1.isPuedeSalir()) {
			String nombre = "Jugador 1";
			finishing(nombre);
		}
		
		// Llego a la meta GANO!
		if(jugador2.isPuedeSalir()) {
			String nombre = "Jugador 2";
			finishing(nombre);
		}
		
		//El personaje perdio
		if(FinJuego()) {
			finishing();
		}
	}

	public void finishing() {
		game.setScreen(new FinDelJuego(game));
		dispose();
	}
	
	public void finishing(String nombre) {
		game.setScreen(new FinDelJuego(game, nombre));
		dispose();
	}
	
	// Se corrobora que si el estado del jugador esta muerto y el tiempo
	public boolean FinJuego() {
		if (jugador1.currentState == Fumiko.State.DEAD) {
			return true;
		}else if (jugador2.currentState == Fumiko.State.DEAD){
			return true;
		}
		return false;
	}
	
	@Override
	public void resize(int width, int height) {
		//actualizar nuestro viewport game
		gamePort.update(width, height);
		
	}
	
	public TiledMap getMap() {
		return map;
	}
	
	public World getWorld() {
		return world;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		hud.dispose();
	}
	
	public Hud getHud() {
		return hud;
	}
	
	
}


