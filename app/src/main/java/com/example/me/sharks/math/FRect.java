package com.example.me.sharks.math;

import android.util.Log;

/**
 * Created by me on 3/31/2016.
 */
public class FRect {
    public float left, top, right, bottom;

    public FRect(float left, float top, float right, float bottom) {
        this.left = left;
        this.top= top;
        this.bottom = bottom;
        this.right=right;
    }

    public boolean testIntersect(FRect other) {
        if (right < other.left || other.right < left) {
            return false;
        }
//        Log.d("LOG", String.format("t,b: %f %f", top, bottom));
//        Log.d("LOG", String.format("t,b: %f %f", other.top, other.bottom));
        if (bottom < other.top || other.bottom < top) {
            return false;
        }
        return true;
    }
}
