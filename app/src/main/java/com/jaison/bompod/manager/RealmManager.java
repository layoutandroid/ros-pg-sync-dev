package com.jaison.bompod.manager;

import android.util.Log;

import com.jaison.bompod.constant.RealmConfig;
import com.jaison.bompod.model.RealmTableConfig;
import com.jaison.bompod.model.TpTable;
import com.jaison.bompod.model.TrTable;
import com.jaison.bompod.model.TrTableConfig;
import com.jaison.bompod.model.hasura.PosRecord;
import com.jaison.bompod.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.ObjectServerError;
import io.realm.PermissionManager;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncManager;
import io.realm.SyncSession;
import io.realm.SyncUser;
import io.realm.permissions.AccessLevel;
import io.realm.permissions.PermissionRequest;
import io.realm.permissions.UserCondition;

/**
 * Created by jaison on 12/10/17.
 */

public class RealmManager {

    private static final String TAG = "RealmManager";
    private static HashMap<String, RealmTableConfig> realmTpConfigMap;
    private static HashMap<String, TrTableConfig> realmTrConfigMap;


    public static boolean initialiseAllRealmConfigs(List<TpTable> tpTables, List<TrTable> trTables, List<PosRecord> posRecordList) {
        SyncUser user = SyncUser.currentUser();
        if (user == null || !user.isValid()) {
            return false;
        }

        realmTpConfigMap = new HashMap<>();
        for (final TpTable tpTable : tpTables) {
            String realmPath = RealmConfig.URL.getForTableName(tpTable.getTableName());
            Log.i(TAG, "REALM PATH: " + realmPath);
            final SyncConfiguration tpTableConfig = new SyncConfiguration.Builder(user, realmPath)
                    .disableSSLVerification()
                    .build();
            RealmTableConfig config = new RealmTableConfig() {
                @Override
                public String getTableName() {
                    return tpTable.getTableName();
                }

                @Override
                public SyncConfiguration getSyncConfiguration() {
                    return tpTableConfig;
                }
            };
            realmTpConfigMap.put(realmPath, config);
        }

        realmTrConfigMap = new HashMap<>();
        for (PosRecord record : posRecordList) {
            for (final TrTable trTable : trTables) {
                String realmPath = RealmConfig.URL.getForTableName(trTable.getTableName()) + record.getId();
                final SyncConfiguration trTableConfig = new SyncConfiguration.Builder(user, realmPath)
                        .disableSSLVerification()
                        .build();
                TrTableConfig config = new TrTableConfig() {
                    @Override
                    public String getTableName() {
                        return trTable.getTableName();
                    }

                    @Override
                    public SyncConfiguration getSyncConfiguration() {
                        return trTableConfig;
                    }

                    @Override
                    public Task getTask(RealmResults<DynamicRealmObject> results) {
                        return trTable.getTask(results);
                    }
                };
                realmTrConfigMap.put(realmPath, config);
            }
        }
        SyncManager.setDefaultSessionErrorHandler(new SyncSession.ErrorHandler() {
            @Override
            public void onError(SyncSession session, ObjectServerError error) {
                Log.e(TAG, "Error: " + session.getState());
                Log.e(TAG, "Error: " + error.getErrorCode() + "\n" + error.toString());
            }
        });
        return true;
    }
    public static SyncConfiguration getSyncConfiguration(String key) {
        if (realmTpConfigMap.containsKey(key))
            return realmTpConfigMap.get(key).getSyncConfiguration();
        if (realmTrConfigMap.containsKey(key)) {
            return realmTrConfigMap.get(key).getSyncConfiguration();
        }
        return null;
    }

    static int realmInitCount = 0;

    public interface StatusCallback {
        void onSuccess();
    }

    public static void initialiseAllRealms(final StatusCallback statusCallback) {
        final int totalRealms = realmTrConfigMap.size() + realmTpConfigMap.size();

        for (final Map.Entry<String, RealmTableConfig> realmConfig : realmTpConfigMap.entrySet()) {
            Realm.getInstanceAsync(realmConfig.getValue().getSyncConfiguration(), new Realm.Callback() {
                @Override
                public void onSuccess(Realm realm) {
                    realmInitCount++;
                    if (totalRealms == realmInitCount) {
                        if (statusCallback != null)
                            statusCallback.onSuccess();
                    }
                }
            });
        }

        for (Map.Entry<String, TrTableConfig> realmConfig : realmTrConfigMap.entrySet()) {
            Realm.getInstanceAsync(realmConfig.getValue().getSyncConfiguration(), new Realm.Callback() {
                @Override
                public void onSuccess(Realm realm) {
                    realmInitCount++;
                    if (totalRealms == realmInitCount) {
                        if (statusCallback != null)
                            statusCallback.onSuccess();
                    }
                }
            });
        }
    }

