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

import java.io.InputStream;
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
            case "gravity":
                boolean gravity = (boolean) value;
                if (gravity) {
                    return new GravityComponent();
                } else {
                    return new NoGravityComponent();
                }
            case "orbit":
                Map<String, Object> orbit = (Map<String, Object>) value;
                String targetId = (String) orbit.get("id");
                Entity target = entityMap.get(targetId);
                BodyComponent targetBody = bodyMapper.get(target);

                return new OrbitComponent(
                        targetBody.position,
                        (float) (double) orbit.get("distance"),
                        (float) (double) orbit.get("angle"),
                        (float) (double) orbit.get("speed")
                );
            case "render":
                Map<String, Object> render = (Map<String, Object>) value;
                List<String> images = (List<String>) render.get("images");
                Texture[] textures = new Texture[images.size()];
                for (int i = 0; i < textures.length; i++) {
                    textures[i] = new Texture(images.get(i));
                }
                return new RenderComponent(
                        textures,
                        (boolean) render.get("repeat_animation"),
                        (float) (double) render.get("animation_speed")
                );
            default:
                throw new IllegalArgumentException("Can't find component: " + key);
        }
    }
}
