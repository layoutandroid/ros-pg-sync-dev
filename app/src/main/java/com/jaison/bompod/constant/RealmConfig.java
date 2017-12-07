package com.jaison.bompod.constant;

import com.jaison.bompod.BuildConfig;

/**
 * Created by jaison on 18/10/17.
 */

public class RealmConfig {

    private static final Boolean isLocal = false;


    public static final String SERVER_IP = "35.200.243.162";
    public static final String REALM_URL = "realm://"+SERVER_IP+"/~/realmtasks";
    private static final String SCHEME ="http";
    private static final String REALMSCHEME ="realm" ;

    public static class URL {
        public static final String AUTH = SCHEME + "://" + SERVER_IP + "/auth";
        public static final String PRODUCT = REALMSCHEME + "://" + SERVER_IP + "/product";
        public static final String POS = REALMSCHEME + "://" + SERVER_IP + "/posdata";
        public static final String Quotation_pos = REALMSCHEME + "://" + SERVER_IP + "/quotation_pos";
        public static String getForTableName(String tableName) {
            return REALM_URL /*REALMSCHEME + "://" + SERVER_IP + "/" + tableName*/;
        }
    }
}
