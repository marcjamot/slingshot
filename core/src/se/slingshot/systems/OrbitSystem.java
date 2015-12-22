package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.OrbitComponent;

/**
 * Created by emanu on 2015-12-22.
 */
public class OrbitSystem extends EntitySystem {
    // ECS
    private ImmutableArray<Entity> orbitEntities;
    private ComponentMapper<OrbitComponent> orbitMapper = ComponentMapper.getFor(OrbitComponent.class);
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        orbitEntities = engine.getEntitiesFor(Family.all(OrbitComponent.class, BodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        for (int i = 0; i < orbitEntities.size(); i++) {
            Entity entity = orbitEntities.get(i);
            OrbitComponent orbit = orbitMapper.get(entity);
            BodyComponent body = bodyMapper.get(entity);

            orbit.angle = orbit.angle + orbit.speed*deltaTime;
            Vector2 tmp = new Vector2(orbit.center);
            body.position.set(tmp.add(MathUtils.cosDeg(orbit.angle)*orbit.distance, MathUtils.sinDeg(orbit.angle)*orbit.distance));

        }
    }

}
