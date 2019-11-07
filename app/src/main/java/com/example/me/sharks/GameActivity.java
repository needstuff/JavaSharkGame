package com.example.me.sharks;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameActivity extends Activity {
    Game game;
    boolean isFirstRun = true;
    GameEngineThread engine;
    SurfaceHolder holder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOG", "CREATED");
        if (isFirstRun) {
            Log.d("LOG", "LAUNCHING GAME");
            isFirstRun = false;
            game = new Game(this);

        }

        SurfaceView v = new SurfaceView(this);
        v.setOnTouchListener(game);
        holder = v.getHolder();
        engine = new GameEngineThread(game, holder);
        setContentView(v);

    }

    @Override
    public void onPause() {
        super.onPause();
        engine.pauseGame();

    }

    @Override
    public void onResume() {
        Log.d("LOG", "RESUME");
        super.onResume();
        engine.startGame();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //onPause();
    }


}