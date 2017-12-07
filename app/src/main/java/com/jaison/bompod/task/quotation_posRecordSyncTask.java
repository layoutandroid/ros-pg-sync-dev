package com.jaison.bompod.task;

import com.jaison.bompod.manager.RealmManager;
import com.jaison.bompod.model.realm.Product;
import com.jaison.bompod.model.realm.quotation_pos;
import com.jaison.bompod.model.realm.quotation_posrealm;

import io.realm.Realm;
import io.realm.SyncConfiguration;

/**
 * Created by jaison on 20/10/17.
 */

public class quotation_posRecordSyncTask extends Task {

    quotation_pos quo_pos;
    SyncConfiguration configuration;
    boolean isDelete;
    quotation_posrealm  quotationrealm;

    public quotation_posRecordSyncTask(quotation_pos quo_pos, String realmName, boolean isDelete, quotation_posrealm quotationrealm) {
        this.quo_pos = quo_pos;
        this.configuration = RealmManager.getSyncConfiguration(realmName);
        this.isDelete = isDelete;
        this.quotationrealm=quotationrealm;
    }
    @Override
    public void onExecute(final ExecutionCallback callback) {
        Realm.getInstance(configuration).executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (isDelete) {
                    quotation_pos p = realm.where(quotation_pos.class).equalTo("id", quo_pos.getid()).findFirst();
                    p.deleteFromRealm();
                } else
                    quotationrealm.setid(quo_pos.getid());
                quotationrealm.setpos_id(quo_pos.getpos_id());
                quotationrealm.setproduct_id(quo_pos.getproduct_id());
                quotationrealm.setgm_id(quo_pos.getgm_id());
                quotationrealm.setcomments(quo_pos.getcomments());
                quotationrealm.setcreated(quo_pos.getcreated());
                quotationrealm.setquotation(quo_pos.getquotation());
                    realm.insertOrUpdate(quotationrealm);
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
        return "Syncs " + quo_pos.toString() + "With realm";
    }
}
