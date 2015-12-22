package se.slingshot;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.ControllableComponent;
import se.slingshot.components.DeathComponent;
import se.slingshot.components.ImageComponent;
import se.slingshot.systems.CollisionSystem;
import se.slingshot.systems.ControlSystem;
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
        MovementSystem movementSystem = new MovementSystem();
        engine.addSystem(movementSystem);
        RenderSystem renderSystem = new RenderSystem();
        engine.addSystem(renderSystem);

        // Debug init data
        Entity player = new Entity();
        player.add(new ImageComponent("spaceship_fire.png"));
        player.add(new BodyComponent(
                new Vector2(3, 3),
                new Vector2(1,0),
                new Vector2(0,0),
                2, 2,
                1, 0.5f
        ));
        player.add(new ControllableComponent(180,10));
        player.add(new DeathComponent());
        engine.addEntity(player);

        // Debug planet
        Entity planet = new Entity();
        planet.add(new ImageComponent("sun.png"));
        planet.add(new BodyComponent(
                new Vector2(4, 4),
                new Vector2(),
                new Vector2(),
                4, 4,
                0, 1.4f
        ));
        engine.addEntity(planet);
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
