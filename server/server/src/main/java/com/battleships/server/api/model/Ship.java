package com.battleships.server.api.model;

public class Ship
{
    public int size;
    public int hp;
    public boolean sunk;

    public Ship(int size) {
        this.size = size;
        this.hp = size;
        this.sunk = false;
    }

    public void hit()
    {
        hp--;
        if(hp <= 0) {
            sunk = true;
        }
    }

    public boolean isSunk() {
        return sunk;
    }
}