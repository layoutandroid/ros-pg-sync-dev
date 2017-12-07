package com.jaison.bompod.model;

import com.jaison.bompod.model.hasura.SubscriptionResponse;
import com.jaison.bompod.task.Task;

/**
 * Created by jaison on 24/10/17.
 */

public interface TpTable {
    String getTableName();
    String topicName();
    String topicId();
    Task getTask(SubscriptionResponse response);
}
