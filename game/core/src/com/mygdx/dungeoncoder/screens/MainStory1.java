package com.mygdx.dungeoncoder.screens;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import Scenes.AdventurerHud;
import Sprites.Adventurer.Adventurer;
import Sprites.Adventurer.AdventurerContactListener;
import Sprites.Enemy;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.utils.*;
import com.mygdx.dungeoncoder.values.DefaultValues;


import static com.mygdx.dungeoncoder.DungeonCoder.V_HEIGHT;
import static com.mygdx.dungeoncoder.DungeonCoder.V_WIDTH;
import static com.mygdx.dungeoncoder.values.DefaultValues.*;


public class MainStory1 implements Screen {
    //Write files
    CodeEvaluator codeEvaluator;
    SaveProcessor saveProcessor;
    BufferedWriter bw = null;
    FileWriter fw = null;
    private TextButton runButton;

    private DungeonCoder game;
    private Stage stage;
    Skin backButtonSkin;
    private TextureAtlas atlas;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private AdventurerHud hud;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //sprites
    private static Adventurer player;

    //textArea
    private TextArea textArea;
    private Skin skin;
    private Skin cancelButtonSkin;

    //buttons
    private TextButton codeButton;
    private TextButton okButton;
    private TextButton noButton;
    private TextButton comeBackNextTimeButton;
    private TextButton viewTaskButton;
    private TextButton hintButton;
    private TextButton quest2YesButton;
    private TextButton quest2NoButton;


    //boolean
    private boolean codeOn;
    private boolean quest1 = false;
    private boolean quest2 = false;
    private boolean quest1Passed = false;
    private boolean quest2Passed = false;
    private boolean playQuest1Again = true;
    private boolean playQuest2Again = true;

    private Window window;

    private Dialog dialog;
    private Dialog dialog2;

    private int progress = 0;
    private int progressInsideTaskTwo = 0;
    private int score = 0;
    public GifRecorder gifRecorder;

    private File file;

    public MainStory1(DungeonCoder g) throws FileNotFoundException {
        game = g;
        saveProcessor = new SaveProcessor();
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);

        codeOn = false;

        gifRecorder = new GifRecorder(game.batch);

        atlas = new TextureAtlas("Dungeon/Adventurer.pack");

        //create cam to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(V_WIDTH / DefaultValues.PPM, V_HEIGHT / DefaultValues.PPM, gamecam);

        //get compiling
        codeEvaluator = new CodeEvaluator();

        //Load our map and setup our map renderer
        mapLoader = new TmxMapLoader();

        map = mapLoader.load("mainstory.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / DefaultValues.PPM);

        //set camera at center at the start of the map
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        //pass the world and map to B2WorldCreator.java
        creator = new B2WorldCreator(this);

        //create adventurer in our world
        player = new Adventurer(this);

        //create our game HUD for scores/timers/level info
        hud = new AdventurerHud(game.batch, this);

        world.setContactListener(new AdventurerContactListener());

        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        Label gifInstruction = new Label("To use the GIF recording function, press F1 to activate it and\n press F1 first time to start recording and second time to end recording", skin); //display deadline from the database
        gifInstruction.setFontScale(1f, 1f);
        gifInstruction.setPosition(790, 7);
        gifInstruction.setColor(Color.WHITE);
        stage.addActor(gifInstruction);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //current date
        //back button
        createBack();
        createTextArea();
        createHint();
    }

    public DungeonCoder getGame() {
        return game;
    }

