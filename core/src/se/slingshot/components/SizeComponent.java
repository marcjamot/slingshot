package se.slingshot.components;

import com.badlogic.ashley.core.Component;

/**
 * DESC
 *
 * @author Marc
 * @since 2015-12
 */
public class SizeComponent implements Component {
    public final float width;
    public final float height;

    public SizeComponent(float width, float height) {
        this.width = width;
        this.height = height;
    }
}
