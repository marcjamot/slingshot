package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;
import se.slingshot.components.*;
import se.slingshot.implementations.Animation;
import se.slingshot.implementations.GameOver;

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
    private final MBassador<GameOver> eventBus;
    private Sound deathSound;

    /**
     * @param eventBus EventBus is used to pass messages between systems conveniently
     */
    public DeathSystem(MBassador<CollisionComponent> eventBus, MBassador<GameOver> gameOverBus) {
        /** EventBus is used to pass messages between systems conveniently */
        eventBus.subscribe(this);
        this.eventBus = gameOverBus;

        deathSound = Gdx.audio.newSound(Gdx.files.internal("boom6.wav"));
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        entities = engine.getEntitiesFor(Family.all(LifetimeComponent.class).get());
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
            explosion.add(new BodyComponent(position, new Vector2(), new Vector2(), 10, 10, 0, 0));
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
            Animation[] animations = new Animation[]{
                    new Animation("explode", textures)
            };
            float timePerAnimation = 0.3f;
            explosion.add(new RenderComponent(false, timePerAnimation, animations));
            explosion.add(new LifetimeComponent(timePerAnimation * textures.length));
            engine.addEntity(explosion);
            deathSound.play();

            // Remove entity
            engine.removeEntity(entity);

            eventBus.post(GameOver.Lose).now();
        }
    }
}
