package Scenes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.dungeoncoder.screens.*;
import com.mygdx.dungeoncoder.utils.SaveProcessor;
import com.mygdx.dungeoncoder.values.DefaultValues;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.dungeoncoder.DungeonCoder;


import java.awt.*;
import java.io.FileNotFoundException;

public class AdventurerHud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private static Integer score;

    private Label countdownLabel;
    private static Label scoreLabel;

    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label adventurerLabel;
    private Label attemptLabel;
    private Label attempt;
    private Label deadlineLabel;
    private Label deadline;
    private Label progressLabel;
    private Label progress;
    private String attemptInfo;
    private String deadlineInfo;
    private String progressInfo;
    private Music music;
    private DungeonCoder game;

    public AdventurerHud(SpriteBatch sb, TaskTwo screen){
        game = screen.getGame();
        music = DungeonCoder.manager.get("UIElements/Animation/backgroundmusic.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.40f);
        music.play();

        worldTimer = 300;
        timeCount = 0;
        score = 0;
        viewport = new FitViewport(DefaultValues.VIRTUAL_WIDTH,DefaultValues.VIRTUAL_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport,sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d",worldTimer),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        adventurerLabel = new Label("Points", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d",score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        levelLabel = new Label("1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("STAGE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        attemptInfo = shareVariable.connect.requestTaskInformation("Task1","Attempts");

        attemptLabel = new Label("Attempt", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        attempt = new Label(attemptInfo, new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        deadlineInfo = shareVariable.connect.requestTaskInformation("Task1","Deadline");

        deadlineLabel = new Label("Deadline", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        deadline = new Label(deadlineInfo, new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        progressInfo = shareVariable.connect.requestTaskInformation("Task1","Completion");

        SaveProcessor sp = new SaveProcessor();
        progressLabel = new Label("Progress", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        progress = new Label(sp.getInsCleared() + " stages cleared", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        countdownLabel.setFontScale(2);
        scoreLabel.setFontScale(2);
        timeLabel.setFontScale(2);
        levelLabel.setFontScale(2);
        worldLabel.setFontScale(2);
        adventurerLabel.setFontScale(2);
        attemptLabel.setFontScale(2);
        attempt.setFontScale(2);
        deadlineLabel.setFontScale(2);
        deadline.setFontScale(2);
        progressLabel.setFontScale(2);
        progress.setFontScale(2);


        table.add(worldLabel).expandX().padTop(10).padLeft(50);
        table.add(adventurerLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(attemptLabel).expandX().padTop(10);
        table.add(deadlineLabel).expandX().padTop(10);
        table.add(progressLabel).expandX().padTop(10);

        table.row();

        table.add(levelLabel).expandX().padLeft(50);
        table.add(scoreLabel).expandX();
        table.add(countdownLabel).expandX();
        table.add(attempt).expandX();
        table.add(deadline).expandX();
        table.add(progress).expandX();

        stage.addActor(table);
    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            worldTimer--;
            countdownLabel.setText(String.format("%03d",worldTimer));
            timeCount = 0;
        }

        if(worldTimer == 0){
           gameOver(game);
        }

    }

    public void stopMusic(){
        music.stop();
    }

    public String getProgressInfo(){
        return progressInfo;
    }

    private void gameOver(DungeonCoder g) {
        game.setScreen(new TimeOver(game));
        music.stop();
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d",score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
