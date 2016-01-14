package se.slingshot.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * DESC
 *
 * @author Marc
 * @since 2016-01
 */
public class RotateTowardsLightComponent implements Component {
    public final Vector2 lightPosition;

    public RotateTowardsLightComponent(Vector2 lightPosition) {
        this.lightPosition = lightPosition;
    }
}
