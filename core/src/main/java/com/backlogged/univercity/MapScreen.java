package com.backlogged.univercity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
 * Handles rendering and logic for the main game screen.
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
  private Button settingsButton;
  private InGameTimer timer;
  private boolean mouseDown;
  private boolean dragging;
  private float oldMouseX;
  private float oldMouseY;
  // Buildings
  private Button bed;
  private Button football;
  private Button book;
  private Button food;
  private BuildingManager buildingManager;

  /**
   * Setup the main game window (map).
   *
   * @param game the current instance of game
   */
  public MapScreen(Game game) {
    this.game = game;
    map = new TmxMapLoader().load("desert.tmx");
    unitScale = 1 / 32f;
    renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    camera = new OrthographicCamera();

    timer = new InGameTimer(5);
    var buildingRenderer = new BuildingRenderer(new TextureAtlas(
        Gdx.files.internal("buildings/buildings.atlas")));
    buildingManager = new BuildingManager(unitScale, buildingRenderer,
        new BuildingPlacementManager((TiledMapTileLayer) map.getLayers().get("Terrain")));
    stage = new Stage(new ScreenViewport());
    skin = new Skin(Gdx.files.internal(Constants.UI_SKIN_PATH));

    timerLabel = new Label("5:00", skin);
    timerLabel.setAlignment(Align.center);

    pauseButton = new Button(skin, "pause");
    pauseButton.addListener(new ClickListener() {
      public void clicked(InputEvent e, float x, float y) {
        if (timer.isUserStopped()) {
          timer.userStartTime();
        } else {
          timer.userStopTime();
        }
      }
    });

    settingsButton = new Button(skin, "settings");
    settingsButton.addListener(new ClickListener() {
      public void clicked(InputEvent e, float x, float y) {
        timer.systemStopTime(); // pause Time while in settings
        game.setScreen(new SettingsScreen(game, game.getScreen()));
      }
    });

    //////////////// BUILDINGS

    bed = new Button(skin, "bed");
    bed.addListener(new ClickListener() {
      public void clicked(InputEvent e, float x, float y) {
        // Deal with clicking later
        buildingManager.setBuildingState();
        buildingManager.chooseLocationOfBuilding("Accommodation");
      }
    });

    football = new Button(skin, "football");
    football.addListener(new ClickListener() {
      public void clicked(InputEvent e, float x, float y) {
        // Deal with clicking later
        buildingManager.setBuildingState();
        buildingManager.chooseLocationOfBuilding("SportsCenter");
      }
    });

    book = new Button(skin, "book");
    book.addListener(new ClickListener() {
      public void clicked(InputEvent e, float x, float y) {
        // Deal with clicking later
        buildingManager.setBuildingState();
        buildingManager.chooseLocationOfBuilding("LectureHall");
      }
    });

    food = new Button(skin, "food");
    food.addListener(new ClickListener() {
      public void clicked(InputEvent e, float x, float y) {
        // Deal with clicking later
        buildingManager.setBuildingState();
        buildingManager.chooseLocationOfBuilding("FoodCourt");
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
    table.add(pauseButton).top().right().spaceRight(10);
    table.add(settingsButton).top(); // .left();
    // Buildings
    table.add(bed).center().left();
    table.add(football).center().left();
    table.add(book).center().left();
    table.add(food).center().left();

    stage.addActor(table);

    map = new TmxMapLoader().load(Constants.MAP_PATH);
    unitScale = 1 / 32f;
    renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    camera = new OrthographicCamera();
    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();
    camera.setToOrtho(false, width * unitScale, (width * unitScale) * (height / width));
    timer.initialiseTimerValues();
    timer.userStartTime();
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
    timer.systemStartTime();
    Soundtrack.play();
  }

  @Override
  public void render(float delta) {
    handleInput();
    camera.update();
    buildingManager.setCamera(camera);
    buildingManager.handleInput();
    buildingManager.update();
    renderer.setView(camera);

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    renderer.render();
    buildingManager.render();
    float timeLeft = timer.updateTime(delta);
    float elapsedTime = timer.timeElapsed(delta);

    if (elapsedTime > Constants.ONE_MONTH) {
      timer.updateTimerValues();
    }

    if (timeLeft < 1) {
      game.setScreen(new GameOverScreen(game));
    }
    timerLabel.setText(timer.output());
    stage.act();
    stage.draw();
  }

  /**
   * Handles the user's mouse input, allowing dragging of the map.
   */
  private void handleMouseInput() {
    mouseDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

    if (!dragging && mouseDown) {
      dragging = true;
      oldMouseX = Gdx.input.getX();
      oldMouseY = Gdx.input.getY();
    } else if (!mouseDown) {
      dragging = false;
    }

    if (dragging) {
      float currentX = Gdx.input.getX();
      float currentY = Gdx.input.getY();
      Vector2 translate = new Vector2(-(currentX - oldMouseX), currentY - oldMouseY);
      translate.scl(Constants.DEFAULT_MOUSE_SENSITIVITY
          * GamePreferences.getMouseSensitivity() * camera.zoom);
      camera.translate(translate);
      oldMouseX = currentX;
      oldMouseY = currentY;
    }
  }

  /**
   * Handle user's keyboard inputs, allowing movement and zooming of the map.
   */
  private void handledKeyboardInput() {
    if (Gdx.input.isKeyPressed(Input.Keys.A)) { // Zoom in
      camera.zoom += Constants.DEFAULT_KEYBOARD_SENSITIVITY
          * GamePreferences.getKeyboardSensitivity();
    }
    if (Gdx.input.isKeyPressed(Input.Keys.Q)) { // Zoom out
      camera.zoom -= Constants.DEFAULT_KEYBOARD_SENSITIVITY
          * GamePreferences.getKeyboardSensitivity();
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      camera.translate(-1 * GamePreferences.getKeyboardSensitivity(), 0, 0);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      camera.translate(1 * GamePreferences.getKeyboardSensitivity(), 0, 0);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      camera.translate(0, -1 * GamePreferences.getKeyboardSensitivity(), 0);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      camera.translate(0, 1 * GamePreferences.getKeyboardSensitivity(), 0);
    }
  }

  /**
   * Collective method for processing user input.
   */
  private void handleInput() {
    handleMouseInput();
    handledKeyboardInput();

    /* TODO: get better clamping */
    camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100 / camera.viewportWidth);

  }

  @Override
  public void resize(int width, int height) {
    if (width == 0 || height == 0) {
      return;
    }
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
    stage.dispose();
    skin.dispose();
    renderer.dispose();
  }

}
