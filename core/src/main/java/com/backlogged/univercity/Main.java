package com.backlogged.univercity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {

    @Override
    public void create() {
        Soundtrack.play();
        setScreen(new TitleScreen(this));
    }

    public void pause() {
        Soundtrack.pause();
    }

    public void resume() {
        Soundtrack.play();
    }
}
