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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Timer;


public class Main implements ApplicationListener {

    Texture backgroundTexture;
    Texture marioTexture;
    Texture splashTexture;
    Texture bananaTexture;
    Texture coinTexture;
    Texture starTexture;

    Sprite marioSprite;
    Sprite splashSprite;
    Sprite bananaSprite;
    Sprite coinSprite;
    Sprite starSprite;
    SpriteBatch spriteBatch;

    private Sound correctSound;
    private Sound wrongSound;
    private BitmapFont bitmapFont;
    Music music;
    FitViewport viewport;
    Vector2 touchPos;
    boolean clickedSplash;
    int currentQuestionIndex;
    ArrayList<Texture> questionTextures;
    public Preloader preloader;
    Array<Sprite> movingSprites;


    @Override
    public void create() {
        backgroundTexture = new Texture("background.png");
        marioTexture = new Texture("mario.png");
        marioSprite = new Sprite(marioTexture);
        marioSprite.setSize(1, 1);
        marioSprite.setPosition(1, 2);

        splashTexture = new Texture("splash.png");
        spriteBatch = new SpriteBatch();
        splashSprite = new Sprite(splashTexture);
        splashSprite.setSize(2, 2);
        splashSprite.setPosition(2, 1f);

        bananaTexture = new Texture("banana.png");
        bananaSprite = new Sprite(bananaTexture);
        coinTexture = new Texture("coin.png");
        coinSprite = new Sprite(coinTexture);
        starTexture = new Texture("star.png");
        starSprite = new Sprite(starTexture);

        viewport = new FitViewport(16, 9);
        touchPos = new Vector2();
        clickedSplash = false;
        currentQuestionIndex = 0;
        movingSprites = new Array<>();

        bitmapFont = new BitmapFont(); //fonte muito bugada, não achei outra solução
        correctAnswers = new HashMap<>();
        correctAnswers.put(0, coinSprite);
        correctAnswers.put(1, starSprite);
        correctAnswers.put(2, bananaSprite);
        correctAnswers.put(3, coinSprite);
        correctAnswers.put(4, starSprite);
        correctAnswers.put(5, bananaSprite);
        correctAnswers.put(6, coinSprite);
        correctAnswers.put(7, bananaSprite);
        correctAnswers.put(8, coinSprite);
        correctAnswers.put(9, coinSprite);
        score = 0;
        questionTextures = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            questionTextures.add(new Texture("questao" + i + ".png"));
        }

        correctSound = Gdx.audio.newSound(Gdx.files.internal("correct.mp3"));
        wrongSound = Gdx.audio.newSound(Gdx.files.internal("wrong.wav"));

        preloader.preloadBundle("delayed-loading", bundle -> {
            music = Gdx.audio.newMusic(Gdx.files.internal("delayed-loading/music.mp3"));
            music.setLooping(true);
            music.setVolume(0.5f);
            music.play();
        });
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
            checkCollision();
            draw();
        }
    }

    private int score;
    private Map<Integer, Sprite> correctAnswers;
    private static final float MIN_DISTANCE_BETWEEN_SPRITES = 1f;

    private void splashRender() {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        splashSprite.draw(spriteBatch);
        spriteBatch.end();

        if (Gdx.input.isTouched()) {
            clickedSplash = true;
            generateSprites();
        }
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

        marioSprite.setX(MathUtils.clamp(marioSprite.getX(), 0, viewport.getWorldWidth() - marioSprite.getWidth()));
        marioSprite.setY(MathUtils.clamp(marioSprite.getY(), 0, viewport.getWorldHeight() - marioSprite.getHeight()));
    }

    private void generateSprites() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float spriteSize = 1f;

        movingSprites.clear();

        Array<Float> yPositions = new Array<>();
        Sprite correctSprite = correctAnswers.get(currentQuestionIndex);
        float correctY = MathUtils.random(0, worldHeight - spriteSize);
        correctSprite.setSize(spriteSize, spriteSize);
        correctSprite.setPosition(worldWidth, correctY);
        movingSprites.add(new Sprite(correctSprite));
        yPositions.add(correctY);
        Array<Texture> possibleTextures = new Array<>(new Texture[]{bananaTexture, coinTexture, starTexture});
        possibleTextures.removeValue(correctSprite.getTexture(), true);
        for (int i = 0; i < 2; i++) {
            float newY;
            boolean isValid;
            do {
                newY = MathUtils.random(0, worldHeight - spriteSize);
                isValid = true;
                for (float y : yPositions) {
                    if (Math.abs(newY - y) < MIN_DISTANCE_BETWEEN_SPRITES) {
                        isValid = false;
                        break;
                    }
                }
            } while (!isValid);

            yPositions.add(newY);
            Texture randomTexture = possibleTextures.random();
            possibleTextures.removeValue(randomTexture, true);
            Sprite sprite = new Sprite(randomTexture);
            sprite.setSize(spriteSize, spriteSize);
            sprite.setPosition(worldWidth, newY);
            movingSprites.add(sprite);
        }
    }

    private void checkCollision() {
        Array<Sprite> spritesToRemove = new Array<>();
        for (Sprite sprite : movingSprites) {
            if (marioSprite.getBoundingRectangle().overlaps(sprite.getBoundingRectangle())) {
                Sprite correctSprite = correctAnswers.get(currentQuestionIndex);
                if (sprite.getTexture() == correctSprite.getTexture()) {
                    if (correctSound != null) correctSound.play();
                    score++;
                } else {
                    if (wrongSound != null) wrongSound.play();
                }
                currentQuestionIndex++;
                if (currentQuestionIndex >= 10) {
                    renderFinalMessage();
                    Gdx.app.exit();
                    return;
                }
                generateSprites();
                spritesToRemove.add(sprite);
                break;
            }
        }

        movingSprites.removeAll(spritesToRemove, true);
    }

    private void renderFinalMessage() {
        if (music != null && music.isPlaying()) {
            music.stop();
        }
        ScreenUtils.clear(0, 0, 0, 1);
        spriteBatch.begin();
        float scale = viewport.getWorldHeight() / 100f;
        bitmapFont.getData().setScale(scale);
        bitmapFont.setColor(Color.WHITE);
        String finalMessage = "Fim de jogo!\nScore final: " + score;
        bitmapFont.draw(spriteBatch, finalMessage, viewport.getWorldWidth() / 4, viewport.getWorldHeight() / 2);
        spriteBatch.end();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        }, 3);
    }


    private void draw() {
        float delta = Gdx.graphics.getDeltaTime();
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.draw(questionTextures.get(currentQuestionIndex), 0, viewport.getWorldHeight() - 2, 6, 2);
        marioSprite.draw(spriteBatch);

        bitmapFont.getData().setScale(0.05f);
        bitmapFont.draw(spriteBatch, "Score: " + score, 1, viewport.getWorldHeight() - 2);

        for (int i = movingSprites.size - 1; i >= 0; i--) {
            Sprite sprite = movingSprites.get(i);
            sprite.translateX(-1f * delta);
            sprite.draw(spriteBatch);
            if (sprite.getX() + sprite.getWidth() < 0) {
                movingSprites.removeIndex(i);
            }
        }
        spriteBatch.end();
    }


    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        marioTexture.dispose();
        for (Texture question : questionTextures) {
            question.dispose();
        }
        spriteBatch.dispose();
        if (music != null) music.dispose();
        if (correctSound != null) correctSound.dispose();
        if (wrongSound != null) wrongSound.dispose();
    }
}
