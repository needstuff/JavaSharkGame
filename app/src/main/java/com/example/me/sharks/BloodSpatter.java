package com.example.me.sharks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.me.sharks.math.Vec2D;

class Blood extends  Particle {
    public float startRad = 5f;

    public Blood(Vec2D pos, Vec2D velocity) {
        super(pos, velocity, 16f);
        startRad += (float)Math.random()*5;
        simulated = true;
    }

    public void draw(Canvas canvas) {
        GPaint.instance.setColor(Color.RED);
        GPaint.instance.setStyle(Paint.Style.FILL);

        float radScale = (ttl - dt_acc)/ttl;
        canvas.drawCircle(pos.x, pos.y, startRad*radScale, GPaint.instance);
    }

    public boolean isLiquid() {
        return true;
    }
}

public class BloodSpatter extends Emitter {



    public BloodSpatter(Vec2D[] points) {
        super(points, (float)Math.random(), .02f, 40);

    }

    public Particle newParticle() {
        Vec2D v = new Vec2D((float)Math.random() * 10, (float)Math.random()*10f);
        return new Blood(world_points[0],v );
    }
}
