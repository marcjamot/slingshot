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
        ControllableComponent playerControllableComponent = new ControllableComponent(180,2);

        CollisionSystem collisionSystem = new CollisionSystem(eventBus);
        engine.addSystem(collisionSystem);
        ControlSystem controlSystem = new ControlSystem();
        engine.addSystem(controlSystem);
        DeathSystem deathSystem = new DeathSystem(eventBus);
        engine.addSystem(deathSystem);
        GravitySystem gravitySystem = new GravitySystem();
        engine.addSystem(gravitySystem);
        MovementSystem movementSystem = new MovementSystem();
        engine.addSystem(movementSystem);
        renderSystem = new RenderSystem(playerControllableComponent);
        engine.addSystem(renderSystem);

        // Debug init data
        Entity player = new Entity();
        Texture[] playerTextures = new Texture[]{
                new Texture("spaceship_fire.png"),
                new Texture("spaceship_fire_2.png")
        };
        player.add(new RenderComponent(playerTextures, true, 0.5f));
        player.add(new BodyComponent(
                new Vector2(3, 3),
                new Vector2(1,0),
                new Vector2(0,0),
                2, 2,
                1, 0.5f
        ));
        player.add(playerControllableComponent);
        player.add(new NoGravityComponent());
        player.add(new CollisionComponent(player));
        engine.addEntity(player);

        // Debug planet
        Entity planet = new Entity();
        Texture[] planetTextures = new Texture[]{
                new Texture("sun.png")
        };
        planet.add(new RenderComponent(planetTextures, false, 1.0f));
        planet.add(new BodyComponent(
                new Vector2(10, 10),
                new Vector2(),
                new Vector2(),
                4, 4,
                1000, 1.4f
        ));
        planet.add(new GravityComponent());
        engine.addEntity(planet);
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
