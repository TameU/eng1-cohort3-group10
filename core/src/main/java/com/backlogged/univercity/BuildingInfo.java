package com.backlogged.univercity;

import java.util.ArrayList;

public class BuildingInfo {
    final String[] type; 
    final String atlasRegion; 
    final ArrayList<Coord> boundingPolygonVertices; 
    final String info; 
    
    public BuildingInfo(String[] type, String atlasRegion, ArrayList<Coord> boundingPolygonVertices, String info) { 
        this.type = type; 
        this.atlasRegion = atlasRegion; 
        this.boundingPolygonVertices = boundingPolygonVertices; 
        this.info = info;
    }    
}
