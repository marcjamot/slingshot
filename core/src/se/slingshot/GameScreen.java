package se.slingshot;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import se.slingshot.components.ImageComponent;
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

        RenderSystem renderSystem = new RenderSystem();
        engine.addSystem(renderSystem);

        // Debug init data
        Entity player = new Entity();
        player.add(new ImageComponent("spaceship_fire.png"));
        player.add(new PositionComponent(3, 3));
        player.add(new SizeComponent(2, 2));
        engine.addEntity(player);

        // Debug planet
        Entity planet = new Entity();
        planet.add(new ImageComponent("sun.png"));
        planet.add(new PositionComponent(4, 4));
        planet.add(new SizeComponent(4, 4));
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