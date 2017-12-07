package com.jaison.bompod.model.table;

import android.util.Log;

import com.google.gson.Gson;
import com.jaison.bompod.constant.RealmConfig;
import com.jaison.bompod.model.TpTable;
import com.jaison.bompod.model.hasura.ProductRecord;
import com.jaison.bompod.model.hasura.SubscriptionData;
import com.jaison.bompod.model.hasura.SubscriptionResponse;
import com.jaison.bompod.task.EmptyTask;
import com.jaison.bompod.task.ProductRecordSyncTask;
import com.jaison.bompod.task.Task;

/**
 * Created by jaison on 24/10/17.
 */

public class TpTable1 implements TpTable {

    private static String TAG = "TpTable1";

    @Override
    public String topicId() {
        return "product1";
    }

    @Override
    public String topicName() {
        return "hasuradb.hdb_catalog.product";
    }

    @Override
    public String getTableName() {
        return "Product";
    }

    @Override
    public Task getTask(SubscriptionResponse response) {
        try {
            SubscriptionData subscriptionData = response.getData();
            ProductRecord record;
            boolean isDelete = false;
            if (subscriptionData.getPayload().getOperation().equalsIgnoreCase("d")) {
                isDelete = true;
                record = new Gson().fromJson(subscriptionData.getPayload().getBeforeData(), ProductRecord.class);
            } else
                record = new Gson().fromJson(subscriptionData.getPayload().getData(), ProductRecord.class);
            Log.i(TAG, "ProductRecord: " + subscriptionData.toString());
            return new ProductRecordSyncTask(record.toTaskObject(), RealmConfig.URL.getForTableName(getTableName()), isDelete);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return new EmptyTask();
        }
    }
}
