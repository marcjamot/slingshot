package se.slingshot.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import se.slingshot.interfaces.FuelInterface;

/**
 * Actor for showing the fuel level
 *
 * @author Marc
 * @since 2015-12
 */
public class FuelBar extends Actor {
    private final FuelInterface fuel;

    public FuelBar(FuelInterface fuel) {
        this.fuel = fuel;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(getX(), getY(), getWidth() * fuel.get(), getHeight());
        shapeRenderer.end();
    }
}