    private void createBack() {
        backButtonSkin = new Skin(Gdx.files.internal("comic-ui.json"));
        TextButton btnBack = new TextButton("Back", backButtonSkin);
        btnBack.setPosition(0, 600);
        btnBack.setSize(100, 100);
        btnBack.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setScreen(new FreeBattleMode(game));
                hud.stopMusic();
            }
        });
        stage.addActor(btnBack);
    }

    private void createTextArea() throws FileNotFoundException {
        //to build string into the text file
        StringBuilder sb = new StringBuilder();
        // The name of the file to open.
        String fileName = "StageTwo.java";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            //FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            //BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/LCLY/Desktop/Dungeon/dungeon-coder/game/core/src/com/mygdx/dungeoncoder/screens/StageTwo.java"));
            BufferedReader bufferedReader = new BufferedReader(new FileReader("StageTwo.java"));

            while ((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                sb.append(line);
                sb.append("\n");
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file when screen is loading '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file when screen is loading '" + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }

        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        String textFileString = sb.toString();
        textArea = new TextArea(textFileString, skin);
        textArea.setX(50);
        textArea.setY(70);
        textArea.setWidth(500);
        textArea.setHeight(500);

        //
        okButton = new TextButton(" I am ready! ", skin);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                stage.addActor(viewTaskButton);
                stage.addActor(codeButton);
                stage.addActor(hintButton);
            }
        });

        //No and then show another dialog and then go back to instructional mode
        noButton = new TextButton(" No, I need more time! ", skin);
        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                new Dialog("Dr.Robot NPC", skin, "dialog") {
                    protected void result(Object object) {
                    }
                }.text("    I guess you are not ready yet, come back next time  ").button(comeBackNextTimeButton, true).
                        key(Input.Keys.ENTER, true).show(stage);

            }
        });

        viewTaskButton = new TextButton("View your mission", skin);
        viewTaskButton.setHeight(50);
        viewTaskButton.setPosition(200, 10);
        viewTaskButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                Object[] listEntries = {"Mission 1",
                        "You have encountered an enemy",
                        "Here we will be learning about how to print words in Java",
                        "",
                        "In Java you can write this to print a line",
                        "",
                        "System.out.println(); OR System.out.print();",
                        "",
                        "This line of code will print out 'Hello World' in Java",
                        "",
                        "Now it's your turn to try",
                        "Dr.Robot is in your way, you need to shout at him to make him get out of the way",
                        "To do that you need to print out the word 'ARGHHHH'",
                        "",
                        "================================================================================================",
                        "Note:",
                        "Please only write your code in the section '// USER WRITE CODE HERE' and",
                        "// DO NOT WRITE CODE PAST THIS POINT"};


                Object[] listEntries2 = {"Mission 2",
                        "You have encountered a new enemy",
                        "",
                        "In this mission, you will need to use Strings.",
                        "Strings are a sequence of characters. In Java programming language, strings",
                        "are created as objects.",
                        "The Java platform provides the String class to create and manipulate strings.",
                        "To create Strings: ",
                        "",
                        "String greeting = \"Hello world!\"",
                        "",
                        "Whenever it encounters a string literal in your code, the compiler creates a String object",
                        "with its value in this case, \"Hello world!\"",
                        "To print it, you can just this",
                        "",
                        "System.out.println(greeting)",
                        "Instead of printing 'ARGHHHH' this time, you will need to print,",
                        "\"No one can stop a Dungeon Coder!\"",
                        "================================================================================================",
                        "Note:",
                        "Please only write your code under the '// USER WRITE CODE HERE' section"};

                //create Text using lists and scrollpane
                List list = new List(skin);
                System.out.println("first quest is: " + DefaultValues.questActivated);
                System.out.println("Second quest is: " + DefaultValues.quest2Activated);
                if (quest1 == true) {
                    list.setItems(listEntries);
                } else if (quest2 == true) {
                    list.setItems(listEntries2);
                }

                ScrollPane scrollPane = new ScrollPane(list, skin);
                scrollPane.setSize(0, 0);
                scrollPane.setFlickScroll(false);
                scrollPane.setScrollingDisabled(true, false);
                Table table = new Table(skin);
                table.setWidth(100f);
                table.add().growX().row();
                table.add(scrollPane).grow();
                int i = 88;
                char p = (char) i;
                cancelButtonSkin = new Skin(Gdx.files.internal("dialogSkins/plain-james-ui.json"));
                TextButton closeButton = new TextButton(String.valueOf(p), skin);
                TextButton closeButtonToo = new TextButton("Close", cancelButtonSkin, "default");
                window = new Window("Task 1", skin);
                window.setDebug(false);
                window.getTitleTable().add(closeButton).height(window.getPadTop());
                window.setPosition(600, 70);
                //window.defaults().spaceBottom(10);//not sure what does this do
                window.setSize(600, 550);
                window.add(scrollPane).colspan(3).left().expand().fillY();
                window.row();
                window.right().bottom();
                window.add(closeButtonToo).padLeft(500);
                stage.addActor(window);
                viewTaskButton.remove();
                //close button on top 'X' button
                closeButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        window.remove();
                        stage.addActor(viewTaskButton);
                    }
                });

                //close button
                closeButtonToo.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        window.remove();
                        stage.addActor(viewTaskButton);
                    }
                });
            }
        });


        //return to instructional
        comeBackNextTimeButton = new TextButton("Ok", skin);
        comeBackNextTimeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                hud.stopMusic();
                game.setScreen(new FreeBattleMode(game));
            }
        });

        dialog = new Dialog("Dr.Robot NPC", skin, "dialog") {
            public void result(Object obj) {
            }
        };

        dialog.text("Hi, " + DefaultValues.username + ", Welcome to the Dungeon!\nTo gain points and complete the stage,you\n will need to solve these problems by using\n Java programming, Are you ready?");
        dialog.button(okButton, true); //sends "true" as the result
        dialog.button(noButton, false);
        dialog.setPosition(500, 300);
        dialog.setHeight(150);
        dialog.setWidth(330);


        quest2YesButton = new TextButton("  Yes  ", skin);
        quest2YesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                //yes is do nothing
            }
        });

        quest2NoButton = new TextButton("  No  ", skin);
        quest2NoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                new Dialog("Mr.Katana NPC", skin, "dialog") {
                    protected void result(Object object) {
                    }
                }.text("You will not be able to complete the stage until\nyou earn enough points from both of us!").button("Ok", true).show(stage);
            }
        });


        dialog2 = new Dialog("Mr.Katana NPC", skin, "dialog") {
            public void result(Object obj) {
            }
        };

        dialog2.text("Looks like you have passed your first test, \nbut what about my test?\nAre you prepared?");
        dialog2.button(quest2YesButton, true); //sends "true" as the result
        dialog2.button(quest2NoButton, false);
        dialog2.setPosition(500, 300);
        dialog2.setHeight(130);
        dialog2.setWidth(350);

        runButton = new TextButton("Run", skin);
        runButton.setPosition(460, 10);
        runButton.setSize(100, 50);
        file = new File("StageTwo.java");
        runButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                String code = textArea.getText();
                //System.out.println("textarea:" + code);

                String path = file.getAbsolutePath();
                System.out.println("The file path of test file is " + path);
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(code);
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                System.out.println("Code saved!");
                String fileName = "StageTwo.java";
                String className = "StageTwo.class";
                String runName = className.substring(0, className.length() - 6);
                File classFile = new File(className);
                String classPath = classFile.getAbsolutePath();
                classPath = classPath.substring(0, classPath.length() - (className.length()));
                System.out.println("classPath is: " + classPath);

                // This will reference one line at a time
                String line = "";

                try {
                    String filepath = file.getAbsolutePath();
                    System.out.println(filepath);
                    if (codeEvaluator.evaluate(filepath) == true) {
                        System.out.println("it gets in the if statement");
                        codeEvaluator.run(classPath, runName);
                    }
                    System.out.println("Code Evaluator: " + codeEvaluator.evaluate(filepath));
                    FileReader fileReader = new FileReader("code.txt");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println("THE LINE PRINTED IS: " + line);
                        System.out.println("playQuest1Again: " + playQuest1Again);
                        if (quest1 == true && quest2 == false) {
                            if (playQuest1Again == true) {
                                if (line.equals("ARGHHHH")) {
                                    quest1Passed = true;
                                    playQuest1Again = false;
                                } else {
                                    quest1Passed = false;
                                    playQuest1Again = true;
                                }
                                questOne();
                            } else {
                                new Dialog("Dr.Robot NPC", skin, "dialog") {
                                    protected void result(Object object) {
                                    }
                                }.text("You have already completed my quest, you may continue your journey.").button("     Ok     ", true).show(stage);
                            }
                        } else if (quest2 == true && quest1 == false) {
                            if (playQuest2Again == true) {
                                if (line.equals("No one can stop a Dungeon Coder!")) {
                                    quest2Passed = true;
                                    playQuest2Again = false;
                                } else {
                                    quest2Passed = false;
                                    playQuest2Again = true;
                                }
                                System.out.println("quest2Passed: " + quest2Passed);
                                questTwo();
                            } else {
                                new Dialog("Mr.Katana NPC", skin, "dialog") {
                                    protected void result(Object object) {
                                    }
                                }.text("You have already completed my quest, you may continue your journey.").button("     Ok     ", true).show(stage);
                            }
                        }
                        shareVariable.connect.requestUpdateProgress(file,"Task1",progressInsideTaskTwo);
                    }
                    bufferedReader.close();
                } catch (FileNotFoundException ex) {
                    System.out.println("Unable to open file when run is being clicked'" + fileName + "'");
                } catch (IOException ex) {
                    System.out.println("Error reading file when run is being clicked'" + fileName + "'");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                System.out.println("the progress: "+progressInsideTaskTwo);
            }

        });


        codeButton = new TextButton("Code Here", skin);
        codeButton.setPosition(50, 10);
        codeButton.setSize(130, 50);
        codeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                if (codeOn == false) {
                    stage.addActor(textArea);
                    stage.addActor(runButton);
                    codeOn = true;
                } else {
                    textArea.remove();
                    runButton.remove();
                    codeOn = false;
                }
            }
        });
        //to find the path of the file
        //System.out.println("File path: " + new File("test.txt").getAbsolutePath());

    }

    private void questOne() {
        if (quest1Passed == true) {
            progress = 40;
            progressInsideTaskTwo += 40;
            score = 150;
            hud.addProgress(progress);
            hud.addScore(score);
            DungeonCoder.manager.get("UIElements/Animation/questcompleted.wav", Music.class).play();
            new Dialog("Dr.Robot NPC", skin, "dialog") {
                protected void result(Object object) {
                }
            }.text("Congratualtion! You have passed my test! \nBut beware, a greater task is yet to come.").button("     Ok     ", true).show(stage);
        } else if (quest1Passed == false) {
            DungeonCoder.manager.get("UIElements/Animation/fail.mp3", Music.class).play();
            new Dialog("Dr.Robot NPC", skin, "dialog") {
                protected void result(Object object) {
                }
            }.text("Too bad... But don't be disappointed, you can still do it!").button("Try again", true).show(stage);
        }
    }

    private void questTwo() {
        if (quest2Passed == true) {
            score = 300;
            progress = 60;
            progressInsideTaskTwo += 60;
            hud.addProgress(progress);
            hud.addScore(score);
            DungeonCoder.manager.get("UIElements/Animation/questcompleted.wav", Music.class).play();
            new Dialog("Mr.Katana NPC", skin, "dialog") {
                protected void result(Object object) {
                }
            }.text("Congratulation! You have earned my respect and you shall pass.").button("     Ok     ", true).show(stage);
        } else if (quest2Passed == false) {
            DungeonCoder.manager.get("UIElements/Animation/fail.mp3", Music.class).play();
            new Dialog("Mr.Katana NPC", skin, "dialog") {
                protected void result(Object object) {
                }
            }.text("Don't be disappointed, you can still do it!").button("Try again", true).show(stage);
        }
    }


    private void createHint() {
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        hintButton = new TextButton("Hint", skin);
        hintButton.setPosition(350, 10);
        hintButton.setSize(100, 30);
        hintButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                new Dialog("Hint", skin, "dialog") {
                    protected void result(Object object) {
                        System.out.println("CLICKED");
                    }
                }.text("This will print the lines and moves the cursor to a new line\n System.out.println(\"SOMETHING\");\n\n" +
                        "This will just print the string without going to the next line\n System.out.print(\"SOMETHING\");\n\n" +
                        "TIP: you can include a '\\n' at the back of the string to create a next line").button("     Ok     ", true).
                        key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);
            }
        });
    }


    public boolean gameOver() {
        if (player.currentState == Adventurer.State.DEAD && player.getStateTimer() > 3) {
            return true;
        } else {
            return false;
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }


    @Override
    public void show() {
        System.out.println("you are in stage two");
    }

    public void handleinput(float dt) {
        //control player using immediate impulses, use world center so the torque wont make the character fly around
        if (player.currentState != Adventurer.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) { // for quick tap
                DungeonCoder.manager.get("UIElements/Animation/jump.wav", Sound.class).setVolume(5, 10);
                DungeonCoder.manager.get("UIElements/Animation/jump.wav", Sound.class).play();
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
                player.currentState = Adventurer.State.JUMPING;
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.previousState == Adventurer.State.JUMPING) {
                    player.b2body.applyLinearImpulse(new Vector2(0, -4f), player.b2body.getWorldCenter(), true);
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) { //isKeyPressed for holding down keys
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
                DungeonCoder.manager.get("UIElements/Animation/footstep.wav", Music.class).play();
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
                DungeonCoder.manager.get("UIElements/Animation/footstep.wav", Music.class).play();
            }
        }

    }

    public void update(float dt) {
        // System.out.println("progress is now: "+progress);
        if (codeOn == true) {
            //do nothing
        } else {
            handleinput(dt);
        }
        //movedRight(dt);
        //takes 1 step in the physics simulation 60 times per second
        world.step(1 / 60f, 6, 2);
        player.update(dt);

        //if character dies, freeze the camera right where he died
        if (player.currentState != Adventurer.State.DEAD) {
            gamecam.position.x = player.b2body.getPosition().x;
        }

        hud.update(dt);

        for (Enemy enemy : creator.getDungeonMonster()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 220 / DefaultValues.PPM) {
                enemy.b2body.setActive(true);//activate goomba
            }
        }
        if (DefaultValues.questActivated == true) {
            quest1 = true;
            quest2 = false;
            DefaultValues.questActivated = false;
            stage.addActor(dialog);
            DungeonCoder.manager.get("UIElements/Animation/robottalking.wav", Music.class).setVolume(2f);
            DungeonCoder.manager.get("UIElements/Animation/robottalking.wav", Music.class).play();
        }

        if (DefaultValues.quest2Activated == true) {
            quest1 = false;
            quest2 = true;
            DefaultValues.quest2Activated = false;
            stage.addActor(dialog2);
            DungeonCoder.manager.get("UIElements/Animation/npc2.mp3", Music.class).setVolume(4f);
            DungeonCoder.manager.get("UIElements/Animation/npc2.mp3", Music.class).play();
        }

        if (touchedFinishline == true) {
            if (progressInsideTaskTwo == 100) {
                if (saveProcessor.checkAchievement() == true) {
                    DungeonCoder.manager.get("UIElements/Animation/achievement.mp3", Sound.class).play();
                }
                DungeonCoder.manager.get("UIElements/Animation/stagecomplete.mp3", Sound.class).play();
                hud.stopMusic();
                touchedFinishline = false;
                saveProcessor.insClear();
                System.out.println("Total clear:" + saveProcessor.getTotalCleared());
                System.out.printf("You have cleared %d instructional stages.\n", saveProcessor.getInsCleared());
                game.setScreen(new StageTwoComplete(game));
            } else {
                touchedFinishline = false;
                new Dialog("Dungeon Coder", skin, "dialog") {
                    protected void result(Object object) {
                    }
                }.text("You have not fulfil the requirement to complete the stage!").button("  Ok  ", true).show(stage);
            }
        }


        //update gamecam with correct corrdinates after changes
        gamecam.update();

        //tell renderer to draw only what our camera can see in our game world
        renderer.setView(gamecam);

    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(172 / 255f, 115 / 255f, 57 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render game map
        renderer.render();

        //update gif
        gifRecorder.update();

        //render our Box2Ddebuglines
        b2dr.render(world, gamecam.combined);

        //set batch to draw what the Hud camera sees.
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getDungeonMonster()) {
            enemy.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (gameOver()) {
            game.setScreen(new FreeBattleGameOver(game));
        }

        stage.act(delta);
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        map.dispose();
        hud.dispose();
        b2dr.dispose();
        renderer.dispose();
        gifRecorder.clearFrames();
        gifRecorder.close();
        stage.dispose();
    }


}
