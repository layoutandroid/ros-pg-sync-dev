package com.jaison.bompod.task;

import com.jaison.bompod.manager.RealmManager;
import com.jaison.bompod.model.hasura.ProductRecord;
import com.jaison.bompod.model.hasura.SubscriptionResponse;
import com.jaison.bompod.model.realm.Product;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.SyncConfiguration;

/**
 * Created by jaison on 20/10/17.
 */

public class ProductRecordSyncTask extends Task {

    Product product;
    SyncConfiguration configuration;
    boolean isDelete;

    public ProductRecordSyncTask(Product product, String realmName, boolean isDelete) {
        this.product = product;
        this.configuration = RealmManager.getSyncConfiguration(realmName);
        this.isDelete = isDelete;
    }

    @Override
    public void onExecute(final ExecutionCallback callback) {
        Realm.getInstance(configuration).executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (isDelete) {
                    Product p = realm.where(Product.class).equalTo("id", product.id).findFirst();
                    p.deleteFromRealm();
                } else
                    realm.insertOrUpdate(product);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                callback.onComplete();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                callback.onError();
            }
        });
    }

    @Override
    public String getDescription() {
        return "Syncs " + product.toString() + "With realm";
    }
}
