/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package uk.co.senab.photoview;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;


public interface IPhotoView {

    float DEFAULT_MAX_SCALE = 3.0f;
    float DEFAULT_MID_SCALE = 1.75f;
    float DEFAULT_MIN_SCALE = 1.0f;
    int DEFAULT_ZOOM_DURATION = 200;

    /**
     * Returns true if the PhotoView is set to allow zooming of Photos.
     *
     * @return true if the PhotoView allows zooming.
     */
    boolean canZoom();

    /**
     * Gets the Display Rectangle of the currently displayed Drawable. The Rectangle is relative to
     * this View and includes all scaling and translations.
     *
     * @return - RectF of Displayed Drawable
     */
    RectF getDisplayRect();

    /**
     * Sets the Display Matrix of the currently displayed Drawable. The Rectangle is considered
     * relative to this View and includes all scaling and translations.
     *
     * @param finalMatrix target matrix to set PhotoView to
     * @return - true if rectangle was applied successfully
     */
    boolean setDisplayMatrix(Matrix finalMatrix);

    /**
     * Gets the Display Matrix of the currently displayed Drawable. The Rectangle is considered
     * relative to this View and includes all scaling and translations.
     *
     * @return copy of current Display Matrix
     */
    Matrix getDisplayMatrix();

    /**
     * Copies the Display Matrix of the currently displayed Drawable. The Rectangle is considered
     * relative to this View and includes all scaling and translations.
     *
     * @param matrix target matrix to copy to
     */
    void getDisplayMatrix(Matrix matrix);

    /**
     * Use {@link #getMinimumScale()} instead, this will be removed in future release
     *
     * @return The current minimum scale level. What this value represents depends on the current
     * {@link ImageView.ScaleType}.
     */
    @Deprecated
    float getMinScale();

    /**
     * Use {@link #setMinimumScale(float minimumScale)} instead, this will be removed in future
     * release
     * <p>&nbsp;</p>
     * Sets the minimum scale level. What this value represents depends on the current {@link
     * ImageView.ScaleType}.
     *
     * @param minScale minimum allowed scale
     */
    @Deprecated
    void setMinScale(float minScale);

    /**
     * @return The current minimum scale level. What this value represents depends on the current
     * {@link ImageView.ScaleType}.
     */
    float getMinimumScale();

    /**
     * Sets the minimum scale level. What this value represents depends on the current {@link
     * ImageView.ScaleType}.
     *
     * @param minimumScale minimum allowed scale
     */
    void setMinimumScale(float minimumScale);

    /**
     * Use {@link #getMediumScale()} instead, this will be removed in future release
     *
     * @return The current middle scale level. What this value represents depends on the current
     * {@link ImageView.ScaleType}.
     */
    @Deprecated
    float getMidScale();

    /**
     * Use {@link #setMediumScale(float mediumScale)} instead, this will be removed in future
     * release
     * <p>&nbsp;</p>
     * Sets the middle scale level. What this value represents depends on the current {@link
     * ImageView.ScaleType}.
     *
     * @param midScale medium scale preset
     */
    @Deprecated
    void setMidScale(float midScale);

    /**
     * @return The current medium scale level. What this value represents depends on the current
     * {@link ImageView.ScaleType}.
     */
    float getMediumScale();

    /**
     * Sets the medium scale level. What this value represents depends on the current {@link ImageView.ScaleType}.
     *
     * @param mediumScale medium scale preset
     */
    void setMediumScale(float mediumScale);

    /**
     * Use {@link #getMaximumScale()} instead, this will be removed in future release
     *
     * @return The current maximum scale level. What this value represents depends on the current
     * {@link ImageView.ScaleType}.
     */
    @Deprecated
    float getMaxScale();

    /**
     * Use {@link #setMaximumScale(float maximumScale)} instead, this will be removed in future
     * release
     * <p>&nbsp;</p>
     * Sets the maximum scale level. What this value represents depends on the current {@link
     * ImageView.ScaleType}.
     *
     * @param maxScale maximum allowed scale preset
     */
    @Deprecated
    void setMaxScale(float maxScale);

    /**
     * @return The current maximum scale level. What this value represents depends on the current
     * {@link ImageView.ScaleType}.
     */
    float getMaximumScale();

    /**
     * Sets the maximum scale level. What this value represents depends on the current {@link
     * ImageView.ScaleType}.
     *
     * @param maximumScale maximum allowed scale preset
     */
    void setMaximumScale(float maximumScale);

    /**
     * Returns the current scale value
     *
     * @return float - current scale value
     */
    float getScale();

    /**
     * Changes the current scale to the specified value.
     *
     * @param scale - Value to scale to
     */
    void setScale(float scale);

    /**
     * Return the current scale type in use by the ImageView.
     *
     * @return current ImageView.ScaleType
     */
    ImageView.ScaleType getScaleType();

    /**
     * Controls how the image should be resized or moved to match the size of the ImageView. Any
     * scaling or panning will happen within the confines of this {@link
     * ImageView.ScaleType}.
     *
     * @param scaleType - The desired scaling mode.
     */
    void setScaleType(ImageView.ScaleType scaleType);

    /**
     * Whether to allow the ImageView's parent to intercept the touch event when the photo is scroll
     * to it's horizontal edge.
     *
     * @param allow whether to allow intercepting by parent element or not
     */
    void setAllowParentInterceptOnEdge(boolean allow);

