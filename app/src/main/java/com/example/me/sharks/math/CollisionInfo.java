package com.example.me.sharks.math;

import android.graphics.Canvas;
import android.graphics.Color;

import com.example.me.sharks.GPaint;

import java.util.ArrayList;

public class CollisionInfo {
    public float depth;
    public ArrayList<Vec2D> manifold;
    public boolean isCollision;
    public Vec2D normal;
    public CollisionInfo() {
        isCollision = false;
        depth = 0;
        manifold = new ArrayList<Vec2D>();
        normal = new Vec2D();
    }

    public void draw(Canvas canvas, Vec2D pos) {
        GPaint.instance.setColor(Color.CYAN);
        Vec2D end = pos.getSum(normal.getScaled(300));
        canvas.drawLine(pos.x, pos.y, end.x, end.y, GPaint.instance);
    }
}