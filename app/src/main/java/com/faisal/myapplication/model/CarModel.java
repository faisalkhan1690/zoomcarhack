package com.faisal.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by faisal khan on 9/13/2015.
 */
public class CarModel extends JSONRoot   {

    private ArrayList<CarDetail> cars;

    public ArrayList<CarDetail> getCars() {
        return cars;
    }

    public void setCars(ArrayList<CarDetail> cars) {
        this.cars = cars;
    }

    public class CarDetail implements Serializable{
        private String name;
        private String image;
        private String type;
        private int hourly_rate;
        private float rating;
        private String seater;
        private int ac;
        private CarLocation location;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getHourly_rate() {
            return hourly_rate;
        }

        public void setHourly_rate(int hourly_rate) {
            this.hourly_rate = hourly_rate;
        }

        public float getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getSeater() {
            return seater;
        }

        public void setSeater(String seater) {
            this.seater = seater;
        }

        public int getAc() {
            return ac;
        }

        public void setAc(int ac) {
            this.ac = ac;
        }

        public CarLocation getLocation() {
            return location;
        }

        public void setLocation(CarLocation location) {
            this.location = location;
        }
    }

    public class CarLocation implements Serializable{
        private String latitude;
        private String longitude;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
