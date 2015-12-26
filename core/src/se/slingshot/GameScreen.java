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
        engine.addSystem(collisionSystem);
        ControlSystem controlSystem = new ControlSystem();
        engine.addSystem(controlSystem);
        DeathSystem deathSystem = new DeathSystem(eventBus);
        engine.addSystem(deathSystem);
        GravitySystem gravitySystem = new GravitySystem();
        engine.addSystem(gravitySystem);
        MovementSystem movementSystem = new MovementSystem();
        engine.addSystem(movementSystem);
        ObjectiveSystem objectiveSystem = new ObjectiveSystem();
        engine.addSystem(objectiveSystem);
        OrbitSystem orbitSystem = new OrbitSystem();
        engine.addSystem(orbitSystem);
        TrajectorySystem trajectorySystem = new TrajectorySystem();
        engine.addSystem(trajectorySystem);
        WinConditionSystem winConditionSystem = new WinConditionSystem();
        engine.addSystem(winConditionSystem);
        RenderInterface[] renderInterfaces = new RenderInterface[]{
                collisionSystem,
                trajectorySystem
        };
        renderSystem = new RenderSystem(playerControllableComponent, renderInterfaces);
        engine.addSystem(renderSystem);


        // Debug init data
        Entity player = new Entity();
        Texture[] playerTextures = new Texture[]{
                new Texture("spaceship_fire.png"),
                new Texture("spaceship_fire_2.png")
        };
        Vector2 playerPosition = new Vector2(8f, 8f);
        player.add(new RenderComponent(playerTextures, true, 0.5f));
        player.add(new BodyComponent(
                playerPosition,
                new Vector2(1,0),
                new Vector2(),
                2f, 2f,
                1, 0.6f
        ));
        player.add(playerControllableComponent);
        player.add(new NoGravityComponent());
        player.add(new CollisionComponent(player));
        player.add(new KillableComponent());
        player.add(new TrajectoryComponent(10));
        engine.addEntity(player);

        // Debug sun
        Entity sun = new Entity();
        Texture[] sunTextures = new Texture[]{
                new Texture("sun.png")
        };
        Vector2 sunPossition = new Vector2(20, 12);
        sun.add(new RenderComponent(sunTextures, false, 1.0f));
        sun.add(new BodyComponent(
                sunPossition,
                new Vector2(),
                new Vector2(),
                5f, 5f,
                400, 2.2f
        ));
        sun.add(new GravityComponent());
        sun.add(new CollisionComponent(sun));
        engine.addEntity(sun);

        // Debug planets
        Entity planet = new Entity();
        Texture[] planetTextures = new Texture[]{
                new Texture("earth.png")
        };
        planet.add(new RenderComponent(planetTextures, false, 1.0f));
        planet.add(new BodyComponent(
                new Vector2(),
                new Vector2(),
                new Vector2(),
                4f, 4f,
                200, 1.8f
        ));
        planet.add(new GravityComponent());
        planet.add(new CollisionComponent(planet));
        planet.add(new OrbitComponent(sunPossition,10,45,20));
        engine.addEntity(planet);

        // Debug planet
        Entity planet2 = new Entity();
        Texture[] planet2Textures = new Texture[]{
                new Texture("earth.png")
        };
        planet2.add(new RenderComponent(planet2Textures, false, 1.0f));
        planet2.add(new BodyComponent(
                new Vector2(40f,20f),
                new Vector2(),
                new Vector2(),
                3f, 3f,
                150, 1.2f
        ));
        planet2.add(new GravityComponent());
        planet2.add(new CollisionComponent(planet2));
        engine.addEntity(planet2);

        //Debug objective
        Entity objective = new Entity();
        Texture[] objectiveTextures = new Texture[]{
                new Texture("win_full.png")
        };
        objective.add(new RenderComponent(objectiveTextures, false, 1.0f));
        objective.add(new BodyComponent(
                new Vector2(30f,15f),
                new Vector2(),
                new Vector2(),
                1f, 1f,
                150, 0.5f
        ));
        objective.add(new ObjectiveComponent(
                playerPosition
        ));
        engine.addEntity(objective);

        //Debug goal area
        Entity goalArea = new Entity();
        Texture[] goalAreaTextures = new Texture[]{
                new Texture("win_dotted.png")
        };
        goalArea.add(new RenderComponent(goalAreaTextures, false, 1.0f));
        goalArea.add(new BodyComponent(
                new Vector2(40f,20f),
                new Vector2(),
                new Vector2(),
                5f, 5f,
                0, 2.5f
        ));
        goalArea.add(new GoalAreaComponent(
                playerPosition
        ));
        engine.addEntity(goalArea);

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
