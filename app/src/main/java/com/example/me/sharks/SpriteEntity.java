package com.example.me.sharks;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.example.me.sharks.math.Attachable;
import com.example.me.sharks.math.FRect;
import com.example.me.sharks.math.Mesh;
import com.example.me.sharks.math.Vec2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SpriteEntity extends MovingEntity{
    private static final float radToDeg = 180f / (float)Math.PI;
    public Vec2D heading;
    public float speed = 0f;
    public float orientation;
    Bitmap bitmap;
    ArrayList<Mesh> meshes = new ArrayList<Mesh>();
    public  boolean invert;
    float halfWidth, halfHeight;
    boolean collisionMeshesEnabled = false;
    private Vec2D newHeading = null;
    public ArrayList<Attachable> attachables = new ArrayList<>();

    public SpriteEntity(Vec2D pos, Vec2D heading, Bitmap bitmap, float mass) {
        super(pos, new Vec2D(), mass);
        this.pos = pos;
        this.heading = heading;
        this.bitmap = bitmap;
        halfWidth = bitmap.getWidth()/2;
        halfHeight = bitmap.getHeight()/2;
    }

    public void update(float dt) {
        collisionMeshesEnabled = false;


        orientation = (float)Math.atan2(heading.y, heading.x);
        if(newHeading != null) {
            float newOrientation = (float)Math.atan2(newHeading.y, newHeading.x);
            float diff = newOrientation-orientation;

            if (Math.abs(diff) < Math.PI/32) {
                heading = newHeading;
                orientation = newOrientation;
                newHeading = null;
            }
            else {
                Vec2D lerp = heading.getLERP(newHeading, .85f);
                setHeading(lerp);
            }
        }


        invert = (Math.abs(orientation) > Math.PI / 2);


        super.update(dt);



        for(Attachable a : attachables) {
            a.update(orientation, pos, invert);
        }

    }

    public void updateCollisionMeshes() {
        collisionMeshesEnabled = true;
        for (int i = 0; i < meshes.size(); i++) {
            meshes.get(i).update(orientation, pos, invert);
        }
    }

    public void draw(Canvas canvas) {
        Matrix m = new Matrix();
        float a = orientation * radToDeg;
        if (invert) {
            m.preScale(-1f, 1f, halfWidth, halfHeight);
            m.preRotate(-a-180, halfWidth, halfHeight);
        }
        else {
            m.preRotate(a, halfWidth, halfHeight);
        }


        m.postTranslate(pos.x - halfWidth, pos.y - halfHeight);

        canvas.drawBitmap(bitmap, m, null);

    }

    public void requestHeading(Vec2D h) {
        newHeading = h.getNormalized();
    }

    public void setHeading(Vec2D h) {
        heading = h;
        heading.normalize();
    }

    public FRect getPruningRect() {
        return new FRect(pos.x - halfWidth*2, pos.y-halfHeight*2, pos.x+halfWidth*2, pos.y+halfHeight*2);
    }
    public void addMesh(Mesh c) {
        meshes.add(c);
    }

    public void drawMeshes(Canvas canvas) {
        FRect p = getPruningRect();
        GPaint.instance.setColor(Color.GREEN);
        GPaint.instance.setStyle(Paint.Style.STROKE);
        canvas.drawRect(p.left, p.top, p.right, p.bottom, GPaint.instance);

        if (!collisionMeshesEnabled) {
            return;
        }
        for (int i = 0; i < meshes.size(); i++) {
            meshes.get(i).draw(canvas);
        }
    }

    public void drawVecs(Canvas canvas) {
        GPaint.instance.setColor(Color.RED);
        GPaint.instance.setStyle(Paint.Style.STROKE);
        //canvas.drawRect(pos.x - halfWidth, pos.y - halfHeight, pos.x + halfWidth, pos.y + halfHeight, GPaint.instance);
        canvas.drawCircle(pos.x, pos.y, 2, GPaint.instance);
        Vec2D p1 = pos.getSum(Vec2D.RIGHT.getScaled(50));
        Vec2D p2 = pos.getSum(heading.getScaled(50));

        GPaint.instance.setColor(Color.YELLOW);
        canvas.drawLine(pos.x, pos.y, p1.x, p1.y, GPaint.instance);
        GPaint.instance.setColor(Color.BLUE);
        canvas.drawLine(pos.x, pos.y, p2.x, p2.y, GPaint.instance);

        if(newHeading != null) {
            Vec2D p3 = pos.getSum(newHeading.getScaled(50));
            GPaint.instance.setColor(Color.RED);
            canvas.drawLine(pos.x, pos.y, p3.x, p3.y, GPaint.instance);
        }
    }


    public FRect getRect() {
        return new FRect(pos.x-halfWidth, pos.y+halfHeight, pos.x+halfWidth, pos.y-halfHeight);
    }

}
