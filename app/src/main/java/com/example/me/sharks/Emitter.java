package com.example.me.sharks;

import android.graphics.Canvas;
import android.util.Log;

import com.example.me.sharks.math.Attachable;
import com.example.me.sharks.math.Vec2D;

import java.util.ArrayList;

/**
 * Created by me on 4/2/2016.
 */
public abstract class Emitter extends Attachable {
    float frequency;
    int maxParticles;
    float ttl;
    float dt_acc;
    boolean isAlive;
    public ArrayList<Particle> particles;

    public Emitter(Vec2D[] points, float ttl, float frequency, int maxParticles) {
        super(points);
        this.frequency = frequency;
        this.maxParticles = maxParticles;
        particles = new ArrayList<>();
        dt_acc = 0;
        this.ttl = ttl;
    }

    public void update(float dt) {
        super.update();
        ArrayList<Particle> dead = new ArrayList<>();
        for (Particle p : particles) {
            p.update(dt);
            if (!p.isAlive) {
                dead.add(p);
            }
        }
        particles.removeAll(dead);
        dt_acc += dt;
        if (dt_acc < ttl) {
            isAlive = true;
            int target = Math.round(dt_acc/frequency);
            while (target > particles.size() && particles.size() < maxParticles) {
                particles.add(this.newParticle());
            }

        }
        else {
            isAlive = false;
            for (Particle p : particles) {
                isAlive = isAlive || p.isAlive;
            }
        }

    }

    public void offSetParticles(Vec2D offset) {
        for (Particle p : particles) {
            p.pos.plusEquals(offset);
        }
    }

    public abstract Particle newParticle();

    public void draw(Canvas canvas) {
        for (Particle p : particles) {
            p.draw(canvas);
        }
    }

}
