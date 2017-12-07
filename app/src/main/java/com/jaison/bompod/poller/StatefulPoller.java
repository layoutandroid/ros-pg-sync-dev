package com.jaison.bompod.poller;

import com.jaison.bompod.apiInterface.SubscriptionInterface;
import com.jaison.bompod.model.TpTable;
import com.jaison.bompod.model.hasura.SubscriptionRequest;
import com.jaison.bompod.model.hasura.SubscriptionResponse;
import com.jaison.bompod.model.realm.SubscriptionMetaData;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.hasura.custom_service_retrofit.RetrofitCallbackHandler;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.exception.HasuraException;
import io.realm.Realm;

/**
 * Created by jaison on 19/10/17.
 */

public class StatefulPoller {

    private List<TpTable> tpTables;
    private Map<String, Boolean> topicPollerMap;
    private Map<String, SubscriptionRequest> subscriptionRequestMap;

    public StatefulPoller(List<TpTable> tpTables) {
        this.tpTables = tpTables;
        this.topicPollerMap = new HashMap<>();
        this.subscriptionRequestMap = new HashMap<>();
        for (TpTable table : tpTables) {
            this.topicPollerMap.put(table.topicId(), false);
            int offset = 0;
            SubscriptionMetaData subscriptionMetaData = Realm.getDefaultInstance().where(SubscriptionMetaData.class).equalTo("topicId", table.topicId()).findFirst();
            if (subscriptionMetaData != null)
                offset = subscriptionMetaData.getOffset();
            SubscriptionRequest request = new SubscriptionRequest(table.topicId(), table.topicName(), offset, 100);
            this.subscriptionRequestMap.put(table.topicId(), request);
        }
    }
    public void startPolling() {
        for (TpTable table : tpTables) {
            poll(this.subscriptionRequestMap.get(table.topicId()), table);
        }
    }
    private void poll(final SubscriptionRequest request, final TpTable tpTable) {
        Hasura.getClient().useCustomService(SubscriptionInterface.class)
                .getTopicData(request)
                .enqueue(new RetrofitCallbackHandler<List<SubscriptionResponse>>() {
                    @Override
                    public void onSuccess(List<SubscriptionResponse> subscriptionResponses) {
                        if (subscriptionResponses.size() == 0) {
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    poll(request, tpTable);
                                }
                            }, 10000);
                        } else {
                            if (listener != null)
                                listener.onDataArrived(subscriptionResponses, tpTable);
                            request.setOffset(subscriptionResponses.get(subscriptionResponses.size() - 1).getOffset() + 1);
                            saveRequestMetaDataToRealm(request);
                            poll(request, tpTable);
                        }
                    }

                    @Override
                    public void onFailure(HasuraException e) {

                    }
                });
    }


    public interface DataListener {
        void onDataArrived(List<SubscriptionResponse> response, TpTable tpTable);
    }

    private DataListener listener;

    public void setListener(DataListener listener) {
        this.listener = listener;
    }

    private void saveRequestMetaDataToRealm(final SubscriptionRequest request) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SubscriptionMetaData data = new SubscriptionMetaData(request.getTopicId(), request.getTopicName(), request.getOffset());
                realm.insertOrUpdate(data);
            }
        });
    }
}
