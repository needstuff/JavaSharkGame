package com.example.me.sharks.math;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.example.me.sharks.GPaint;
import com.example.me.sharks.SpriteEntity;

import java.util.ArrayList;

/**
 * Created by me on 4/1/2016.
 */
public class Poly extends Mesh {
    public Poly(Vec2D[] points) {
        super(points);

    }
    public void draw(Canvas canvas) {
        int last_i = local_points.length-1;
        Vec2D p1, p2;
        GPaint.instance.setColor(Color.RED);
        for (int i = 0; i < last_i; i++) {
            p1 = world_points[i];
            p2 = world_points[i+1];
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, GPaint.instance);

        }
        p1 = world_points[0];
        p2 = world_points[last_i];
        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, GPaint.instance);
        GPaint.instance.setColor(Color.BLUE);
        ArrayList<LineSeg> segs = genLineSegments();
        for (int i = 0; i < segs.size(); i++) {
            LineSeg seg = segs.get(i);
            Vec2D n = seg.calcVector();
            Vec2D a = seg.A();

            Vec2D p3 = a.getSum(n.getLeftNormal().getNormalized().getScaled(10f));
            canvas.drawLine(a.x, a.y, p3.x, p3.y, GPaint.instance);
        }
    }

    public Shape getShape() {
        return Shape.ConvexPoly;
    }

    public ArrayList<LineSeg> genLineSegments() {
        int last_i = world_points.length - 1;

        ArrayList<LineSeg> segs = new ArrayList<>();
        if (!inverted) {

            for (int i = 0; i < last_i; i++) {
                segs.add(new LineSeg( new Vec2D[] {world_points[i], world_points[i + 1]} ));
            }
            segs.add(new LineSeg(new Vec2D[] {world_points[last_i], world_points[0]}));
        }
        else {
            for(int i = last_i; i > 0; i--) {
                segs.add(new LineSeg(new Vec2D[] { world_points[i], world_points[i - 1]}));
            }
            segs.add(new LineSeg(new Vec2D[] { world_points[0], world_points[last_i]}));
        }

        return segs;
    }

    public Projection project(Vec2D axis) {
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        for (int i = 0; i < world_points.length; i++) {
            float proj = world_points[i].dot(axis);
            min = Math.min(min, proj);
            max = Math.max(max, proj);
        }
        return new Projection(min, max);
    }

    public void interpolate(Vec2D[] updated, float percent) {
        for (int i = 0; i < local_points.length; i++) {
            local_points[i].lerp(updated[i], percent);
        }
    }

}
