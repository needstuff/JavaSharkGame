package com.example.me.sharks;

import android.graphics.Bitmap;

import com.example.me.sharks.math.Vec2D;

/**
 * Created by me on 4/2/2016.
 */
public abstract class Interactable extends SpriteEntity {
    public boolean isEdible;
    public float healthValue;
    public boolean isAlive;
    public float ttl;
    public Interactable(Vec2D pos, Vec2D heading, Bitmap bitmap, float mass) {
        super(pos, heading, bitmap, mass);
        ttl=0f;
    }

    public void update(float dt) {
        isAlive = isAlive && ttl >= 0f;
        super.update(dt);
    }
    //public abstract void updateOnDeath(Game game);
}
