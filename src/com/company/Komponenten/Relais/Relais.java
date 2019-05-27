package com.company.Komponenten.Relais;

import io.javalin.Context;


public abstract class Relais {
    private String name;
    private String location;
    private boolean favourite;
    private int relais_id;
    private boolean isOn;


    public Relais(String name, String location, int id) {
        this.name = name;
        this.location = location;
        this.relais_id = id;
        favourite = false;
        isOn = false;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void calledWithGet(Context ctx) {
        String res = "{ " +
                "relaisname : " + getName() + ", " +
                "location : " + getLocation() + ", " +
                "relais_id : " + getId() + ", " +
                "favourite: " + favourite + ", " +
                "ison: " + isOn + "}";
        ctx.result(res);
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return relais_id;
    }

    public void setOn() {
        isOn = true;
    }

    public void setOnFor(int s) {
        //after s seconds turn the relais off
    }

    public void setOnFromTo() {

    }
}
