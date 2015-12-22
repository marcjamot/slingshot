package se.slingshot.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by emanu on 2015-12-21.
 */
public class BodyComponent implements Component {
    public Vector2 position;
    public Vector2 direction;
    public Vector2 velocity;
    public final float height;
    public final float width;
    public float weight;
    public float radius;

    public BodyComponent(Vector2 position, Vector2 direction, Vector2 velocity, float height, float width, float weight, float radius) {
        this.position = position;
        this.direction = direction;
        this.velocity = velocity;
        this.height = height;
        this.width = width;
        this.weight = weight;
        this.radius = radius;
    }
}
