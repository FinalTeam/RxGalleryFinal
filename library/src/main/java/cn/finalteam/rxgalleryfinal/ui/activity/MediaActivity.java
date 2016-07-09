package cn.finalteam.rxgalleryfinal.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.di.component.ActivityFragmentComponent;
import cn.finalteam.rxgalleryfinal.di.component.DaggerActivityFragmentComponent;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.module.ActivityFragmentModule;
import cn.finalteam.rxgalleryfinal.ui.fragment.ImageGridFragment;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaPageFragment;
import cn.finalteam.rxgalleryfinal.ui.fragment.VideoGridFragment;
import cn.finalteam.rxgalleryfinal.view.ActivityFragmentView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 上午10:01
 */
public class MediaActivity extends BaseActivity implements ActivityFragmentView {

    @Inject
    Configuration mConfiguration;

//    @Inject
//    ActivityFragmentPresenterImpl mActivityFragmentPresenter;
//
    @Inject
    ImageGridFragment mImageGridFragment;
    @Inject
    VideoGridFragment mVideoGridFragment;
    @Inject
    MediaPageFragment mMediaPageFragment;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galleryfinal_activity_media);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if(mConfiguration.isImage()) {
            showImageGridFragment();
        } else {
            showVideoGridFragment();
        }
    }


    @Override
    public void showImageGridFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mImageGridFragment)
                .commit();
        setTitle("图片");
    }

    @Override
    public void showVideoGridFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mVideoGridFragment)
                .commit();
        setTitle("视频");
    }

    @Override
    public void showMediaPageFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mMediaPageFragment)
                .commit();
    }

    @Override
    protected void setupComponent(RxGalleryFinalComponent rxGalleryFinalComponent) {
        ActivityFragmentComponent activityFragmentComponent = DaggerActivityFragmentComponent.builder()
            .rxGalleryFinalComponent(rxGalleryFinalComponent)
            .activityFragmentModule(new ActivityFragmentModule())
            .build();
        activityFragmentComponent.inject(this);
    }
}
