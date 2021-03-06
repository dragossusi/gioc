package com.rachierudragos.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.rachierudragos.game.MyGame;

/**
 * Created by Dragos on 19.05.2016.
 */
public class MenuState extends State {
    private Texture background;
    private Texture playbtn;
    private Texture dualplaybtn;
    private Texture settings;
    private int scor;
    private Preferences preferences;
    private Rectangle collidePlayBtn;
    private Rectangle collideDualPlayBtn;
    private Circle collideSettings;
    private boolean creez = false;
    private BitmapFont font;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        cam.update();
        background = new Texture("bg2.jpg");
        playbtn = new Texture("play.png");
        dualplaybtn = new Texture("dualbutton.png");
        settings = new Texture("settings.png");
        preferences = Gdx.app.getPreferences("highscore");
        scor = preferences.getInteger("scor", 0);
        collidePlayBtn = new Rectangle(MyGame.WIDTH / 2 - playbtn.getWidth() / 2, MyGame.HEIGHT / 2 - 150, 98, 40);
        collideDualPlayBtn = new Rectangle(MyGame.WIDTH / 2 - dualplaybtn.getWidth() / 2, MyGame.HEIGHT / 2 - 100, 84, 40);
        collideSettings = new Circle();
        collideSettings.set(MyGame.WIDTH - 40, MyGame.HEIGHT - 40, 24);
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            float clickX = Gdx.input.getX();
            float clickY = Gdx.input.getY();
            Vector3 input = new Vector3(clickX, clickY, 0);
            cam.unproject(input);
            if (collidePlayBtn.contains(input.x, input.y)) {
                //Normal
                gsm.set(new PlayState(gsm));
                dispose();
            } else if (collideDualPlayBtn.contains(input.x, input.y)) {
                //Dual
                if (preferences.getString("nume", "").equals(""))
                    Gdx.input.getTextInput(new Input.TextInputListener() {
                        @Override
                        public void input(String text) {
                            creez = true;
                            preferences.putString("nume", text).flush();
                            Gdx.app.log("nume ", text);
                        }

                        @Override
                        public void canceled() {
                            creez = false;
                        }
                    }, "Numele jucatorului:", "", "Georgel");
                else creez = true;
            } else if (collideSettings.contains(input.x, input.y)) {
                //Settings
                gsm.set(new SettingsState(gsm));
                dispose();
            }
        }
        if (creez) {
            gsm.set(new DualPlayState(gsm));
            dispose();
        }
    }


    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0, 0, MyGame.WIDTH, MyGame.HEIGHT);
        sb.draw(playbtn, MyGame.WIDTH / 2 - playbtn.getWidth() / 2, MyGame.HEIGHT / 2 - 150);
        sb.draw(dualplaybtn, MyGame.WIDTH / 2 - dualplaybtn.getWidth() / 2, MyGame.HEIGHT / 2 - 100);
        sb.draw(settings, MyGame.WIDTH - 64, MyGame.HEIGHT - 64, 48, 48);
        if (scor != 0) {
            int aux = scor;
            int xScor = 230;
            while (aux != 0) {
                xScor -= 12;
                aux /= 10;
            }
            font.setColor(Color.WHITE);
            font.draw(sb, String.valueOf(scor), xScor, 600);
            boolean nou = preferences.getBoolean("nou", false);
            if (nou)
                font.draw(sb, "New highscore!", 80, 700);
        }
        sb.end();
        /*
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.circle(collideSettings.x,collideSettings.y,24);
        shapeRenderer.end();
        */
    }

    @Override
    public void dispose() {
        background.dispose();
        playbtn.dispose();
    }
}
