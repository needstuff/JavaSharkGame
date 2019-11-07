package com.example.me.sharks.math;

import android.graphics.Canvas;
import android.graphics.Color;

import com.example.me.sharks.GPaint;

import java.util.ArrayList;

public abstract class Mesh extends Attachable {
    public Mesh(Vec2D[] points) {
        super(points);
    }
    public abstract void draw(Canvas canvas);
    public abstract Shape getShape();
    public abstract Projection project(Vec2D axis);

    public enum Shape {
        Cicle, ConvexPoly,  Rect,  LineSeg
    }

}


