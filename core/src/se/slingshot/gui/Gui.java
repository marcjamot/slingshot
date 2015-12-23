package se.slingshot.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import se.slingshot.interfaces.FuelInterface;

/**
 * In-game gui
 *
 * @author Marc
 * @since 2015-12
 */
public class Gui {
    private Stage stage;
    private FuelBar fuelBar;

    public void create(FuelInterface fuel) {
        stage = new Stage();

        fuelBar = new FuelBar(fuel);
        fuelBar.setPosition(0, 0);
        fuelBar.setSize(stage.getWidth(), stage.getHeight() / 50);
        stage.addActor(fuelBar);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        fuelBar.setSize(width, height / 50);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
