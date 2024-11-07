package com.backlogged.univercity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public final class Soundtrack {
    private Soundtrack() {
        // restricts instantiation of class
    }

    private static final Music soundtrack = Gdx.audio.newMusic(Gdx.files.internal(Constants.SOUNDTRACK_PATH));
    private static final float volume = 1f;
    public static void play() {
        soundtrack.setVolume(volume);
        soundtrack.setLooping(true);
        soundtrack.play();
    }

    public static void pause() {
        soundtrack.pause();
    }
}
