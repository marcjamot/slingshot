package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import net.engio.mbassy.bus.MBassador;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.CollisionComponent;
import se.slingshot.interfaces.RenderInterface;

/**
 * Checks for collisions between KillableComponent and BodyComponent
 *
 * @author Marc
 * @since 2015-12
 */
public class CollisionSystem extends EntitySystem implements RenderInterface {
    // ECS
    private ImmutableArray<Entity> entities;
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<CollisionComponent> collisionMapper = ComponentMapper.getFor(CollisionComponent.class);

    // Collision
    /** EventBus is used to pass messages between systems conveniently */
    private final MBassador<CollisionComponent> eventBus;
    /** If collision circles should be drawn */
    private final boolean debug;

    /**
     * @param eventBus EventBus is used to pass messages between systems conveniently
     * @param debug    If collision circles should be drawn
     */
    public CollisionSystem(MBassador<CollisionComponent> eventBus, boolean debug) {
        this.eventBus = eventBus;
        this.debug = debug;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(BodyComponent.class, CollisionComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        // Outside screen
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            BodyComponent body = bodyMapper.get(entity);

            float x = body.position.x;
            float y = body.position.y;
            if(x < 0 || 160 < x){
                CollisionComponent collision = new CollisionComponent(entity);
                eventBus.post(collision).now();
            }
            if(y < 0 || 90 < y){
                CollisionComponent collision = new CollisionComponent(entity);
                eventBus.post(collision).now();
            }
        }

        // Collision
        for (int i = 0; i < entities.size(); i++) {
            Entity entity1 = entities.get(i);
            BodyComponent body1 = bodyMapper.get(entity1);
            CollisionComponent collision1 = collisionMapper.get(entity1);

            for (int j = i+1; j < entities.size(); j++) {
                Entity entity2 = entities.get(j);
                BodyComponent body2 = bodyMapper.get(entity2);
                CollisionComponent collision2 = collisionMapper.get(entity2);

                // Logic
                float distance = body1.position.dst(body2.position);
                float radiusDistance = body1.radius + body2.radius;
                if(distance < radiusDistance) {
                    /** Tell all interested systems that the collision occurred */
                    eventBus.post(collision1).now();
                    eventBus.post(collision2).now();
                }
            }
        }
    }

    @Override
    public void render(Camera camera, SpriteBatch spriteBatch) {
        if (debug) {
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = entities.get(i);
                BodyComponent body = bodyMapper.get(entity);

                ShapeRenderer shapeRenderer = new ShapeRenderer();
                shapeRenderer.setProjectionMatrix(camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.circle(body.position.x, body.position.y, body.radius);
                shapeRenderer.end();
                shapeRenderer.dispose();
            }
        }
    }
}
