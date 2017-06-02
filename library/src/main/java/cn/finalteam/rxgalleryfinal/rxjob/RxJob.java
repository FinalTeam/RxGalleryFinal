package cn.finalteam.rxgalleryfinal.rxjob;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/31 上午9:09
 */
public class RxJob {

    private static RxJob rxJob;
    private final JobManager jobManager;

    public RxJob() {
        jobManager = new JobManager();
    }

    public static RxJob getDefault() {
        if (rxJob == null) {
            rxJob = new RxJob();
        }
        return rxJob;
    }

    public void addJob(Job job) {
        jobManager.addJob(job);
    }

    public void clearJob() {
        jobManager.clear();
    }
}
