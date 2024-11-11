package com.backlogged.univercity;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import java.util.Collection;
import java.util.HashMap;

/**
 * BuildingPlacementManager manages what buildings are currently placed on the
 * map, whether or not
 * you can place a building at a certain location and placing a building.
 */
public class BuildingPlacementManager implements IBuildingPlacementManager {
  private TiledMapTileLayer terrainLayer;
  private HashMap<Coord, AbstractBuilding> placedBuildings = new HashMap<>();
  int count = 0;

  /**
   * Constructs a BuildingPlacementManager instance.
   *
   * @param terrainLayer The layer containing terrain information within the map.
   */
  public BuildingPlacementManager(TiledMapTileLayer terrainLayer) {
    this.terrainLayer = terrainLayer;
  }

  /**
   * Determines if its possible to place a building at the location of the cursor
   * by checking if any
   * of the tiles that the building will take up contain either a terrain type
   * that cant be built on
   * or another building.
   *
   * @param row      The row to start checking from.
   * @param column   The column to start checking from.
   * @param building The {@link AbstractBuilding} to check.
   * @return Returns true its possible and false if not.
   */
  public boolean canBePlacedAtLocation(int row, int column, AbstractBuilding building) {

    for (var tileOffset : building.getTileCoverageOffsets()) {
      TiledMapTileLayer.Cell terrainCell = terrainLayer.getCell(column + tileOffset.getColumn(),
          row + tileOffset.getRow());
      if (!terrainCell.getTile().getProperties().get("canBeBuiltOn", Boolean.class)
          || placedBuildings.containsKey(tileOffset)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Determines if its possible to place a building at the location of the cursor
   * by checking if any
   * of the tiles that the building will take up contain another building.
   *
   * @param row      The row to start checking from.
   * @param column   The column to start checking from.
   * @param building The {@link AbstractBuilding} to check.
   * @return Returns true its possible and false if not.
   */
  public boolean canBePlacedAtLocationIgnoreTerrain(
      int row, int column, AbstractBuilding building) {
    for (var tileOffset : building.getTileCoverageOffsets()) {
      if (placedBuildings.containsKey(
          new Coord(tileOffset.getRow() + row, tileOffset.getColumn() + column))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Places a building at a given location.
   *
   * @param row    The row to place the building.
   * @param column The column to place the building.
   */
  public void placeBuilding(int row, int column, AbstractBuilding building) {

    for (var tileOffset : building.getTileCoverageOffsets()) {
      placedBuildings.put(
          new Coord(row + tileOffset.getRow(), column + tileOffset.getColumn()), building);
    }
    building.setMapPosition(new Coord(row, column));
    count++;
  }

  /**
   * Retrives all the currently placed buildings.
   *
   * @return A collection of the currently placed buildings.
   */
  public Collection<AbstractBuilding> getPlacedBuildings() {
    return placedBuildings.values();
  }

  /**
   * Retrives the current number of buildings.
   *
   * @return The number of buildings currently placed on the map.
   */
  public int getCount() {
    return count;
  }

  /** Resets the count to zero and clears any placed buildings. */
  public void reset() {
    placedBuildings.clear();
    count = 0;
  }
}