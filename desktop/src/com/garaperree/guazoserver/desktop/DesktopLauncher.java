package com.garaperree.guazoserver.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.garaperree.guazoserver.GuazoServer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GuazoServer.V_WIDTH;
		config.height = GuazoServer.V_HEIGHT;
		config.title = "Server";
		new LwjglApplication(new GuazoServer(), config);
	}
}
