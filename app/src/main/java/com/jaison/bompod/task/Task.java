package com.jaison.bompod.task;

import java.util.UUID;

/**
 * Created by jaison on 20/10/17.
 */

public abstract class Task {

    public String id = UUID.randomUUID().toString();

    enum Status {
        PRE_EXECUTE,
        EXECUTING,
        POST_EXECUTE,
        ERROR,
        IDLE,
        COMPLETE
    }

    public interface ExecutionCallback {
        void onComplete();
        void onError();
    }

    public interface Listener {
        void onTaskStatusChanged(Status status);
    }

    public void assign(Listener listener) {
        this.listener = listener;
    }

    Listener listener;
    private Status currentStatus = Status.IDLE;

    private void setCurrentStatus(Status status) {
        this.currentStatus = status;
        if (listener != null) {
            listener.onTaskStatusChanged(status);
        }
    }

    public abstract void onExecute(ExecutionCallback callback);

    protected void execute() {
        setCurrentStatus(Status.PRE_EXECUTE);
        onPreExecute(new ExecutionCallback() {

            @Override
            public void onError() {
                setCurrentStatus(Status.ERROR);
            }

            @Override
            public void onComplete() {
                setCurrentStatus(Status.EXECUTING);
                onExecute(new ExecutionCallback() {

                    @Override
                    public void onError() {
                        setCurrentStatus(Status.ERROR);
                    }

                    @Override
                    public void onComplete() {
                        setCurrentStatus(Status.POST_EXECUTE);
                        onPostExecute(new ExecutionCallback() {

                            @Override
                            public void onError() {
                                setCurrentStatus(Status.ERROR);
                            }

                            @Override
                            public void onComplete() {
                                setCurrentStatus(Status.COMPLETE);
                            }
                        });
                    }
                });
            }
        });
    }

    public abstract String getDescription();

    public void handleError() {

    }

    public void onPreExecute(ExecutionCallback callback) {
        callback.onComplete();
    }
    public void onPostExecute(ExecutionCallback callback) {
        callback.onComplete();
    }
}
