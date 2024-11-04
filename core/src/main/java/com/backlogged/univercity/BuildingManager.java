package com.backlogged.univercity;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set; 
import java.util.Map.Entry;

public class BuildingManager {
    private enum BuildingState { 
        NOT_BUILDING, 
        BUILDING, 
        DELETING, 
        MOVING,
    }
    private static class BuildingInfoFromJsonFactory {
        private static ArrayList<Coord> getTileCoverageOffsetsFromJson(JsonValue tileCoverageOffsetsJson) {
            var tileCoverageOffsets = new ArrayList<Coord>();
            for (var tileOffset : tileCoverageOffsetsJson) {
                tileCoverageOffsets.add(new Coord(tileOffset.getInt("row"), tileOffset.getInt("column")));
            }
            return tileCoverageOffsets;
        }

        public static HashMap<String, BuildingInfo> getBuildingsFromJson(FileHandle buildingsJsonFile, TextureAtlas buildingAtlas, float unitScale) {
            HashMap<String, BuildingInfo> buildingMap = new HashMap<>();
            JsonValue buildings = new JsonReader().parse(buildingsJsonFile);
            for (var building : buildings) { 
                Sprite sprite = buildingAtlas.createSprite(building.get("atlasRegion").asString());
                sprite.setScale(unitScale); 
                sprite.setOrigin(0, 0);
                buildingMap.put(building.name,
                        new BuildingInfo(building.get("type").asStringArray(),
                                getTileCoverageOffsetsFromJson(
                                        building.get("tileCoverageOffsets")),
                                building.get("info").asString(), sprite));
            } 
            return buildingMap;
        }  
    } 

    private static class BuildingFactory {
        public static AbstractBuilding createBuilding(HashMap<String, BuildingInfo> buildingMap, String buildingClassName) {
            AbstractBuilding building = null;
            try {
            Class<?> buildingClass = Class.forName("com.backlogged.univercity." + buildingClassName);  
            building = (AbstractBuilding)buildingClass.getConstructor(BuildingInfo.class).newInstance(buildingMap.get(buildingClassName));
            } catch(Exception e) {  
                throw new IllegalArgumentException(String.format(
                    "%s\nBuilding %s does not exist", e.toString(), buildingClassName));  
            } 
            return building;
        }
    } 
    
    private final HashMap<String, BuildingInfo> buildingMap;
    private IBuildingRenderer renderer;
    private IBuildingPlacementManager placementManager;
    private boolean isChoosingLocation = false;
    private AbstractBuilding buildingToBePlaced;
    private int currentRow, currentColumn; 
    private BuildingState buildingState = BuildingState.NOT_BUILDING;
    private OrthographicCamera camera;
    public BuildingManager(float unitScale,  
                            IBuildingRenderer renderer, 
                            IBuildingPlacementManager placementManager) {
        if (renderer == null || placementManager == null) {
            throw new IllegalArgumentException("renderer and placement manager MUST both be initialized");
        }  
        buildingMap = BuildingInfoFromJsonFactory.getBuildingsFromJson(Gdx.files.internal("Buildings.json"), renderer.getAtlas(), unitScale);
        this.renderer = renderer; 
        this.placementManager = placementManager;
    } 

     
    
     public Set<Entry<String, BuildingInfo>> getBuildings() { 
        return buildingMap.entrySet();
    } 
    private void placeBuilding(int row, int column) { 
        placementManager.placeBuilding(row, column, buildingToBePlaced); 
        buildingToBePlaced = null; 
        isChoosingLocation = false;
    } 
    private void tryPlaceBuilding(String buildingType) { 
        AbstractBuilding building = BuildingFactory.createBuilding(buildingMap, buildingType);
        buildingToBePlaced = building;
        isChoosingLocation = true;
    }  
    public void setCamera(OrthographicCamera camera) { 
        this.camera = camera;
    } 
    private Vector2 getWorldCoordinates() { 
        var cursorPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0); 
        var worldCoordinates = camera.unproject(cursorPos); 
        return new Vector2(worldCoordinates.x, worldCoordinates.y);
    }
    public void handleInput() { 
        switch (buildingState) { 
            case NOT_BUILDING: { 
                if (Gdx.input.isKeyPressed(Input.Keys.B)) {
                    tryPlaceBuilding("LectureHall");
                    buildingState = BuildingState.BUILDING;
                } 
                
            } break;
            case BUILDING: { 
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) { 
                    placeBuilding(currentRow, currentColumn);
                    buildingState = BuildingState.NOT_BUILDING;
                } 
                
            } break; 
            case DELETING: { 
                //TODO: UNIMPLEMENTED
            } break; 
            case MOVING: { 
                //TODO: UNIMPLEMENTEDt
            }
        }
    }
    public void update() { 
        if (isChoosingLocation) { 
            var worldCoordinates = getWorldCoordinates();
            currentRow = (int)worldCoordinates.x; 
            currentColumn = (int)worldCoordinates.y;
        }
    }
    public void reset() { 
        placementManager.reset();
    }

    public int getBuildingCount() {
        return placementManager.getCount();
    }  
    
    public void render() { 
        if (isChoosingLocation) renderer.renderPlacementSquares(currentRow, currentColumn, camera, buildingToBePlaced);
        renderer.renderBuildings(placementManager.getPlacedBuildings(), camera);
    }
    
}
