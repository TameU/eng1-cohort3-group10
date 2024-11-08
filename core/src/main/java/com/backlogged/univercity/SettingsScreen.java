package com.backlogged.univercity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Handles the rendering and logic of the settings screen.
 */
public class SettingsScreen implements Screen {
  private Skin skin;
  private Stage stage;
  private Table table;
  private Texture bgTexture;
  private ScrollPane scrollPane;

  private Label settingsLabel;

  private TextButton backButton;

  private Label musicEnabledLabel;
  private CheckBox musicEnabledCheckBox;

  private Label musicVolumeLabel;
  private Slider musicVolumeSlider;

  private Label soundEnabledLabel;
  private CheckBox soundEnabledCheckBox;

  private Label soundVolumeLabel;
  private Slider soundVolumeSlider;

  private Label fullScreenLabel;
  private CheckBox fullScreenCheckBox;

  private Label mouseSensitivityLabel;
  private Slider mouseSensitivitySlider;

  private Label keyboardSensitivityLabel;
  private Slider keyboardSensitivitySlider;

  /**
   * Sets up the settings screen to an initial state.
   *
   * @param game           Current instance of the Game class.
   * @param previousScreen Instance of the last screen to return to after the user
   *                       is finished editing settings
   */
  public SettingsScreen(Game game, Screen previousScreen) {
    skin = new Skin(Gdx.files.internal(Constants.UI_SKIN_PATH));
    stage = new Stage(new ScreenViewport());
    table = new Table(skin);

    settingsLabel = new Label("Settings", skin, "game-title");
    backButton = new TextButton("Back", skin, "blue-text-button");
    backButton.addListener(new ClickListener() {
      public void clicked(InputEvent e, float x, float y) {
        game.setScreen(previousScreen);
      }
    });

    table.add(settingsLabel).spaceBottom(20).padTop(10);
    table.row();
    createScrollPane();
    table.add(scrollPane).growX().bottom();
    table.row();
    table.add(backButton).size(Value.percentWidth(.3f, table),
        Value.percentHeight(.1f, table))
        .padTop(20)
        .padBottom(10);
    table.setFillParent(true);
    table.setDebug(true);
    stage.addActor(table);

    bgTexture = new Texture(Constants.BACKGROUND_PICTURE_PATH);

  }

