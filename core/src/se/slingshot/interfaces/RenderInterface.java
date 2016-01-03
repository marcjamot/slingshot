package se.slingshot.interfaces;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Allows other systems to render using the RenderSystem
 *
 * @author Marc
 * @since 2015-12
 */
public interface RenderInterface {
    void render(Camera camera, SpriteBatch spriteBatch);
}
