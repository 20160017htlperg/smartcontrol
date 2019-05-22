package com.company.Komponenten;

import com.company.Komponenten.Relais.Relais;
import com.company.Komponenten.Sensoren.Sensor;
import com.company.Utils.Utils;
import io.javalin.Context;
import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Verwaltung {
    private Javalin app = null; //Server

    private CopyOnWriteArrayList<Sensor> SensorList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Relais> RelaisList = new CopyOnWriteArrayList<>();

    private String RelaisPath = "/relais/";
    private String SensorPath = "/sensoren/";

    public Verwaltung(int port) {
        app = Javalin.create().start(port);
        app.get(SensorPath,this::listSensor);

    }

    public boolean addToRelais(Relais r) {
        if(RelaisList.contains(r)) {
            return false;
        }


        /**TODO:
         * Add the missing function
         */
        if(RelaisList.add(r)) {
            app.get(RelaisPath+r.getName().toLowerCase()+"/",r::calledWithGet);
            
            return true;
        }


        return false;
    }

    public boolean addToSensor(Sensor s){
        if (SensorList.contains(s))
            return false;

        if(SensorList.add(s)){
            app.get(SensorPath+s.getName().toLowerCase()+"/",s::calledWithGet);
            app.get(SensorPath+s.getName().toLowerCase()+"/data/",s::getData);
            return true;
        }
        return false;
    }


    private void listSensor(Context context) {
        // TODO: Json mit allen Sensoren erstellen!
        Iterator<Sensor> sensorIterator = SensorList.iterator();
        ArrayList<HashMap<String, String>> tmp = new ArrayList<>();
        while(sensorIterator.hasNext()) {
            Sensor s = sensorIterator.next();
            HashMap<String, String> hash = new HashMap<>();
            hash.put("sensorname",s.getName());
            tmp.add(hash);
        }
        String result = Utils.callJSON(tmp);
        context.result(result);
    }

    private void listSchalter(Context context){
        context.result("");
    }

    private Javalin getApp(){
        return (app);
    }
}
