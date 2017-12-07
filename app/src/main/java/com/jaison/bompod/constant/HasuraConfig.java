package com.jaison.bompod.constant;

/**
 * Created by jaison on 18/10/17.
 */

public class HasuraConfig {
    public static String PROJECT_NAME = "h34-adieux98-stg";
    private static String PREFIX = "http://";
    private static String DOMAIN = ".hasura-app.io";

    public static class URL {
        public static String AUTH = PREFIX + "auth." + PROJECT_NAME + DOMAIN + "/v2";
        public static String SUBSCRIBE = PREFIX + "subscribe." + PROJECT_NAME + DOMAIN;
        public static String DATA = PREFIX + "data." + PROJECT_NAME + DOMAIN;
    }

}
