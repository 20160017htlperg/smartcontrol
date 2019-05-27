package com.company.Komponenten.Relais;

import io.javalin.Context;

public class FakeRelais extends Relais {

    public FakeRelais(String name, String location, int id) {
        super(name, location, id);
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