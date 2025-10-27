package com.testing.game;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SimpleFactory implements  EntityFactory{

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data){
        return FXGL.entityBuilder(data)
                //.view(new Rectangle(40, 40, Color.RED))
                .view("Schwarze Spielfigur klein.png")
                .with(new ProjectileComponent(new Point2D(1,0), 00))
                .build();
    }

    @Spawns("ally")
    public Entity newAlly(SpawnData data){
        var texture = FXGL.texture("brick.jpg").multiplyColor(Color.GREEN);

        return FXGL.entityBuilder(data)
                .view(texture)
                .with(new ProjectileComponent(new Point2D(-1,0), 100))
                .build();
    }
}