    /**
     * Allows to set all three scale levels at once, so you don't run into problem with setting
     * medium/minimum scale before the maximum one
     *
     * @param minimumScale minimum allowed scale
     * @param mediumScale  medium allowed scale
     * @param maximumScale maximum allowed scale preset
     */
    void setScaleLevels(float minimumScale, float mediumScale, float maximumScale);

    /**
     * Register a callback to be invoked when the Photo displayed by this view is long-pressed.
     *
     * @param listener - Listener to be registered.
     */
    void setOnLongClickListener(View.OnLongClickListener listener);

    /**
     * Register a callback to be invoked when the Matrix has changed for this View. An example would
     * be the user panning or scaling the Photo.
     *
     * @param listener - Listener to be registered.
     */
    void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener listener);

    /**
     * PhotoViewAttacher.OnPhotoTapListener reference should be stored in a variable instead, this
     * will be removed in future release.
     * <p>&nbsp;</p>
     * Returns a listener to be invoked when the Photo displayed by this View is tapped with a
     * single tap.
     *
     * @return PhotoViewAttacher.OnPhotoTapListener currently set, may be null
     */
    @Deprecated
    PhotoViewAttacher.OnPhotoTapListener getOnPhotoTapListener();

    /**
     * Register a callback to be invoked when the Photo displayed by this View is tapped with a
     * single tap.
     *
     * @param listener - Listener to be registered.
     */
    void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener listener);

    /**
     * Enables rotation via PhotoView internal functions.
     *
     * @param rotationDegree - Degree to rotate PhotoView to, should be in range 0 to 360
     */
    void setRotationTo(float rotationDegree);

    /**
     * Enables rotation via PhotoView internal functions.
     *
     * @param rotationDegree - Degree to rotate PhotoView by, should be in range 0 to 360
     */
    void setRotationBy(float rotationDegree);

    /**
     * PhotoViewAttacher.OnViewTapListener reference should be stored in a variable instead, this
     * will be removed in future release.
     * <p>&nbsp;</p>
     * Returns a callback listener to be invoked when the View is tapped with a single tap.
     *
     * @return PhotoViewAttacher.OnViewTapListener currently set, may be null
     */
    @Deprecated
    PhotoViewAttacher.OnViewTapListener getOnViewTapListener();

    /**
     * Register a callback to be invoked when the View is tapped with a single tap.
     *
     * @param listener - Listener to be registered.
     */
    void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener listener);

    /**
     * Changes the current scale to the specified value.
     *
     * @param scale   - Value to scale to
     * @param animate - Whether to animate the scale
     */
    void setScale(float scale, boolean animate);

    /**
     * Changes the current scale to the specified value, around the given focal point.
     *
     * @param scale   - Value to scale to
     * @param focalX  - X Focus Point
     * @param focalY  - Y Focus Point
     * @param animate - Whether to animate the scale
     */
    void setScale(float scale, float focalX, float focalY, boolean animate);

    /**
     * Allows you to enable/disable the zoom functionality on the ImageView. When disable the
     * ImageView reverts to using the FIT_CENTER matrix.
     *
     * @param zoomable - Whether the zoom functionality is enabled.
     */
    void setZoomable(boolean zoomable);

    /**
     * Enables rotation via PhotoView internal functions. Name is chosen so it won't collide with
     * View.setRotation(float) in API since 11
     *
     * @param rotationDegree - Degree to rotate PhotoView to, should be in range 0 to 360
     * @deprecated use {@link #setRotationTo(float)}
     */
    void setPhotoViewRotation(float rotationDegree);

    /**
     * Extracts currently visible area to Bitmap object, if there is no image loaded yet or the
     * ImageView is already destroyed, returns {@code null}
     *
     * @return currently visible area as bitmap or null
     */
    Bitmap getVisibleRectangleBitmap();

    /**
     * Allows to change zoom transition speed, default value is 200 (PhotoViewAttacher.DEFAULT_ZOOM_DURATION).
     * Will default to 200 if provided negative value
     *
     * @param milliseconds duration of zoom interpolation
     */
    void setZoomTransitionDuration(int milliseconds);

    /**
     * Will return instance of IPhotoView (eg. PhotoViewAttacher), can be used to provide better
     * integration
     *
     * @return IPhotoView implementation instance if available, null if not
     */
    IPhotoView getIPhotoViewImplementation();

    /**
     * Sets custom double tap listener, to intercept default given functions. To reset behavior to
     * default, you can just pass in "null" or public field of PhotoViewAttacher.defaultOnDoubleTapListener
     *
     * @param newOnDoubleTapListener custom OnDoubleTapListener to be set on ImageView
     */
    void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener newOnDoubleTapListener);

    /**
     * Will report back about scale changes
     *
     * @param onScaleChangeListener OnScaleChangeListener instance
     */
    void setOnScaleChangeListener(PhotoViewAttacher.OnScaleChangeListener onScaleChangeListener);

    /**
     * Will report back about fling(single touch)
     *
     * @param onSingleFlingListener OnSingleFlingListener instance
     */
    void setOnSingleFlingListener(PhotoViewAttacher.OnSingleFlingListener onSingleFlingListener);
}
