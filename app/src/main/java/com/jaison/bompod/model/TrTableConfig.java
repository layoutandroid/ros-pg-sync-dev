package com.jaison.bompod.model;

import com.jaison.bompod.task.Task;

import io.realm.DynamicRealmObject;
import io.realm.RealmResults;

/**
 * Created by jaison on 24/10/17.
 */

public interface TrTableConfig extends RealmTableConfig {
    Task getTask(RealmResults<DynamicRealmObject> results);
}
