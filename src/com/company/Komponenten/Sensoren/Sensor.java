package com.company.Komponenten.Sensoren;

import com.company.Datenbank.Database;
import com.company.Exception.TimeException;
import com.company.Tasks.Task;
import com.company.Tasks.TaskHandler;
import io.javalin.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class Sensor {
    protected String name = "";             //Name des Sensors für die Darstellung auf der Website. Der Name dient dabei als Primärschlüssl d.h. dieser muss eindeutig sein!
    protected int sensor_id = 0;            //Sensortyp des Tabellenblattes.
    protected long interval = 0;            //Gibt den Intervall an, in dem die Messungen durchgeführt weden sollen. (Abstand zwischen den Messungen)
    protected String unit = "";             //z.B. cm mm km liter ...
    protected String location = "";         //Ort des Sensors
    protected boolean favourite = false;
    protected TaskHandler handler = new TaskHandler();

    public Sensor(String name, int sensor_id, String unit, long interval, String location, boolean favourite){
        this.name = name;
        this.sensor_id = sensor_id;
        this.unit = unit;
        this.interval = interval;
        this.location = location;
        this.favourite = favourite;
        createTask();
        handler.start();
    }

    protected abstract float messung() throws Exception;

    public boolean writeToDB(Void v) {
        try {
            Database.writeInMeasurements(sensor_id, messung());
            createTask();
            return (true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

            return (false);
        }
    }

    public Task createTask() {
        Task t = new Task((System.currentTimeMillis() + interval),this::writeToDB);
        handler.addTask(t);
        return (t);
    }

    @Override
    public boolean equals (Object obj) {
        if((obj == null) || !(obj instanceof Sensor))
            return (false);
        return (((Sensor) obj).getName().equals(this.getName()));
    }

     /** Getter and Setter */
    public String getName() {
        return this.name;
    }

    public void calledWithGet(Context ctx) {


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

    //ctx.result(Database.readSensorValuesSingle(sensor_id, 100)); amount --> letzte 100 Einträge des Typs sensor_id

    public void getData(Context ctx) {
        /**
         *
         */

        String fromTime = ctx.queryParam("from");
        System.out.println("From Time: " + fromTime);
        String toTime = ctx.queryParam("to");
        System.out.println("To Time: " + toTime);
        String display = ctx.queryParam("display");
        System.out.println("Displaying: "+display);

        if(fromTime != null && toTime != null && display != null) {
            displayDataAndAmount(ctx, fromTime, toTime, display);
        }else
        if(fromTime != null && toTime != null) {
            displayDataTime(ctx, fromTime, toTime);
        }else
        if(display != null) {
            displayDataAmount(ctx, display);
        }else {
            ctx.result(Database.readSensorValuesSingle(sensor_id,1000));
        }
    }

    private void displayDataAndAmount(Context ctx, String fromTime, String toTime, String displayNr) {
        Date fTime = null;
        Date tTime = null;
        String json = "";
        int disNr = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
            fTime = sdf.parse(fromTime);
            tTime = sdf.parse(toTime);
            disNr = Integer.parseInt(displayNr);



        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
        }

        int entries = Database.getAmountOfEntries(sensor_id, sdf.format(fTime), sdf.format(tTime));
        long difference = tTime.getTime() - fTime.getTime();


        if(entries  <= disNr) {
            json = Database.readSensorValuesSingle(sensor_id, sdf.format(fTime), sdf.format(tTime));
        }else
        if((difference/1000/3600) <= disNr) {
            json = Database.readSensorValuesHourly(sensor_id, sdf.format(fTime), sdf.format(tTime));
        }else
        if((difference/1000/3600/24) <= disNr) {
            json = Database.readSensorValuesDaily(sensor_id, sdf.format(fTime), sdf.format(tTime));
        }else
        if((difference/1000/3600/24/7) <= disNr) {
            json = Database.readSensorValuesWeekly(sensor_id, sdf.format(fTime), sdf.format(tTime));
        }else
        if((difference/1000/3600/24/7/4.345f) <= disNr) {
            json = Database.readSensorValuesMonthly(sensor_id, sdf.format(fTime), sdf.format(tTime));
        }else
        if((difference/1000/3600/24/7/4.345f/12) <= disNr) {
            json = Database.readSensorValuesYearly(sensor_id, sdf.format(fTime), sdf.format(tTime));
        }


        /**
         * Warning: When changing the data format in the database you NEED to change the data format here too or the
         * sensor will not be find the date.
         */

        System.out.println("Gferlich: " + Database.getAmountOfEntries(sensor_id, sdf.format(fTime), sdf.format(tTime)));
        System.out.println(json);

        ctx.result(json);
    }

    private void displayDataTime(Context ctx, String fromTime, String toTime) {
        long fTime = 0;
        long tTime = 0;

        try {
            fTime = Long.parseLong(fromTime);
            tTime = Long.parseLong(toTime);

            if (fTime >= tTime) {
                throw new TimeException("From time is higher than to time!\nError happened in Sensor.java - getData()");
            }
            if (fTime < 0 || tTime < 0) {
                throw new TimeException("fromTime or toTime is lower than zero!\nError happened in Sensor.java - getData()");
            }
            //TODO
            //ctx.result(Database.readSensorValuesSingle(sensor_id, fTime, tTime));
            return;
        } catch (TimeException TE) {
            System.out.println("No usage of Parameters or Wrong usage of Parameters.\n" +
                    "Displaying the last 1000 database entries.\nDetailed Error message:\n" + TE.getMessage());
        } catch (NumberFormatException NFE) {
            System.out.println("Couldn't parse the Strings fromTime and toTime.\n" +
                    "Displaying the last 1000 database entries instead.");
        } catch (Exception E) {
            ctx.result(Database.readSensorValuesSingle(sensor_id, 1000));
        }

    }

    private void displayDataAmount(Context ctx, String amount) {
        try{
            int amnt = Integer.parseInt(amount);
            ctx.result(Database.readSensorValuesSingle(sensor_id, amnt));
            return;
        }catch(NumberFormatException NFE) {
            System.out.println("Couldn't parse the input String.\nDisplaying the last 1000 database entries instead.");
        }
        ctx.result(Database.readSensorValuesSingle(sensor_id, 1000));
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
