package com.backlogged.univercity;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.Sprite;;
public class BuildingInfo {
    final String[] type; 
    final ArrayList<Coord> boundingPolygonVertices; 
    final String info; 
    final Sprite buildingSprite;
    public BuildingInfo(String[] type, ArrayList<Coord> boundingPolygonVertices, String info, Sprite buildingSprite) { 
        this.type = type; 
        this.boundingPolygonVertices = boundingPolygonVertices; 
        this.info = info; 
        this.buildingSprite = buildingSprite;
    }    
}
