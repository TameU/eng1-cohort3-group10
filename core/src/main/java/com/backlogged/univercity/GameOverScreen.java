package com.backlogged.univercity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Handles the rendering and logic for the game over screen.
 */
public class GameOverScreen implements Screen {
  private Skin skin;
  private Stage stage;
  private Table table;
  private Label gameOverLabel;
  private TextButton startAgainButton;
  private TextButton quitButton;

  /**
   * Sets up the game over screen.
   *
   * @param game the current instance of the game
   */
  public GameOverScreen(Game game) {
    skin = new Skin(Gdx.files.internal(Constants.UI_SKIN_PATH));
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
    table.add(startAgainButton).top().padTop(100).width(Value.percentWidth(0.3f, table))
        .height(Value.percentHeight(0.1f, table));
    table.row();
    table.add(quitButton).top().padTop(50).width(Value.percentWidth(0.3f, table))
        .height(Value.percentHeight(0.1f, table));
  }

  @Override
  public void show() {
    Soundtrack.pause();
    Sound gameOverSound = Gdx.audio.newSound(Gdx.files.internal(Constants.GAME_OVER_SOUND_PATH));
    gameOverSound.play(1f);
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
    if (width == 0 || height == 0) {
      return;
    }
    gameOverLabel.setFontScale(width / Constants.GAME_OVER_FONT_SCALING_FACTOR);
    startAgainButton.getStyle().font.getData()
        .setScale(width / Constants.TEXT_BUTTON_FONT_SCALING_FACTOR);
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
