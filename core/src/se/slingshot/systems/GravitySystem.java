package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.ControllableComponent;
import se.slingshot.components.GravityComponent;
import se.slingshot.components.NoGravityComponent;

/**
 * Created by emanu on 2015-12-22.
 */
public class GravitySystem extends EntitySystem {
    // ECS
    private final static float G = 0.0667f;
    private final static float MAX_ACC = 10f;
    private ImmutableArray<Entity> gravityEntities;
    private ImmutableArray<Entity> noGravityEntities;
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        gravityEntities = engine.getEntitiesFor(Family.all(GravityComponent.class, BodyComponent.class).get());
        noGravityEntities = engine.getEntitiesFor(Family.all(NoGravityComponent.class, BodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        for (int i = 0; i < noGravityEntities.size(); i++) {
            Entity noGravityEntity = noGravityEntities.get(i);
            BodyComponent noGravityBody = bodyMapper.get(noGravityEntity);

            for (int j = 0; j < gravityEntities.size(); j++) {
                Entity gravityEntity = gravityEntities.get(j);
                BodyComponent gravityBody = bodyMapper.get(gravityEntity);

                Vector2 gravityDir = new Vector2(gravityBody.position.x-noGravityBody.position.x, gravityBody.position.y-noGravityBody.position.y);
                float distance = gravityDir.len();
                float acceleration = (G * gravityBody.weight) / (distance * distance);
                acceleration = Math.min(acceleration,MAX_ACC);
                gravityDir.nor();
                gravityDir.scl(acceleration * deltaTime);
                noGravityBody.velocity.add(gravityDir);

            }
        }
    }
}
