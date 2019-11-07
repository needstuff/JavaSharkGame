package com.example.me.sharks.math;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.me.sharks.GPaint;

/**
 * Created by me on 3/31/2016.
 */
public class Circle extends Mesh {
    public float radius;

    public Circle(Vec2D pos, float radius) {
        super(new Vec2D[] {pos});
        this.radius = radius;
    }

    public void draw(Canvas canvas) {
        GPaint.instance.setStyle(Paint.Style.STROKE);
        GPaint.instance.setColor(Color.RED);
        canvas.drawCircle(world_points[0].x,world_points[0].y,radius, GPaint.instance);
    }

    public Shape getShape() {
        return Shape.Cicle;
    }

    public Projection project(Vec2D axis) {
        float proj = world_points[0].dot(axis);
        return new Projection(proj-radius, proj+radius);
    }
}
