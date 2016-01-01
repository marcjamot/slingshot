package se.slingshot.implementations;

import com.badlogic.gdx.graphics.Texture;

/**
 * Holds all textures that belonds to the same animation
 *
 * @author Marc
 * @since 2016-01
 */
public class Animation {
    public final String name;
    public final Texture[] textures;

    public Animation(String name, Texture[] textures) {
        this.name = name;
        this.textures = textures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Animation animation = (Animation) o;

        return name.equals(animation.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
