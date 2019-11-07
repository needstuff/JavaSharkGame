package com.example.me.sharks.math;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by me on 3/31/2016.
 */
public class CollisionFunc {

//    public static Mesh.CollisionInfo lineSegCircle(LineSeg seg, Circle c) {
//        Mesh.CollisionInfo info = new Mesh.CollisionInfo();
//
//        Vec2D n = seg.b.getDifference(seg.a);
//        float len_n = n.getLength();
//        n.divideEquals(len_n);
//        Vec2D cToa = seg.a.getDifference(c.pos);
//        float lenProj = cToa.dot(n);
//        if (Math.abs(lenProj) > len_n) {
//            return info;
//        }
//        Vec2D proj = n.getScaled(lenProj);
//        Vec2D perp = cToa.getDifference(proj);
//        float d = perp.getLength();
//        if (d >= c.radius) {
//            return info;
//        }
//        info.depth = c.radius-d;
//        info.isCollision = true;
//        perp.divideEquals(d);
//        info.normal = perp;
//        return info;
//    }

    public static CollisionInfo poly2poly(Poly p, Poly c) {
        CollisionInfo info = new CollisionInfo();
        ArrayList<LineSeg> segs = p.genLineSegments();

        segs.addAll(c.genLineSegments());
        info.depth = Float.MAX_VALUE;
        for(int i = 0; i < segs.size(); i++) {
            LineSeg curr = segs.get(i);
            Vec2D n = curr.calcVector().getLeftNormal().getNormalized();

            Projection p1 = p.project(n);
            Projection p2 = c.project(n);
            float overlap1 = p1.max - p2.min;
            float overlap2 = p2.max - p1.min;

            float minOverlap = Math.min(overlap1, overlap2);
            if ( minOverlap <= 0) {
                return info;
            }

            if (minOverlap < info.depth) {
                info.depth = minOverlap;
                info.normal = n;
                if (overlap2 < overlap1) {
                    info.normal.reverse();
                }
            }
        }

        info.isCollision = true;
        return info;
    }



    public static CollisionInfo resolveCollision(Mesh m1, Mesh m2) {
        if (m1.getShape() == Mesh.Shape.ConvexPoly && m2.getShape() == Mesh.Shape.ConvexPoly) {
            return CollisionFunc.poly2poly((Poly)m1, (Poly)m2);
        }
        return new CollisionInfo();
    }

}
