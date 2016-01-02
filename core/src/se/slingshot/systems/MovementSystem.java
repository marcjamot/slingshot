package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.ControllableComponent;
import se.slingshot.implementations.GameOver;

/**
 * Handles movement of entities
 *
 * @author emanu
 * @since 2015-12
 */
public class MovementSystem extends EntitySystem {
    // ECS
    private ImmutableArray<Entity> controlEntities;
    private ImmutableArray<Entity> bodyEntities;
    private ComponentMapper<ControllableComponent> controlableMapper = ComponentMapper.getFor(ControllableComponent.class);
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);

    // Movement
    public boolean gameOver;

    public MovementSystem(MBassador<GameOver> eventBus) {
        eventBus.subscribe(this);
    }

    @Override
    public void addedToEngine(Engine engine) {
        controlEntities = engine.getEntitiesFor(Family.all(ControllableComponent.class, BodyComponent.class).get());
        bodyEntities = engine.getEntitiesFor(Family.all(BodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        if(gameOver){
            return;
        }

        //Applying Controls
        for (int i = 0; i < controlEntities.size(); i++) {
            Entity entity = controlEntities.get(i);
            ControllableComponent control = controlableMapper.get(entity);
            BodyComponent body = bodyMapper.get(entity);

            body.direction.rotate(control.directionThrust * deltaTime);
            Vector2 tmp = new Vector2(body.direction);
            tmp.scl(control.forwardThrust / body.weight * deltaTime);
            body.velocity.add(tmp);
        }

        //Movement
        for (int i = 0; i < bodyEntities.size(); i++) {
            Entity entity = bodyEntities.get(i);
            BodyComponent body = bodyMapper.get(entity);

            Vector2 tmp = new Vector2(body.velocity);
            body.position.add(tmp.scl(deltaTime));
        }
    }

    @Handler
    @SuppressWarnings("unused")
    public void handle(GameOver collision) {
        gameOver = true;
    }
}
