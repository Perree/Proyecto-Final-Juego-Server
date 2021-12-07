package com.garaperree.guazoserver.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.garaperree.guazoserver.utiles.Config;
import com.garaperree.guazoserver.GuazoServer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Config.ANCHO;
		config.height = Config.ALTO;
		config.title = "El mejor juego del mundo";
		new LwjglApplication(new GuazoServer(), config);
	}
}
