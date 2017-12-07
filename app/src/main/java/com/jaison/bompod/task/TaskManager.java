package com.jaison.bompod.task;

import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by jaison on 20/10/17.
 */

public class TaskManager {

    private static final String TAG = "TaskManager";

    private static TaskManager instance;

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }


    private boolean isIdle = true;

    private TaskManager() {
        this.taskQueue = new LinkedList<>();
    }

    Queue<Task> taskQueue;

    public void addTask(Task task) {
        Log.i(TAG, "Task Added : " + task.getDescription());
        this.taskQueue.add(task);
        this.startExecutionIfNotAlready();
    }

    private void startExecutionIfNotAlready() {
        Log.i(TAG, "TaskManager IDLE: " + isIdle);
        if (isIdle) executeTasks();
    }

    private void executeTasks() {
        final Task task = taskQueue.peek();
        if (task == null) {
            isIdle = true;
            return;
        }
        isIdle = false;
        Log.i(TAG, "Executing Task: " + task.id + "\n" + task.getDescription());
        task.assign(new Task.Listener() {
            @Override
            public void onTaskStatusChanged(Task.Status status) {
                switch (status) {
                    case IDLE:
                        break;
                    case PRE_EXECUTE:
                        Log.i(TAG, "Task " + task.id + "State: PRE_EXECUTE");
                        break;
                    case EXECUTING:
                        Log.i(TAG, "Task " + task.id + "State: EXECUTING");
                        break;
                    case POST_EXECUTE:
                        Log.i(TAG, "Task " + task.id + "State: POST_EXECUTE");
                        break;
                    case ERROR:
                        Log.i(TAG, "Task " + task.id + "State: ERROR");
                        task.handleError();
                        executeTasks();
                    case COMPLETE:
                        Log.i(TAG, "Task " + task.id + "State: COMPLETE");
                        taskQueue.remove(task);
                        executeTasks();
                        break;
                }
            }
        });

        task.execute();
    }

}
