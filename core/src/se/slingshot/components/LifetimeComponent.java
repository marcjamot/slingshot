package se.slingshot.components;

import com.badlogic.ashley.core.Component;

/**
 * DESC
 *
 * @author Marc
 * @since 2015-12
 */
public class LifetimeComponent implements Component {
    public final float timeUntilDeath;

    public float lifetime;

    public LifetimeComponent(float timeUntilDeath) {
        this.timeUntilDeath = timeUntilDeath;
    }
}
