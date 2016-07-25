package cn.finalteam.rxgalleryfinal.ui.adapter;

import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Desction:
 * A {@link PagerAdapter} which behaves like an {@link android.widget.Adapter} with view types and
 * view recycling.
 * Author:pengjianbo
 * Date:15/12/22 下午6:21
 */
public abstract class RecyclingPagerAdapter extends PagerAdapter {
    static final int IGNORE_ITEM_VIEW_TYPE = AdapterView.ITEM_VIEW_TYPE_IGNORE;

    private final RecycleBin recycleBin;

    public RecyclingPagerAdapter() {
        this(new RecycleBin());
    }

    RecyclingPagerAdapter(RecycleBin recycleBin) {
        this.recycleBin = recycleBin;
        recycleBin.setViewTypeCount(getViewTypeCount());
    }

    @Override
    public void notifyDataSetChanged() {
        recycleBin.scrapActiveViews();
        super.notifyDataSetChanged();
    }

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        int viewType = getItemViewType(position);
        View view = null;
        if (viewType != IGNORE_ITEM_VIEW_TYPE) {
            view = recycleBin.getScrapView(position, viewType);
        }
        view = getView(position, view, container);
        container.addView(view);
        return view;
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        int viewType = getItemViewType(position);
        if (viewType != IGNORE_ITEM_VIEW_TYPE) {
            recycleBin.addScrapView(view, position, viewType);
        }
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * <p>
     * Returns the number of types of Views that will be created by
     * {@link #getView}. Each type represents a set of views that can be
     * converted in {@link #getView}. If the adapter always returns the same
     * type of View for all items, this method should return 1.
     * </p>
     * <p>
     * This method will only be called when when the adapter is set on the
     * the {@link AdapterView}.
     * </p>
     *
     * @return The number of types of Views that will be created by this adapter
     */
    public int getViewTypeCount() {
        return 1;
    }

    /**
     * Get the type of View that will be created by {@link #getView} for the specified item.
     *
     * @param position The position of the item within the adapter's data set whose view type we
     * want.
     * @return An integer representing the type of View. Two views should share the same type if one
     * can be converted to the other in {@link #getView}. Note: Integers must be in the
     * range 0 to {@link #getViewTypeCount} - 1. {@link #IGNORE_ITEM_VIEW_TYPE} can
     * also be returned.
     * @see #IGNORE_ITEM_VIEW_TYPE
     */
    @SuppressWarnings("UnusedParameters") // Argument potentially used by subclasses.
    public int getItemViewType(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     * we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     * is non-null and of an appropriate type before using. If it is not possible to convert
     * this view to display the correct data, this method can create a new view.
     * Heterogeneous lists can specify their number of view types, so that this View is
     * always of the right type (see {@link #getViewTypeCount()} and
     * {@link #getItemViewType(int)}).
     * @param container The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    public abstract View getView(int position, View convertView, ViewGroup container);

    /**
     * The RecycleBin facilitates reuse of views across layouts. The RecycleBin has two levels of
     * storage: ActiveViews and ScrapViews. ActiveViews are those views which were onscreen at the
     * start of a layout. By construction, they are displaying current information. At the end of
     * layout, all views in ActiveViews are demoted to ScrapViews. ScrapViews are old views that
     * could potentially be used by the adapter to avoid allocating views unnecessarily.
     * <p>
     * This class was taken from Android's implementation of {@link android.widget.AbsListView} which
     * is copyrighted 2006 The Android Open Source Project.
     */
    public static class RecycleBin {
        /**
         * Views that were on screen at the start of layout. This array is populated at the start of
         * layout, and at the end of layout all view in activeViews are moved to scrapViews.
         * Views in activeViews represent a contiguous range of Views, with position of the first
         * view store in mFirstActivePosition.
         */
        private View[] activeViews = new View[0];
        private int[] activeViewTypes = new int[0];

        /** Unsorted views that can be used by the adapter as a convert view. */
        private SparseArray<View>[] scrapViews;

        private int viewTypeCount;

        private SparseArray<View> currentScrapViews;

        public void setViewTypeCount(int viewTypeCount) {
            if (viewTypeCount < 1) {
                throw new IllegalArgumentException("Can't have a viewTypeCount < 1");
            }
            //noinspection unchecked
            SparseArray<View>[] scrapViews = new SparseArray[viewTypeCount];
            for (int i = 0; i < viewTypeCount; i++) {
                scrapViews[i] = new SparseArray<View>();
            }
            this.viewTypeCount = viewTypeCount;
            currentScrapViews = scrapViews[0];
            this.scrapViews = scrapViews;
        }

        protected boolean shouldRecycleViewType(int viewType) {
            return viewType >= 0;
        }

        /** @return A view from the ScrapViews collection. These are unordered. */
        View getScrapView(int position, int viewType) {
            if (viewTypeCount == 1) {
                return retrieveFromScrap(currentScrapViews, position);
            } else if (viewType >= 0 && viewType < scrapViews.length) {
                return retrieveFromScrap(scrapViews[viewType], position);
            }
            return null;
        }

        /**
         * Put a view into the ScrapViews list. These views are unordered.
         *
         * @param scrap The view to add
         */
        void addScrapView(View scrap, int position, int viewType) {
            if (viewTypeCount == 1) {
                currentScrapViews.put(position, scrap);
            } else {
                scrapViews[viewType].put(position, scrap);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                scrap.setAccessibilityDelegate(null);
            }
        }

        /** Move all views remaining in activeViews to scrapViews. */
        void scrapActiveViews() {
            final View[] activeViews = this.activeViews;
            final int[] activeViewTypes = this.activeViewTypes;
            final boolean multipleScraps = viewTypeCount > 1;

            SparseArray<View> scrapViews = currentScrapViews;
            final int count = activeViews.length;
            for (int i = count - 1; i >= 0; i--) {
                final View victim = activeViews[i];
                if (victim != null) {
                    int whichScrap = activeViewTypes[i];

                    activeViews[i] = null;
                    activeViewTypes[i] = -1;

                    if (!shouldRecycleViewType(whichScrap)) {
                        continue;
                    }

                    if (multipleScraps) {
                        scrapViews = this.scrapViews[whichScrap];
                    }
                    scrapViews.put(i, victim);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        victim.setAccessibilityDelegate(null);
                    }
                }
            }

            pruneScrapViews();
        }

        /**
         * Makes sure that the size of scrapViews does not exceed the size of activeViews.
         * (This can happen if an adapter does not recycle its views).
         */
        private void pruneScrapViews() {
            final int maxViews = activeViews.length;
            final int viewTypeCount = this.viewTypeCount;
            final SparseArray<View>[] scrapViews = this.scrapViews;
            for (int i = 0; i < viewTypeCount; ++i) {
                final SparseArray<View> scrapPile = scrapViews[i];
                int size = scrapPile.size();
                final int extras = size - maxViews;
                size--;
                for (int j = 0; j < extras; j++) {
                    scrapPile.remove(scrapPile.keyAt(size--));
                }
            }
        }

        static View retrieveFromScrap(SparseArray<View> scrapViews, int position) {
            int size = scrapViews.size();
            if (size > 0) {
                // See if we still have a view for this position.
                for (int i = 0; i < size; i++) {
                    int fromPosition = scrapViews.keyAt(i);
                    View view = scrapViews.get(fromPosition);
                    if (fromPosition == position) {
                        scrapViews.remove(fromPosition);
                        return view;
                    }
                }
                int index = size - 1;
                View r = scrapViews.valueAt(index);
                scrapViews.remove(scrapViews.keyAt(index));
                return r;
            } else {
                return null;
            }
        }
    }
}
