package com.jaison.bompod.model.hasura;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 19/10/17.
 */

public class PosRecord {

    @SerializedName("id")
    int id;

    public PosRecord(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
