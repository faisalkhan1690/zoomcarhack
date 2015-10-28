package com.faisal.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.faisal.myapplication.constant.AppConstant;
import com.faisal.myapplication.model.CarModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class DetailActivity extends AppCompatActivity  {

    private Button btnBookingHistory;
    private Button btnBook;
    private Button btnShare;
    private Button btnSms;
    private Button btnBack;
    private CarModel.CarDetail item;
    private GoogleMap googleMap;
    private ImageView imgCar;
    private TextView tvCarName;
    private TextView tvPerHour;
    private RatingBar ratingBar;
    private TextView tvSeater;
    private TextView tvAc;
    private View popupView;
    private PopupWindow popupWindow;
    private DatePicker datePicker;
    private ImageView imgClose;
    private Button btnSubmit;
    private String dob;
    private EditText tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        item=(CarModel.CarDetail) getIntent().getSerializableExtra(AppConstant.MODEL);
        btnBookingHistory=(Button)findViewById(R.id.btn_booking_history);
        btnBook=(Button)findViewById(R.id.btn_book);
        btnShare=(Button)findViewById(R.id.btn_share); 
        btnSms=(Button)findViewById(R.id.btn_sms);
        btnBack=(Button)findViewById(R.id.btn_back);
        tvCarName = (TextView) findViewById(R.id.tv_car_name);
        tvPerHour = (TextView) findViewById(R.id.tv_per_hour);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        imgCar = (ImageView) findViewById(R.id.img_car);
        tvSeater = (TextView) findViewById(R.id.tv_seater);
        tvAc = (TextView) findViewById(R.id.tv_ac);
        tvDate = (EditText) findViewById(R.id.tvDate);

        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, "Sms", Toast.LENGTH_SHORT).show();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, "Share", Toast.LENGTH_SHORT).show();
            }
        });
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAct();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        btnBookingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this,BookingHistory.class));
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvDate.getText().toString().equals("")){
                    Toast.makeText(getApplication(),"Please select a date",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplication(),"Booked for date "+tvDate.getText().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.dialogdate, null);
        popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.blank)
                .showImageOnLoading(R.drawable.blank).build();


        imageLoader.displayImage(item.getImage(), imgCar, options);
        tvCarName.setText(item.getName());
        tvPerHour.setText(item.getHourly_rate()+" Per hour");
        ratingBar.setRating(item.getRating());
        ratingBar.setNumStars(5);
        tvSeater.setText(item.getSeater()+"+1");
        if(item.getAc()==1){
            tvAc.setText("Yes");
        }else{
            tvAc.setText("No");
        }

        try {
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            } else {
                double lat = Double.parseDouble(item.getLocation().getLatitude());
                double lon = Double.parseDouble(item.getLocation().getLongitude());
                LatLng addressLoc = new LatLng(lat, lon);
                //googleMap.setTrafficEnabled(true);
                googleMap.setMyLocationEnabled(true);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addressLoc, 2));
                CameraPosition cameraPosition = CameraPosition.builder().target(addressLoc).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),1000, null);

                //destination position
                googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        .title(item.getName().toString())
                        .snippet(item.getType().toString())
                        .position(addressLoc).flat(false));

            }
        }
    }



    void dialogAct()  {
        datePicker = (DatePicker) popupView.findViewById(R.id.dpDob);
        imgClose = (ImageView) popupView.findViewById(R.id.imgClose);
        btnSubmit = (Button) popupView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String dd=datePicker.getDayOfMonth()+"";
                String mm=datePicker.getMonth()+"";
                String yy=datePicker.getYear()+"";
                if(mm.length()<2) mm="0"+mm;
                dob=yy+"-"+mm+"-"+dd;
                tvDate.setText(dob);
                popupWindow.dismiss();

            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(tvDate, Gravity.CENTER, 0, 0);
    }
}
