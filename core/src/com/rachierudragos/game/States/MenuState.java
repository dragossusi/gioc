package com.rachierudragos.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.rachierudragos.game.MyGame;

/**
 * Created by Dragos on 19.05.2016.
 */
public class MenuState extends State {
    private Texture background;
    private Texture playbtn;
    private int scor;
    private Preferences preferences;
    private Rectangle textureBounds;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("bg.png");
        playbtn = new Texture("play.png");
        preferences = Gdx.app.getPreferences("highscore");
        scor = preferences.getInteger("scor", 0);
        textureBounds = new Rectangle(MyGame.WIDTH / 2 - playbtn.getWidth() / 2, MyGame.HEIGHT / 2 - 150, 98, 40);
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            float clickX = Gdx.input.getX();
            float clickY = MyGame.HEIGHT - Gdx.input.getY();
            Gdx.app.log("x :", String.valueOf(clickX));
            Gdx.app.log("y :", String.valueOf(clickY));

            if (textureBounds.contains(clickX, clickY)) {
                gsm.set(new PlayState(gsm));
                dispose();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        cam.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0, 0, MyGame.WIDTH, MyGame.HEIGHT);
        sb.draw(playbtn, MyGame.WIDTH / 2 - playbtn.getWidth() / 2, MyGame.HEIGHT / 2 - 150);
        if (scor != 0) {
            int aux = scor;
            int xScor = 230;
            while (aux != 0) {
                xScor -= 12;
                aux /= 10;
            }
            BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
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
        shapeRenderer.rect(textureBounds.getX(),textureBounds.getY(),textureBounds.width,textureBounds.height);
        shapeRenderer.end();
        */
    }

    @Override
    public void dispose() {
        background.dispose();
        playbtn.dispose();
    }
}
