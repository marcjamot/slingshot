package se.slingshot;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import net.engio.mbassy.bus.MBassador;
import se.slingshot.components.CollisionComponent;
import se.slingshot.interfaces.RenderInterface;
import se.slingshot.level.LevelLoader;
import se.slingshot.systems.*;

/**
 * Screen when a level is loaded and in game
 *
 * @author Marc
 * @since 2015-12
 */
public class GameScreen implements Screen {
    private Engine engine;
    private RenderSystem renderSystem;

    private final String levelName;

    public GameScreen(String levelName) {
        this.levelName = levelName;
    }

    @Override
    public void show() {
        engine = new PooledEngine();
        MBassador<CollisionComponent> eventBus = new MBassador<>();

        CollisionSystem collisionSystem = new CollisionSystem(eventBus, false);
        ControlSystem controlSystem = new ControlSystem();
        DeathSystem deathSystem = new DeathSystem(eventBus);
        GravitySystem gravitySystem = new GravitySystem();
        MovementSystem movementSystem = new MovementSystem();
        ObjectiveSystem objectiveSystem = new ObjectiveSystem();
        OrbitSystem orbitSystem = new OrbitSystem();
        TrajectorySystem trajectorySystem = new TrajectorySystem();
        RenderInterface[] renderInterfaces = new RenderInterface[]{
                collisionSystem,
                trajectorySystem
        };
        renderSystem = new RenderSystem(renderInterfaces, controlSystem);
        WinConditionSystem winConditionSystem = new WinConditionSystem();

        // Add the systems in the order they should execute
        engine.addSystem(controlSystem);
        engine.addSystem(gravitySystem);
        engine.addSystem(orbitSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(collisionSystem);
        engine.addSystem(deathSystem);
        engine.addSystem(objectiveSystem);
        engine.addSystem(winConditionSystem);
        engine.addSystem(trajectorySystem);
        engine.addSystem(renderSystem);

        new LevelLoader().from(engine, levelName);
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
