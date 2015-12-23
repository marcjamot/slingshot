package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
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
    private ImmutableArray<Entity> collisionEntities;
    private ImmutableArray<Entity> bodyEntities;
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
        collisionEntities = engine.getEntitiesFor(Family.all(BodyComponent.class, CollisionComponent.class).get());
        bodyEntities = engine.getEntitiesFor(Family.all(BodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < collisionEntities.size(); i++) {
            Entity collisionEntity = collisionEntities.get(i);
            BodyComponent cBody = bodyMapper.get(collisionEntity);
            CollisionComponent cCollision = collisionMapper.get(collisionEntity);

            for (int j = 0; j < bodyEntities.size(); j++) {
                Entity bodyEntity = bodyEntities.get(j);
                if (collisionEntity == bodyEntity)
                    continue;
                BodyComponent bBody = bodyMapper.get(bodyEntity);

                // Logic
                Vector2 b1 = new Vector2(bBody.position);
                Vector2 b2 = new Vector2(cBody.position);
                float distance = b1.dst(b2);
                float radiusDistance = cBody.radius + bBody.radius;
                if (distance < radiusDistance) {
                    /** Tell all interested systems that the collision occurred */
                    eventBus.post(cCollision).now();
                }
            }
        }
    }

    @Override
    public void render(Camera camera, SpriteBatch spriteBatch, float pixelPerMeter) {
        if (debug) {
            for (int i = 0; i < bodyEntities.size(); i++) {
                Entity entity = bodyEntities.get(i);
                BodyComponent body = bodyMapper.get(entity);

                ShapeRenderer shapeRenderer = new ShapeRenderer();
                shapeRenderer.setTransformMatrix(camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.circle(body.position.x * pixelPerMeter, body.position.y * pixelPerMeter, body.radius * pixelPerMeter);
                shapeRenderer.end();
            }
        }
    }
}
