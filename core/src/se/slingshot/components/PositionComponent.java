package se.slingshot.components;

import com.badlogic.ashley.core.Component;

/**
 * DESC
 *
 * @author Marc
 * @since 2015-12
 */
public class PositionComponent implements Component {
    public float x;
    public float y;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
