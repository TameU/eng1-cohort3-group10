package com.backlogged.univercity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;

public class BuildingInfo {
  final String[] type;
  final ArrayList<Coord> tileCoverageOffsets;
  final String info;
  final Sprite buildingSprite;

  public BuildingInfo(String[] type, ArrayList<Coord> tileCoverageOffsets, String info, Sprite buildingSprite) {
    this.type = type;
    this.tileCoverageOffsets = tileCoverageOffsets;
    this.info = info;
    this.buildingSprite = buildingSprite;
  }
}
