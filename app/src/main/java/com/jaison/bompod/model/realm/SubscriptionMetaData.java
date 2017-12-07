package com.jaison.bompod.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jaison on 19/10/17.
 */

public class SubscriptionMetaData extends RealmObject {

    @PrimaryKey
    String topicId;

    String topicName;

    int offset;

    public SubscriptionMetaData() {
    }

    public SubscriptionMetaData(String topicId, String topicName, int offset) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }
}
