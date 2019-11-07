package com.example.me.sharks.math;

import android.graphics.Canvas;
import android.graphics.Color;

import com.example.me.sharks.GPaint;
import com.example.me.sharks.SpriteEntity;

/**
 * Created by me on 3/30/2016.
 */
public class LineSeg extends Mesh {
  
    public LineSeg(Vec2D[] points) {
        super(points);
    }

    public void draw(Canvas canvas) {
        GPaint.instance.setColor(Color.RED);
        canvas.drawLine(world_points[0].x, world_points[0].y, world_points[1].x, world_points[1].y, GPaint.instance);
    }

    public Shape getShape() {
        return Shape.LineSeg;
    }

    public Vec2D calcVector() {
        return world_points[1].getDifference(world_points[0]);
    }

    public Projection project(Vec2D axis) {
        float proj_a = world_points[0].dot(axis);
        float proj_b = world_points[1].dot(axis);
        if (proj_a < proj_b) {
            return new Projection(proj_a, proj_b);
        }
        return new Projection(proj_b, proj_a);
    }

    public Vec2D A() {
        return world_points[0];
    }

    public Vec2D B() {
        return world_points[1];
    }

}