package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import se.slingshot.components.GoalAreaComponent;

/**
 * Handles win condition
 *
 * @author emanu
 * @since 2015-12
 */
public class WinConditionSystem extends EntitySystem {
    // ECS
    private ImmutableArray<Entity> entities;
    private ComponentMapper<GoalAreaComponent> goalAreaMapper = ComponentMapper.getFor(GoalAreaComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(GoalAreaComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            GoalAreaComponent goalArea = goalAreaMapper.get(entity);

            float distance = goalArea.position.dst(goalArea.objectPosition);
            if (distance <= goalArea.radius) {
                System.out.println("Winning");
            }
        }
    }
}
