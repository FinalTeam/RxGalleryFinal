package cn.finalteam.rxgalleryfinal.anim;

/**
 * This interface is a custom listener to determine the end of an animation.
 * 
 * @author Phu
 * 
 */
public interface AnimationListener {

	/**
	 * This method is called when the animation ends.
	 * 
	 * @param animation
	 *            The Animation object.
	 */
	public void onAnimationEnd(Animation animation);
}

