package com.example.me.sharks;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by me on 3/27/2016.
 */
public class GameEngineThread implements Runnable {
    private Game game;
    SurfaceHolder holder;
    Thread t;
    boolean isRunning;
    static int fps = 1/30;
    float acc = 0f;

    public GameEngineThread(Game game, SurfaceHolder holder) {
        this.game = game;
        this.holder = holder;

    }

    public void startGame() {
        isRunning = true;
        t = new Thread(this);
        t.start();
    }

    public void pauseGame() {
        if (t != null && isRunning) {
            try {
                isRunning = false;
                t.join();
            } catch (InterruptedException e) {

            }
        }
        t = null;
    }

    public void run() {
        double startTime = System.currentTimeMillis();
        while (isRunning) {
            double now = System.currentTimeMillis();
            double deltaTime =  (now - startTime) / 1000.0d;
            startTime = now;
            acc+= deltaTime;
            game.update((float) deltaTime);
            if (acc >= fps) {
                game.draw();
                acc = 0f;
            }


            while(!holder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = holder.lockCanvas();

            canvas.drawBitmap(game.frameBuffer,0,0, null);

            holder.unlockCanvasAndPost(canvas);

        }
    }


}
