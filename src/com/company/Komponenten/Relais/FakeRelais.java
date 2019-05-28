package com.company.Komponenten.Relais;

import com.company.Utils.Rest_Services;
import io.javalin.Context;

import java.net.MalformedURLException;
import java.net.URL;

public class FakeRelais extends Relais {

    private String IP = "";

    public FakeRelais(String name, String location, int id,String IP) {
        super(name, location, id);
        this.IP = IP;
    }

    @Override
    public boolean getIsOn() throws Exception {
            if(Rest_Services.rest_GET(new URL("http://"+IP+"/?m=1")).trim().toUpperCase().contains("OFF")){
                return (false);
            }
            return (true);
    }

    @Override
    public void toggle(Context ctx) {
        try {
            Rest_Services.rest_GET(new URL("http://"+IP+"/?m=1&o=1"));
            ctx.result("true");
        }catch (Exception e) {
            ctx.result("null");
        }
    }

    @Override
    public void extendActiveTime(Context ctx, int time) {
        ctx.result("true");
    }


}