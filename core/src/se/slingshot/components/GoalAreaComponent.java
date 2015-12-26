package se.slingshot.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Entity is a final destination on a level
 *
 * @author emanu
 * @since 2015-12
 */
public class GoalAreaComponent implements Component {
    public Vector2 objectPosition;

    public GoalAreaComponent(Vector2 objectPosition) {
        this.objectPosition = objectPosition;
    }
}
