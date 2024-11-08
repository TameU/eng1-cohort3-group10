package com.backlogged.univercity;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import java.util.HashMap; 
import java.util.Collection;

public class BuildingPlacementManager implements IBuildingPlacementManager { 
    private TiledMapTileLayer terrainLayer;
    private HashMap<Coord, AbstractBuilding> placedBuildings = new HashMap<>();
    int count = 0; 
    
    public BuildingPlacementManager(TiledMapTileLayer terrainLayer) { 
        this.terrainLayer = terrainLayer;
    }
    public boolean canBePlacedAtLocation(int row, int column, AbstractBuilding building) {

        for (var tileOffset : building.getTileCoverageOffsets()) {
            TiledMapTileLayer.Cell terrainCell =
                    terrainLayer.getCell(column + tileOffset.getColumn(), row + tileOffset.getRow());
            if (!terrainCell.getTile().getProperties().get("canBeBuiltOn", Boolean.class)
                    || placedBuildings.containsKey(tileOffset))
                return false;
        }
        return true;
    }
    
    public void placeBuilding(int row, int column, AbstractBuilding building) {

        for (var tileOffset : building.getTileCoverageOffsets()) {
            placedBuildings.put(new Coord(row + tileOffset.getRow(), column + tileOffset.getColumn()),
                    building);
        }
        building.setMapPosition(new Coord(row, column));
        count++;
    } 
    public Collection<AbstractBuilding> getPlacedBuildings() { 
        return placedBuildings.values();
    } 
    public int getCount() { 
        return count;
    }
    public void reset() { 
        placedBuildings.clear(); 
        count = 0;
    }
}
