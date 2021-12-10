package com.garaperree.guazoserver.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.garaperree.guazoserver.GuazoServer;
import com.garaperree.guazoserver.diseños.Config;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Config.ANCHO;
		config.height = Config.ALTO;
		config.title = "Server";
		new LwjglApplication(new GuazoServer(), config);
	}
}
