package com.backlogged.univercity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import java.util.Collection;

public class BuildingRenderer implements Disposable, IBuildingRenderer {
    private SpriteBatch batch = new SpriteBatch();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private TextureAtlas buildingAtlas;
    private boolean debugGridEnabled = false;
    
    
    public BuildingRenderer(TextureAtlas buildingAtlas) { 
        this.buildingAtlas = buildingAtlas;
    } 
    public void enableDebugGrid(boolean b) { 
        debugGridEnabled = b;
    }
    public void drawDebugGrid(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        var h = Gdx.graphics.getHeight();
        var w = Gdx.graphics.getWidth();

        for (float y = 0; y <= h; y++) {
            shapeRenderer.line(0, y, w, y);
        }
        for (float x = 0; x <= w; x++) {
            shapeRenderer.line(x, 0, x, h);
        }

        shapeRenderer.end();
    }

    public void renderPlacementSquares(int row, int column, OrthographicCamera camera,
            AbstractBuilding building) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        Gdx.gl.glLineWidth(5);
        for (var tileOffset : building.getTileCoverageOffsets()) {
            int offsetX = row + tileOffset.getRow();
            int offsetY = column + tileOffset.getColumn();
            shapeRenderer.line(0.f + offsetX, 0.5f + offsetY, 0.25f + offsetX, 0.f + offsetY);
            shapeRenderer.line(0.25f + offsetX, 0.f + offsetY, 1.f + offsetX, 1.f + offsetY);
        }
        shapeRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    public void renderBuildings(Collection<AbstractBuilding> placedBuildings, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (var building : placedBuildings) {
            building.draw(batch);
        }
        batch.end(); 
        if (debugGridEnabled) drawDebugGrid(camera);
    }  
    public TextureAtlas getAtlas() { 
        return buildingAtlas;
    }
    public void dispose() { 
        if (buildingAtlas != null) buildingAtlas.dispose();
        batch.dispose(); 
        shapeRenderer.dispose();
    }
}
