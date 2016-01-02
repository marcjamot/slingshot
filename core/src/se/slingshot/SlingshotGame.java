package se.slingshot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import se.slingshot.interfaces.ScreenInterface;
import se.slingshot.menu.ChooseLevelScreen;

public class SlingshotGame extends Game implements ScreenInterface {
    @Override
    public void create() {
        Screen mainScreen = new GameScreen();
        setScreen(mainScreen);
    }

    @Override
    public void change(Screen screen) {
        setScreen(screen);
    }
}
