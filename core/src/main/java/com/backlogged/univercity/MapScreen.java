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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;



/**
 * First screen of the application. Displayed after the application is created.
 */
public class MapScreen implements Screen {

    private Game game;
    private TiledMap map;
    private float unitScale;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Skin skin;
    private Stage stage;
    private Table table;
    private Label timerLabel;
    private Button pauseButton;
    private InGameTimer timer;
    private boolean pauseClicked;

    public MapScreen(Game game) {
        this.game = game;

        timer = new InGameTimer(5);

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.local("testskin.json"));

        timerLabel = new Label("5:00", skin);
        timerLabel.setAlignment(Align.center);

        pauseButton = new Button(skin, "pause");
        pauseButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                if (timer.isUserStopped())
                    timer.userStartTime();
                else
                    timer.userStopTime();
            }
        });

        timerLabel.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new GameOverScreen(game));
            }
        });

        table = new Table(skin);
        table.setFillParent(true);
        table.setDebug(true);
        table.add(timerLabel).expand().top();
        table.add(pauseButton).top().right();

        stage.addActor(table);

        map = new TmxMapLoader().load("desert.tmx");
        unitScale = 1 / 32f;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        camera = new OrthographicCamera();
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera.setToOrtho(false, width * unitScale, (width * unitScale) * (height / width));
        timer.resetTime();
        timer.resetYear();
        timer.resetElapse();
        timer.resetSemester();
        timer.userStartTime();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        timer.systemStartTime();
    }

    @Override
    public void render(float delta) {
        handleInput();
        camera.update();
        renderer.setView(camera);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        float timeLeft = timer.updateTime(delta);
        float elapsedTime = timer.timeElapsed(delta);

        if(elapsedTime > Constants.THRESHOLD){
            timer.updateValues();
        }

        if (timeLeft < 1)
            game.setScreen(new GameOverScreen(game));
        timerLabel.setText(timer.output());
        stage.act();
        stage.draw();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.02;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-1, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(1, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 1, 0);
        }

        /* TODO: get better clamping */
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100 / camera.viewportWidth);

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = MathUtils.floor(width / 32f);
        camera.viewportHeight = camera.viewportWidth * height / width;
        camera.update();
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
        timer.systemStopTime();
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
        timer.systemStartTime();
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
