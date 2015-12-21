package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.slingshot.components.ImageComponent;
import se.slingshot.components.PositionComponent;
import se.slingshot.components.SizeComponent;

/**
 * DESC
 *
 * @author Marc
 * @since 2015-12
 */
public class RenderSystem extends EntitySystem {
    // ECS
    private ImmutableArray<Entity> entities;
    private ComponentMapper<ImageComponent> imageMapper = ComponentMapper.getFor(ImageComponent.class);
    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<SizeComponent> sizeMapper = ComponentMapper.getFor(SizeComponent.class);

    // Render
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ImageComponent.class, PositionComponent.class, SizeComponent.class).get());

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
        spriteBatch.setTransformMatrix(camera.combined);

        // todo: render background

        spriteBatch.begin();
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            ImageComponent image = imageMapper.get(entity);
            PositionComponent position = positionMapper.get(entity);
            SizeComponent size = sizeMapper.get(entity);

            System.out.println("Målar x["+position.x+"] y["+position.y+"] w["+size.width+"] h["+size.height+"]");
            spriteBatch.draw(image.texture, position.x, position.y, size.width, size.height);
        }
        spriteBatch.end();
    }
}
