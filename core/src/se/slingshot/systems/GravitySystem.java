package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.FullGravityComponent;
import se.slingshot.components.HalfGravityComponent;
import se.slingshot.components.NoGravityComponent;

/**
 * Created by emanu on 2015-12-22.
 */
public class GravitySystem extends EntitySystem {
    // ECS
    public final static float G = 0.00667f;
    private final static float MAX_ACC = 10f;
    private ImmutableArray<Entity> noGravityEntities;
    private ImmutableArray<Entity> halfGravityEntities;
    private ImmutableArray<Entity> fullGravityEntities;
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        halfGravityEntities = engine.getEntitiesFor(Family.all(HalfGravityComponent.class, BodyComponent.class).get());
        fullGravityEntities = engine.getEntitiesFor(Family.all(FullGravityComponent.class, BodyComponent.class).get());

        noGravityEntities = engine.getEntitiesFor(Family.all(NoGravityComponent.class, BodyComponent.class).get());


    }

    @Override
    public void update(float deltaTime) {

        //noGravity to halfGravity
        applyGravity(noGravityEntities,halfGravityEntities,deltaTime);

        //noGravity to fullGravity
        applyGravity(noGravityEntities,fullGravityEntities,deltaTime);

        //halfGravity to fullGravity
        applyGravity(halfGravityEntities,fullGravityEntities,deltaTime);
    }

    private void applyGravity(ImmutableArray<Entity> entities1,ImmutableArray<Entity> entities2, float deltaTime){
        for (int i = 0; i < entities1.size(); i++) {
            Entity entity1 = entities1.get(i);
            BodyComponent body1 = bodyMapper.get(entity1);

            for (int j = 0; j < entities2.size(); j++) {
                Entity entity2 = entities2.get(j);
                BodyComponent body2 = bodyMapper.get(entity2);

                Vector2 gravityDir = new Vector2(body2.position.x-body1.position.x, body2.position.y-body1.position.y);
                float distance = gravityDir.len();
                float acceleration = (G * body2.weight) / (distance * distance);
                acceleration = Math.min(acceleration,MAX_ACC);
                gravityDir.nor();
                gravityDir.scl(acceleration * deltaTime*2);
                body1.velocity.add(gravityDir);

            }
        }
    }
}
