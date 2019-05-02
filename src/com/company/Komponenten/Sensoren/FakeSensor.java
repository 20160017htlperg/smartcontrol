package com.company.Komponenten.Sensoren;

import com.company.Datenbank.Database;
import io.javalin.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FakeSensor extends Sensor {
    private final static String sensorClassName = "FakeSensor";

    public FakeSensor(String name, int sensor_id, String unit, long interval, String location, boolean favourite) {
        super(name, sensor_id, unit, interval, location, favourite);
    }

    public void calledWithGet(Context ctx) {
        //TODO BRUH MOMENT

        //"sensorname" : "Sensor1",
        //"unit" : "cm²"
        //"table" : "tabelle1"
        //"interval" : 123987192873

        long entries = Database.getAmountOfEntries(sensor_id);

        String result = String.format("{" +
                "\"%s\" : \"%s\", " +
                "\"%s\" : \"%s\", " +
                "\"%s\" : \"%s\", " +
                "\"%s\" : %d ," +
                "\"%s\" : %d ," +
                "\"%s\" : %b , "+
                "\"%s\" : %d "+
                "}","sensorname",name,"unit", unit, "location" , location, "sensor_id", sensor_id, "interval", interval, "favourite", favourite, "entries", entries);
        ctx.result(result);
    }

    public static Collection<Sensor> initSensor(){
        ArrayList<Sensor> sensors = new ArrayList<>();
        HashMap[] sensorMap = Database.readSensorData(sensorClassName);


        for(HashMap<String, String> sensor : sensorMap){
            String name = sensor.get("name");
            int sensor_id = Integer.parseInt(sensor.get("sensor_id"));
            String unit = sensor.get("unit");
            long interval = Long.parseLong(sensor.get("interval"));
            String location = sensor.get("location");
            boolean favourite = Boolean.parseBoolean(sensor.get("favourite"));

            sensors.add(new FakeSensor(name, sensor_id, unit, interval, location, favourite));
        }

        return sensors;
    }

    @Override
    public float messung() {
        return ((((float) (Math.random() *100))+(float) (Math.random() *100))/2);
    }
}
