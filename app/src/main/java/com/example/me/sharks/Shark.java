package com.example.me.sharks;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.me.sharks.math.CollisionFunc;
import com.example.me.sharks.math.CollisionInfo;
import com.example.me.sharks.math.FRect;
import com.example.me.sharks.math.Mesh;
import com.example.me.sharks.math.Poly;
import com.example.me.sharks.math.Vec2D;

/**
 * Created by me on 3/31/2016.
 */
public class Shark extends SpriteEntity {
    static final Vec2D[] topHeadOpen = new Vec2D[] {new Vec2D(110,0), new Vec2D(110,-45), new Vec2D(180,-35)};
    static final Vec2D[] topHeadClosed = new Vec2D[] {new Vec2D(110,0), new Vec2D(110,-45), new Vec2D(180,0)};

    static final Vec2D[] bottomHeadOpen = new Vec2D[] {new Vec2D(110,0), new Vec2D(150,25), new Vec2D(110,30)};
    static final Vec2D[] bottomHeadClosed = new Vec2D[] {new Vec2D(110,0), new Vec2D(150,0), new Vec2D(110,30)};
    Poly topHead = new Poly(topHeadClosed);
    Poly bottomHead = new Poly(bottomHeadClosed);
    Poly mouth = new Poly(new Vec2D[] {new Vec2D(110,0), new Vec2D(110,-45), new Vec2D(150,25)});
    boolean isMouthOpen = false;
    float biteTimeout = .3f;
    float eatTimeout = .1f;
    float dt_acc = 0f;
    boolean action = false;
    public  float health = 50;
    private Game game;
    public int score;

    public Shark(Vec2D pos, Vec2D heading, Bitmap bitmap, Game game) {
        super(pos, heading, bitmap, 10);
        attachables.add(mouth);
        addMesh(bottomHead);
        addMesh(topHead);

        this.game = game;
    }

    public void update(float dt) {
        if(action) {
            dt_acc += dt;
            if (dt_acc > biteTimeout) {
                action = false;
                dt_acc = 0f;
                isMouthOpen = !isMouthOpen;
            }
            else {
                if (isMouthOpen) {
                    if (dt < eatTimeout) {
                        for(int j = 0; j < game.interactables.size(); j++) {
                            Interactable i = game.interactables.get(j);
                            if (inMouth(i)) {
                                eat(i);
                                Emitter blood = new BloodSpatter(new Vec2D[] {new Vec2D(110,0)});
                                blood.parent = this;
                                game.emitters.add(blood);

                            }
                        }
                    }
                    topHead.interpolate(topHeadClosed, .65f);
                    bottomHead.interpolate(bottomHeadClosed, .65f);
                } else {
                    topHead.interpolate(topHeadOpen, .80f);
                    bottomHead.interpolate(bottomHeadOpen, .80f);
                }
            }
        }

        super.update(dt);

    }

    public void thrust() {
        float max = 1200;
        float s = velocity.getLength();
        if (s + 800 > max ){
            if (s >= max) {
                return;
            }
            applyForce(heading.getScaled(max - (s+800)));
            return;
        }

        applyForce(heading.getScaled(800 * mass));
    }



    public void drawMeshes(Canvas canvas) {
        super.drawMeshes(canvas);


    }

    public void eat(Interactable i) {
        i.isAlive = false;
        health += i.healthValue;
        score += 1;
    }

    public FRect getPruningRect() {
        Vec2D offset = pos.getSum(heading.getScaled(halfWidth * .8f));
        float t = 100;
        return new FRect(offset.x-t, offset.y-t, offset.x+t, offset.y+t);
    }

    public boolean inMouth(Interactable i) {
        if (isMouthOpen && i.isEdible) {
            CollisionInfo info = CollisionFunc.resolveCollision(
                    (Poly)mouth,
                    i.meshes.get(0)
            );

            if (info.isCollision) {
               return true;
            }
        }
        return false;
    }

    public void toggleMouth() {
        if (!action) {
            action = true;
        }

    }
}
