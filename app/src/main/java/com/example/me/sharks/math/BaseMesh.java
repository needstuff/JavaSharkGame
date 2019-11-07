package com.example.me.sharks.math;

import com.example.me.sharks.SpriteEntity;

/**
 * Created by me on 3/31/2016.
 */
public class BaseMesh {
    SpriteEntity parent = null;

    public void setParentEntity(SpriteEntity e) {
        this.parent = e;
    }

    public SpriteEntity getParentEntity() {
        return parent;
    }



}
