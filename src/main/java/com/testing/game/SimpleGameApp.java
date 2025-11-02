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

    /*  ToDo
     *  add more fixed players
     *  test skipping players
     *  add player queue
     *  add input for every player, cycle through players
     *  daily streak update 2
     */

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
                tryMove(player, answer);
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
        /*Entity player2 = FXGL.spawn("playerBlack", 100, 152);
        ActivePlayer p2 = new ActivePlayer(player2);
        gameField[0][1] = p2.getPlayer();*/

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

    void tryMove(ActivePlayer player, String x){
        int fields = 0;
        try{
            fields = Integer.parseInt(x);
        }
        catch(NumberFormatException e){
            getNotificationService().pushNotification("Not a valid integer!");
        }
        movePlayer(player, fields);
    }

    void movePlayer(ActivePlayer player, int fields){
        if(fields < 0){
            for(int i = 0; i > fields; i--){
                if(player.getCoords().get(0) == 0 && player.getCoords().get(1) == 0){
                    getNotificationService().pushNotification("Cannot move back further!");
                    break;
                }
                simpleMovement(player, false);
                player.addFuel(1);
            }
        }
        else if(player.useFuel(fields)){
            for(int i = 0; i < fields; i++){
                simpleMovement(player, true);
                if(player.getCoords().get(0) == 8 && player.getCoords().get(1) == 8){
                    getNotificationService().pushNotification("GG, you won!!");
                    break;
                }
            }
        }
        else getNotificationService().pushNotification("Not enough fuel!");
    }

    void simpleMovement(ActivePlayer player, boolean forwards){
        List<Integer> goal = player.getCoords();
        int playerX = player.getCoords().get(0);
        int playerY = player.getCoords().get(1);
        gameField[playerX][playerY] = null;
        boolean skip;
        int x = 0;
        int y = 0;
        do
        {
            skip = false;
            goal = nextField(goal, forwards);
            x = goal.get(0);
            y = goal.get(1);
            if(gameField[x][y] != null) skip = true;
        }
        while(skip);
        gameField[x][y] = player.getPlayer();
        player.setCoords(goal);
        if(x - playerX == 0 || x - playerX % 2 == 0){
            player.getPlayer().translateX(46 * (x - playerX));
            player.getPlayer().translateY(52 * (y - playerY));
        }
        else if(x % 2 == 0 && x - playerX % 2 != 0){
            player.getPlayer().translateX(46 * (x - playerX));
            player.getPlayer().translateY(-26 + 52 * (y - playerY));
        }
        else{
            player.getPlayer().translateX(46 * (x - playerX));
            player.getPlayer().translateY(26 + 52 * (y - playerY));
        }
    }

    private List<Integer> nextField(List<Integer> list, boolean forwards){
        int x = list.get(0);
        int y = list.get(1);
        List<Integer> nextField = new ArrayList<>();
        if(x >= 8 && y >= 8 && forwards){
            nextField.add(8);
            nextField.add(8);
        }
        else if(x <= 0 && y <= 0 && !forwards){
            nextField.add(0);
            nextField.add(0);
        }
        if(forwards){
            if(x % 2 == 0 && y < 8){
                nextField.add(x);
                nextField.add(y+1);
            }
            else if(x % 2 == 0 && y == 8){
                nextField.add(x+1);
                nextField.add(y-1);
            }
            else if(x % 2 != 0 && y > 0){
                nextField.add(x);
                nextField.add(y-1);
            }
            else if(x % 2 != 0 && y == 0){
                nextField.add(x+1);
                nextField.add(y);
            }
        }
        else if (!forwards){
            if(x % 2 == 0 && y > 0){
                nextField.add(x);
                nextField.add(y-1);
            }
            else if(x % 2 == 0 && y == 0){
                nextField.add(x-1);
                nextField.add(y);
            }
            else if(x % 2 != 0 && y < 7){
                nextField.add(x);
                nextField.add(y+1);
            }
            else if(x % 2 != 0 && y == 7){
                nextField.add(x-1);
                nextField.add(y+1);
            }
        }
        return nextField;
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