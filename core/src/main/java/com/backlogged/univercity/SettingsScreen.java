package com.backlogged.univercity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsScreen implements Screen {
    private Skin skin;
    private Stage stage;
    private Table table;
    private Label gameOverLabel;
    private TextButton startAgainButton;
    private Texture bgTexture;

    public SettingsScreen(Game game, Screen previousScreen) {
        skin = new Skin(Gdx.files.local("testskin.json"));
        stage = new Stage(new ScreenViewport());
        table = new Table(skin);

        gameOverLabel = new Label("Settings", skin, "no-background");
        startAgainButton = new TextButton("Back", skin);
        startAgainButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(previousScreen);
            }
        });

        table.add(gameOverLabel).space(20);
        table.row();
        table.add(startAgainButton).size(Value.percentWidth(.3f, table), Value.percentHeight(.1f, table));
        table.setFillParent(true);
        stage.addActor(table);

        bgTexture = new Texture("UniverCityBackgroundBlur.png");

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(255, 255, 255, 255);
        stage.getBatch().begin();
        renderBackground();
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
    }

    private void renderBackground() {
        float aspectRatio = 16f / 9f;
        float worldWidth = stage.getWidth();
        float worldHeight = stage.getHeight();
        float padding = 20;

        float height = worldHeight;
        float width = height * aspectRatio;

        if (width < worldWidth) {
            width = worldWidth;
            height = width / aspectRatio;
        }

        float correctionX = (worldWidth - width) / 2;
        float correctionY = (worldHeight - height) / 2;

        Vector2 offsetVect2 = getMouseDirection();
        float offsetX = (-offsetVect2.x * padding / 2) + correctionX;
        float offsetY = (offsetVect2.y * padding / 2) + correctionY;

        stage.getBatch().draw(bgTexture, offsetX - padding / 2, offsetY - padding / 2,
                width + padding, height + padding);

    }

    private Vector2 getMouseDirection() {
        float x = Gdx.input.getX();
        float y = Gdx.input.getY();

        int centerX = (int) stage.getWidth() / 2;
        int centerY = (int) stage.getHeight() / 2;

        float diffX = centerX - x;
        float diffY = centerY - y;

        float normX = diffX / centerX;
        float normY = diffY / centerY;

        return new Vector2(normX, normY);

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
        stage.dispose();
        skin.dispose();
        bgTexture.dispose();

    }
}
