package com.jaison.bompod.model.table;

import android.util.Log;

import com.google.gson.Gson;
import com.jaison.bompod.constant.RealmConfig;
import com.jaison.bompod.model.TpTable;
import com.jaison.bompod.model.hasura.ProductRecord;
import com.jaison.bompod.model.hasura.SubscriptionData;
import com.jaison.bompod.model.hasura.SubscriptionResponse;
import com.jaison.bompod.model.realm.quotation_posrealm;
import com.jaison.bompod.task.EmptyTask;
import com.jaison.bompod.task.ProductRecordSyncTask;
import com.jaison.bompod.task.Task;
import com.jaison.bompod.task.quotation_posRecordSyncTask;

/**
 * Created by jaison on 24/10/17.
 */

public class quotation_pos implements TpTable {

    private static String TAG = "quotation_pos";

    @Override
    public String topicId() {
        return "quotation_pos";
    }

    @Override
    public String topicName() {
        return "hasuradb.public.quotation_pos";
    }

    @Override
    public String getTableName() {
        return "quotation_pos";
    }



    @Override
    public Task getTask(SubscriptionResponse response) {
        try {
            SubscriptionData subscriptionData = response.getData();
            com.jaison.bompod.model.realm.quotation_pos record;
            boolean isDelete = false;
            if (subscriptionData.getPayload().getOperation().equalsIgnoreCase("d")) {
                isDelete = true;
                record = new Gson().fromJson(subscriptionData.getPayload().getBeforeData(), com.jaison.bompod.model.realm.quotation_pos.class);
            } else
                record = new Gson().fromJson(subscriptionData.getPayload().getData(), com.jaison.bompod.model.realm.quotation_pos.class);
            Log.i(TAG, "ProductRecord: " + record.getquotation());
            quotation_posrealm   quotationrealm=new quotation_posrealm();

            return new quotation_posRecordSyncTask(record, RealmConfig.URL.getForTableName(getTableName()), isDelete,quotationrealm);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return new EmptyTask();
        }
    }
}
