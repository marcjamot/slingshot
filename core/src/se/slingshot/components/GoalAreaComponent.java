package se.slingshot.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by emanu on 2015-12-22.
 */
public class GoalAreaComponent implements Component {
    public Vector2 position;
    public float radius;
    public Vector2 objectPosition;

    public GoalAreaComponent(Vector2 position, float radius, Vector2 objectPosition){
        this.position = position;
        this.radius = radius;
        this.objectPosition = objectPosition;
    }
}
