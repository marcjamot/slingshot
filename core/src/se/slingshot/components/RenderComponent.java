package se.slingshot.components;

import com.badlogic.ashley.core.Component;
import se.slingshot.implementations.Animation;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles images and animation
 *
 * @author Marc
 * @since 2015-12
 */
public class RenderComponent implements Component {
    public final Map<String, Animation> animations;
    public Animation activeAnimation;
    public final boolean repeatAnimation;
    public final float timePerAnimation;

    /** If the entity should be rendered */
    public boolean visible = true;

    public int animationIndex;
    public float animationDeltaTime;

    public RenderComponent(boolean repeatAnimation, float timePerAnimation, Animation[] animations) {
        this.repeatAnimation = repeatAnimation;
        this.timePerAnimation = timePerAnimation;
        this.animations = new HashMap<>();
        for (Animation animation : animations) {
            this.animations.put(animation.name, animation);
        }
        this.activeAnimation = animations[0];
    }

    /**
     * Changes the active animation if given animation exists
     * @param animationName Animation name to change to
     */
    public void changeActiveAnimation(String animationName){
        if(animations.containsKey(animationName)){
            activeAnimation = animations.get(animationName);
            animationIndex = 0;
            animationDeltaTime = 0;
        }
    }
}
