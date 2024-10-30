package com.backlogged.univercity;

import java.util.ArrayList;

public abstract class AbstractBuilding {
    private BuildingInfo buildingInfo; 
    private Coord mapPos;
    public AbstractBuilding(BuildingInfo buildingInfo) { 
        this.buildingInfo = buildingInfo;
    } 
        public final String[] getType() {
        return buildingInfo.type;
    } 
    public final String getAtlasRegion() { 
        return buildingInfo.atlasRegion;
    } 
    public final ArrayList<Coord> getBoundingPolygonVertices() { 
        return buildingInfo.boundingPolygonVertices;
    } 
    public final String getInfo() { 
        return buildingInfo.info;
    }  
    public final void setMapPosition(Coord mapPos) { 
        this.mapPos = mapPos;
    } 
    public final Coord getMapPos() { 
        return mapPos;
    }
}