  /**
   * Creates the scroll pane and fills it with all the settings options.
   */
  private void createScrollPane() {
    Table preferencesTable = new Table(skin);
    preferencesTable.setDebug(true);
    musicEnabledLabel = new Label("Music Enabled", skin, "game-title");
    musicEnabledCheckBox = new CheckBox("", skin);
    musicEnabledCheckBox.setChecked(GamePreferences.isMusicEnabled());
    musicEnabledCheckBox.addListener(new ClickListener() {
      public void clicked(InputEvent e, float x, float y) {
        GamePreferences.setMusicEnabled(musicEnabledCheckBox.isChecked());
      }
    });

    musicVolumeLabel = new Label("Music Volume", skin, "game-title");
    musicVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
    musicVolumeSlider.setValue(GamePreferences.getMusicVolume());
    musicVolumeSlider.addListener(new ChangeListener() {
      public void changed(ChangeEvent event, Actor actor) {
        GamePreferences.setMusicVolume(musicVolumeSlider.getValue());
      }
    });

    soundEnabledLabel = new Label("Sound Enabled", skin, "game-title");
    soundEnabledCheckBox = new CheckBox("", skin);
    soundEnabledCheckBox.setChecked(GamePreferences.isSoundEnabled());
    soundEnabledCheckBox.addListener(new ClickListener() {
      public void clicked(InputEvent e, float x, float y) {
        GamePreferences.setSoundEnabled(soundEnabledCheckBox.isChecked());
      }
    });

    soundVolumeLabel = new Label("Sound Volume", skin, "game-title");
    soundVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
    soundVolumeSlider.setValue(GamePreferences.getSoundVolume());
    soundVolumeSlider.addListener(new ChangeListener() {
      public void changed(ChangeEvent event, Actor actor) {
        GamePreferences.setSoundVolume(soundVolumeSlider.getValue());
      }
    });

    fullScreenLabel = new Label("Fullscreen", skin, "game-title");
    fullScreenCheckBox = new CheckBox("", skin);
    fullScreenCheckBox.setChecked(GamePreferences.isFullscreen());
    fullScreenCheckBox.addListener(new ClickListener() {
      public void clicked(InputEvent e, float x, float y) {
        GamePreferences.setFullscreen(fullScreenCheckBox.isChecked());
        if (fullScreenCheckBox.isChecked()) {
          Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
          Gdx.graphics.setWindowedMode(1280, 720);
        }
      }
    });

    mouseSensitivityLabel = new Label("Mouse Sensitivity", skin, "game-title");
    mouseSensitivitySlider = new Slider(0.1f, 2f, 0.1f, false, skin);
    mouseSensitivitySlider.setValue(GamePreferences.getMouseSensitivity());
    mouseSensitivitySlider.addListener(new ChangeListener() {
      public void changed(ChangeEvent event, Actor actor) {
        GamePreferences.setMouseSensitivity(mouseSensitivitySlider.getValue());
      }
    });

    keyboardSensitivityLabel = new Label("Keyboard Sensitivity", skin, "game-title");
    keyboardSensitivitySlider = new Slider(0.1f, 2f, 0.1f, false, skin);
    keyboardSensitivitySlider.setValue(GamePreferences.getKeyboardSensitivity());
    keyboardSensitivitySlider.addListener(new ChangeListener() {
      public void changed(ChangeEvent event, Actor actor) {
        GamePreferences.setKeyboardSensitivity(keyboardSensitivitySlider.getValue());
      }
    });

    preferencesTable.add(musicEnabledLabel).space(20);
    preferencesTable.add(musicEnabledCheckBox).space(20);
    preferencesTable.row();
    preferencesTable.add(musicVolumeLabel).space(20);
    preferencesTable.add(musicVolumeSlider)
        .width(Value.percentWidth(.3f, preferencesTable)).space(20);
    preferencesTable.row();
    preferencesTable.add(soundEnabledLabel).space(20);
    preferencesTable.add(soundEnabledCheckBox).space(20);
    preferencesTable.row();
    preferencesTable.add(soundVolumeLabel).space(20);
    preferencesTable.add(soundVolumeSlider)
        .width(Value.percentWidth(.3f, preferencesTable)).space(20);
    preferencesTable.row();
    preferencesTable.add(fullScreenLabel).space(20);
    preferencesTable.add(fullScreenCheckBox).space(20);
    preferencesTable.row();
    preferencesTable.add(mouseSensitivityLabel).space(20);
    preferencesTable.add(mouseSensitivitySlider)
        .width(Value.percentWidth(.3f, preferencesTable)).space(20);
    preferencesTable.row();
    preferencesTable.add(keyboardSensitivityLabel).space(20);
    preferencesTable.add(keyboardSensitivitySlider)
        .width(Value.percentWidth(.3f, preferencesTable)).space(20);
    scrollPane = new ScrollPane(preferencesTable);
    scrollPane.setScrollingDisabled(true, false);
    scrollPane.setDebug(true);
    stage.setScrollFocus(scrollPane);
  }

  // create a dictionary for key names if the key is not a Unicode character
  // ignore modifier and platform specific keys
  // check if the key is used for something else in the preferences
  // if it is, then don't allow the user to set it
  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
    Soundtrack.play();
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

  /**
   * Handles rendering for the background image, including scaling correctly,
   * and moving relative to the mouse position.
   */
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

  /**
   * Returns a 2-dimensional <a href="https://en.wikipedia.org/wiki/Unit_vector">
   * unit vector</a> from the centre of the screen, towards the mouse position.
   */
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
    if (width == 0 || height == 0) {
      return;
    }
    stage.getViewport().update(width, height, true);
    backButton.getStyle().font.getData()
        .setScale(width / Constants.TEXT_BUTTON_FONT_SCALING_FACTOR);
    settingsLabel.setFontScale(width / Constants.SETTINGS_TITLE_FONT_SCALING_FACTOR);
    musicEnabledLabel.setFontScale(width / Constants.SETTINGS_LABEL_FONT_SCALING_FACTOR);
    musicVolumeLabel.setFontScale(width / Constants.SETTINGS_LABEL_FONT_SCALING_FACTOR);
    soundEnabledLabel.setFontScale(width / Constants.SETTINGS_LABEL_FONT_SCALING_FACTOR);
    soundVolumeLabel.setFontScale(width / Constants.SETTINGS_LABEL_FONT_SCALING_FACTOR);
    fullScreenLabel.setFontScale(width / Constants.SETTINGS_LABEL_FONT_SCALING_FACTOR);
    mouseSensitivityLabel.setFontScale(width / Constants.SETTINGS_LABEL_FONT_SCALING_FACTOR);
    keyboardSensitivityLabel.setFontScale(width / Constants.SETTINGS_LABEL_FONT_SCALING_FACTOR);
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
