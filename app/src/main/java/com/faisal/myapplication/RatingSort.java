package com.faisal.myapplication;

import com.faisal.myapplication.model.CarModel;

import java.util.Comparator;

/**
 * Created by faisal khan on 9/13/2015.
 */
public class RatingSort implements Comparator<CarModel.CarDetail> {
    @Override
    public int compare(CarModel.CarDetail o1, CarModel.CarDetail o2) {
        if (o1.getRating() == o2.getRating()) {
            return 0;
        } else if (o1.getRating() < o2.getRating()) {
            return 1;
        } else {
            return -1;
        }
    }
}
