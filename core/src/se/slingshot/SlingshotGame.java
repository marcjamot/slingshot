package se.slingshot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import se.slingshot.interfaces.ScreenInterface;
import se.slingshot.menu.ChooseLevelScreen;

public class SlingshotGame extends Game implements ScreenInterface {
    private String loadedLevel;
    private Music music;

    @Override
    public void create() {
        menu();
        music = Gdx.audio.newMusic(Gdx.files.internal("low-fi.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    @Override
    public void dispose() {
        super.dispose();
        music.dispose();
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
