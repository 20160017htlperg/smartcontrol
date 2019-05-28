package com.company.Komponenten;

import com.company.Komponenten.Relais.FakeRelais;
import com.company.Komponenten.Sensoren.DHT11_Sensor;
import com.company.Komponenten.Sensoren.FakeSensor;
import com.company.Komponenten.Sensoren.Sensor;

import java.io.File;
import java.util.ArrayList;

public class Setup {

    private Verwaltung vsys = null;

    public Setup(File configDat,int port){

        vsys = new Verwaltung(port);
        //initSensors();
        initRelais();
        //vsys.addToSensor(new DHT11_Sensor("DHT11_Sensor",2,"celsius",10000000,"Garage",true));
    }

    private void initSensors(){
        ArrayList<Sensor> sensorAr = new ArrayList<>();

        sensorAr.addAll(FakeSensor.initSensor());

        for(Sensor sensor : sensorAr) {
            vsys.addToSensor(sensor);
        }
    }

    private void initRelais(){
        vsys.addToRelais(new FakeRelais("buchi","gucci",5,"192.168.137.2"));
    }
}
