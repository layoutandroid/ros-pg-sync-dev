package com.jaison.bompod.model.hasura;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.jaison.bompod.util.StringUtil;

import java.lang.reflect.Type;

/**
 * Created by jaison on 19/10/17.
 */

public class SubscriptionResponse<T> {

    @SerializedName("offset")
    int offset;

    @SerializedName("data")
    String data;

    @SerializedName("group_id")
    String topicId;

    @SerializedName("topic")
    String topicName;

    public int getOffset() {
        return offset;
    }

    public SubscriptionData getData() {
        String unescapedJson = StringUtil.unescapeJavaString(data);
        return new Gson().fromJson(unescapedJson, SubscriptionData.class);
    }

    public T getAfterData() {
        String unescapedJson = StringUtil.unescapeJavaString(data);
        return new Gson().fromJson(unescapedJson, new TypeToken<T>() {}.getType());
    }

    public String getTopicId() {
        return topicId;
    }

    public String getTopicName() {
        return topicName;
    }
}
