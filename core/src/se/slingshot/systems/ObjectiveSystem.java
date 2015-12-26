package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.ObjectiveComponent;

/**
 * Created by emanu on 2015-12-26.
 */
public class ObjectiveSystem extends EntitySystem {
    // ECS
    private ImmutableArray<Entity> entities;
    private ComponentMapper<ObjectiveComponent> objectiveMapper = ComponentMapper.getFor(ObjectiveComponent.class);
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ObjectiveComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            ObjectiveComponent objective = objectiveMapper.get(entity);
            BodyComponent objectiveBody = bodyMapper.get(entity);

            if(!objective.taken){
                System.out.println("not taken");
                float distance = objectiveBody.position.dst(objective.objectPosition);
                if(distance<=objectiveBody.radius) {
                    System.out.println("objective taken");
                    objective.taken = true;
                }
            }
        }
    }
}
