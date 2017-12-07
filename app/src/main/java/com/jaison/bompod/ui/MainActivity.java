package com.jaison.bompod.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.jaison.bompod.R;
import com.jaison.bompod.manager.RealmManager;
import com.jaison.bompod.model.TpTable;
import com.jaison.bompod.model.TrTable;
import com.jaison.bompod.model.TrTableConfig;
import com.jaison.bompod.model.hasura.PosRecord;
import com.jaison.bompod.model.hasura.ProductRecord;
import com.jaison.bompod.model.hasura.SubscriptionData;
import com.jaison.bompod.model.hasura.SubscriptionResponse;
import com.jaison.bompod.model.table.Quotation_posrealm;
import com.jaison.bompod.model.table.TpTable1;
import com.jaison.bompod.model.table.TrTable1;
import com.jaison.bompod.model.table.crate;
import com.jaison.bompod.model.table.quotation_pos;
import com.jaison.bompod.poller.StatefulPoller;
import com.jaison.bompod.task.ProductRecordSyncTask;
import com.jaison.bompod.task.TaskManager;
import com.jaison.bompod.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.exception.HasuraException;
import io.realm.DynamicRealmObject;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public static void launchActivity(Activity startingActivity) {
        startingActivity.startActivity(new Intent(startingActivity, MainActivity.class));
    }

    private static String TAG = "MainActivity";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productListAdapter = new ProductListAdapter();
        recyclerView.setAdapter(productListAdapter);

        List<PosRecord> posRecords = new ArrayList<>();
        posRecords.add(new PosRecord(1));
        initialise(posRecords);
    }

    private void initialise(List<PosRecord> records) {
        List<TrTable> trTables = new ArrayList<>();
        trTables.add(new TrTable1());
        trTables.add(new Quotation_posrealm());
        final List<TpTable> tpTables = new ArrayList<>();
        tpTables.add(new TpTable1());
        tpTables.add(new crate());
        tpTables.add(new quotation_pos());
        RealmManager.initialiseAllRealmConfigs(tpTables, trTables, records);
        RealmManager.initialiseAllRealms(new RealmManager.StatusCallback() {
            @Override
            public void onSuccess() {
                RealmManager.grantRealmPermissions(new RealmManager.StatusCallback() {
                    @Override
                   public void onSuccess() {
                        startPoller(tpTables);
                        Map<TrTableConfig, RealmResults<DynamicRealmObject>> trConfigResultsMap = RealmManager.registerToTrTableChanges(new RealmManager.DataChangeListener() {
                            @Override
                            public void onChange(RealmResults<DynamicRealmObject> data, TrTableConfig config) {
                               Log.i(TAG, "On Change Called for PosData: " + data.toString());
                               TaskManager.getInstance().addTask(config.getTask(data));
                            }
                        });

                       for (Map.Entry<TrTableConfig, RealmResults<DynamicRealmObject>> entry : trConfigResultsMap.entrySet()) {
                            Log.i(TAG, "Syncing: " + entry.getValue().toString());
                            TaskManager.getInstance().addTask(entry.getKey().getTask(entry.getValue()));
                        }
                    }
                });
            }
        });
    }

    private void startPoller(List<TpTable> tpTables) {
        StatefulPoller poller = new StatefulPoller(tpTables);
        poller.setListener(new StatefulPoller.DataListener() {
            @Override
            public void onDataArrived(List<SubscriptionResponse> responseList, TpTable tpTable) {
                for (SubscriptionResponse response: responseList) {
                    TaskManager.getInstance().addTask(tpTable.getTask(response));
                }
            }
        });
        poller.startPolling();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RealmManager.unRegisterAllListeners();
    }
}
