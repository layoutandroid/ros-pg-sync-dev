package com.jaison.bompod.task;

/**
 * Created by jaison on 24/10/17.
 */

public class EmptyTask extends Task {

    @Override
    public void onExecute(ExecutionCallback callback) {
        callback.onComplete();
    }

    @Override
    public String getDescription() {
        return "Does Nothing";
    }
}
