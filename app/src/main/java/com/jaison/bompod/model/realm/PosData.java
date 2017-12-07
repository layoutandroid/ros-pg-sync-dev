package com.jaison.bompod.model.realm;

import android.util.Log;

import com.jaison.bompod.constant.RealmConfig;
import com.jaison.bompod.manager.RealmManager;
import com.jaison.bompod.task.TaskConvertible;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.SyncConfiguration;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jaison on 12/10/17.
 */

public class PosData extends RealmObject implements TaskConvertible<JSONObject> {

    private static final String TAG = "PosData";

    @PrimaryKey
    public String id;

    public int posId;
    public int productId;
    public int quantitySold;
    public String timestamp;
    public boolean isSynced = false;

    public PosData() {
    }

    @Override
    public JSONObject toTaskObject() {
        try {
            JSONObject object = new JSONObject();
            object.put("id", id);
            object.put("pos_id", posId);
            object.put("product_id", productId);
            object.put("quantity_sold", quantitySold);
            return object;
        } catch (JSONException e) {
            throw new RuntimeException("Cannot create json object");
        }
    }

    @Override
    public String getRealmName() {
        return RealmConfig.URL.POS + posId;
    }

    @Override
    public boolean shouldExecuteTask() {
        return !isSynced;
    }

    @Override
    public void onPostExecute() {
        final String constId = id;
        SyncConfiguration syncConfiguration = RealmManager.getSyncConfiguration(getRealmName());
        if (syncConfiguration == null) {
            Log.i(TAG, "SyncConfig for " + getRealmName() + " is null. SKIPPING SYNC WITH REALM");
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
                Log.i(TAG, "Current value: " + PosData.this.toString());
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.i(TAG, "Failed to sync value in realm : " + error.getLocalizedMessage());
                Log.i(TAG, "Current value: " + PosData.this.toString());
            }
        });
    }

    @Override
    public String toString() {
        return "PosData{" +
                "id='" + id + '\'' +
                ", posId=" + posId +
                ", productId=" + productId +
                ", quantitySold=" + quantitySold +
                ", timestamp='" + timestamp + '\'' +
                ", isSynced=" + isSynced +
                '}';
    }
}
