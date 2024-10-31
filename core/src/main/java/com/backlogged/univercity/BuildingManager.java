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
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set; 
import java.util.Map.Entry;


public class BuildingManager implements Disposable {

    private static class BuildingFactory {
        private static HashMap<String, BuildingInfo> buildingMap = new HashMap<>();
        private static TextureAtlas buildingAtlas;
        private static ArrayList<Coord> getBoundingPolygonVerticesFromJson(JsonValue vertices) {
            var boundingPolygonVertices = new ArrayList<Coord>();
            for (var vertex : vertices) {
                boundingPolygonVertices.add(new Coord(vertex.getInt("row"), vertex.getInt("column")));
            }
            return boundingPolygonVertices;
        }

        public static void getBuildingsFromJson(FileHandle buildingsJsonFile, FileHandle textureAtlasFile, float unitScale) {
            buildingAtlas = new TextureAtlas(textureAtlasFile);
            JsonValue buildings = new JsonReader().parse(buildingsJsonFile);
            for (var building : buildings) { 
                Sprite sprite = buildingAtlas.createSprite(building.get("atlasRegion").asString());
                sprite.setScale(unitScale); 
                sprite.setOrigin(0, 0);
                buildingMap.put(building.name,
                        new BuildingInfo(building.get("type").asStringArray(),
                                getBoundingPolygonVerticesFromJson(
                                        building.get("boundingPolygonVertices")),
                                building.get("info").asString(), sprite));
            }
        } 
        public static Set<Entry<String, BuildingInfo>> getBuildings() { 
            return buildingMap.entrySet();
        } 
        public static TextureAtlas getBuildingAtlas() { 
            return buildingAtlas;
        }
        public static AbstractBuilding createBuilding(String building) {
            switch (building) {
                case "LectureHall":
                    return new LectureHall(buildingMap.get(building));
                case "Accommodation": 
                    return new Accommodation(buildingMap.get(building)); 
                case "SportsCenter": 
                    return new SportsCenter(buildingMap.get(building)); 
                case "FoodCourt": 
                    return new FoodCourt(buildingMap.get(building));
                default:
                    throw new IllegalArgumentException(String.format(
                            "Building %s is either unimplemented or does not exist", building));
            }
        }
    }
    private static BuildingManager instance;
    private TiledMapTileLayer terrainLayer;
    private HashMap<Coord, AbstractBuilding> placedBuildings = new HashMap<>();
    private SpriteBatch batch = new SpriteBatch();
    private int count = 0;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private BuildingManager(TiledMap map, float unitScale, String buildingAtlasPath) {
        if (map == null) {
            throw new IllegalArgumentException("the TiledMap was null, has to be initialized");
        } 
        BuildingFactory.getBuildingsFromJson(Gdx.files.internal("Buildings.json"), Gdx.files.internal(buildingAtlasPath), unitScale);
        terrainLayer = (TiledMapTileLayer) map.getLayers().get("Terrain"); 
    } 

    public static BuildingManager getInstance(TiledMap map, float unitScale, String buildingAtlasPath) {
        if (instance == null) {
            instance = new BuildingManager(map, unitScale, buildingAtlasPath);
        }
        return instance;
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
 
    public void renderBuildings(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined); 
        batch.begin();
        for (var building : placedBuildings.values()) {
            Sprite bs = building.getSprite();
            bs.setPosition(building.getMapPos().getRow(), building.getMapPos().getColumn());
            bs.draw(batch);
        }
        batch.end();
    } 
    public void dispose() { 
        var atlas = BuildingFactory.getBuildingAtlas(); 
        if (atlas != null) atlas.dispose();
        batch.dispose(); 
        shapeRenderer.dispose();
    }
}
