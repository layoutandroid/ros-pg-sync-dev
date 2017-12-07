package com.jaison.bompod.model.hasura;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by jaison on 21/10/17.
 */

public class SubscriptionPayload {

    @SerializedName("after")
    JsonElement data;

    @SerializedName("before")
    JsonElement beforeData;

    @SerializedName("op")
    String operation;

    public JsonElement getBeforeData() {
        return beforeData;
    }

    public JsonElement getData() {
        return data;
    }

    public String getOperation() {
        return operation;
    }


}
