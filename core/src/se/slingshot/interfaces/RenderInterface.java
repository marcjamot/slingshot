package se.slingshot.interfaces;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * DESC
 *
 * @author Marc
 * @since 2015-12
 */
public interface RenderInterface {
    void render(Camera camera, SpriteBatch spriteBatch, float pixelPerMeter);
}
