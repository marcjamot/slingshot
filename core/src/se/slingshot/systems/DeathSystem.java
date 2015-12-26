package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;
import se.slingshot.components.*;

/**
 * Handles entities that dies
 *
 * @author Marc
 * @since 2015-12
 */
public class DeathSystem extends EntitySystem {
    // ECS
    private Engine engine;
    private ImmutableArray<Entity> entities;
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<KillableComponent> killableMapper = ComponentMapper.getFor(KillableComponent.class);
    private ComponentMapper<LifetimeComponent> lifetimeMapper = ComponentMapper.getFor(LifetimeComponent.class);
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
        this.engine = engine;
        entities = engine.getEntitiesFor(Family.all(LifetimeComponent.class).get());
        /** Receive collisions */
        eventBus.subscribe(this);
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            LifetimeComponent lifetime = lifetimeMapper.get(entity);

            lifetime.lifetime += deltaTime;
            if(lifetime.lifetime > lifetime.timeUntilDeath){
                engine.removeEntity(entity);
            }
        }
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
        KillableComponent killable = killableMapper.get(entity);
        if(killable != null && killable.alive){
            BodyComponent body = bodyMapper.get(entity);
            RenderComponent render = renderMapper.get(entity);

            // Add death explosion
            Entity explosion = new Entity();
            Vector2 position = new Vector2(body.position);
            explosion.add(new BodyComponent(position, new Vector2(), new Vector2(), 1, 1, 0, 0));
            Texture[] textures = new Texture[]{
                    new Texture("explosion_1.png"),
                    new Texture("explosion_2.png"),
                    new Texture("explosion_3.png"),
                    new Texture("explosion_4.png"),
                    new Texture("explosion_5.png"),
                    new Texture("explosion_6.png"),
                    new Texture("explosion_7.png"),
                    new Texture("explosion_8.png")
            };
            float timePerAnimation = 0.3f;
            explosion.add(new RenderComponent(textures, false, timePerAnimation));
            explosion.add(new LifetimeComponent(timePerAnimation * textures.length));
            engine.addEntity(explosion);

            // Remove entity
            engine.removeEntity(entity);
        }
    }
}
