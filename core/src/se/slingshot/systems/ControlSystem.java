package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;
import se.slingshot.components.ControllableComponent;
import se.slingshot.components.RenderComponent;
import se.slingshot.implementations.GameOver;
import se.slingshot.interfaces.FuelInterface;
import se.slingshot.interfaces.ScreenInterface;

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
    private final ScreenInterface screenHandler;
    private boolean forwardThrust = false;
    private boolean leftThrust = false;
    private boolean rightThrust = false;
    private float fuel;
    private boolean gameOver;

    // SFX
    private Sound sound;
    private boolean soundPlaying;

    public ControlSystem(ScreenInterface screenHandler, MBassador<GameOver> eventBus) {
        this.screenHandler = screenHandler;
        eventBus.subscribe(this);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ControllableComponent.class, RenderComponent.class).get());
        Gdx.input.setInputProcessor(this);

        sound = Gdx.audio.newSound(Gdx.files.internal("ScatterNoise1.mp3"));
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            ControllableComponent control = controlableMapper.get(entity);
            RenderComponent render = renderMapper.get(entity);

            fuel = control.fuel;
            // If we have no fuel, we can't move the ship
            if (control.fuel == 0 || gameOver) {
                render.changeActiveAnimation("still");
                control.directionThrust = 0;
                control.forwardThrust = 0;
                sound.stop();
                soundPlaying = false;
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
                if (!soundPlaying) {
                    long soundId = sound.play();
                    sound.setLooping(soundId, true);
                    soundPlaying = true;
                }
            } else {
                render.changeActiveAnimation("still");
                sound.stop();
                soundPlaying = false;
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (gameOver) {
            switch (keycode) {
                case Input.Keys.ENTER:
                    screenHandler.reloadLevel();
                    break;
                case Input.Keys.ESCAPE:
                    screenHandler.menu();
                    break;
            }
        } else {
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

    @Handler
    @SuppressWarnings("unused")
    public void handle(GameOver collision) {
        gameOver = true;
        sound.stop();
    }
}
