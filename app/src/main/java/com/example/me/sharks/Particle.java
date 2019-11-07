package com.example.me.sharks;

import android.graphics.Canvas;

import com.example.me.sharks.math.Vec2D;

/**
 * Created by me on 4/2/2016.
 */
public abstract class Particle extends MovingEntity {
    public float ttl;
    public boolean isAlive;
    public float dt_acc;
    public boolean simulated;

    public Particle(Vec2D pos, Vec2D velocity, float ttl) {
        super(pos, velocity);
        this.ttl = ttl;
        dt_acc = 0f;
        isAlive = true;
    }

    public abstract boolean isLiquid();

    public void update(float dt) {
        super.update(dt);
        dt_acc += dt;
        isAlive = dt_acc < ttl;
    }

    public abstract  void draw(Canvas canvas);
}