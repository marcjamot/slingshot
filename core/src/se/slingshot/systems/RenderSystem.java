package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.ControllableComponent;
import se.slingshot.components.RenderComponent;
import se.slingshot.gui.Gui;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles animation and rendering
 *
 * @author Marc
 * @since 2015-12
 */
public class RenderSystem extends EntitySystem {
    // ECS
    private ImmutableArray<Entity> entities;
    private ComponentMapper<RenderComponent> imageMapper = ComponentMapper.getFor(RenderComponent.class);
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);

    // Render
    private final static float TILE_SIZE = 256;

    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;

    private Texture starImage;
    private List<Vector3> stars;

    private final Gui gui = new Gui();
    private final ControllableComponent controllableComponent;

    /**
     * @param controllableComponent Player controllable component for fuel level in gui
     */
    public RenderSystem(ControllableComponent controllableComponent) {
        this.controllableComponent = controllableComponent;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(RenderComponent.class, BodyComponent.class).get());

        gui.create(controllableComponent);
        camera = new OrthographicCamera();
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        resize(width, height);
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

    /**
     * Screen resize
     * @param width Screen width
     * @param height Screen height
     */
    public void resize(int width, int height){
        camera.setToOrtho(false, 30, 30);
        gui.resize(width, height);
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
        spriteBatch.setTransformMatrix(camera.combined);

        // Background
        spriteBatch.begin();
        for (int i = 0; i < 100; i++) {
            Vector3 star = stars.get(i);
            spriteBatch.draw(starImage, star.x * TILE_SIZE, star.y * TILE_SIZE, star.z * TILE_SIZE, star.z * TILE_SIZE);
        }
        spriteBatch.end();

        // Entities
        spriteBatch.begin();
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            BodyComponent body = bodyMapper.get(entity);
            RenderComponent render = imageMapper.get(entity);

            // Check if entity should be rendered
            if (!render.visible) {
                continue;
            }

            // Animation
            render.animationDeltaTime += deltaTime;
            if (render.animationDeltaTime > render.timePerAnimation) {
                render.animationDeltaTime -= render.timePerAnimation;
                render.animationIndex += 1;
                if (render.animationIndex >= render.textures.length) {
                    if (render.repeatAnimation) {
                        render.animationIndex = 0;
                    } else {
                        render.animationIndex -= 1;
                    }
                }
            }

            // Draw
            float halfWidth = body.width * 0.5f;
            float halfHeight = body.height * 0.5f;
            float rotation = body.direction.angle() - 90;

            spriteBatch.draw(
                    render.textures[render.animationIndex],
                    body.position.x * TILE_SIZE - halfWidth * TILE_SIZE,
                    body.position.y * TILE_SIZE - halfHeight * TILE_SIZE,
                    halfWidth * TILE_SIZE,
                    halfHeight * TILE_SIZE,
                    body.width * TILE_SIZE,
                    body.height * TILE_SIZE,
                    1, 1, rotation, 0, 0, (int) TILE_SIZE, (int) TILE_SIZE, false, false);
        }
        spriteBatch.end();

        // Debug draw
        if (drawPointActive) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setTransformMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.circle(drawPointX * TILE_SIZE, drawPointY * TILE_SIZE, drawPointSize);
            shapeRenderer.end();
        }

        gui.render(deltaTime);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        gui.dispose();
        spriteBatch.dispose();
    }

    private static boolean drawPointActive;
    private static float drawPointX;
    private static float drawPointY;
    private static float drawPointSize;

    public static void drawPoint(float x, float y, float size) {
        drawPointActive = true;
        drawPointX = x;
        drawPointY = y;
        drawPointSize = size;
    }
}
