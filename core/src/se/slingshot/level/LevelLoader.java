package se.slingshot.level;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import se.slingshot.components.*;
import se.slingshot.implementations.Animation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads a level from a json file
 *
 * @author Marc
 * @since 2015-12
 */
public class LevelLoader {
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    private Map<String, Entity> entityMap = new HashMap<>();

    public void from(Engine engine, String file) {
        InputStream inputStream = Gdx.files.internal(file).read();
        Genson genson = new Genson();
        List<Map<String, Object>> entityList = genson.deserialize(inputStream, new GenericType<List<Map<String, Object>>>() {
        });

        for (Map<String, Object> entityData : entityList) {
            Entity entity = new Entity();
            entityData.forEach((key, value) -> {
                Component component = parseComponent(entity, key, value);
                if (component != null) {
                    entity.add(component);
                }
            });
            engine.addEntity(entity);
        }
    }

    private Component parseComponent(Entity entity, String key, Object value) {
        switch (key) {
            case "id":
                entityMap.put((String) value, entity);
                return null;
            case "body":
                Map<String, Object> body = (Map<String, Object>) value;
                return new BodyComponent(
                        new Vector2((float) (double) body.get("start_x"), (float) (double) body.get("start_y")),
                        new Vector2((float) (double) body.get("direction_x"), (float) (double) body.get("direction_y")),
                        new Vector2((float) (double) body.get("velocity_x"), (float) (double) body.get("velocity_y")),
                        (float) (double) body.get("height"),
                        (float) (double) body.get("width"),
                        (float) (double) body.get("weight"),
                        (float) (double) body.get("radius")
                );
            case "collision":
                return new CollisionComponent(entity);
            case "controllable":
                Map<String, Object> controllable = (Map<String, Object>) value;
                return new ControllableComponent(
                        (float) (double) controllable.get("direction_speed"),
                        (float) (double) controllable.get("forward_force")
                );
            case "goal_area":
                Map<String, Object> goalArea = (Map<String, Object>) value;
                String goalAreaId = (String) goalArea.get("id");
                Entity goalAreaEntity = entityMap.get(goalAreaId);
                BodyComponent goalAreaBody = bodyMapper.get(goalAreaEntity);

                return new GoalAreaComponent(
                        goalAreaBody.position
                );
            case "gravity":
                boolean gravity = (boolean) value;
                if (gravity) {
                    return new GravityComponent();
                } else {
                    return new NoGravityComponent();
                }
            case "killable":
                return new KillableComponent();
            case "objective":
                Map<String, Object> objective = (Map<String, Object>) value;
                String objectiveId = (String) objective.get("id");
                Entity objectiveEntity = entityMap.get(objectiveId);
                BodyComponent objectiveBody = bodyMapper.get(objectiveEntity);

                return new ObjectiveComponent(
                        objectiveBody.position
                );
            case "orbit":
                Map<String, Object> orbit = (Map<String, Object>) value;
                String orbitId = (String) orbit.get("id");
                Entity orbitEntity = entityMap.get(orbitId);
                BodyComponent orbitBody = bodyMapper.get(orbitEntity);

                return new OrbitComponent(
                        orbitBody.position,
                        (float) (double) orbit.get("distance"),
                        (float) (double) orbit.get("angle"),
                        (float) (double) orbit.get("speed")
                );
            case "render":
                Map<String, Object> render = (Map<String, Object>) value;
                Map<String, List<String>> animationData = (Map<String, List<String>>) render.get("animations");
                List<Animation> animations = new ArrayList<>();
                animationData.forEach((animationName, animationImages) -> {
                    Texture[] textures = new Texture[animationImages.size()];
                    for (int i = 0; i < animationImages.size(); i++) {
                        String animationImage = animationImages.get(i);
                        textures[i] = new Texture(animationImage);
                    }
                    animations.add(new Animation(animationName, textures));
                });
                return new RenderComponent(
                        (boolean) render.get("repeat_animation"),
                        (float) (double) render.get("animation_speed"),
                        animations.toArray(new Animation[animations.size()])
                );
            case "trajectory":
                return new TrajectoryComponent(
                        (int) (long) value
                );
            default:
                throw new IllegalArgumentException("Can't find component: " + key);
        }
    }
}
