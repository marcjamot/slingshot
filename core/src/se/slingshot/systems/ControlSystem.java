package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import se.slingshot.components.ControllableComponent;
import se.slingshot.components.RenderComponent;
import se.slingshot.interfaces.FuelInterface;

/**
 * System for user control input
 *
 * @author emanu
 * @since 2015-12
 */
public class ControlSystem extends EntitySystem implements InputProcessor, FuelInterface {
    // ECS
    private ImmutableArray<Entity> entities;
    private ComponentMapper<ControllableComponent> controlableMapper = ComponentMapper.getFor(ControllableComponent.class);
    private ComponentMapper<RenderComponent> renderMapper = ComponentMapper.getFor(RenderComponent.class);

    // Control
    private boolean forwardThrust = false;
    private boolean leftThrust = false;
    private boolean rightThrust = false;
    private float fuel;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ControllableComponent.class, RenderComponent.class).get());
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            ControllableComponent control = controlableMapper.get(entity);
            RenderComponent render = renderMapper.get(entity);

            fuel = control.fuel;
            // If we have no fuel, we can't move the ship
            if (control.fuel == 0) {
                render.changeActiveAnimation("still");
                control.directionThrust = 0;
                control.forwardThrust = 0;
                continue;
            }

            // Check input
            if (forwardThrust) {
                control.forwardThrust = control.forwardThrustForce;
            } else {
                control.forwardThrust = 0;
            }
            float directionThrust = 0;
            if (leftThrust) {
                directionThrust += 1;
            }
            if (rightThrust) {
                directionThrust -= 1;
            }
            control.directionThrust = control.directionThrustSpeed * directionThrust;

            // Add thruster active time for fuel consumption
            if (forwardThrust) {
                render.changeActiveAnimation("moving");
                control.fuel -= deltaTime * 0.1f;
                if (control.fuel < 0) {
                    control.fuel = 0;
                }
            } else {
                render.changeActiveAnimation("still");
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                forwardThrust = true;
                break;
            case Input.Keys.A:
                leftThrust = true;
                break;
            case Input.Keys.D:
                rightThrust = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                forwardThrust = false;
                break;
            case Input.Keys.A:
                leftThrust = false;
                break;
            case Input.Keys.D:
                rightThrust = false;
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public float get() {
        return fuel;
    }
}
