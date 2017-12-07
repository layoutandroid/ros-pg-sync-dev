package com.jaison.bompod.model;

import com.jaison.bompod.task.Task;

import io.realm.DynamicRealmObject;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;

/**
 * Created by jaison on 24/10/17.
 */

public interface RealmTableConfig {
    String getTableName();
    SyncConfiguration getSyncConfiguration();
}
