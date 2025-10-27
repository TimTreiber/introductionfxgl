package com.testing.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import static com.almasb.fxgl.dsl.FXGL.*;
import com.almasb.fxgl.dsl.FXGL;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class SimpleGameApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1094);
        settings.setHeight(825);
        settings.setTitle("Mississippi");
    }

    @Override
    protected void initInput() {
        onKeyDown(KeyCode.F, () ->{
            getNotificationService().pushNotification("Hello World");
        });
    }

    @Override
    protected void initGame(){
        FXGL.getGameWorld().addEntityFactory(new SimpleFactory());
        FXGL.getGameWorld().addEntityFactory(new GameboardFactory());
        FXGL.spawn("background", 10, 10);
        //FXGL.spawn("enemy", 100, 100);
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

    public static void main(String[] args) {
        launch(args);
    }
}