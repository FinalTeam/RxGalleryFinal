package cn.finalteam.rxgalleryfinal.rxjob;

import android.support.annotation.NonNull;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/31 上午9:15
 */
public interface Job {
    public enum Result {

        SUCCESS(), FAILURE();

        private Object data;

        Result(){
        }

        public void setResultData(Object data){
            this.data = data;
        }

        public Object getResultData() {
            return data;
        }
    }

    public class Params {
        private Object data;
        private String tag;

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

    Result onRunJob();
}
