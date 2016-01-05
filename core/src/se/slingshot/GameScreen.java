package se.slingshot;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import net.engio.mbassy.bus.MBassador;
import se.slingshot.components.CollisionComponent;
import se.slingshot.implementations.GameOver;
import se.slingshot.interfaces.RenderInterface;
import se.slingshot.interfaces.ScreenInterface;
import se.slingshot.level.LevelLoader;
import se.slingshot.systems.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen when a level is loaded and in game
 *
 * @author Marc
 * @since 2015-12
 */
public class GameScreen implements Screen {
    private Engine engine;
    private RenderSystem renderSystem;

    private final ScreenInterface screenHandler;
    private final String levelName;

    public GameScreen(ScreenInterface screenHandler, String levelName) {
        this.screenHandler = screenHandler;
        this.levelName = levelName;
    }

    @Override
    public void show() {
        engine = new PooledEngine();
        MBassador<CollisionComponent> collisionBus = new MBassador<>();
        MBassador<GameOver> gameOverBus = new MBassador<>();

        CollisionSystem collisionSystem = new CollisionSystem(collisionBus, true);
        ControlSystem controlSystem = new ControlSystem(screenHandler, gameOverBus);
        DeathSystem deathSystem = new DeathSystem(collisionBus, gameOverBus);
        GravitySystem gravitySystem = new GravitySystem();
        MovementSystem movementSystem = new MovementSystem(gameOverBus);
        ObjectiveSystem objectiveSystem = new ObjectiveSystem();
        OrbitSystem orbitSystem = new OrbitSystem();
        TrajectorySystem trajectorySystem = new TrajectorySystem();
        List<RenderInterface> preRenderInterfaces = new ArrayList<>();
        preRenderInterfaces.add(trajectorySystem);
        List<RenderInterface> postRenderInterfaces = new ArrayList<>();
        postRenderInterfaces.add(collisionSystem);
        renderSystem = new RenderSystem(preRenderInterfaces, postRenderInterfaces, controlSystem, gameOverBus);
        WinConditionSystem winConditionSystem = new WinConditionSystem(gameOverBus);

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
