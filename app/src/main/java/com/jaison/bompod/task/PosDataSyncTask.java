package com.jaison.bompod.task;

import android.util.Log;

import com.jaison.bompod.constant.RealmConfig;
import com.jaison.bompod.manager.RealmManager;
import com.jaison.bompod.model.realm.PosData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.exception.HasuraException;
import io.realm.DynamicRealmObject;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;

/**
 * Created by jaison on 20/10/17.
 */

public class PosDataSyncTask extends Task {

    private static final String TAG = "PosDataSyncTask";

    JSONObject insertQuery;
    RealmResults<DynamicRealmObject> results;
    boolean isSynced = false;

    public PosDataSyncTask(RealmResults<DynamicRealmObject> results) {
        this.results = results;
        try {
            JSONArray jsonArray = new JSONArray();
            for (DynamicRealmObject dynamicObject : results) {
                String id = dynamicObject.get("id");
                long posId = dynamicObject.get("posId");
                long productId = dynamicObject.get("productId");
                long quantitySold = dynamicObject.get("quantitySold");
                isSynced = dynamicObject.getBoolean("isSynced");
                if (!isSynced) {
                    JSONObject object = new JSONObject();
                    object.put("id", id);
                    object.put("pos_id", posId);
                    object.put("product_id", productId);
                    object.put("quantity_sold", quantitySold);
                    jsonArray.put(object);
                }
            }
            insertQuery = new JSONObject()
                    .put("type", "insert")
                    .put("args", new JSONObject()
                            .put("table", "pos_data")
                            .put("objects", jsonArray)
                    );
        } catch (JSONException e) {
            throw new RuntimeException("JsonException: " + e.toString());
        }
    }

    @Override
    public void onExecute(final ExecutionCallback callback) {
        if (!isSynced)
            Hasura.getClient()
                    .useDataService()
                    .setRequestBody(insertQuery)
                    .expectResponseType(JSONObject.class)
                    .enqueue(new Callback<JSONObject, HasuraException>() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            callback.onComplete();
                        }

                        @Override
                        public void onFailure(HasuraException e) {
                            callback.onError();
                        }
                    });
        else
            callback.onComplete();
    }

    @Override
    public void onPostExecute(final ExecutionCallback callback) {
        for (final DynamicRealmObject dynamicObject : results) {
            final String constId = dynamicObject.getString("id");
            long posId = dynamicObject.get("posId");
            SyncConfiguration syncConfiguration = RealmManager.getSyncConfiguration(RealmConfig.URL.POS + posId);
            if (syncConfiguration == null) {
                Log.i(TAG, "SyncConfig for " + RealmConfig.URL.POS + posId + " is null. SKIPPING SYNC WITH REALM");
                return;
            }
            Realm.getInstance(syncConfiguration).executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    PosData posData = realm.where(PosData.class).equalTo("id", constId).equalTo("isSynced", false).findFirst();
                    if (posData != null)
                        posData.isSynced = true;
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "Successfully synced value in realm");
                    Log.i(TAG, "Current value: " + dynamicObject.toString());
                    callback.onComplete();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.i(TAG, "Failed to sync value in realm : " + error.getLocalizedMessage());
                    Log.i(TAG, "Current value: " + dynamicObject.toString());
                    callback.onError();
                }
            });
        }
    }

    @Override
    public String getDescription() {
        return "Inserts query -> " + insertQuery.toString() + " into Hasura";
    }
}
