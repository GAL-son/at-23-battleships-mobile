package com.battleships.model.client.ship;

public class Ship {
    private int size;
    private int health;
    private String type;//??


    public Ship(int size, int health) {
        this.size = size;
        this.health = health;
        this.type = resolveType(size);
    }

    private String resolveType(int size) {
        String name;
        switch (size) {
            case 1:
                name = "maluch";
                break;
            case 2:
                name = "korweta";
                break;
            case 3:
                name = "fregata";
                break;
            case 4:
                name = "lotniskowiec";
                break;
            default:
                name = "WhatTheHeeel";
        }
        return name;
    }

    public void hitShip() {
        this.health--;
        //
    }

    private boolean isSunk()
    {
        if(this.health==0)
        {
            return true;
        }
        else
        {
            return  false;

        }
    }

    public int getSize() {
        return size;
    }

    public int getHealth() {
        return health;
    }

    public String getType() {
        return type;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setType(String type) {
        this.type = type;
    }

}
