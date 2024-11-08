package com.backlogged.univercity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * This class is a singleton that manages and plays the game's soundtrack.
 * The soundtrack is set to loop continuously when played.
 */
public final class Soundtrack {

  private Soundtrack() {
    // restricts instantiation of class
  }

  private static final Music soundtrack = Gdx.audio.newMusic(
      Gdx.files.internal(Constants.SOUNDTRACK_PATH));
  private static final float volume = 1f;

  /**
   * Starts or resumes the soundtrack. Updates volume.
   */
  public static void play() {
    soundtrack.setVolume(volume);
    soundtrack.setLooping(true);
    soundtrack.play();
  }

  /**
   * Pauses the soundtrack.
   */
  public static void pause() {
    soundtrack.pause();
  }
}
