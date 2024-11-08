package com.backlogged.univercity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public final class Soundtrack {
    /**
    * This class is a singleton that manages and plays the game's soundtrack.
    * The soundtrack is set to loop continuously when played.
    */

    private Soundtrack() {
        // restricts instantiation of class
    }

    private static final Music soundtrack = Gdx.audio.newMusic(Gdx.files.internal(Constants.SOUNDTRACK_PATH));

    /**
     * Starts or resumes the soundtrack. Updates volume.
     */
    public static void play() {
        soundtrack.setVolume(GamePreferences.getMusicVolume());
        soundtrack.setLooping(true);
        if (GamePreferences.isMusicEnabled())
            soundtrack.play();
    }

    /**
     * Pauses the soundtrack.
     */
    public static void pause() {
        soundtrack.pause();
    }
}
