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

    public GameOverScreen(Game game) {
        this.game = game;
        skin = new Skin(Gdx.files.local("testskin.json"));
        stage = new Stage(new ScreenViewport());
        table = new Table(skin);
        gameOverLabel = new Label("Game Over!", skin, "no-background");
        startAgainButton = new TextButton("Try again!", skin);
        startAgainButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new MapScreen(game));
            }
        });
        table.add(gameOverLabel).space(20);
        table.row();
        table.add(startAgainButton).size(Value.percentWidth(.3f, table), Value.percentHeight(.1f, table));
        table.setFillParent(true);
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(255, 255, 255, 255);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