    static int permissionGrantCount = 0;

    public static void grantRealmPermissions(final StatusCallback statusCallback) {
        final int totalRealms = realmTrConfigMap.size() + realmTpConfigMap.size();
        SyncUser user = SyncUser.currentUser();






        //Grant Read access to product realm
        for (final Map.Entry<String, RealmTableConfig> realmConfig : realmTpConfigMap.entrySet()) {
            UserCondition productCondition = UserCondition.noExistingPermissions();/*userId(user.getIdentity());*/
            AccessLevel productAccessLevel = AccessLevel.READ;
            PermissionRequest productRequest = new PermissionRequest(productCondition, realmConfig.getKey(), productAccessLevel);
            Log.i(TAG, "product request for " + productRequest);
            user.getPermissionManager().applyPermissions(productRequest, new PermissionManager.ApplyPermissionsCallback() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "Applied permissions for " + realmConfig.getKey());
                    permissionGrantCount++;
                    if (totalRealms == permissionGrantCount) {
                        if (statusCallback != null)
                            statusCallback.onSuccess();
                    }
                }

                @Override
                public void onError(ObjectServerError error) {
                    Log.i(TAG, "Error applying permissions for product");
                    Log.i(TAG, error.toString());
                }
            });
        }

        for (final Map.Entry<String, TrTableConfig> realmConfig : realmTrConfigMap.entrySet()) {
            // Permission for Pos
            UserCondition posCondition = UserCondition.noExistingPermissions();
            AccessLevel posAccessLevel = AccessLevel.WRITE;
            PermissionRequest posRequest = new PermissionRequest(posCondition, realmConfig.getKey(), posAccessLevel);

            user.getPermissionManager().applyPermissions(posRequest, new PermissionManager.ApplyPermissionsCallback() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "Applied permissions for " + realmConfig.getKey());
                    permissionGrantCount++;
                    if (totalRealms == permissionGrantCount) {
                        if (statusCallback != null)
                            statusCallback.onSuccess();
                    }
                }

                @Override
                public void onError(ObjectServerError error) {
                    Log.i(TAG, "Error applying permissions for pos");
                    Log.i(TAG, error.toString());
                }
            });
        }
    }

    static Map<TrTableConfig, RealmResults<DynamicRealmObject>> trTableConfigResultsMap;
    static List<DynamicRealm> realmList;

    public interface DataChangeListener {
        void onChange(RealmResults<DynamicRealmObject> data, TrTableConfig config);
    }

    public static Map<TrTableConfig, RealmResults<DynamicRealmObject>> registerToTrTableChanges(final DataChangeListener changeListener) {
        trTableConfigResultsMap = new HashMap<>();
        realmList = new ArrayList<>();
        for (final Map.Entry<String, TrTableConfig> realmConfig : realmTrConfigMap.entrySet()) {
            final TrTableConfig config = realmConfig.getValue();
            DynamicRealm dynamicRealm = DynamicRealm.getInstance(config.getSyncConfiguration());
            final RealmResults<DynamicRealmObject> results = dynamicRealm.where(config.getTableName())/*.equalTo("isSynced", false)*/.findAll();
            results.addChangeListener(new RealmChangeListener<RealmResults<DynamicRealmObject>>() {
                @Override
                public void onChange(RealmResults<DynamicRealmObject> dynamicRealmObjects) {
                    changeListener.onChange(results, config);
                }
            });
            realmList.add(dynamicRealm);
            trTableConfigResultsMap.put(config, results);
        }
        return trTableConfigResultsMap;
    }

    public static void unRegisterAllListeners() {
        for (Map.Entry<TrTableConfig, RealmResults<DynamicRealmObject>> entry: trTableConfigResultsMap.entrySet()) {
            RealmResults<DynamicRealmObject> results = entry.getValue();
            results.removeAllChangeListeners();
        }
        for (DynamicRealm realm : realmList) {
            realm.close();
        }
    }
}
