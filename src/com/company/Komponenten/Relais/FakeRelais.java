package com.company.Komponenten.Relais;

import io.javalin.Context;

public class FakeRelais extends Relais {

    private String IP = "";

    public FakeRelais(String name, String location, int id,String IP) {
        super(name, location, id);
        this.IP = IP;
    }

    @Override
    public boolean getIsOn() {
        return false;
    }

    @Override
    public void toggle(Context ctx) {
        ctx.result("true");
    }

    @Override
    public void extendActiveTime(Context ctx, int time) {
        ctx.result("true");
    }


}