package cn.finalteam.rxgalleryfinal.rxjob;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


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
        try {
            if (jobQueue.isEmpty() && queueFree) {
                jobQueue.offer(job);
                start();
            } else {
                jobQueue.offer(job);
            }
        } catch (Exception e) {
        }

    }

    private void start() {
        Observable.create((ObservableOnSubscribe<Job>) e -> {
            queueFree = false;
            Job job;
            while ((job = jobQueue.poll()) != null) {
                job.onRunJob();
            }
            e.onComplete();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Job>() {
                    @Override
                    public void onNext(@NonNull Job job) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        queueFree = true;
                    }

                });
    }

    public void clear() {
        try {
            jobQueue.clear();
        } catch (Exception e) {
        }
    }
}
