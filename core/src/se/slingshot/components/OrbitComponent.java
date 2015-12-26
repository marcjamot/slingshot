package se.slingshot.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Entity is orbiting around a point
 *
 * @author emanu
 * @since 2015-12
 */
public class OrbitComponent implements Component {
    public Vector2 center;
    public float distance;
    public float angle;
    public float speed;

    public OrbitComponent(Vector2 center, float distance, float angle, float speed) {
        this.center = center;
        this.distance = distance;
        this.angle = angle;
        this.speed = speed;
    }
}
