package com.jaison.bompod.apiInterface;

import com.jaison.bompod.model.hasura.SubscriptionRequest;
import com.jaison.bompod.model.hasura.SubscriptionResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jaison on 19/10/17.
 */

public interface SubscriptionInterface {

    @POST("v1/http")
    Call<List<SubscriptionResponse>> getTopicData(@Body SubscriptionRequest request);
}
