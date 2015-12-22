package se.slingshot.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

/**
 * Handles images and animation
 *
 * @author Marc
 * @since 2015-12
 */
public class RenderComponent implements Component {
    public final Texture[] textures;
    public final boolean repeatAnimation;
    public final float timePerAnimation;

    public int animationIndex;
    public float animationDeltaTime;

    public RenderComponent(Texture[] textures, boolean repeatAnimation, float timePerAnimation) {
        this.textures = textures;
        this.repeatAnimation = repeatAnimation;
        this.timePerAnimation = timePerAnimation;
    }
}
