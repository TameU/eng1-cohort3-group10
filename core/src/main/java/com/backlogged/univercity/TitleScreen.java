package com.backlogged.univercity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TitleScreen implements Screen {
    private Game game;
    private Stage stage;
    private Table table;
    private Skin skin;

    private TiledMap map;
    private float unitScale;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    private Label titleLabel;
    private TextButton playButton;
    private TextButton optionsButton;
    private TextButton quitButton;

    public TitleScreen(Game game) {
        this.game = game;

        map = new TmxMapLoader().load("desert.tmx");
        unitScale = 1 / 32f;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        camera = new OrthographicCamera();
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera.setToOrtho(false, width * unitScale, (width * unitScale) * (height / width));

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.local("testskin.json"));
        table = new Table(skin);
        table.setFillParent(true);
        table.setDebug(true);

        stage.addActor(table);

        titleLabel = new Label("UniverCity", skin, "game-title");
        titleLabel.setFontScale(3f);

        playButton = new TextButton("Play", skin, "blue-text-button");
        playButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new MapScreen(game));
            }
        });

        optionsButton = new TextButton("Options", skin, "blue-text-button");

        quitButton = new TextButton("Quit", skin, "blue-text-button");
        quitButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(titleLabel).top().padTop(100);
        table.row();
        table.add(playButton).top().padTop(100).width(Value.percentWidth(0.3f, table)).height(Value.percentHeight(0.1f, table));
        table.row();
        table.add(optionsButton).top().padTop(50).width(Value.percentWidth(0.3f, table)).height(Value.percentHeight(0.1f, table));
        table.row();
        table.add(quitButton).top().padTop(50).width(Value.percentWidth(0.3f, table)).height(Value.percentHeight(0.1f, table));

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        camera.update();
        renderer.setView(camera);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = MathUtils.floor(width / 32f);
        camera.viewportHeight = camera.viewportWidth * height / width;
        camera.update();
        titleLabel.setFontScale(width / 1300f);
        playButton.getStyle().font.getData().setScale(width / 3000f);
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
