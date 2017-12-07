package com.jaison.bompod.model.table;

import com.jaison.bompod.model.TrTable;
import com.jaison.bompod.task.PosDataSyncTask;
import com.jaison.bompod.task.Quotation_PosDataSyncTask;
import com.jaison.bompod.task.Task;

import io.realm.DynamicRealmObject;
import io.realm.RealmResults;

/**
 * Created by jaison on 24/10/17.
 */

public class Quotation_posrealm implements TrTable {

    @Override
    public String getTableName() {
        return "quotation_pos";
    }

    @Override
    public Task getTask(RealmResults<DynamicRealmObject> results) {
        return new Quotation_PosDataSyncTask(results);
    }
}
