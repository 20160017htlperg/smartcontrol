package com.company.Komponenten.Relais;

import io.javalin.Context;

import java.net.MalformedURLException;


public abstract class Relais {
    private String name;
    private String location;
    private boolean favourite;
    private int relais_id;


    public Relais(String name, String location, int id) {
        this.name = name;
        this.location = location;
        this.relais_id = id;
        favourite = false;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void calledWithGet(Context ctx) {
        String res = null;
        try {
            res = "{ " +
                    "\"relaisname\" : \"" + getName() + "\", " +
                    "\"location\" : \"" + getLocation() + "\", " +
                    "\"relais_id\" : " + getId() + ", " +
                    "\"favourite\": " + favourite + ", " +
                    "\"ison\": " + getIsOn() + "}";
            ctx.result(res);
        } catch (Exception e) {
            ctx.result("null");
        }
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

    public void IsOn(Context ctx){
        try {
            if(getIsOn()){
                ctx.result("true");
            }else{
                ctx.result("false");
            }
        } catch (Exception e) {
            ctx.result("null");
        }
    }

    public abstract boolean getIsOn() throws Exception;
    public abstract void toggle(Context ctx);
    public abstract void extendActiveTime(Context ctx,int time);
}


