package se.slingshot;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import net.engio.mbassy.bus.MBassador;
import se.slingshot.components.*;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.ControllableComponent;
import se.slingshot.components.CollisionComponent;
import se.slingshot.components.RenderComponent;
import se.slingshot.interfaces.RenderInterface;
import se.slingshot.level.LevelLoader;
import se.slingshot.systems.*;

/**
 * DESC
 *
 * @author Marc
 * @since 2015-12
 */
public class GameScreen implements Screen {
    private Engine engine;
    private RenderSystem renderSystem;

    @Override
    public void show() {
        engine = new PooledEngine();
        MBassador<CollisionComponent> eventBus = new MBassador<CollisionComponent>();
        ControllableComponent playerControllableComponent = new ControllableComponent(180f,1.5f);

        CollisionSystem collisionSystem = new CollisionSystem(eventBus, true);
        ControlSystem controlSystem = new ControlSystem();
        DeathSystem deathSystem = new DeathSystem(eventBus);
        GravitySystem gravitySystem = new GravitySystem();
        MovementSystem movementSystem = new MovementSystem();
        OrbitSystem orbitSystem = new OrbitSystem();
        RenderInterface[] renderInterfaces = new RenderInterface[]{
                collisionSystem
        };
        renderSystem = new RenderSystem(playerControllableComponent, renderInterfaces);
        WinConditionSystem winConditionSystem = new WinConditionSystem();

        // Add the systems in the order they should execute
        engine.addSystem(controlSystem);
        engine.addSystem(gravitySystem);
        engine.addSystem(orbitSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(collisionSystem);
        engine.addSystem(deathSystem);
        engine.addSystem(winConditionSystem);
        engine.addSystem(renderSystem);

        new LevelLoader().from(engine, "level_1.json");
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderSystem.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
