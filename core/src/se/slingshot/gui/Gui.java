package se.slingshot.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import se.slingshot.components.ControllableComponent;

/**
 * In-game gui
 *
 * @author Marc
 * @since 2015-12
 */
public class Gui {
    private Stage stage;

    public void create(ControllableComponent controllableComponent){
        stage = new Stage();

        FuelBar fuelBar = new FuelBar(controllableComponent);
        fuelBar.setPosition(0, 0);
        fuelBar.setSize(stage.getWidth(), stage.getHeight()/50);
        stage.addActor(fuelBar);
    }

    public void resize(int width, int height){
        stage.getViewport().update(width, height);
    }

    public void render(float delta){
        stage.act(delta);
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
    }
}
