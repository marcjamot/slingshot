package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import se.slingshot.components.ControllableComponent;

/**
 * Created by emanu on 2015-12-21.
 */
public class ControlSystem extends EntitySystem implements InputProcessor {
    // ECS
    private ImmutableArray<Entity> entities;
    private ComponentMapper<ControllableComponent> controlableMapper = ComponentMapper.getFor(ControllableComponent.class);

    private boolean forwardThrust = false;
    private boolean leftThrust = false;
    private boolean rightThrust = false;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ControllableComponent.class).get());
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void update(float deltaTime) {

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            ControllableComponent control = controlableMapper.get(entity);

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
}
