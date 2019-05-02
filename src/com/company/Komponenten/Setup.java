package com.company.Komponenten;

import com.company.Komponenten.Sensoren.FakeSensor;
import com.company.Komponenten.Sensoren.Sensor;

import java.io.File;
import java.util.ArrayList;

public class Setup {

    private Verwaltung vsys = null;

    public Setup(File configDat,int port){

        vsys = new Verwaltung(port);
        initSensors();
    }

    private void initSensors(){
        ArrayList<Sensor> sensorAr = new ArrayList<>();

        sensorAr.addAll(FakeSensor.initSensor());

        for(Sensor sensor : sensorAr) {
            vsys.addToSensor(sensor);
        }
    }
}
