package com.backlogged.univercity;
import java.util.Collection;
public interface IBuildingPlacementManager {
    public void placeBuilding(int row, int column, AbstractBuilding building); 
    public boolean canBePlacedAtLocation(int row, int column, AbstractBuilding building);
    public void reset(); 
    public int getCount(); 
    public Collection<AbstractBuilding> getPlacedBuildings();
}
