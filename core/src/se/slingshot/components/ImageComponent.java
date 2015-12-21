package se.slingshot.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

/**
 * DESC
 *
 * @author Marc
 * @since 2015-12
 */
public class ImageComponent implements Component {
    public final Texture texture;


    public ImageComponent(String path) {
        texture = new Texture(path);
    }
}
