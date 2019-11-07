package com.example.me.sharks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.me.sharks.math.CollisionFunc;
import com.example.me.sharks.math.CollisionInfo;
import com.example.me.sharks.math.FRect;
import com.example.me.sharks.math.Vec2D;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by me on 3/27/2016.
 */
public class Game implements View.OnTouchListener{
    static final float BLUE = 360*.55f;
    static final Vec2D G = new Vec2D(0, 800f);
    Shark shark = null;
    public Bitmap frameBuffer;
    Canvas canvas;
    CellPart partition;
    ArrayList<Interactable> interactables = new ArrayList<Interactable>();
    ArrayList<Emitter> emitters = new ArrayList<>();
    Vec2D screenPos = new Vec2D(500, 500);
    Vec2D sharkWorldPos;
    Vec2D sharkOffset;
    float floor =  4000;
    boolean isSwimming = false;
    float percentDepth;
    boolean aboveSurface = false;
    static final float WATER_FRICTION = .98f;
    static final float gravity = 800f;
    Context context;

    public Game(Context context) {
        shark  = new Shark(new Vec2D(300,300), new Vec2D(1,0),
                BitmapFactory.decodeResource(context.getResources(), R.mipmap.sharksmall), this);

        this.context = context;

        Point size = new Point();
        ((Activity)(context)).getWindowManager().getDefaultDisplay().getSize(size);
        frameBuffer = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.RGB_565);
        canvas = new Canvas(frameBuffer);
        partition = new CellPart(size.x, size.y, 100);

    }
    public Fish genFish() {
        float s1 = Math.random() < .5? 1:-1;
        float s2 = Math.random() < .5? 1:-1;
        float r1 = (float)Math.random() * 300 * s1 + ((s1 < 0) ? 0: partition.width);
        float r2 = (float)Math.random() * 300 * s2 + ((s2 < 0) ? 0: partition.height);

        Fish fish1  = new Fish(new Vec2D(r1,r2), new Vec2D(1,0),
            BitmapFactory.decodeResource(context.getResources(), R.mipmap.fish3));

        return fish1;
    }
    private Vec2D worldPos(MovingEntity e) {
        Vec2D offset = e.pos.getDifference(partition.center);
        return screenPos.getSum(offset);
    }

    private boolean inWater(MovingEntity e) {
        return worldPos(e).y > 0;
    }

    private  float dragScale(float cos) {
        if (cos < .58) {
            return 2f;
        }
        if (cos < .76) {
            return .5f;
        }
        return 0f;
    }

    public void update(float dt) {
        isSwimming = isSwimming && !aboveSurface;
        if(isSwimming) {
            shark.thrust();
        }


        sharkOffset = partition.center.getDifference(shark.pos);
        sharkWorldPos = screenPos.getSum(sharkOffset.getReverse());
        float distance = sharkOffset.getLength();
        float f = 25 + (distance/200);
        Vec2D scrollVec = sharkOffset.getScaled(f*dt);
        shark.pos.plusEquals(scrollVec);


        screenPos.plusEquals(scrollVec.getReverse());
        aboveSurface = sharkWorldPos.y < 0;
        if(aboveSurface) {
           shark.applyForce(new Vec2D(0, shark.mass*gravity));
        }
        else {
            Vec2D friction = shark.velocity.getReverse().getScaled(shark.mass);
            float dotr = Math.abs(shark.heading.dot(Vec2D.RIGHT));
            float dotu = Math.abs(shark.heading.dot(Vec2D.UP));
            friction.x *= WATER_FRICTION + dragScale(dotr);
            friction.y *= WATER_FRICTION + dragScale(dotu);
            shark.applyForce(friction);

        }

        for(Interactable i : interactables) {
            i.pos.plusEquals(scrollVec);
        }


        for (Interactable i : interactables) {
            if (!inWater(i) && !shark.inMouth(i)) {
                i.applyForce(new Vec2D(0, i.mass*gravity));
            }
            else {
                i.applyForce(i.velocity.getReverse().getScaled(WATER_FRICTION*i.mass));
            }
            i.update(dt);
        }
        shark.update(dt);
        shark.updateCollisionMeshes();

        for (Emitter e : emitters) {
            e.update(dt);
            for(Particle p : e.particles) {
                if (p.simulated) {
                    if (!inWater(p)) {
                        p.applyForce(G);
                    } else {
                        if (p.isLiquid()) {
                            p.velocity = new Vec2D((float) Math.random() * 10f, (float) Math.random() * 10f);
                            p.simulated = false;
                        }
                    }
                }
                p.pos.plusEquals(scrollVec);

            }
        }
        FRect sharkPrune = shark.getPruningRect();
        ArrayList<Interactable> tests = new ArrayList<>();
        for(Interactable i : interactables) {
            FRect other = i.getPruningRect();
            if (sharkPrune.testIntersect(other)) {
                i.updateCollisionMeshes();
                tests.add(i);
            }
        }
        for(int i = 0; i < shark.meshes.size(); i++) {
            for(int j = 0; j < tests.size(); j++) {
                for (int k = 0; k < tests.get(j).meshes.size(); k++) {
                    CollisionInfo info = CollisionFunc.resolveCollision(
                            shark.meshes.get(i),
                            tests.get(j).meshes.get(k));
                    if (info.isCollision) {
                        tests.get(j).pos.plusEquals(info.normal.getScaled(info.depth));
                    }
                }

            }
        }


        for(Iterator<Interactable> i = interactables.iterator(); i.hasNext();) {
            Interactable c = i.next();
            if (!partition.isInScreen(c.pos)) {
                if ( c.ttl == 0f) {
                    c.ttl = 10f;
                }
                else {
                    c.ttl -= dt;
                }
            }
            else {
                c.ttl = 0f;
            }
        }

        for (Iterator<Interactable> i = interactables.iterator(); i.hasNext(); ) {
            if (!i.next().isAlive ) {
                i.remove();
            }
        }
        while (interactables.size() < 20) {
            interactables.add(genFish());
        }
        for (Iterator<Emitter> i = emitters.iterator(); i.hasNext(); ) {
            if (!i.next().isAlive) {
                i.remove();
            }
        }
    }

    public void draw() {
        frameBuffer.eraseColor(Color.TRANSPARENT);
        percentDepth = Math.min(1, Math.max(0, screenPos.y / floor));

        boolean deep = percentDepth > .8f;
        if(!deep) {
            float waterline = screenPos.y - (partition.height/2);
            //Log.d("LOG", "WATRLINE: " + waterline);
            if (waterline < 0) {
                GPaint.instance.setColor(Color.HSVToColor(new float[]{BLUE + (.10f * percentDepth), .7f, .5f - (.2f * percentDepth)}));
                GPaint.instance.setStyle(Paint.Style.FILL);
                canvas.drawRect(0, waterline*-1, partition.width, partition.height, GPaint.instance);
            }
            else {
                canvas.drawColor(Color.HSVToColor(new float[]{BLUE + (.10f * percentDepth), .7f, .5f - (.2f * percentDepth)}));
            }
        }


        //partition.draw(canvas);
        for (int i = 0; i < interactables.size(); i++) {
            interactables.get(i).draw(canvas);
        }
        for (int i = 0; i < interactables.size(); i++) {
            interactables.get(i).drawMeshes(canvas);
        }
        shark.draw(canvas);

        for (Emitter e : emitters) {
            e.draw(canvas);
        }
        shark.drawMeshes(canvas);
        shark.drawVecs(canvas);
        if (deep) {
            float percentFade = (percentDepth-.8f) / .2f;
            canvas.drawColor(Color.HSVToColor(175 + (int)(75*percentFade),
                    new float[]{BLUE  + .10f, 1f, .3f}));
        }


        GPaint.instance.setAlpha(100);
        GPaint.instance.setColor(Color.RED);
        GPaint.instance.setStyle(Paint.Style.FILL);
        canvas.drawCircle(50, 50, 50, GPaint.instance);

        GPaint.instance.setColor(Color.YELLOW);
        GPaint.instance.setTextSize(100f);
        canvas.drawText(String.format("x%d", shark.score), 0f, 100f, GPaint.instance);
        GPaint.instance.setTextSize(25f);
        canvas.drawText(String.format("Depth: %d", (int) sharkWorldPos.y), 150f, 50f, GPaint.instance);
    }

    public boolean onTouch(View view, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Vec2D contact = new Vec2D(x,y);

        if (contact.isInRect(0,100,0,100)) {
            shark.toggleMouth();
            return true;
        }

        if (contact.isInRect(shark.getRect())) {
            shark.speed *= .90;
            return true;
        }
        int a =  event.getAction();
        if (a == event.ACTION_DOWN || a == event.ACTION_MOVE) {
            shark.requestHeading(contact.getDifference(shark.pos));
            isSwimming = true;
        }
        else if (a == event.ACTION_UP) {
            isSwimming = false;
        }


        return true;

    }
}
