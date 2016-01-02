package se.slingshot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import se.slingshot.interfaces.ScreenInterface;
import se.slingshot.menu.ChooseLevelScreen;

public class SlingshotGame extends Game implements ScreenInterface {
    private String loadedLevel;

    @Override
    public void create() {
        menu();
    }

    @Override
    public void startLevel(String name) {
        loadedLevel = name;
        setScreen(new GameScreen(this, name));
    }

    @Override
    public void reloadLevel() {
        setScreen(new GameScreen(this, loadedLevel));
    }

    @Override
    public void menu() {
        setScreen(new ChooseLevelScreen(this));
    }
}
