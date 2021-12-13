package com.garaperree.guazoserver.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.garaperree.guazoserver.GuazoServer;
import com.garaperree.guazoserver.diseños.Recursos;
import com.garaperree.guazoserver.diseños.Texto;
import com.garaperree.guazoserver.escenas.Hud;
import com.garaperree.guazoserver.io.JuegoEventListener;
import com.garaperree.guazoserver.objetos.B2WorldCreator;
import com.garaperree.guazoserver.servidor.Servidor;
import com.garaperree.guazoserver.sprites.Fumiko;
import com.garaperree.guazoserver.utiles.Render;
import com.garaperree.guazoserver.utiles.Utiles;
import com.garaperree.guazoserver.utiles.WorldContactListener;

public class PantallaJuego implements Screen, JuegoEventListener{
	//Referenciar a nuestro Juego, para setear las pantallas
	private GuazoServer game;
	private TextureAtlas atlas;
	
	// Red
	private Servidor servidor;
	
	// Booleanos para la red
	private boolean empieza = false;
	
	// Diseños
	private Texto espera;
	
	// Control de camara
	private OrthographicCamera gamecam;
	private Viewport gamePort;
	
	// Overlays
	private Hud hud;
	
	// Variables del Tiled map
	private TmxMapLoader maploader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer; // Dibuja el mapa
	
	// Variables de Box2d
	private World world;
	private Box2DDebugRenderer b2dr;
	
	// Referenciar a nuestro personaje principal (sprites)
	private Fumiko jugador1, jugador2;
	
