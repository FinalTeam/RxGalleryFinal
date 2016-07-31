package cn.finalteam.rxgalleryfinal.rxjob.job;

import android.content.Context;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.rxjob.Job;
import cn.finalteam.rxgalleryfinal.rxjob.JobCreator;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/31 上午11:46
 */
public class ImageThmbnailJobCreate implements JobCreator {

    private MediaBean mediaBean;
    private Context context;
    private Job job;

    public ImageThmbnailJobCreate(Context context, MediaBean mediaBean) {
        this.context = context;
        this.mediaBean = mediaBean;
    }

    @Override
    public Job create() {
        Job.Params params = new Job.Params(mediaBean.getOriginalPath(), mediaBean);
        job = new ImageThmbnailJob(context, params);
        return job;
    }


}
