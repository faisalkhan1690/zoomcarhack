package com.faisal.myapplication.utill;

import android.os.AsyncTask;

import com.faisal.myapplication.callback.IAsynTaskCallBack;
import com.faisal.myapplication.model.JSONRoot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by faisal pc on 8/18/2015.
 */
public class MyAppAsyncTask extends AsyncTask<String, String, JSONRoot> {

    private IAsynTaskCallBack callback;
    private String url;
    private HashMap<String, String> parameters;
    private JSONRoot baseModel;

    public MyAppAsyncTask(JSONRoot baseModel, String url, HashMap<String, String> parameters, IAsynTaskCallBack callback) {
        this.callback = callback;
        this.url = url;
        this.parameters = parameters;
        this.baseModel = baseModel;
    }

    @Override
    protected JSONRoot doInBackground(String... params) {

//        URL url;
//        String response = "";
//        try {
//            url = new URL(this.url);
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(15000);
//            conn.setConnectTimeout(15000);
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//
//
//            OutputStream os = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(
//                    new OutputStreamWriter(os, "UTF-8"));
//            writer.write(getPostDataString(parameters));
//
//            writer.flush();
//            writer.close();
//            os.close();
//            int responseCode=conn.getResponseCode();
//
//            if (responseCode == HttpsURLConnection.HTTP_OK) {
//                String line;
//                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                while ((line=br.readLine()) != null) {
//                    response+=line;
//                }
//            }
//            return new GsonParser().getModel(response, baseModel);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;

        try {
            URL mUrl = new URL(this.url);
            HttpsURLConnection con = (HttpsURLConnection) mUrl.openConnection();
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);
            return new GsonParser().getModel(in.readLine(), baseModel);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new GsonParser().getModel("", baseModel);
        } catch (IOException e) {
            e.printStackTrace();
            return new GsonParser().getModel("", baseModel);
        }
    }

    @Override
    protected void onPostExecute(JSONRoot result) {
        callback.responceFromAsynTask(result);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(String... text) {
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
