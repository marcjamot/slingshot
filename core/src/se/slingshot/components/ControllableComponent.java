package se.slingshot.components;

import com.badlogic.ashley.core.Component;

/**
 * Entity is being controlled by player
 *
 * @author emanu
 * @since 2015-12
 */
public class ControllableComponent implements Component {
    public float directionThrust;
    public float forwardThrust;
    public float directionThrustSpeed;
    public float forwardThrustForce;
    public float fuel = 1.0f;

    public ControllableComponent(float directionThrustSpeed, float forwardThrustForce) {
        this.directionThrustSpeed = directionThrustSpeed;
        this.forwardThrustForce = forwardThrustForce;
    }
}
