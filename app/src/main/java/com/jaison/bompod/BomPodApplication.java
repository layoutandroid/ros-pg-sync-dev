package com.jaison.bompod;

import android.app.Application;

import com.jaison.bompod.apiInterface.SubscriptionInterface;
import com.jaison.bompod.manager.UserManager;

import io.hasura.custom_service_retrofit.RetrofitCustomService;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.ProjectConfig;
import io.hasura.sdk.exception.HasuraInitException;
import io.realm.Realm;
import io.realm.log.LogLevel;
import io.realm.log.RealmLog;

/**
 * Created by jaison on 12/10/17.
 */

public class BomPodApplication extends Application {

    public static final String SERVER_URL = "35.200.243.162/";
    public static final String AUTH_URL = "http://"+SERVER_URL+"auth";
    public static final String REALM_URL = "realm://"+SERVER_URL+"~/realmtasks";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmLog.setLevel(LogLevel.DEBUG);

        RetrofitCustomService<SubscriptionInterface> ss = new RetrofitCustomService.Builder()
                .serviceName("subscribe")
                .build(SubscriptionInterface.class);

        ProjectConfig config = new ProjectConfig.Builder()
                .setDefaultRole("admin")
                .setProjectName("bompod-dev")
                .build();

        try {
            Hasura.setProjectConfig(config)
                    .enableLogs()
                    .addCustomService(ss)
                    .initialise(this);
        } catch (HasuraInitException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }


        /*
        TrTables implements TrInterface or extends RealmTableConfig
        TrTableManager manager = new TrTableManager.Builder()
                        .addTable(TableTOne.class)
                        .addTable(TableTTwo.class)
                        .build();

        TpTables implements TpInterface or extends TpTable
        TpTableManager manager = new TpTableManager.Builder()
                        .addTable(TablePOne.class)
                        .addTable(TablePTwo.class)
                        .build();


         All pollable tables implement Pollable or extends PollableTable

         StatefulPoller<TablePOneRecord> pollerTablePOne = new StatefulPoller.Builder()
                               .addTopicId("topic1")
                               .addTopicName("pollerTablePOne")
                               .build();
         StatefulPoller pollerTablePTwo = new StatefulPoller.Builder()
                               .addTopicId("topic2")
                               .addTopicName("pollerTablePTwo")
                               .build();
         StatefulPollerManager manager = new StatefulPollerManager.Builder()
                               .addPoller(pollerTablePOne)
                               .addPoller(pollerTablePTwo)
                               .build();

         manager.startPollers();

         //Poll Response
         TablePOne tablePOne = tablePOneRecord.getAsRealmTable();
         ProductRecordSyncTask realmSyncTask = new ProductRecordSyncTask<>(tablePOne);
         TaskManager.getInstance().addTask(realmSyncTask);


         //Realm Change
         RealmResults<T> results
        */

    }
}
