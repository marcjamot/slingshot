package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.ImageComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

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
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);

    // Render
    private final static float TILE_SIZE = 256;

    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;

    private Texture starImage;
    private List<Vector3> stars;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ImageComponent.class, BodyComponent.class).get());

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        spriteBatch = new SpriteBatch();

        starImage = new Texture("star.png");
        stars = new ArrayList<Vector3>(100);
        for (int i = 0; i < 100; i++) {
            float x = MathUtils.random(0.0f, 20.0f);
            float y = MathUtils.random(0.0f, 20.0f);
            float size = MathUtils.random(0.0f, 0.1f);
            Vector3 star = new Vector3(x, y, size);
            stars.add(i, star);
        }
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
        spriteBatch.setTransformMatrix(camera.combined);

        // todo: render background
        spriteBatch.begin();
        for (int i = 0; i < 100; i++) {
            Vector3 star = stars.get(i);
            spriteBatch.draw(starImage, star.x * TILE_SIZE, star.y * TILE_SIZE, star.z * TILE_SIZE, star.z * TILE_SIZE);
        }
        spriteBatch.end();

        spriteBatch.begin();
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            ImageComponent image = imageMapper.get(entity);
            BodyComponent body = bodyMapper.get(entity);

            float rotation = body.direction.angle() - 90;
            spriteBatch.draw(image.texture, body.position.x * TILE_SIZE, body.position.y * TILE_SIZE, 0, 0, body.width * TILE_SIZE, body.height * TILE_SIZE, 1, 1, rotation, 0, 0, 256, 256, false, false);
        }
        spriteBatch.end();
    }
}
