package com.example.me.sharks.math;

import android.graphics.Canvas;

import com.example.me.sharks.SpriteEntity;

/**
 * Created by me on 4/3/2016.
 */
public class Attachable {

    protected Vec2D[] local_points;
    protected Vec2D[] world_points;
    public boolean inverted = false;
    public SpriteEntity parent = null;

    public Attachable(Vec2D[] points) {
        local_points = new Vec2D[points.length];
        world_points = new Vec2D[points.length];
        for (int i = 0; i < points.length; i++) {
            local_points[i] = new Vec2D(points[i]);
            world_points[i] = new Vec2D(points[i]);
        }


    }

    public void update(float angle, Vec2D origin, boolean invert) {
        for (int i = 0; i < local_points.length; i++) {
            Vec2D new_point;
            if (invert) {

                Vec2D p = local_points[i];
                new_point = new Vec2D(-p.x, p.y).getRotated((float)Math.PI + angle);
            }
            else {
                new_point = local_points[i].getRotated(angle);
            }
            world_points[i] = origin.getSum(new_point);
        }
        inverted = invert;
    }

    public void update() {
        if (parent != null) {
            update(parent.orientation, parent.pos, parent.invert);
        }
    }
}
