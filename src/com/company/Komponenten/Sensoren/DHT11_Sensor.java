package com.company.Komponenten.Sensoren;

public class DHT11_Sensor extends Sensor {
    public DHT11_Sensor(String name, int sensor_id, String unit, long interval, String location, boolean favourite) {
        super(name, sensor_id, unit, interval, location, favourite);
    }

    @Override
    protected float messung() throws Exception {
        return 0;
    }
}
