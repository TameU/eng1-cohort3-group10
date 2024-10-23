package com.backlogged.univercity;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class FirstScreen implements Screen {

    private TiledMap map;
    private float unitScale;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    @Override
    public void show() {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        map = new TmxMapLoader().load("desert.tmx");
        unitScale = 1 / 32f;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 30, 20);

    }

    @Override
    public void render(float delta) {
        renderer.setView(camera);
        renderer.render();

    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
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