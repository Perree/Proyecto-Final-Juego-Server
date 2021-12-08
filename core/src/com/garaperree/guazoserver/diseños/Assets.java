package com.garaperree.guazoserver.diseños;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static BitmapFont font;
	private static final GlyphLayout gl = new GlyphLayout(); // Conocer el tamaño del texto para centrar
	
	public static Animation<AtlasRegion> Cromo;
	
	public static TextureRegion background;
	public static TextureRegion gameOver;
	public static TextureRegion getReady;
	public static TextureRegion tap;
	public static TextureRegion downPipe;
	public static TextureRegion unPipe;
	
	public static void load() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasregion.txt"));
		
		background = atlas.findRegion("merged-full-background");
		background = atlas.findRegion("merged-full-background");
	}
}
