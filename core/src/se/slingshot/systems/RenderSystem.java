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
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;

    private Texture starImage;
    private List<Vector3> stars;

    private final Gui gui = new Gui();
    private final FuelInterface fuel;
    private final List<RenderInterface> preRenderInterfaces;
    private final List<RenderInterface> postRenderInterfaces;

    /**
     * @param preRenderInterfaces Rendered before entities
     * @param postRenderInterfaces Rendered after entities
     */
    public RenderSystem(List<RenderInterface> preRenderInterfaces, List<RenderInterface> postRenderInterfaces, FuelInterface fuel, MBassador<GameOver> eventBus) {
        this.preRenderInterfaces = preRenderInterfaces;
        this.postRenderInterfaces = postRenderInterfaces;
        this.fuel = fuel;
        eventBus.subscribe(this);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(RenderComponent.class, BodyComponent.class).get());

        float screenWidth = 160.0f;
        float screenHeight = 90.0f;
        camera = new OrthographicCamera(screenWidth, screenHeight);
        camera.position.set(screenWidth / 2.0f, screenHeight / 2.0f, 0.0f);
        spriteBatch = new SpriteBatch();

        gui.create(fuel);
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        resize(width, height);

        starImage = new Texture("star.png");
        stars = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            float x = MathUtils.random(0.0f, screenWidth);
            float y = MathUtils.random(0.0f, screenHeight);
            float size = MathUtils.random(0.1f, 0.8f);
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
        gui.resize(width, height);
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        // Background
        spriteBatch.begin();
        for (Vector3 star : stars) {
            spriteBatch.draw(starImage, star.x, star.y, star.z, star.z);
        }
        spriteBatch.end();

        // Pre render interfaces
        for (RenderInterface renderInterface : preRenderInterfaces) {
            renderInterface.render(camera, spriteBatch);
        }

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
                    body.position.x - halfWidth,
                    body.position.y - halfHeight,
                    halfWidth,
                    halfHeight,
                    body.width,
                    body.height,
                    1, 1, rotation, 0, 0, (int) textureWidth, (int) textureHeight, false, false);
        }
        spriteBatch.end();

        // Gui
        gui.render(deltaTime);

        // Post render interfaces
        for (RenderInterface renderInterface : postRenderInterfaces) {
            renderInterface.render(camera, spriteBatch);
        }

        // Debug draw
        if (drawPointActive) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setTransformMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.circle(drawPointX, drawPointY, drawPointSize);
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
        if (collision == GameOver.Win) {
            texture = new Texture("win.png");
        } else if (collision == GameOver.Lose) {
            texture = new Texture("lose.png");
        }
        final Texture finalTexture = texture;
        postRenderInterfaces.add((c, sB) -> {
            sB.begin();
            sB.draw(finalTexture, 20, 20, 120, 50);
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
