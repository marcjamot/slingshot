package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.DeathComponent;

/**
 * Checks for collisions between KillableComponent and BodyComponent
 *
 * @author Marc
 * @since 2015-12
 */
public class CollisionSystem extends EntitySystem {
    // ECS
    private ImmutableArray<Entity> killableEntities;
    private ImmutableArray<Entity> bodyEntities;
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        killableEntities = engine.getEntitiesFor(Family.all(BodyComponent.class, DeathComponent.class).get());
        bodyEntities = engine.getEntitiesFor(Family.all(BodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < killableEntities.size(); i++) {
            Entity killableEntity = killableEntities.get(i);
            BodyComponent killableBody = bodyMapper.get(killableEntity);

            for (int j = 0; j < bodyEntities.size(); j++) {
                Entity bodyEntity = bodyEntities.get(j);
                if(killableEntity == bodyEntity)
                    continue;
                BodyComponent bodyBody = bodyMapper.get(bodyEntity);

                // Logic
                float distance = killableBody.position.dst(bodyBody.position);
                float radiusDistance = getRadius(killableBody) + getRadius(bodyBody);
                if(distance < radiusDistance)
                    System.out.println("YOU DIE NOW");
            }
        }
    }

    private float getRadius(BodyComponent body){
        return Math.min(body.width, body.height);
    }

}
