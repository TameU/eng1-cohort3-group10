package com.backlogged.univercity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {
    @Override
    public void create() {
        if (GamePreferences.isFullscreen())
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        else
            Gdx.graphics.setWindowedMode(1280, 720);
        setScreen(new TitleScreen(this));
    }
}
