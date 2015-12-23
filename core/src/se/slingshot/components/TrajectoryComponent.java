package se.slingshot.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Entity has a trajectory displayed
 *
 * @author emanu
 * @since 2015-12
 */
public class TrajectoryComponent implements Component {

    public ArrayList<Vector2> trajectory;

    public TrajectoryComponent(int length) {
        trajectory = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            trajectory.add(new Vector2());
        }
    }
}
