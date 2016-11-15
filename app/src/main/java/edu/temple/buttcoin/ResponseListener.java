package edu.temple.buttcoin;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by rober_000 on 11/12/2016.
 */

public interface ResponseListener {
    void respondToResult(ArrayList<Double> result);
    void respondToResult(Drawable result);
    void respondToResult(String[][] result);
    void respondToResult(String result);
}
//TODO separate into separate interfaces