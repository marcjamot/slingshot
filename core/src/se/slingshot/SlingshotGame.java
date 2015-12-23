package se.slingshot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class SlingshotGame extends Game {
    @Override
    public void create() {
        Screen gameScreen = new GameScreen();
        setScreen(gameScreen);
    }
}