	public PantallaJuego(GuazoServer game) {
		this.game = game;
		
		// Carga las texturas del personaje
		atlas = new TextureAtlas("fumiko/personaje.atlas");
		
		// Crea una camara para seguir al personaje a travez del mundo
		gamecam = new OrthographicCamera(); 
		
		// Crea un FitViewport para mantenar el aspecto virtual
		gamePort = new FitViewport(GuazoServer.V_WIDTH/GuazoServer.PPM, GuazoServer.V_HEIGHT/GuazoServer.PPM, gamecam);
		
		// Crea una hud para puntos/tiempos/niveles
		hud = new Hud(game.batch); 
		
		// Cargando el mapa
		maploader = new TmxMapLoader();
		map = maploader.load("mapas/nivel1/nivel1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1/GuazoServer.PPM);	
	
		// Inicializando la camara del juego para poder estar centrado al comienzo
		gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		
		// Crea el box2d world, sin gravedad en el eje x, -10 en el eje, permitiendo que los cuerpos descansen
		world = new World(new Vector2(0, -15), true);
		
		// Permite hacer los debugs de nuestro box2d world
		b2dr = new Box2DDebugRenderer();
		
		// Crea los objetos para poder ser colisionados 
		new B2WorldCreator(this);
		
		// Crear personajes en nuestro juego
		jugador1 = new Fumiko(this);
		jugador2 = new Fumiko(this);
		
		// Texto para la conexion
		espera = new Texto(Recursos.FUENTE, 100, Color.WHITE, false);
		espera.setTexto("Esperando jugadores...");
		espera.setPosition((GuazoServer.V_WIDTH/2)-(espera.getAncho()/2), (GuazoServer.V_HEIGHT/2)+(espera.getAlto()/2));
		
		Utiles.listener = this;
		
		// Hilo Servidor 
		servidor = new Servidor();
		
		world.setContactListener(new WorldContactListener());		
	}

	
	public TextureAtlas getAtlas() {
		return atlas;
	}
	
	@Override
	public void show() {
	}
	
	// Controlar jugador
	private void handleInput(float dt) {
		
		if(jugador1.mueveArriba) {
			if(jugador1.currentState != Fumiko.State.DEAD) {
				jugador1.jump(); 
				servidor.enviarATodos("coordenadas!p1!"+jugador1.getY()); // enviamos las coordenadas a los jugadores
			}
		}
		
		if(jugador1.mueveIzquierda) {
			if (jugador1.b2body.getLinearVelocity().x >=-2 
					&& jugador1.currentState != Fumiko.State.DEAD) {
				jugador1.left();
				servidor.enviarATodos("coordenadas!p1!"+jugador1.getX());
			}
		}
		
		if(jugador1.mueveDerecha) {
			if (jugador1.b2body.getLinearVelocity().x <=2 
					&& jugador1.currentState != Fumiko.State.DEAD) {
				jugador1.right();
				servidor.enviarATodos("coordenadas!p1!"+jugador1.getX());
			}
		}
		
		if(jugador2.mueveArriba) {
			if(jugador2.currentState != Fumiko.State.DEAD) {
				jugador2.jump();
				servidor.enviarATodos("coordenadas!p2!"+jugador2.getY());
			}
			
		}
		
		if(jugador2.mueveIzquierda) {
			if (jugador2.b2body.getLinearVelocity().x >=-2 
					&& jugador1.currentState != Fumiko.State.DEAD) {
				jugador2.left();
				servidor.enviarATodos("coordenadas!p2!"+jugador2.getX());
			}
		}
		
		if(jugador2.mueveDerecha) {
			if (jugador2.b2body.getLinearVelocity().x <=2 
					&& jugador2.currentState != Fumiko.State.DEAD) {
				jugador2.right();
				servidor.enviarATodos("coordenadas!p2!"+jugador2.getX());
			}
			
		}
		
		// controlar a nuestro jugador mediante impulsos
//		if(jugador1.currentState != Fumiko.State.DEAD) {
//			if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
//				jugador1.jump();
//			
//			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && jugador1.b2body.getLinearVelocity().x <=2)
//				jugador1.b2body.applyLinearImpulse(new Vector2(0.1f, 0),jugador1.b2body.getWorldCenter(), true);
//			
//			if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && jugador1.b2body.getLinearVelocity().x >=-2)
//				jugador1.b2body.applyLinearImpulse(new Vector2(-0.1f, 0),jugador1.b2body.getWorldCenter(), true);
//		}
	}
	
	public void update(float dt) {
		// Maneja la entradas del usuario
		handleInput(dt);
		 
		// Toma 1 paso en fisicas (60 veces por segundo)
		world.step(1/60f, 6, 2);
		
		jugador1.update(dt);
		jugador2.update(dt);
		hud.update(dt);
		
		jugadorGanaMuere();
		
		//Sigue la camara del jugador (no lo necesitamos)
//		gamecam.position.x = fumiko.b2body.getPosition().x;
		
		//actualiza la camara del juego con las coordenadas correctas despues de hacer los cambios
		gamecam.update();
		
		//renderiza lo que la camara puede ver
		renderer.setView(gamecam);
	}


	private void jugadorGanaMuere() {
		// Cuando el personaje se cae en la lava
		if (jugador1.getY() < 0) {
			jugador1.currentState = Fumiko.State.DEAD;
		}
		
		// Pinches 1
		if((jugador1.getX() > 2.42f && jugador1.getY() >= 4.50f) && 
				(jugador1.getX() <= 2.81f && jugador1.getY() <= 5.15f)) {
			jugador1.currentState = Fumiko.State.DEAD;
		}
		
		// Pinches 2
		if((jugador1.getX() >= 4.9895763 && jugador1.getY() >= 4.98f) && 
				(jugador1.getX() <= 6.335001 && jugador1.getY() <= 4.99f)) {
			jugador1.currentState = Fumiko.State.DEAD;
		}
		
		// Pinches 3
		if((jugador1.getX() >= 5.12f && jugador1.getY() <= 1.5f) && 
				(jugador1.getX() <= 5.55f && jugador1.getY() >= 1.46f)) {
			jugador1.currentState = Fumiko.State.DEAD;
		}
		
		// Usamos la ubicacion del personaje para poder determinar la meta
		if ((jugador1.getX() <= 1.64f && jugador1.getY() >= 1.46f) &&
				(jugador1.getX() >= 1.32f && jugador1.getY() <= 1.6f)) {
			jugador1.llegoSalida();
			servidor.enviarATodos("termino!1");
		}
		
		//Cuando el personaje se cae en la lava
		if (jugador2.getY() < 0) {
			jugador2.currentState = Fumiko.State.DEAD;
		}
		
		// Pinches 1
		if((jugador2.getX() > 2.42f && jugador2.getY() >= 4.50f) && 
				(jugador2.getX() <= 2.81f && jugador2.getY() <= 5.15f)) {
			jugador2.currentState = Fumiko.State.DEAD;
		}
		
		// Pinches 2
		if((jugador2.getX() >= 4.9895763 && jugador2.getY() >= 4.98f) && 
				(jugador2.getX() <= 6.335001 && jugador2.getY() <= 4.99f)) {
			jugador2.currentState = Fumiko.State.DEAD;
		}
		
		// Pinches 3
		if((jugador2.getX() >= 5.12f && jugador2.getY() <= 1.5f) && 
				(jugador2.getX() <= 5.55f && jugador2.getY() >= 1.46f)) {
			jugador2.currentState = Fumiko.State.DEAD;
		}
		
		// Usamos la ubicacion del personaje para poder determinar la meta
		if ((jugador2.getX() <= 1.64f && jugador2.getY() >= 1.46f) &&
				(jugador2.getX() >= 1.32f && jugador2.getY() <= 1.6f)) {
			jugador2.llegoSalida();
			servidor.enviarATodos("termino!2");
		}	
	}


	@Override
	public void render(float delta) {
		Render.limpiarPantalla();
		if(!empieza) {
			Render.begin();
			espera.dibujar();
			Render.end();
		}else {
			// Separa la actualizacion logica del renderizado
			update(delta);
			
			// Limpiar pantalla con negro
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
			
			// Renderizar el mapa del juego
			renderer.render();
			
			// Renderizar Box2DDebugRenderer
			b2dr.render(world, gamecam.combined);
			
			game.batch.setProjectionMatrix(gamecam.combined);
			game.batch.begin();
			jugador1.draw(game.batch);
			jugador2.draw(game.batch);
			game.batch.end();
			
			// Setea el batch para dibujar el hud
			game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
			hud.stage.draw();
			
			// Si el tiempo se acaba, se termina el juego
			if(hud.getWorldTimer()==0) {
				acaboTiempo();
			}
			
			// Llego a la meta GANO!
			if(jugador1.isPuedeSalir()) {
				ganadorJugador();
			}
			
			// Llego a la meta GANO!
			if(jugador2.isPuedeSalir()) {
				ganadorJugador();
			}
			
			// El personaje perdio
			if(FinJuego()) {
				perdedor();
			}
		}
	}
	
	// Se termino el tiempo
	public void acaboTiempo() {
		servidor.enviarATodos("acaboTiempo");
		game.setScreen(new AcaboTiempo(game));
		dispose();
	}

	// Un jugador ha muerto y por lo tanto ha ganado el otro
	public void perdedor() {
		game.setScreen(new PerdioJuego(game));
		dispose();
	}
	
	// Un jugador ha ganado
	public void ganadorJugador() {
		game.setScreen(new FinDelJuego(game));
		dispose();
	}
	
	// Se corrobora que si el estado del jugador esta muerto
	public boolean FinJuego() {
		if (jugador1.currentState == Fumiko.State.DEAD) {
			servidor.enviarATodos("termino!2");
			return true;
		}else if (jugador2.currentState == Fumiko.State.DEAD){
			servidor.enviarATodos("termino!1");
			return true;
		}
		return false;
	}
	
	@Override
	public void resize(int width, int height) {
		// Actualizar nuestro viewport game
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
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
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


	@Override
	public boolean handle(Event event) {
		return false;
	}


	@Override
	public void empezar() {
		empieza = true;
	}

	@Override
	public void apretoTecla(int nroPlayer, String tecla) {
		if(nroPlayer==1) {
			if(tecla.equals("Arriba")) {
				jugador1.mueveArriba=true;
			}
			
			if(tecla.equals("Izquierda")) {
				jugador1.mueveIzquierda=true;
			}
			
			if(tecla.equals("Derecha")) {
				jugador1.mueveDerecha=true;
			}
		} else {
			if(tecla.equals("Arriba")) {
				jugador2.mueveArriba=true;
			}
			
			if(tecla.equals("Izquierda")) {
				jugador2.mueveIzquierda=true;
			}
			
			if(tecla.equals("Derecha")) {
				jugador2.mueveDerecha=true;
			}
		}
	}


	@Override
	public void soltoTecla(int nroPlayer, String tecla) {
		if(nroPlayer==1) {
			if(tecla.equals("Arriba")) {
				jugador1.mueveArriba=false;
			}
			
			if(tecla.equals("Izquierda")) {
				jugador1.mueveIzquierda=false;
			}
			
			if(tecla.equals("Derecha")) {
				jugador1.mueveDerecha=false;
			}
		} else {
			if(tecla.equals("Arriba")) {
				jugador2.mueveArriba=false;
			}
			
			if(tecla.equals("Izquierda")) {
				jugador2.mueveIzquierda=false;
			}
			
			if(tecla.equals("Derecha")) {
				jugador2.mueveDerecha=false;
			}
		}
	}
}


