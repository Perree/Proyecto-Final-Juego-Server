package com.garaperree.guazoserver.pantallas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.garaperree.guazoserver.GuazoServer;

public class FinDelJuego implements Screen {
	private Viewport viewport;
	private Stage stage;
	private Game game;

	public FinDelJuego(Game game) {
		this.game = game;

		viewport = new FitViewport(GuazoServer.V_WIDTH, GuazoServer.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, ((GuazoServer) game).batch);

		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

		Table table = new Table();
		table.center();
		table.setFillParent(true);

		Label finJuegoLabel = new Label("REINICIANDO SERVIDOR", font);
		Label quienGano = new Label(".......", font);
		Label juegarDeNuevoLabel = new Label("Aguarde unos segundos", font);

		table.add(finJuegoLabel).expandX();
		table.row();
		table.add(quienGano).expandX().padTop(10f);
		table.row();
		table.add(juegarDeNuevoLabel).expandX().padTop(10f);

		stage.addActor(table);
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		game.setScreen(new PantallaJuego((GuazoServer) game));
		dispose();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
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
		stage.dispose();
	}

}
