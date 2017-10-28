package Sprites;

import Scenes.Hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.screens.TaskThree;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class Coin extends InteractiveTileObject{
    private static TiledMapTileSet tileSet;//start counting on 1 instead of 0
    private final int BLANK_COIN = 28; //in the tile its ID is 27 but because everything starts from 1 so we add 1 to it

    public Coin(TaskThree screen, Rectangle bounds){
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(DefaultValues.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collision");
        if(getCell().getTile().getId() == BLANK_COIN){
            DungeonCoder.manager.get("Mario/sounds/bump.wav", Sound.class).play();
        }else{
            DungeonCoder.manager.get("Mario/sounds/coin.wav", Sound.class).play();
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(150);

    }
}
