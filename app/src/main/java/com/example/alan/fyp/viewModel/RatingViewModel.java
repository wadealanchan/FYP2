package com.example.alan.fyp.viewModel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

/**
 * Created by wadealanchan on 29/1/2018.
 */

public class RatingViewModel {



    private ObservableInt numberOfStar = new ObservableInt();
    private ObservableInt numberOfRating = new ObservableInt();



    public ObservableInt getNumberOfStar() {
        return numberOfStar;
    }

    public void setNumberOfStar(ObservableInt numberOfStar) {
        this.numberOfStar = numberOfStar;
    }






}
