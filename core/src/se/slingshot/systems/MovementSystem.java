package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.ControllableComponent;

/**
 * Created by emanu on 2015-12-21.
 */
public class MovementSystem extends EntitySystem {
    // ECS
    private ImmutableArray<Entity> entities;
    private ComponentMapper<ControllableComponent> controlableMapper = ComponentMapper.getFor(ControllableComponent.class);
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ControllableComponent.class, BodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            ControllableComponent control = controlableMapper.get(entity);
            BodyComponent body = bodyMapper.get(entity);

            body.direction.rotate(control.directionThrust * deltaTime);
            Vector2 tmp = new Vector2(body.direction);
            body.velocity.add(tmp.scl(control.forwardThrust/body.weight * deltaTime));
            tmp = new Vector2(body.velocity);
            body.position.add(tmp.scl(deltaTime));
        }
    }

}
