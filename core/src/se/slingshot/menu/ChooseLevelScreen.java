package se.slingshot.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.slingshot.interfaces.ScreenInterface;

/**
 * DESC
 *
 * @author Marc
 * @since 2016-01
 */
public class ChooseLevelScreen implements Screen, InputProcessor {
    private final ScreenInterface screenHandler;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture background;

    public ChooseLevelScreen(ScreenInterface screenHandler) {
        this.screenHandler = screenHandler;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);

        camera = new OrthographicCamera(2.55f, 3.3f);
        batch = new SpriteBatch();
        background = new Texture("chooselevel.jpg");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
        batch.setTransformMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        batch.dispose();
        background.dispose();
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float x = (float) screenX / Gdx.graphics.getWidth();
        float y = (float) screenY / Gdx.graphics.getHeight();

        // Level 1
        if (0.1f < x && x < 0.28f && 0.58f < y && y < 0.74f) {
            screenHandler.startLevel("level_1.json");
        }
        System.out.println("x[" + x + "] y[" + y + "]");
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
