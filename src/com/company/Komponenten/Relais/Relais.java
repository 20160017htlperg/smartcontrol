package com.company.Komponenten.Relais;

public abstract class Relais {
    private String name;
    private String location;
    private boolean favourite;
    private int relais_id;
    private boolean isOn;


    public Relais(String n, String l, int id) {
        this.name = n;
        this.location = l;
        this.relais_id = id;
        favourite = false;
        isOn = false;
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
        //after s seconds turn the relai off
    }

    public void setOnFromTo() {

    }



}
