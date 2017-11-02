package Sprites;

import Scenes.Hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.screens.TaskThree;
import com.mygdx.dungeoncoder.values.DefaultValues;
import com.mygdx.dungeoncoder.values.ItemDef;
import com.mygdx.dungeoncoder.values.Mushroom;

public class Coin extends InteractiveTileObject{
    private static TiledMapTileSet tileSet;//start counting on 1 instead of 0
    private final int BLANK_COIN = 28; //in the tile its ID is 27 but because everything starts from 1 so we add 1 to it

    public Coin(TaskThree screen, MapObject object){
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(DefaultValues.COIN_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Coin", "Collision");
        if(getCell().getTile().getId() == BLANK_COIN){
            DungeonCoder.manager.get("Mario/sounds/bump.wav", Sound.class).play();
        }else{
            if(object.getProperties().containsKey("mushroom")){
                //spawn a mushroom at this position
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x,body.getPosition().y + 16/DefaultValues.PPM),
                        Mushroom.class));
                DungeonCoder.manager.get("Mario/sounds/powerup_spawn.wav", Sound.class).play();
            }else{
                DungeonCoder.manager.get("Mario/sounds/coin.wav", Sound.class).play();
            }
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(150);

    }
}
