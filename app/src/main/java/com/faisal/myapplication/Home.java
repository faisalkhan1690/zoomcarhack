package com.faisal.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.faisal.myapplication.callback.IAsynTaskCallBack;
import com.faisal.myapplication.constant.AppConstant;
import com.faisal.myapplication.model.ApiHitModel;
import com.faisal.myapplication.model.CarModel;
import com.faisal.myapplication.model.JSONRoot;
import com.faisal.myapplication.utill.MyAppAsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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

    private ArrayList<CarModel.CarDetail> listData;
    private ArrayList<CarModel.CarDetail> listDataTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialization();

        fetchCarData();
        fetchApiHits();

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
                intent.putExtra(AppConstant.MODEL, listDataTemp.get(position));
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

                if (s.length() == 0) {
                    listDataTemp.addAll(listData);
                    adapter.notifyDataSetChanged();
                } else {
                    for (CarModel.CarDetail model : listData) {
                        if (model.getName().toString().toLowerCase().contains(s.toString().toLowerCase())) {
                            listDataTemp.add(model);
                        } else if ((model.getRating() + "").contains(s.toString())) {
                            listDataTemp.add(model);
                        } else if ((model.getHourly_rate() + "").contains(s.toString())) {
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

    private void initialization() {
        lvCars = (ListView) findViewById(R.id.lv_cars);
        tvHeader = (TextView) findViewById(R.id.tv_header_id);
        etSearch = (EditText) findViewById(R.id.et_search);
        tvTotalCars = (TextView) findViewById(R.id.tv_total_cars);
        tvTotalApi = (TextView) findViewById(R.id.tv_total_api);
        btnSort = (Button) findViewById(R.id.btn_sort);
        btnPrice = (Button) findViewById(R.id.btn_price);
        btnRating = (Button) findViewById(R.id.btn_rating);

        listData = new ArrayList<CarModel.CarDetail>();
        listDataTemp = new ArrayList<CarModel.CarDetail>();
        adapter = new CarListAdapter(listDataTemp, Home.this);
        lvCars.setAdapter(adapter);
    }

    private void fetchCarData() {
        final ProgressDialog progressDialog = new ProgressDialog(Home.this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new MyAppAsyncTask(new CarModel(), AppConstant.CAR_DETAILS, new HashMap<String, String>(), new IAsynTaskCallBack() {
            @Override
            public void responceFromAsynTask(final JSONRoot result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if(result==null){
                            return;
                        }
                        CarModel model=(CarModel)result;
                        tvTotalCars.setText(model.getCars().size()+"");
                        listDataTemp.addAll(model.getCars());
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }).execute();
    }

    private void fetchApiHits() {

        final ProgressDialog progressDialog = new ProgressDialog(Home.this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new MyAppAsyncTask(new ApiHitModel(), AppConstant.API_HIT_COUNT, new HashMap<String, String>(), new IAsynTaskCallBack() {
            @Override
            public void responceFromAsynTask(final JSONRoot result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if(result==null){
                            return;
                        }
                        ApiHitModel model = (ApiHitModel) result;
                        tvTotalApi.setText(model.getApi_hits());
                    }
                });
            }
        }).execute();

    }
}