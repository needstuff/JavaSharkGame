package com.example.me.sharks.math;

import android.graphics.Matrix;
import android.util.Log;

public class Vec2D {
    public static final Vec2D UP = new Vec2D(0, 1);
    public static final Vec2D RIGHT = new Vec2D(1, 0);

    public float x, y;

    public Vec2D(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Vec2D() {
        x = y = 0f;
    }

    public Vec2D(Vec2D old) {
        this.x = old.x;
        this.y = old.y;
    }

    public void plusEquals(Vec2D v) {
        this.x += v.x;
        this.y += v.y;
    }

    public Vec2D getSum(Vec2D v) {
        return new Vec2D(this.x + v.x, this.y + v.y);
    }

    public void minusEquals(Vec2D v) {
        this. x -= v.x;
        this. y -= v.y;
    }

    public Vec2D getDifference(Vec2D v) {
        return new Vec2D(this.x - v.x, this.y - v.y);
    }

    public float dot(Vec2D v) {
        return this.x * v.x + this.y * v.y;
    }

    public void normalize() {
        float lenSquared = x*x+y*y;
        float len = (float)Math.sqrt(lenSquared);
        x /= len;
        y /= len;
    }

    public Vec2D getNormalized() {
        float len = (float) Math.sqrt(x*x+y*y);
        return new Vec2D(x / len, y / len);
    }

    public void scale(float s) {
        x *= s;
        y *= s;
    }

    public Vec2D getScaled(float s) {
        return new Vec2D(x * s, y * s);
    }

    public void divideEquals(float s) {
        x /= s;
        y /= s;
    }

    public float getLength() {
        return (float)Math.sqrt(x*x+y*y);
    }

    public float getLenSquared() {
        return x*x+y*y;
    }

    public Vec2D getRotated(float angle) {
        float ca = (float)Math.cos((double)angle);
        float sa = (float)Math.sin((double) angle);
        float nx = x * ca - y * sa;
        float ny = x * sa + y * ca;
        return new Vec2D(nx, ny);
    }

    public void rotate(float angle) {
        float ca = (float)Math.cos((double)angle);
        float sa = (float)Math.sin((double)angle);
        float nx = x * ca - y * sa;
        float ny = x * sa + y * ca;
        x = nx;
        y = ny;
    }

    public float calcAngle(Vec2D other) {
        float dp = this.dot(other);
        float ac = dp / (this.getLength() * other.getLength());
        return (float)Math.acos((double)ac);
    }

    public Vec2D getLeftNormal() {
        return new Vec2D(this.y, -this.x);
    }

    public Vec2D getRightNormal() {
        return new Vec2D(-this.y, this.x);
    }

    public Vec2D getReverse() {
        return new Vec2D(-this.x, -this.y);
    }
    public void reverse() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public boolean isInRect(float minx, float maxx, float miny, float maxy) {
        return x > minx && x < maxx && y > miny && y < maxy;
    }

    public boolean isInRect(FRect r) {
        //Log.d("FUCK", String.format("Is %f > %f, %f < %f, %f > %f, %f < %f", x, r.left, x, r.right, y, r.bottom, y, r.top));
        return x > r.left && x < r.right && y > r.bottom && y < r.top;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vec2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vec2D getLERP(Vec2D other, float percent) {
        float remainder = 1 - percent;
        return new Vec2D(x*percent + other.x * remainder,y*percent + other.y * remainder);
    }

    public void lerp(Vec2D other, float percent) {
        float remainder = 1 - percent;
        this.x = x*percent + other.x * remainder;
        this.y = y*percent + other.y * remainder;
    }

    public String toString() {
        return String.format("(%f,%f)", x, y);
    }

}
