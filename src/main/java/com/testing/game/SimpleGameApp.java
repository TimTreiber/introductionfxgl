package com.testing.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.Map;

import com.almasb.fxgl.core.math.FXGLMath;
import static com.almasb.fxgl.dsl.FXGL.*;

import javafx.geometry.Rectangle2D;
import javafx.util.Duration;


public class SimpleGameApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setTitle("Mississippi");
    }

    @Override
    protected void initInput() {
        onKeyDown(KeyCode.F, () ->{
            getNotificationService().pushNotification("Hello World");
        });
    }

    private Entity player;

    @Override
    protected void initGame(){
        FXGL.getGameWorld().addEntityFactory(new SimpleFactory());
        FXGL.getGameWorld().addEntityFactory(new GameboardFactory());
        generateBackground(9,9);
        player = FXGL.spawn("playerBlack", 100, 100);
        movePlayer(player, 3);
        
        //FXGL.spawn("ally", 600, 100);
        /*FXGL.run(() -> {
            FXGL.spawn("ally", FXGLMath.randomPoint(
                new Rectangle2D(0,0,FXGL.getAppWidth(), FXGL.getAppHeight()))
            );
            FXGL.spawn("enemy", FXGLMath.randomPoint(
                new Rectangle2D(0,0,FXGL.getAppWidth(), FXGL.getAppHeight()))
            );
        }, Duration.seconds(1));*/
    }

    void movePlayer(Entity player, int fields){
        player.translateY(52*fields);
    }

    void generateBackground(int row, int col){
        /* hexagon size:
         * height = 52 px
         * width = 60 px
         * hexagon spacing/offset:
         * horiz = 45
         * vert = 52
         */
        int currentX = 100;
        int currentY = 100;
        int horizOffset = 46;
        int vertOffset = 52;
        if(row <= 0 || col <= 0) return;
        for(int i = 0; i < row; i++){
            if(i % 2 == 0) currentY = 100;
            else currentY = 100 + vertOffset/2;
            for(int j = 0; j < col; j++){
                if(i % 2 != 0 && j + 1 == col) continue;
                FXGL.spawn("background", currentX, currentY);
                currentY += vertOffset;
            }
            currentX += horizOffset;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}