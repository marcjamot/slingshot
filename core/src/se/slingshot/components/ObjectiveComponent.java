package se.slingshot.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by emanu on 2015-12-26.
 */
public class ObjectiveComponent implements Component {
    public Vector2 objectPosition;
    public boolean taken = false;

    public ObjectiveComponent(Vector2 objectPosition){
        this.objectPosition = objectPosition;
    }
}
