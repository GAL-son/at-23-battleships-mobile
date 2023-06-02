package com.battleships.server.api.model;

public class Ship
{
    public int size;
    public int hp;
    public boolean isSunk;

    Ship(int size) {
        this.size = size;
        this.hp = size;
        this.isSunk = false;
    }

    Boolean hit()
    {
        hp--;
        return hp <= 0;
    }
}