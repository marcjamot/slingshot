package se.slingshot.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import se.slingshot.components.ControllableComponent;

/**
 * DESC
 *
 * @author Marc
 * @since 2015-12
 */
public class FuelBar extends Actor {
    private final ControllableComponent controllableComponent;

    public FuelBar(ControllableComponent controllableComponent) {
        this.controllableComponent = controllableComponent;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(getX(), getY(), getWidth() * controllableComponent.fuel, getHeight());
        shapeRenderer.end();
    }
}
