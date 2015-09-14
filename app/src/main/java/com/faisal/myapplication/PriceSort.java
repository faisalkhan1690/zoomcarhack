package com.faisal.myapplication;

import java.util.Comparator;

/**
 * Created by faisal khan on 9/13/2015.
 */
public class PriceSort implements Comparator<CarModel> {
    @Override
    public int compare(CarModel o1, CarModel o2) {
        if (o1.getHourly_rate() == o2.getHourly_rate()) {
            return 0;
        } else if (o1.getHourly_rate() < o2.getHourly_rate()) {
            return 1;
        } else {
            return -1;
        }
    }
}
