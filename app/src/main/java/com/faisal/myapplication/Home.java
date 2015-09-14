package com.faisal.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;

public class Home extends AppCompatActivity {

    private ListView lvCars;
    private CarListAdapter adapter;
    private TextView tvHeader;
    private EditText etSearch;
    private TextView tvTotalCars;
    private TextView tvTotalApi;
    private Button btnSort;
    private Button btnPrice;
    private Button btnRating;

    private ArrayList<CarModel> listData;
    private ArrayList<CarModel> listDataTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lvCars = (ListView) findViewById(R.id.lv_cars);
        tvHeader = (TextView) findViewById(R.id.tv_header_id);
        etSearch = (EditText) findViewById(R.id.et_search);
        tvTotalCars = (TextView) findViewById(R.id.tv_total_cars);
        tvTotalApi = (TextView) findViewById(R.id.tv_total_api);
        btnSort = (Button) findViewById(R.id.btn_sort);
        btnPrice = (Button) findViewById(R.id.btn_price);
        btnRating = (Button) findViewById(R.id.btn_rating);

        listData = new ArrayList<CarModel>();
        listDataTemp = new ArrayList<CarModel>();
        adapter = new CarListAdapter(listDataTemp, Home.this);
        lvCars.setAdapter(adapter);

        new fetchData().execute();
        new fetchData1().execute();

        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(listDataTemp, new PriceSort());
                adapter.notifyDataSetChanged();
            }
        });

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(listDataTemp, new RatingSort());
                adapter.notifyDataSetChanged();
            }
        });

        lvCars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Home.this, DetailActivity.class);
                intent.putExtra("model", listDataTemp.get(position));
                startActivity(intent);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                listDataTemp.clear();

                if(s.length()==0){
                    listDataTemp.addAll(listData);
                    adapter.notifyDataSetChanged();
                }else{
                    for (CarModel model: listData) {
                        if(model.getName().toString().toLowerCase().contains(s.toString().toLowerCase())){
                            listDataTemp.add(model);
                        }else if((model.getRating()+"").contains(s.toString())){
                            listDataTemp.add(model);
                        }else if((model.getHourly_rate()+"").contains(s.toString())){
                            listDataTemp.add(model);
                        }
                    }
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    class fetchData extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Home.this);
            progressDialog.setTitle("Please Wait..");
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            try{
                String murl="https://zoomcar.0x10.info/api/zoomcar?type=json&query=list_cars";
                URL url = new URL(murl);
                HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
                InputStream ins = con.getInputStream();
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader in = new BufferedReader(isr);
                return in.readLine();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String file_url) {

            if(file_url==null){
                return ;
            }

            try {
                JSONObject mainObject = new JSONObject(file_url);
                JSONArray carArray = mainObject.getJSONArray("cars");
                for (int i = 0; i <carArray.length() ; i++) {
                    CarModel model=new CarModel();
                    JSONObject carObject=carArray.getJSONObject(i);
                    model.setName(carObject.getString("name"));
                    if(carObject.getInt("ac")==1){
                        model.setAc(true);
                    }else{
                        model.setAc(false);
                    }
                    model.setHourly_rate(carObject.getInt("hourly_rate"));
                    model.setImage(carObject.getString("image"));
                    model.setRating(carObject.getInt("rating"));
                    model.setSeater(carObject.getString("seater"));
                    model.setType(carObject.getString("type"));
                    JSONObject locationObject=carObject.getJSONObject("location");
                    model.setLatitude(locationObject.getString("latitude"));
                    model.setLongitude(locationObject.getString("longitude"));
                    listData.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            tvTotalCars.setText(listData.size()+"");
            listDataTemp.addAll(listData);
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }


    class fetchData1 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... f_url) {
            String murl="http://zoomcar.0x10.info/api/zoomcar?type=json&query=api_hits";
            String SetServerString = "";
            HttpClient Client = new DefaultHttpClient();
            try {
                HttpGet httpget = new HttpGet(murl);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                SetServerString = Client.execute(httpget, responseHandler);
                Log.e("response", SetServerString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return SetServerString;
        }


        @Override
        protected void onPostExecute(String file_url) {

            if(file_url==null){
                return ;
            }

            try {
                JSONObject mainObject = new JSONObject(file_url);
                tvTotalApi.setText(mainObject.getString("api_hits"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}