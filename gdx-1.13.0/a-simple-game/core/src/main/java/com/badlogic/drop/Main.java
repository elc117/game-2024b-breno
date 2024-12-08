package com.badlogic.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;



public class Main implements ApplicationListener {
    Texture backgroundTexture;
    Texture marioTexture;
    Texture splashTexture;
    Sprite marioSprite;
    Sprite splashSprite;
    Music music;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Vector2 touchPos;
    boolean clickedSplash;
    BitmapFont font;
    Array<String> questions;
    int currentQuestionIndex;
    private String currentQuestion;
    public Preloader preloader;


    @Override
    public void create() {
        backgroundTexture = new Texture("background.png");
        marioTexture = new Texture("mario.png");
        splashTexture = new Texture("splash.png");
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        marioSprite = new Sprite(marioTexture);
        marioSprite.setSize(1, 1);
        marioSprite.setPosition(1, 2);
        splashSprite = new Sprite(splashTexture);
        splashSprite.setSize(4, 2);
        splashSprite.setPosition(2, 1.5f);
        touchPos = new Vector2();
        clickedSplash = false;

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(0.1f); //preciso arrumar o tamanho da fonte

        questions = new Array<>();
        questions.add("Qual é a formação geológica mais conhecida no Caçapava Geoparque Mundial da Unesco?");
        questions.add("Em qual bioma está inserido o Caçapava Geoparque Mundial da Unesco?");
        questions.add("Qual a importância do Caçapava Geoparque Mundial da Unesco para a educação científica?");
        questions.add("Quando o Caçapava Geoparque Mundial da Unesco foi oficialmente reconhecido como geoparque?");
        questions.add("Qual a área aproximada do Caçapava Geoparque Mundial da Unesco?");
        questions.add("Qual é a vegetação predominante no Caçapava Geoparque Mundial da Unesco?");
        questions.add("Qual das seguintes instituições tem parceria com o Caçapava Geoparque Mundial da Unesco?");
        questions.add("Qual é a importância histórica da cidade de Caçapava do Sul no contexto do geoparque?");
        questions.add("Qual dos seguintes minerais é explorado na região do Caçapava Geoparque Mundial da Unesco?");
        questions.add("Qual é o significado do nome 'Caçapava'");
        questions.add("Qual é a relação do Caçapava Geoparque Mundial da Unesco com a UNESCO?");
        questions.add("Qual é a importância histórica do Forte Dom Pedro II em Caçapava do Sul?");
        questions.add("Como foi formado o Forte Dom Pedro II");
        currentQuestionIndex = 0;
        currentQuestion = questions.get(currentQuestionIndex);

        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        if (!clickedSplash) splashRender();
        else {
            input();
            draw();
        }
    }

    private void nextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size;
    }

    private void splashRender() {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        splashSprite.draw(spriteBatch);
        spriteBatch.end();

        if (Gdx.input.isTouched()) {
            clickedSplash = true;
        }

            preloader.preloadBundle("delayed-loading", bundle -> {
                music = Gdx.audio.newMusic(Gdx.files.internal("delayed-loading/music.mp3"));
                music.setLooping(true);
                music.setVolume(.5f);
                music.play();
            });
        }

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            marioSprite.translateX(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            marioSprite.translateX(-speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            marioSprite.translateY(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            marioSprite.translateY(-speed * delta);
        }
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            marioSprite.setCenter(touchPos.x, touchPos.y);
        }

        // Restringe o movimento do Mario dentro dos limites
        marioSprite.setX(MathUtils.clamp(marioSprite.getX(), 0, viewport.getWorldWidth() - marioSprite.getWidth()));
        marioSprite.setY(MathUtils.clamp(marioSprite.getY(), 0, viewport.getWorldHeight() - marioSprite.getHeight()));

        // Troca de pergunta ao pressionar a barra de espaço
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            nextQuestion();
        }
    }

    private void draw() {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        marioSprite.draw(spriteBatch);

        font.draw(spriteBatch, questions.get(currentQuestionIndex),
            0.5f, viewport.getWorldHeight() - 0.5f);
        spriteBatch.end();
    }



    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
    }

}
