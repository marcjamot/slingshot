package se.slingshot.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.RotateTowardsLightComponent;

/**
 * DESC
 *
 * @author Marc
 * @since 2016-01
 */
public class RotateTowardsLightSystem extends IteratingSystem {

    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<RotateTowardsLightComponent> rtlMapper = ComponentMapper.getFor(RotateTowardsLightComponent.class);

    public RotateTowardsLightSystem() {
        super(Family.all(BodyComponent.class, RotateTowardsLightComponent.class).get());
    }

    public final Vector2 tmp = new Vector2();

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent body = bodyMapper.get(entity);
        RotateTowardsLightComponent rtl = rtlMapper.get(entity);

        tmp.set(rtl.lightPosition).sub(body.position).rotate(-45).nor();
        body.direction.set(tmp);
    }
}
