package cn.finalteam.rxgalleryfinal.rxjob;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/31 上午9:12
 */
public class JobManager {

    private final Queue<Job> jobQueue;
    private boolean queueFree = true;

    public JobManager() {
        jobQueue = new LinkedBlockingQueue<>();
    }

    public void addJob(Job job) {
        if(jobQueue.isEmpty() && queueFree){
            jobQueue.offer(job);
            start();
        } else {
            jobQueue.offer(job);
        }

    }

    private void start() {
        Observable.create(new Observable.OnSubscribe<Job>() {
            @Override
            public void call(Subscriber<? super Job> subscriber) {
                queueFree = false;
                Job job;
                while ((job = jobQueue.poll()) != null){
                    job.onRunJob();
                }
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Job>() {
            @Override
            public void onCompleted() {
                queueFree = true;
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Job job) {
            }
        });
    }
}
