package se.slingshot.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by emanu on 2015-12-21.
 */
public class ControllableComponent implements Component {
    public float directionThrust;
    public float forwardThrust;
    public float directionThrustSpeed = 20;
    public float forwardThrustForce = 10;

    public ControllableComponent(float directionThrust, float forwardThrust) {
        this.directionThrust = directionThrust;
        this.forwardThrust = forwardThrust;
    }
}
