package com.jaison.bompod.model.table;

import com.jaison.bompod.model.TrTable;
import com.jaison.bompod.task.Task;
import com.jaison.bompod.task.PosDataSyncTask;

import io.realm.DynamicRealmObject;
import io.realm.RealmResults;

/**
 * Created by jaison on 24/10/17.
 */

public class TrTable1 implements TrTable {

    @Override
    public String getTableName() {
        return "PosData";
    }

    @Override
    public Task getTask(RealmResults<DynamicRealmObject> results) {
        return new PosDataSyncTask(results);
    }
}
