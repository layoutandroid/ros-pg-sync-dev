package com.jaison.bompod.model.hasura;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 19/10/17.
 */

public class SubscriptionRequest {

    @SerializedName("group_id")
    String topicId;

    @SerializedName("topic")
    String topicName;

    @SerializedName("offset")
    int offset;

    @SerializedName("max_length")
    int limit;

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public SubscriptionRequest(String topicId, String topicName, int offset, int limit) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.offset = offset;
        this.limit = limit;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }
}
