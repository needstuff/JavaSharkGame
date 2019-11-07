package com.example.me.sharks;

import android.graphics.Canvas;
import android.graphics.Color;

import com.example.me.sharks.math.Vec2D;

import java.util.ArrayList;


public class CellPart {
    ArrayList<ArrayList<Vec2D>> rows;
    public float width, height;
    public static CellPart instance = null;
    public Vec2D center;
    float block_w, block_h;
    public CellPart(float width, float height, float blocksize) {

        int nw = (int)(width / blocksize);
        int nh = (int) (height / blocksize);
        block_w = blocksize + ((width % blocksize) / nw);
        block_h = blocksize + ((height % blocksize) / nh);
        rows = new ArrayList<ArrayList<Vec2D>>();
        for (int y = 0; y < nh + 1; y++) {
            ArrayList<Vec2D> row = new ArrayList<Vec2D>();
            for (int x = 0; x < nw + 1; x++) {
                row.add(new Vec2D(x * block_w, y* block_h));
            }
            rows.add(row);
        }
        this.width = width;
        this.height = height;
        instance = this;
        center = new Vec2D(width/2,height/2);
    }
    public void draw(Canvas canvas) {
        int c = rows.get(0).size()-1;
        GPaint.instance.setColor(Color.GRAY);
        int r = rows.size()-1;

        for (int i = 0; i < c+1; i++) {
            Vec2D a = rows.get(0).get(i);
            Vec2D b = rows.get(r).get(i);
            canvas.drawLine(a.x, a.y, b.x, b.y, GPaint.instance);
        }
        for (int i = 0; i < rows.size(); i++) {
            Vec2D a = rows.get(i).get(0);
            Vec2D b = rows.get(i).get(c);
            canvas.drawLine(a.x, a.y, b.x, b.y, GPaint.instance);
        }
    }

    public boolean isInScreen(Vec2D point) {
        return point.x > 0 && point.x < width && point.y > 0 && point.y < height;
    }
}
