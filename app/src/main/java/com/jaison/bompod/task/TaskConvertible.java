package com.jaison.bompod.task;

/**
 * Created by jaison on 20/10/17.
 */

public interface TaskConvertible<T> {
    T toTaskObject();
    String getRealmName();
    boolean shouldExecuteTask();
    void onPostExecute();
}
