package com.backlogged.univercity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {
    private Music soundtrack;

    @Override
    public void create() {
        soundtrack = Gdx.audio.newMusic(Gdx.files.internal(Constants.SOUNDTRACK_PATH));
        soundtrack.setLooping(true);
        soundtrack.setVolume(1f);
        soundtrack.play();
        setScreen(new TitleScreen(this));
    }

    public void pause() {
        soundtrack.pause();
    }

    public void resume() {
        soundtrack.play();
    }
}
