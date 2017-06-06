package cn.finalteam.rxgalleryfinal.rxjob;

import android.support.annotation.NonNull;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/7/31 上午9:15
 */
public interface Job {
    Result onRunJob();

    enum Result {

        SUCCESS(), FAILURE();

        private Object data;

        Result() {
        }

        public Object getResultData() {
            return data;
        }

        public void setResultData(Object data) {
            this.data = data;
        }
    }

    class Params {
        private final Object data;
        private final String tag;

        public Params(@NonNull String tag, Object requestData) {
            this.tag = tag;
            this.data = requestData;
        }

        public String getTag() {
            return tag;
        }

        public Object getRequestData() {
            return data;
        }
    }
}
