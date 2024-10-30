package com.backlogged.univercity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen implements Screen {
    private Game game;
    private Skin skin;
    private Stage stage;
    private Table table;
    private Label gameOverLabel;
    private TextButton startAgainButton;
    private TextButton quitButton;

    public GameOverScreen(Game game) {
        this.game = game;

        skin = new Skin(Gdx.files.local("testskin.json"));
        stage = new Stage(new ScreenViewport());
        table = new Table(skin);
        table.setFillParent(true);
        table.setDebug(true);

        stage.addActor(table);

        gameOverLabel = new Label("Game Over!", skin, "game-over");

        startAgainButton = new TextButton("Try again!", skin, "red-text-button");
        startAgainButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new MapScreen(game));
            }
        });

        quitButton = new TextButton("Quit", skin, "red-text-button");
        quitButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(gameOverLabel).top().padTop(100);
        table.row();
        table.add(startAgainButton).top().padTop(100).width(Value.percentWidth(0.3f, table)).height(Value.percentHeight(0.1f, table));
        table.row();
        table.add(quitButton).top().padTop(50).width(Value.percentWidth(0.3f, table)).height(Value.percentHeight(0.1f, table));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameOverLabel.setFontScale(width / 1000f);
        startAgainButton.getStyle().font.getData().setScale(width / 3000f);
        stage.getViewport().update(width, height, true);
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

    }
}
