package com.example.me.sharks;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.me.sharks.math.Mesh;
import com.example.me.sharks.math.Poly;
import com.example.me.sharks.math.Vec2D;

/**
 * Created by me on 3/31/2016.
 */
public class Fish extends Interactable {
    float dt_acc = 0.0f;
    float limit = 1 +(float)Math.random() * 5f;

    public Fish(Vec2D pos, Vec2D heading, Bitmap bitmap) {
        super(pos, heading, bitmap, 1f);
        //Mesh c = new Circle(new Vec2D(8,0), 20);
        Mesh c = new Poly(new Vec2D[] {new Vec2D(-10,-15), new Vec2D(20,-15), new Vec2D(20,15), new Vec2D(-10,15)});
        addMesh(c);
        speed = 50 + (float)Math.random() * 200f;
        isEdible = true;
        healthValue = 10f;
        isAlive = true;
    }

    public void update(float dt) {
        dt_acc += dt;
        if ( dt_acc > limit) {
            dt_acc = 0.0f;
            limit = 1 + (float)Math.random() * 10f;
            speed = 50f + (float)Math.random() * 50;
            int sign = -1;
            if (Math.random() < .5) {
                sign = 1;
            }

            requestHeading(heading.getRotated((float) (Math.random() * Math.PI * sign)));



        }
//        if (velocity.getLenSquared() < speed*speed) {
          applyForce(heading.getScaled(speed*mass));
//        }
        super.update(dt);
    }


//    public void updateOnDeath(Game game) {
//
//    }
}


