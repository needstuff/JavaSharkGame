package com.example.me.sharks;

import android.graphics.Bitmap;

import com.example.me.sharks.math.Vec2D;

/**
 * Created by me on 4/5/2016.
 */
public class Boat extends SpriteEntity {

    public Boat(Vec2D pos, Vec2D heading, Bitmap bitmap, float mass) {
        super(pos, heading, bitmap, mass);
    }

}
