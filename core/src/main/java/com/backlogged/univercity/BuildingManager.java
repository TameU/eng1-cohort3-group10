package com.backlogged.univercity;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set; 
import java.util.Map.Entry;


public class BuildingManager {

    class BuildingFactory {
        private static HashMap<String, BuildingInfo> buildingMap = new HashMap<>();
        private static ArrayList<Coord> getBoundingPolygonVerticesFromJson(JsonValue vertices) {
            var boundingPolygonVertices = new ArrayList<Coord>();
            for (var vertex : vertices) {
                boundingPolygonVertices.add(new Coord(vertex.getInt("row"), vertex.getInt("column")));
            }
            return boundingPolygonVertices;
        }

        public static void getBuildingsFromJson(FileHandle buildingsJsonFile) {

            JsonValue buildings = new JsonReader().parse(buildingsJsonFile);
            for (var building : buildings) {
                buildingMap.put(building.name,
                        new BuildingInfo(building.get("type").asStringArray(),
                                building.get("atlasRegion").asString(),
                                getBoundingPolygonVerticesFromJson(
                                        building.get("boundingPolygonVertices")),
                                building.get("info").asString()));
            }
        } 
        public static Set<Entry<String, BuildingInfo>> getBuildings() { 
            return buildingMap.entrySet();
        }
        public static AbstractBuilding createBuilding(String building) {
            switch (building) {
                case "lectureHall":
                    return new LectureHall(buildingMap.get(building));
                case "accomodation": 
                    return null; 
                case "sportsCenter": 
                    return null; 
                case "foodCourt": 
                    return null;
                default:
                    throw new IllegalArgumentException(String.format(
                            "Building %s is either unimplemented or does not exist", building));
            }
        }
    }

    private TiledMapTileLayer terrainLayer;
    private TextureAtlas buildingAtlas;
    private HashMap<Coord, AbstractBuilding> placedBuildings = new HashMap<>();
    private SpriteBatch batch = new SpriteBatch();
    private int count = 0;
    private float unitScale;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public BuildingManager(TiledMap map, float unitScale, String buildingAtlasPath) {
        if (map == null) {
            throw new IllegalArgumentException("the TiledMap was null, has to be initialized");
        } 
        BuildingFactory.getBuildingsFromJson(Gdx.files.internal("Buildings.json"));
        this.buildingAtlas = new TextureAtlas(Gdx.files.internal(buildingAtlasPath));
        terrainLayer = (TiledMapTileLayer) map.getLayers().get("Terrain"); 
        this.unitScale = unitScale;
    }
    //TODO: I need to implement this custom layer property in Tiled for Terrain
    public boolean canBePlacedAtLocation(int row, int column, AbstractBuilding building) {

        for (var vertex : building.getBoundingPolygonVertices()) {
            TiledMapTileLayer.Cell terrainCell =
                    terrainLayer.getCell(column + vertex.getColumn(), row + vertex.getRow());
            if (!terrainCell.getTile().getProperties().get("canBeBuiltOn", Boolean.class)
                    || placedBuildings.containsKey(vertex))
                return false;
        }
        return true;
    }


    public void placeBuilding(int row, int column, String buildingType) {

        var building = BuildingFactory.createBuilding(buildingType);
        for (var vertex : building.getBoundingPolygonVertices()) {
            placedBuildings.put(new Coord(row + vertex.getRow(), column + vertex.getColumn()),
                    building);
        }
        building.setMapPosition(new Coord(row, column));
        count++;

    }

    public int getBuildingCount() {
        return count;
    }

    public void drawDebugGrid(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        var h = Gdx.graphics.getHeight();
        var w = Gdx.graphics.getWidth();

        for (float y = 0; y <= h; y++) {
            shapeRenderer.line(0, y, w, y);
        }
        for (float x = 0; x <= w; x++) {
            shapeRenderer.line(x, 0, x, h);
        }

        shapeRenderer.end();
    }

    public void setView(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
    }
    //TODO: This is rushed. Will implement a better solution later but this works for now.
    public void renderBuildings() {
        batch.begin();
        for (var building : placedBuildings.values()) {
            Sprite bs = buildingAtlas.createSprite(building.getAtlasRegion());
            bs.setScale(unitScale);
            bs.setOrigin(0, 0);
            bs.setPosition(building.getMapPos().getRow(), building.getMapPos().getColumn());
            bs.draw(batch);
        }
        batch.end();
    }
}
