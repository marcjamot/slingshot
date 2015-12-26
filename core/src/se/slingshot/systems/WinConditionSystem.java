package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.GoalAreaComponent;
import se.slingshot.components.ObjectiveComponent;

/**
 * Handles win condition
 *
 * @author emanu
 * @since 2015-12
 */
public class WinConditionSystem extends EntitySystem {
    // ECS
    private ImmutableArray<Entity> goalAreaEntities;
    private ImmutableArray<Entity> objectiveEntities;
    private ComponentMapper<GoalAreaComponent> goalAreaMapper = ComponentMapper.getFor(GoalAreaComponent.class);
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        goalAreaEntities = engine.getEntitiesFor(Family.all(GoalAreaComponent.class).get());
        objectiveEntities = engine.getEntitiesFor(Family.all(ObjectiveComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        for (int i = 0; i < goalAreaEntities.size(); i++) {
            Entity goalAreaEntity = goalAreaEntities.get(i);
            GoalAreaComponent goalArea = goalAreaMapper.get(goalAreaEntity);
            BodyComponent goalAreaBody = bodyMapper.get(goalAreaEntity);

            float distance = goalAreaBody.position.dst(goalArea.objectPosition);
            if(distance<=goalAreaBody.radius  && objectiveEntities.size() == 0){
                System.out.println("Win!");
            }
        }
    }
}
