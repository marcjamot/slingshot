package se.slingshot.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * DESC
 *
 * @author Marc
 * @since 2015-12
 */
public class CollisionComponent implements Component {
    /** Reference to the entity that owns the component */
    public final Entity entity;

    public CollisionComponent(Entity entity) {
        this.entity = entity;
    }
}
