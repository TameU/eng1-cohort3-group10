package com.backlogged.univercity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

  public final Sprite getSprite() {
    return buildingInfo.buildingSprite;
  }

  public final ArrayList<Coord> getTileCoverageOffsets() {
    return buildingInfo.tileCoverageOffsets;
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

  public final void draw(SpriteBatch batch) {
    buildingInfo.buildingSprite.setPosition(this.mapPos.getRow(), this.mapPos.getColumn());
    buildingInfo.buildingSprite.draw(batch);
    buildingInfo.buildingSprite.setPosition(0, 0);
  }
}
