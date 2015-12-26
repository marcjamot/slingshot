package se.slingshot.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;
import se.slingshot.components.CollisionComponent;
import se.slingshot.components.KillableComponent;
import se.slingshot.components.RenderComponent;

/**
 * Handles entities that dies
 *
 * @author Marc
 * @since 2015-12
 */
public class DeathSystem extends EntitySystem {
    // ECS
    private ComponentMapper<KillableComponent> killableMapper = ComponentMapper.getFor(KillableComponent.class);
    private ComponentMapper<RenderComponent> renderMapper = ComponentMapper.getFor(RenderComponent.class);

    // Death
    /** EventBus is used to pass messages between systems conveniently */
    private final MBassador<CollisionComponent> eventBus;

    /**
     * @param eventBus EventBus is used to pass messages between systems conveniently
     */
    public DeathSystem(MBassador<CollisionComponent> eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void addedToEngine(Engine engine) {
        /** Receive collisions */
        eventBus.subscribe(this);
    }

    @Override
    public void update(float deltaTime) {
    }

    /**
     * Called when a collision occurs with the help of eventBus
     *
     * @param collision Collision component
     */
    @Handler
    @SuppressWarnings("unused")
    public void handle(CollisionComponent collision) {
        Entity entity = collision.entity;
        if(killableMapper.get(entity) != null){
            RenderComponent render = renderMapper.get(entity);
            render.visible = false;
        }
    }
}
