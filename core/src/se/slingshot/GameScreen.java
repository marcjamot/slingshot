package se.slingshot;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import se.slingshot.components.*;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.ControllableComponent;
import se.slingshot.components.DeathComponent;
import se.slingshot.components.RenderComponent;
import se.slingshot.systems.CollisionSystem;
import se.slingshot.systems.ControlSystem;
import se.slingshot.systems.GravitySystem;
import se.slingshot.systems.MovementSystem;
import se.slingshot.systems.RenderSystem;

/**
 * DESC
 *
 * @author Marc
 * @since 2015-12
 */
public class GameScreen implements Screen {
    private Engine engine;

    @Override
    public void show() {
        engine = new PooledEngine();

        CollisionSystem collisionSystem = new CollisionSystem();
        engine.addSystem(collisionSystem);
        ControlSystem controlSystem = new ControlSystem();
        engine.addSystem(controlSystem);
        GravitySystem gravitySystem = new GravitySystem();
        engine.addSystem(gravitySystem);
        MovementSystem movementSystem = new MovementSystem();
        engine.addSystem(movementSystem);
        RenderSystem renderSystem = new RenderSystem();
        engine.addSystem(renderSystem);

        // Debug init data
        Entity player = new Entity();
        Texture[] playerTextures = new Texture[]{
                new Texture("spaceship_fire.png"),
                new Texture("spaceship_fire_2.png")
        };
        player.add(new RenderComponent(playerTextures, true, 0.5f));
        player.add(new BodyComponent(
                new Vector2(9.5f, 15f),
                new Vector2(1,0),
                new Vector2((float)Math.sqrt(GravitySystem.G*80 / 5),(float)Math.sqrt(GravitySystem.G*30 / 0.5)),
                0.25f, 0.25f,
                1, 0.1f
        ));
        player.add(new ControllableComponent(180,1));
        player.add(new NoGravityComponent());
        player.add(new DeathComponent());
        engine.addEntity(player);

        // Debug sun
        Entity sun = new Entity();
        Texture[] sunTextures = new Texture[]{
                new Texture("sun.png")
        };
        sun.add(new RenderComponent(sunTextures, false, 1.0f));
        sun.add(new BodyComponent(
                new Vector2(10, 10),
                new Vector2(),
                new Vector2(),
                1f, 1f,
                80, 1.4f
        ));
        sun.add(new FullGravityComponent());
        engine.addEntity(sun);

        // Debug planet
        Entity planet = new Entity();
        Texture[] planetTextures = new Texture[]{
                new Texture("earth.png")
        };
        planet.add(new RenderComponent(planetTextures, false, 1.0f));
        planet.add(new BodyComponent(
                new Vector2(10, 20),
                new Vector2(),
                new Vector2((float)Math.sqrt(GravitySystem.G*80 / 10),0),
                0.8f, 0.8f,
                50, 1.4f
        ));
        planet.add(new HalfGravityComponent());
        engine.addEntity(planet);


        // Debug planet
        Entity planet2 = new Entity();
        Texture[] planet2Textures = new Texture[]{
                new Texture("earth.png")
        };
        planet2.add(new RenderComponent(planet2Textures, false, 1.0f));
        planet2.add(new BodyComponent(
                new Vector2(10, 15),
                new Vector2(),
                new Vector2((float)Math.sqrt(GravitySystem.G*80 / 5),0),
                0.5f, 0.5f,
                30, 1.4f
        ));
        planet2.add(new HalfGravityComponent());
        engine.addEntity(planet2);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {

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
