package com.garaperree.guazoserver.io;

import com.badlogic.gdx.scenes.scene2d.EventListener;

public interface JuegoEventListener extends EventListener {

	public void empezar();
	public void apretoTecla(int nroPlayer, String tecla);
	public void soltoTecla(int nroPlayer, String tecla);

//	public void asignarJugador(int jugador);
//	public void asignarCoordenadas(int nroJugador, float coordenadas);
//	public void actualizarPelota(float posX, float posY);
//	public void actualizarPuntaje(int nroJugador);
//	public void terminoJuego(int nroJugador);
}
