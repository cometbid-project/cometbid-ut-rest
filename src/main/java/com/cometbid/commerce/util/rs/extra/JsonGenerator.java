/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cometbid.commerce.util.rs.extra;

import com.cometbid.commerce.utility.HibernateProxyTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Gbenga
 */
public class JsonGenerator<T> {

    private static JsonGenerator INSTANCE;

    private JsonGenerator() {
    }

    public static JsonGenerator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JsonGenerator();
        }
        return INSTANCE;
    }

    /**
     *
     * @param entityList
     * @param recordCount
     * @param arrayName
     * @return
     */
    public String getCollectionAsJson(Collection<T> entityList, int recordCount, String arrayName) {

        GsonBuilder b = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.create();

        JsonElement element = gson.toJsonTree(entityList, new TypeToken<Collection<T>>() {
        }.getType());

        if (!element.isJsonArray()) {
            // fail appropriately
            throw new RuntimeException();
        }

        JsonArray jsonArray = element.getAsJsonArray();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("totalRecords", recordCount);
        jsonObj.put(arrayName, jsonArray);

        Logger.getLogger("com.cometbid.commerce.util.rs.extra.JsonGenerator")
                .log(Level.INFO, "JSon String formatted: {0}", jsonObj.toString());

        return jsonObj.toString();
    }

    /**
     *
     * @param entityList
     * @param arrayName
     * @return
     */
    public String getCollectionAsJson(Collection<T> entityList, String arrayName) {

        GsonBuilder b = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.create();

        JsonElement element = gson.toJsonTree(entityList, new TypeToken<Collection<T>>() {
        }.getType());

        if (!element.isJsonArray()) {
            // fail appropriately
            throw new RuntimeException();
        }

        JsonArray jsonArray = element.getAsJsonArray();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put(arrayName, jsonArray);

        Logger.getLogger("com.cometbid.commerce.util.rs.extra.JsonGenerator")
                .log(Level.INFO, "JSon String formatted: {0}", jsonObj.toString());

        return jsonObj.toString();
    }

    /**
     *
     * @param type
     * @param objectName
     * @return
     */
    public String getTypeAsJson(T type, String objectName) {

        GsonBuilder b = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.create();

        JsonElement element = gson.toJsonTree(type);
        JsonObject jsonObject = element.getAsJsonObject();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put(objectName, jsonObject);

        Logger.getLogger("com.cometbid.commerce.util.rs.extra.JsonGenerator")
                .log(Level.INFO, "JSon String formatted: {0}", jsonObj.toString());

        return jsonObj.toString();
    }

    /**
     *
     * @param listOfLong
     * @param arrayName
     * @return
     */
    public static String getIntegerListAsJsonStr(Collection<Integer> listOfLong, String arrayName) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.create();

        JsonElement element = gson.toJsonTree(listOfLong, new TypeToken<Collection<Integer>>() {
        }.getType());

        if (!element.isJsonArray()) {
            // fail appropriately
            throw new RuntimeException();
        }

        JsonArray jsonArray = element.getAsJsonArray();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("totalRecords", listOfLong.size());
        jsonObj.put(arrayName, jsonArray);

        Logger.getLogger("com.cometbid.commerce.util.rs.extra.JsonGenerator")
                .log(Level.INFO, "JSon String formatted: {0}", jsonObj.toString());

        return jsonObj.toString();
    }

    /**
     *
     * @param listOfString
     * @param arrayName
     * @return
     */
    public static String getStringListAsJsonStr(Collection<String> listOfString, String arrayName) {

        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = b.create();

        JsonElement element = gson.toJsonTree(listOfString, new TypeToken<Collection<Long>>() {
        }.getType());

        if (!element.isJsonArray()) {
            // fail appropriately
            throw new RuntimeException();
        }

        JsonArray jsonArray = element.getAsJsonArray();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("totalRecords", listOfString.size());
        jsonObj.put(arrayName, jsonArray);

        Logger.getLogger("com.cometbid.commerce.util.rs.extra.JsonGenerator")
                .log(Level.INFO, "JSon String formatted: {0}", jsonObj.toString());

        return jsonObj.toString();
    }

}
