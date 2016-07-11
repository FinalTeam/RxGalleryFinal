package cn.finalteam.rxgalleryfinal.anim;

import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;

/**
 * This interface is implemented only by animation classes that can be combined
 * to animate together.
 * 
 */
public interface Combinable {
	
	public void animate();
	public AnimatorSet getAnimatorSet();
	public Animation setInterpolator(TimeInterpolator interpolator);
	public long getDuration();
	public Animation setDuration(long duration);
	public Animation setListener(AnimationListener listener);

}
