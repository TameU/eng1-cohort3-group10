package com.backlogged.univercity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;


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

/**
 * Manages building placement etc. within the game and
 * Handles input, rendering, and state transitions for buildings.
 * 
 */
public class BuildingManager {

    /**
     * Represents the states of building interactions.
     */
    private enum BuildingState {
        NOT_BUILDING,  // No active building placement
        BUILDING,      // Actively placing a building
        DELETING,      // Deleting a building (not implemented)
        MOVING         // Moving a building (not implemented)
    }

    /**
     * Reads building information from a JSON file and initializes BuildingInfo objects.
     */
    private static class BuildingInfoFromJsonFactory {

        /**
         * Parses tile coverage offsets from JSON.
         *
         * @param tileCoverageOffsetsJson JSON object representing tile offsets.
         * @return ArrayList of Coord objects defining tile coverage.
         */
        private static ArrayList<Coord> getTileCoverageOffsetsFromJson(JsonValue tileCoverageOffsetsJson) {
            var tileCoverageOffsets = new ArrayList<Coord>();
            for (var tileOffset : tileCoverageOffsetsJson) {
                tileCoverageOffsets.add(new Coord(tileOffset.getInt("row"), tileOffset.getInt("column")));
            }
            return tileCoverageOffsets;
        }

        /**
         * Parses building data from JSON and generates a map of BuildingInfo objects.
         *
         * @param buildingsJsonFile JSON file containing building data.
         * @param buildingAtlas Texture atlas for building sprites.
         * @param unitScale Scale factor for tiled map renderer projection matrix
         * @return HashMap associating building types with their BuildingInfo data.
         */
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

    /**
     * Factory for creating building instances.
     */
    private static class BuildingFactory {

        /**
         * Instantiates an AbstractBuilding based on the specified class name.
         *
         * @param buildingMap Map of building data.
         * @param buildingClassName Name of the building class.
         * @return New AbstractBuilding instance.
         * @throws IllegalArgumentException if the building class cannot be instantiated.
         */
        public static AbstractBuilding createBuilding(HashMap<String, BuildingInfo> buildingMap, String buildingClassName) {
            AbstractBuilding building = null;
            try {
                Class<?> buildingClass = Class.forName("com.backlogged.univercity." + buildingClassName);
                building = (AbstractBuilding) buildingClass.getConstructor(BuildingInfo.class)
                        .newInstance(buildingMap.get(buildingClassName));
            } catch (Exception e) {
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
    private int currentRow;
    private int currentColumn;
    private BuildingState buildingState = BuildingState.NOT_BUILDING;
    private OrthographicCamera camera;

    /**
     * Constructs a building manager instance.
     *
     * @param unitScale Scale factor to match with tiled map renderer projection matrix
     * @param renderer Renderer for displaying buildings.
     * @param placementManager Manager for handling building placements.
     */
    public BuildingManager(float unitScale, IBuildingRenderer renderer, IBuildingPlacementManager placementManager) {
        if (renderer == null || placementManager == null) {
            throw new IllegalArgumentException("renderer and placement manager MUST both be initialized");
        }
        buildingMap = BuildingInfoFromJsonFactory
                .getBuildingsFromJson(Gdx.files.local("buildings/Buildings.json"),
                        renderer.getAtlas(), unitScale);
        this.renderer = renderer;
        this.placementManager = placementManager;
    }

    /**
     * Transitions the BuildingManager to building mode if not currently building.
     */
    public void setBuildingState() {
        if (buildingState == BuildingState.NOT_BUILDING) {
            buildingState = BuildingState.BUILDING;
        }
    }

    /**
     * Retrieves all available building information.
     *
     * @return Set of map entries associating building names with BuildingInfo data.
     */
    public Set<Entry<String, BuildingInfo>> getBuildings() {
        return buildingMap.entrySet();
    }

    /**
     * Resets building placement state.
     */
    private void resetState() {
        buildingToBePlaced = null;
        isChoosingLocation = false;
    }

    /**
     * Places the building at the specified coordinates.
     *
     * @param row Row coordinate for placement.
     * @param column Column coordinate for placement.
     */
    private void placeBuilding(int row, int column) {
        placementManager.placeBuilding(row, column, buildingToBePlaced);
        resetState();
    }

    /**
     * Initiates location choosing state for placing a building
     *
     * @param buildingType Name of the building type to place.
     */
    public void chooseLocationOfBuilding(String buildingType) {
        AbstractBuilding building = BuildingFactory.createBuilding(buildingMap, buildingType);
        buildingToBePlaced = building;
        isChoosingLocation = true;
    }

    /**
     * Sets the camera used for projecting world coordinates.
     *
     * @param camera Camera instance to set.
     */
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    /**
     * Converts screen coordinates to world coordinates, adjusted for the camera's perspective.
     *
     * @return Vector2 representing world coordinates of the cursor position.
     */
    private Vector2 getWorldCoordinates() {
        var cursorPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        var worldCoordinates = camera.unproject(cursorPos);
        return new Vector2(worldCoordinates.x, worldCoordinates.y);
    }

    /**
     * Processes input based on the current building state.
     */
    public void handleInput() {
        switch (buildingState) {
            case NOT_BUILDING: {
                // No action taken when not building.
            }
            break;
            case BUILDING: {
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    placeBuilding(currentRow, currentColumn);
                    resetState();
                    buildingState = BuildingState.NOT_BUILDING;
                }
            }
            break;
            case DELETING:
                // TODO: Implement building deletion functionality.
                break;
            case MOVING:
                // TODO: Implement building movement functionality.
                break;
        }
        if (buildingState != BuildingState.NOT_BUILDING
                && Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            resetState();
            buildingState = BuildingState.NOT_BUILDING;
        }
    }

    /**
     * Updates the BuildingManager, updating the row and column based on cursor location if placing a building.
     */
    public void update() {
        if (isChoosingLocation) {
            var worldCoordinates = getWorldCoordinates();
            currentRow = (int) worldCoordinates.x;
            currentColumn = (int) worldCoordinates.y;
        }
    }

    /**
     * Resets the building placement manager, clearing all placed buildings.
     */
    public void reset() {
        placementManager.reset();
    }

    /**
     * Gets the total count of buildings currently placed.
     *
     * @return Integer count of placed buildings.
     */
    public int getBuildingCount() {
        return placementManager.getCount();
    }

    /**
     * Renders buildings, and the placement squares if state is BUILDING.
     */
    public void render() {
        if (isChoosingLocation) {
            renderer.renderPlacementSquares(currentRow, currentColumn, camera, buildingToBePlaced);
        }
        renderer.renderBuildings(placementManager.getPlacedBuildings(), camera);
    }
}
