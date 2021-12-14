package com.garaperree.guazoserver.escenas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.garaperree.guazoserver.GuazoServer;

public class Hud implements Disposable {
	// Stage maneja la ventana gráfica (Viewport) y distribuye los eventos de
	// entrada.
	public Stage stage;

	// Administra una cámara y determina cómo se asignan las coordenadas mundiales
	// hacia y desde la pantalla.
	private Viewport viewport;

	// Marcadores y tiempos
	private Integer worldTimer;
	private static Integer nivel;
	private float timeCount;

	// Scene2d widgets
	private Label countdownLabel;
	private Label timeLabel;
	private static Label nivelLabel;
	private Label worldLabel;

	public Hud(SpriteBatch sb) {
		// Variables
		worldTimer = 50;
		timeCount = 0;
		nivel = 1;

		viewport = new FitViewport(GuazoServer.V_WIDTH, GuazoServer.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, sb);

		// Creando tabla
		Table table = new Table();
		table.top();
		table.setFillParent(true);

		// Creando los labels
		countdownLabel = new Label(String.format("%03d", worldTimer),
				new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		timeLabel = new Label("TIEMPO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		nivelLabel = new Label(String.format("%02d", nivel), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		worldLabel = new Label("NIVEL", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

		// Agregar los labels, padding top
		table.add(worldLabel).expandX().padTop(10);
		table.add(timeLabel).expandX().padTop(10);

		// Agrega una segundo fila
		table.row();
		table.add(nivelLabel).expandX();
		table.add(countdownLabel).expandX();

		// Agrega tabla al stage
		stage.addActor(table);

	}

	public void update(float dt) {
		// Disminuyendo el tiempo
		timeCount += dt;
		if (timeCount >= 1) {
			if (worldTimer > 0) {
				worldTimer--;
			}
			countdownLabel.setText(String.format("%02d", worldTimer));
			timeCount = 0;
		}
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	// Lo utilizo para poder obtener el tiempo
	public Integer getWorldTimer() {
		return worldTimer;
	}

}
