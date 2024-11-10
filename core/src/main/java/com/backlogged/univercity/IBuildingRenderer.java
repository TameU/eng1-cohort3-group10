package com.backlogged.univercity;

import java.util.Collection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
public interface IBuildingRenderer {
    public void renderBuildings(Collection<AbstractBuilding> placedBuildings, OrthographicCamera camera); 
    public void renderPlacementFeedback(boolean canBePlacedAtLocation, int row, int column, OrthographicCamera camera,
    AbstractBuilding building); 
    public TextureAtlas getAtlas();
}
