package com.faisal.myapplication.utill;

import com.faisal.myapplication.model.JSONRoot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Hisham on 8/18/2015.
 */
public class GsonParser {

    private Gson gson;

    public GsonParser() {
        gson = new GsonBuilder().create();
    }

    /**
     * Parsing Json here in the appropriate model
     * @param source
     * @param baseModelTypeClass
     * @return
     */
    public JSONRoot getModel(InputStream source, JSONRoot baseModelTypeClass){
        Reader reader = new InputStreamReader(source);
        return gson.fromJson(reader, baseModelTypeClass.getClass());
    }

    /**
     * Parsing Json here in the appropriate model
     * @param json
     * @param baseModelTypeClass
     * @return
     */
    public JSONRoot getModel(String json, JSONRoot baseModelTypeClass){
        return gson.fromJson(json, baseModelTypeClass.getClass());
    }
}
