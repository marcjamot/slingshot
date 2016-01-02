package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.RenderComponent;
import se.slingshot.gui.Gui;
import se.slingshot.implementations.GameOver;
import se.slingshot.interfaces.FuelInterface;
import se.slingshot.interfaces.RenderInterface;

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
    private final static float PIXEL_PER_METER = 256;

    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;

    private Texture starImage;
    private List<Vector3> stars;

    private final Gui gui = new Gui();
    private final FuelInterface fuel;
    private final List<RenderInterface> renderInterfaces;

    public RenderSystem(List<RenderInterface> renderInterfaces, FuelInterface fuel, MBassador<GameOver> eventBus) {
        this.renderInterfaces = renderInterfaces;
        this.fuel = fuel;
        eventBus.subscribe(this);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(RenderComponent.class, BodyComponent.class).get());

        gui.create(fuel);
        camera = new OrthographicCamera();
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        resize(width, height);
        spriteBatch = new SpriteBatch();

        starImage = new Texture("star.png");
        stars = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            float x = MathUtils.random(0.0f, 60.0f);
            float y = MathUtils.random(0.0f, 30.0f);
            float size = MathUtils.random(0.0f, 0.1f);
            Vector3 star = new Vector3(x, y, size);
            stars.add(i, star);
        }
    }

    /**
     * Screen resize
     *
     * @param width  Screen width
     * @param height Screen height
     */
    public void resize(int width, int height) {
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
            spriteBatch.draw(starImage, star.x * PIXEL_PER_METER, star.y * PIXEL_PER_METER, star.z * PIXEL_PER_METER, star.z * PIXEL_PER_METER);
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
                if (render.animationIndex >= render.activeAnimation.textures.length) {
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

            Texture texture = render.activeAnimation.textures[render.animationIndex];
            float textureWidth = texture.getWidth();
            float textureHeight = texture.getHeight();

            spriteBatch.draw(
                    texture,
                    body.position.x * PIXEL_PER_METER - halfWidth * PIXEL_PER_METER,
                    body.position.y * PIXEL_PER_METER - halfHeight * PIXEL_PER_METER,
                    halfWidth * PIXEL_PER_METER,
                    halfHeight * PIXEL_PER_METER,
                    body.width * PIXEL_PER_METER,
                    body.height * PIXEL_PER_METER,
                    1, 1, rotation, 0, 0, (int) textureWidth, (int) textureHeight, false, false);
        }
        spriteBatch.end();

        gui.render(deltaTime);
        for (RenderInterface renderInterface : renderInterfaces) {
            renderInterface.render(camera, spriteBatch, PIXEL_PER_METER);
        }

        // Debug draw
        if (drawPointActive) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setTransformMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.circle(drawPointX * PIXEL_PER_METER, drawPointY * PIXEL_PER_METER, drawPointSize);
            shapeRenderer.end();
            shapeRenderer.dispose();
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        gui.dispose();
        spriteBatch.dispose();
    }

    @Handler
    @SuppressWarnings("unused")
    public void handle(GameOver collision) {
        Texture texture = null;
        if(collision == GameOver.Win) {
            texture = new Texture("win.png");
        } else if(collision == GameOver.Lose){
            texture = new Texture("lose.png");
        }
        final Texture finalTexture = texture;
        renderInterfaces.add((c, sB, pPM) -> {
            sB.begin();
            sB.draw(finalTexture, 3 * pPM, 3 * pPM, 40 * pPM, 20 * pPM);
            sB.end();
        });
    }

    private static boolean drawPointActive;
    private static float drawPointX;
    private static float drawPointY;
    private static float drawPointSize;

    @SuppressWarnings("unused")
    public static void drawPoint(float x, float y, float size) {
        drawPointActive = true;
        drawPointX = x;
        drawPointY = y;
        drawPointSize = size;
    }
}
