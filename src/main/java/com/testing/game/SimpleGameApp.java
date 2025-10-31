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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import com.almasb.fxgl.core.math.FXGLMath;
import static com.almasb.fxgl.dsl.FXGL.*;

import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;


public class SimpleGameApp extends GameApplication {

    public ActivePlayer currentPlayer;
    public Entity[][] gameField = new Entity[9][9];

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setTitle("Mississippi");
    }

    @Override
    protected void initInput() {
        onKeyDown(KeyCode.F, () ->{
            useInput("getFuel", currentPlayer);
        });
        onKeyDown(KeyCode.A, () ->{
            useInput("addFuel", currentPlayer);
        });
        onKeyDown(KeyCode.M, () ->{
            useInput("move", currentPlayer);
        });
    }

    public void useInput(String mode, ActivePlayer player){
        switch (mode)
        {
            case "getFuel":
                getNotificationService().pushNotification(String.format("Current Fuel: %d", player.getFuel()));
                break;
            case "addFuel":
                player.addFuel(20);
                getNotificationService().pushNotification("Added 20 Fuel!");
                break;
            case "move":
                getDialogService().showInputBox("This is an input box. You can type stuff...", answer -> {
                movePlayer(player, Integer.parseInt(answer));
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void initGame(){
        FXGL.getGameWorld().addEntityFactory(new SimpleFactory());
        FXGL.getGameWorld().addEntityFactory(new GameboardFactory());
        generateBackground(9,9);
        Entity player = FXGL.spawn("playerBlack", 100, 100);
        ActivePlayer p1 = new ActivePlayer(player);
        gameField[0][0] = p1.getPlayer();
        currentPlayer = p1;
        /*FXGL.run(() -> {
            FXGL.spawn("ally", FXGLMath.randomPoint(
                new Rectangle2D(0,0,FXGL.getAppWidth(), FXGL.getAppHeight()))
            );
            FXGL.spawn("enemy", FXGLMath.randomPoint(
                new Rectangle2D(0,0,FXGL.getAppWidth(), FXGL.getAppHeight()))
            );
        }, Duration.seconds(1));*/
    }

    void movePlayer(ActivePlayer player, int fields){
        if(player.useFuel(fields)){
            for(int i = 0; i < fields; i++){
                simpleMovement(player);
            }
        }
        else getNotificationService().pushNotification("Not enough fuel!");
    }

    void simpleMovement(ActivePlayer player){
        List<Integer> list = nextField(0,0);
        player.getPlayer().translateY(52);
    }

    private List<Integer> nextField(int x, int y){
        List<Integer> list = new ArrayList<>();
        if(x % 2 == 0 && y < 8){
            list.add(x);
            list.add(y+1);
        }
        else if(x % 2 == 0 && y == 8){
            list.add(x+1);
            list.add(y-1);
        }
        else if(x % 2 != 0 && y > 0){
            list.add(x);
            list.add(y-1);
        }
        else if(x % 2 != 0 && y == 0){
            list.add(x+1);
            list.add(y);
        }
        return list;
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
                if(j + 1 == col && i % 2 != 0) continue;
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