package cn.finalteam.rxgalleryfinal.anim;

import android.view.View;

/**
 * The parent class of all animation classes.
 */
public abstract class Animation {

    public static final int DIRECTION_DOWN = 4;
    public static final int DURATION_DEFAULT = 300; // 300 ms
    // constants
    static final int DIRECTION_LEFT = 1;
    static final int DIRECTION_RIGHT = 2;
    static final int DIRECTION_UP = 3;
    static final int DURATION_LONG = 500;    // 500 ms

    View view;

    /**
     * This method animates the properties of the view specific to the Animation
     * object.
     */
    public abstract void animate();

}
