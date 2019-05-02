package com.company;

import com.company.Datenbank.Database;
import com.company.Komponenten.Setup;



public class Main {

    public static void main(String[] args) {
        Setup one = new Setup(null, 1234);
        //System.out.println("Entered Main");
        //http://localhost:1234/sensoren/gucci/data?from=2018-03-27%2018:14:25&to=2019-04-30%2013:21:52&display=50
        String s = Database.readSensorValuesHourly(50, "2016-09-21 23:00:00", "2016-09-27 03:30:00");
        System.out.println(s);
        String s0 = Database.readSensorValuesDaily(50, "2018-05-29 05:00:00", "2018-06-29 05:00:00");
        System.out.println(s0);
        String s1 = Database.readSensorValuesWeekly(50, "2017-05-29 05:00:00", "2019-03-27 23:28:05");
        System.out.println(s1);
        String s2 = Database.readSensorValuesMonthly(50, "2017-05-29 05:00:00", "2019-03-27 23:28:05");
        System.out.println(s2);
        String s3 = Database.readSensorValuesYearly(50, "2016-03-17 16:00:00", "2019-03-27 23:28:05");
        System.out.println(s3);


    }
}
