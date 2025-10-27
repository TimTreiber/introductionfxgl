package com.testing.game;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

public class GameboardFactory implements  EntityFactory{

    @Spawns("background")
    public Entity newBackground(SpawnData data){
        return FXGL.entityBuilder(data)
                .view("Hexagon.png")
                .build();
    }
}