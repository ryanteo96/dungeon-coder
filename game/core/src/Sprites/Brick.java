package Sprites;

import Scenes.Hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.screens.MarioGame;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class Brick extends InteractiveTileObject {

    public Brick(MarioGame screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(DefaultValues.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if(mario.isBig()) {
            Gdx.app.log("Brick", "Collision");
            //with the destroyed bit, after the first collision,
            //the filter will filter out the brick object as if the brick is not there anymore
            setCategoryFilter(DefaultValues.DESTROYED_BIT);
            getCell().setTile(null); //basically remove the tile
            Hud.addScore(200);
            DungeonCoder.manager.get("Mario/sounds/breakblock.wav", Sound.class).play();
        }
        DungeonCoder.manager.get("Mario/sounds/bump.wav", Sound.class).play();
    }
}
