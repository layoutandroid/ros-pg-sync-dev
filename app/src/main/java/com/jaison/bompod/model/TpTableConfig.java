package com.jaison.bompod.model;

import com.jaison.bompod.model.hasura.SubscriptionResponse;
import com.jaison.bompod.task.Task;

import java.util.List;

/**
 * Created by jaison on 24/10/17.
 */

public interface TpTableConfig extends RealmTableConfig {
    Task getTask(SubscriptionResponse response);
}
