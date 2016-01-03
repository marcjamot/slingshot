package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.GravityComponent;
import se.slingshot.components.TrajectoryComponent;
import se.slingshot.interfaces.RenderInterface;

import java.util.ArrayList;

/**
 * Handles trajectory calculation and rendering
 *
 * @author emanu
 * @since 2015-12
 */
public class TrajectorySystem extends EntitySystem implements RenderInterface {

    private final static float timestepTime = 0.1f;

    private ImmutableArray<Entity> trajectoryEntities;
    private ImmutableArray<Entity> gravityEntities;

    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<TrajectoryComponent> trajectoryMapper = ComponentMapper.getFor(TrajectoryComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        gravityEntities = engine.getEntitiesFor(Family.all(GravityComponent.class, BodyComponent.class).get());
        trajectoryEntities = engine.getEntitiesFor(Family.all(TrajectoryComponent.class, BodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        for (int i = 0; i < trajectoryEntities.size(); i++) {
            Entity trajetoryEntety = trajectoryEntities.get(i);
            BodyComponent trajectoryBody = bodyMapper.get(trajetoryEntety);
            TrajectoryComponent trajectory = trajectoryMapper.get(trajetoryEntety);


            Vector2 position = new Vector2(trajectoryBody.position);
            Vector2 velocity = new Vector2(trajectoryBody.velocity);

            for (int j = 0; j < trajectory.trajectory.size(); j++) {

                for (int k = 0; k < gravityEntities.size(); k++) {
                    Entity gravityEntity = gravityEntities.get(k);
                    BodyComponent gravityBody = bodyMapper.get(gravityEntity);

                    Vector2 gravityDir = new Vector2(gravityBody.position);
                    gravityDir.sub(position);
                    float distance = gravityDir.len();
                    float acceleration = (GravitySystem.G * gravityBody.weight) / (distance * distance);
                    acceleration = Math.min(acceleration, GravitySystem.MAX_ACC);
                    gravityDir.nor();
                    gravityDir.scl(acceleration * j * timestepTime);
                    velocity.add(gravityDir);

                }

                Vector2 tmp = new Vector2(velocity);
                position.add(tmp.scl(j * timestepTime));

                trajectory.trajectory.get(j).set(position);
            }
        }
    }

    @Override
    public void render(Camera camera, SpriteBatch spriteBatch) {
        for (int i = 0; i < trajectoryEntities.size(); i++) {
            Entity entity = trajectoryEntities.get(i);
            TrajectoryComponent trajectoryComponent = trajectoryMapper.get(entity);

            ArrayList<Vector2> trajectory = trajectoryComponent.trajectory;

            for (int j = 0; j < trajectory.size(); j++) {
                Vector2 position = trajectory.get(j);

                ShapeRenderer shapeRenderer = new ShapeRenderer();
                shapeRenderer.setProjectionMatrix(camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.circle(position.x, position.y, 0.11f - 0.01f * j);
                shapeRenderer.end();
                shapeRenderer.dispose();
            }


        }
    }

}
