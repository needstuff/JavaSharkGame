package com.example.me.sharks;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.me.sharks.math.FRect;
import com.example.me.sharks.math.Mesh;
import com.example.me.sharks.math.Vec2D;

import java.util.ArrayList;

public class Entity {
    private static final float radToDeg = 180f / (float)Math.PI;
    public Vec2D pos, heading;
    public float speed;
    public float mass;
    public float orientation;
    Bitmap bitmap;
    ArrayList<Mesh> meshes = new ArrayList<Mesh>();
    private  boolean invert;
    float halfWidth, halfHeight;

    public Entity(Vec2D pos, Vec2D heading, Bitmap bitmap) {
        this.pos = pos;
        this.heading = heading;
        this.bitmap = bitmap;
        halfWidth = bitmap.getWidth()/2;
        halfHeight = bitmap.getHeight()/2;
    }

    public void update(float dt) {
        orientation = (float)Math.atan2(heading.y, heading.x);
        invert = (Math.abs(orientation) > Math.PI / 2);

        pos.plusEquals(heading.getScaled(speed * dt));

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

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setHeading(Vec2D h) {
        heading = h;
        heading.normalize();
    }

    public void addSpeed(float more) {
        speed = Math.min(speed+more, 4);
    }

    public void addMesh(Mesh c) {
        c.setParentEntity(this);
        meshes.add(c);
    }

    public void drawMeshes(Canvas canvas) {
        for (int i = 0; i < meshes.size(); i++) {
            meshes.get(i).draw(canvas);
        }
    }

    public void drawVecs(Canvas canvas) {
        GPaint.instance.setColor(Color.RED);
        GPaint.instance.setStyle(Paint.Style.STROKE);
        canvas.drawRect(pos.x - halfWidth, pos.y - halfHeight, pos.x + halfWidth, pos.y + halfHeight, GPaint.instance);
        canvas.drawCircle(pos.x, pos.y, 2, GPaint.instance);
        //canvas.drawBitmap(bitmap, pos.x - bitmap.getWidth() / 2, pos.y - bitmap.getHeight() / 2, null);
        Vec2D p1 = pos.getSum(Vec2D.RIGHT.getScaled(50));
        Vec2D p2 = pos.getSum(heading.getScaled(50));

        GPaint.instance.setColor(Color.YELLOW);
        canvas.drawLine(pos.x, pos.y, p1.x, p1.y, GPaint.instance);
        GPaint.instance.setColor(Color.BLUE);
        canvas.drawLine(pos.x, pos.y, p2.x, p2.y, GPaint.instance);
    }

    public void handleCollision(Entity e) {
        for (int i = 0; i < meshes.size(); i++) {
            for (int j = 0; j < e.meshes.size(); j++) {
                Mesh.CollisionInfo info = meshes.get(i).testCollision(e.meshes.get(j));

                if (info.isCollision) {
                    if (mass > e.mass) {
                        e.pos = e.pos.getSum(info.normal.getScaled(info.depth));
                    }
                    else {
                        pos = pos.getSum(info.normal.getScaled(info.depth).getReverse());
                    }
                }
            }
        }
    }

    public FRect getRect() {
        return new FRect(pos.x-halfWidth, pos.y+halfHeight, pos.x+halfWidth, pos.y-halfHeight);
    }

}
