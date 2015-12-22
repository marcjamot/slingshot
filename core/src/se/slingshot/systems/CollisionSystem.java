package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
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
                Vector2 b1 = new Vector2(killableBody.position).add(0, killableBody.height);
                Vector2 b2 = new Vector2(bodyBody.position).add(0, bodyBody.height);
                float distance = b1.dst(b2);
                float radiusDistance = killableBody.radius + bodyBody.radius;
                if(distance < radiusDistance)
                    System.out.println("YOU DIE NOW");
            }
        }
    }
}
