package com.jaison.bompod.model.hasura;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 21/10/17.
 */

public class SubscriptionData {

    @SerializedName("payload")
    SubscriptionPayload payload;

    public SubscriptionPayload getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "SubscriptionData{" +
                "payload=" + payload +
                '}';
    }
}
