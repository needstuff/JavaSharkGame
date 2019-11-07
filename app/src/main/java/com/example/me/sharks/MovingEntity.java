package com.example.me.sharks;

import android.graphics.Canvas;

import com.example.me.sharks.math.Vec2D;

/**
 * Created by me on 4/2/2016.
 */
public abstract class MovingEntity  {
    public Vec2D pos, velocity;
    public float mass, inverseMass;
    public Vec2D netForce;

    public MovingEntity(Vec2D pos, Vec2D velocity, float mass) {
        this.pos = new Vec2D(pos);
        this.velocity = new Vec2D(velocity);
        this.netForce = new Vec2D();
        this.mass = mass;
        inverseMass = 1/mass;
    }

    public MovingEntity(Vec2D pos, Vec2D velocity) {
        this.pos = new Vec2D(pos);
        this.velocity = new Vec2D(velocity);
        this.netForce = new Vec2D();
        mass = 1;
        inverseMass = 1;
    }

    public void applyForce(Vec2D force) {
        this.netForce.plusEquals(force);
    }


    public void update(float dt) {
        velocity.plusEquals(netForce.getScaled(inverseMass * dt));
        pos.plusEquals(velocity.getScaled(dt));
        netForce.x = 0f;
        netForce.y = 0f;
    }

    public abstract void draw(Canvas canvas);
}

