package com.testing.game;
import java.util.ArrayList;
import java.util.List;

import com.almasb.fxgl.entity.Entity;

public class ActivePlayer {

    private Entity player;
    private int x = 0;
    private int y = 0;
    private int fuel = 15;

    public ActivePlayer(Entity player){
        this.player = player;
    }

    public List<Integer> getCoords(){
        List<Integer> list = new ArrayList<>();
        list.add(x);
        list.add(y);
        return list;
    }

    public boolean useFuel(int amount){
        if(amount <= fuel){
            fuel -= amount;
            return true;
        }
        else return false;
    }

    public void addFuel(int amount){
        fuel += amount;
    }

    public int getFuel(){
        return fuel;
    }

    public Entity getPlayer(){
        return player;
    }
}
