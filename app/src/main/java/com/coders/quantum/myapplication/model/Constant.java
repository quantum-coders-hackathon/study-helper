package com.coders.quantum.myapplication.model;

public class Constant {
    private static final String API_KEY = "Enter your API";
    private static final int DB_VERSION=1;
    private static final String DB_NAME="study_helper.db";

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static int getDbVersion() {
        return DB_VERSION;
    }

}
