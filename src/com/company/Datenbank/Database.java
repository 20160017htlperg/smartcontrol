/* (C) Eduard Bogdan, Christoph Fürholzer */
package com.company.Datenbank;

import java.sql.*;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.company.Utils.*;

public class Database {
    //the databases used for this project and which should be read from.
    //Notice: the first table is the one that the values are saved in and the second one is the configuration database
    //where the Sensors will be initialized from for the Java Program.

    /**     +--------------------+---------------------------------------------------+
     *      | name               | what should stand in it                           |
     *      +--------------------+---------------------------------------------------+
     *      | table_measurements | the name of the table where the measurements lie. |
     *      | table_sensors      | the name of the table where the sensor data lies. |
     *      +--------------------+---------------------------------------------------+
     */
    private static String table_measurements = "measurements";
    private static String table_sensors = "sensors";


    /** #     | variable         | what should stand in it
     * -------+------------------+----------------------------------------------------------------------------------------
     *  0     | s_measurement_id | The name of the ID Measurements
     *  1     | s_sensor_id      | The name of the ID of a specific sensor
     *  2     | s_timestamp      | The name for timestamps
     *  3     | s_value          | The database row that defines what s_value the measurement has.
     *        |                  | The database row that defines what s_value a sensor has (!!!)
     *  4     | s_quality_code   | The definition if the measurement taken was a good one or a bad one
     *  5     | s_attribute      | The database row that defines what s_attribute the configuration database holds
     *  6     | s_favourite      | (Only for config database) Defines if the sensor is a favorite for the user
     */
    private static String s_measurement_id = "measurement_id";
    private static String s_sensor_id = "sensor_id";
    private static String s_timestamp = "timestamp";
    private static String s_value = "value";
    private static String s_quality_code = "quality_code";
    private static String s_attribute = "attribute";
    private static String s_favourite = "favourite";

    /**
     * The format that the database is written to.
     */
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Data to connect to the Database
     */
    private static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String DB_URL = "jdbc:mysql://smartcontrol.techniknews.biz/smartcontrol_project?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static String USER = "smart";
    private static String PASS = "grot1234";

    /********************************************
     *  __          _______  _____ _______ ______ *
     *  \ \        / /  __ \|_   _|__   __|  ____|*
     *   \ \  /\  / /| |__) | | |    | |  | |__   *
     *    \ \/  \/ / |  _  /  | |    | |  |  __|  *
     *     \  /\  /  | | \ \ _| |_   | |  | |____ *
     *      \/  \/   |_|  \_\_____|  |_|  |______|*
     *********************************************/

    /**
     *
     * @param  command                  The command that this method will execute.
     * @throws ClassNotFoundException   If the Class could not be found an exception will be returned
     * @throws SQLException             If the command given is invalid an SQLException will be thrown.
     */
    private static void insertData(String command) throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();

        stmt.executeUpdate(command);

        stmt.close();
        conn.close();
        // return (output.toString());
    }

    /**
     * @return  Returns the current timestamp in the format, which is specified in "sdf"
     */
    public static String timeStamp() {
        Date date = new Date();
        long time = date.getTime();
        return sdf.format(time);
    }

    /**
     * @param sensor_id the id of the sensor that this measurement belongs to
     * @param value     the value that the measurement has
     */
    public static void writeInMeasurements(int sensor_id, float value) {
        //insert into tableName (s_sensor_id, s_timestamp, s_value, s_quality_code) values (0, '26.01.2001', 37.6, 0)
        String command = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (%d, '%s', " + value + ", %d)",
                table_measurements, s_sensor_id, s_timestamp, s_value, s_quality_code,
                sensor_id, timeStamp(), 0);

        System.out.println(command);
        try {
            insertData(command);
        } catch (Exception e) {
            System.out.println("Error in Database.java - writeInMeasurements()");
            e.printStackTrace();
        }
    }

    /***********************************
     *  |  __ \|  ____|   /\   |  __ \  *
     *  | |__) | |__     /  \  | |  | | *
     *  |  _  /|  __|   / /\ \ | |  | | *
     *  | | \ \| |____ / ____ \| |__| | *
     *  |_|  \_\______/_/    \_\_____/  *
     ***********************************/

    /**
     * /**
     *      *   __  __                                 _
     *      *  |  \/  | ___  ___ _____      _____ _ __| |_ ___
     *      *  | |\/| |/ _ \/ __/ __\ \ /\ / / _ \ '__| __/ _ \
     *      *  | |  | |  __/\__ \__ \\ V  V /  __/ |  | ||  __/
     *      *  |_|  |_|\___||___/___/ \_/\_/ \___|_|   \__\___|
     *      *
     *      */


    public static int getAmountOfEntries(int sensor_id) {
        return getAmountOfEntries(sensor_id, "1970-01-01 00:00:00", "3000-12-31 23:59:59");
    }


    public static int getAmountOfEntries(int sensor_id, String timestamp_start, String timestamp_end) {
        int result = 0;
        try{
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            //select count(*) from measurements where sensor_id = 0
            ResultSet rs = stmt.executeQuery(String.format("SELECT count(*) FROM %s WHERE %s = %d AND %s between '%s' and '%s'",
                    table_measurements, s_sensor_id, sensor_id, s_timestamp, timestamp_start, timestamp_end));

            rs.next();

            result = rs.getInt("count(*)");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * @param sensor_id         the sensor_id the data should be read from
     * @param timestamp_start   the timestamp to start looking after the sensor data
     * @param timestamp_end     the timestamp to stop looking after the sensor data
     * @return                  returns a string that contains all the database entries for a specific sensor between
     *                          the two timestamps.
     */
    public static String readSensorValuesSingle(int sensor_id, String timestamp_start, String timestamp_end) {
        String data = "";

        //convert timestamp_start to date
        Date date_start = new Date();
        Date date_end = new Date();

        try{
            date_start = sdf.parse(timestamp_start);
            System.out.println(date_start);
            date_end = sdf.parse(timestamp_end);
            System.out.println(date_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            //select * from measurements where s_sensor_id = 0 and s_timestamp > 2345678 and s_timestamp < 3456789
            ResultSet output = stmt.executeQuery(String.format("select * from %s where sensor_id = %d and timestamp between '%s' and '%s'", table_measurements,sensor_id, sdf.format(date_start), sdf.format(date_end)));
            Container<Value> values = new JsonDataContainer<>();

            while (output.next()) {
                Value<Integer> v1 = new Value(output.getInt(s_measurement_id), "measurement_id");
                Value<String> v2 =  new Value(output.getString(s_timestamp), "timestamp");
                Value<Float> v3 =   new Value(output.getFloat(s_value), "value");
                Value<Integer> v4 = new Value(output.getInt(s_quality_code), "quality_code");

                values.add(v1);
                values.add(v2);
                values.add(v3);
                values.add(v4);
                values.wrapTogether();

            }

            data = values.toString();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (data);
    }



    /**
     * @param sensor_id         the sensor_id the data should be read from
     * @param timestamp_start   the timestamp to start looking after the sensor data
     * @param timestamp_end     the timestamp to stop looking after the sensor data
     * @return                  returns a string that contains all the database entries for a specific sensor between
     *                          the two timestamps.
     */
    public static String readSensorValuesHourly(int sensor_id, String timestamp_start, String timestamp_end) {
        String data = "";

        //convert timestamp_start to date
        Date date_start = new Date();
        Date date_end = new Date();

        try{
            date_start = sdf.parse(timestamp_start);
            date_end = sdf.parse(timestamp_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            //select * from measurements where s_sensor_id = 0 and s_timestamp > 2345678 and s_timestamp < 3456789
            //SELECT avg(value),extract(hour from timestamp),convert(timestamp,date) FROM measurements WHERE sensor_id = 0 AND timestamp between '2017-03-25 19:39:40' and '2020-03-25 21:39:52' GROUP BY extract(hour from timestamp),convert(timestamp,date);

            ResultSet output = stmt.executeQuery(
                    String.format("select avg(value) as value, extract(hour from timestamp) as hour, convert(timestamp,date) as date FROM measurements WHERE sensor_id = %d AND timestamp between '%s' and '%s' GROUP BY convert(timestamp,date),extract(hour from timestamp)",sensor_id,sdf.format(date_start), sdf.format(date_end)));
            Container<Value> values = new JsonDataContainer<>();

            while (output.next()) {
                Value<Integer>  v1 =   new Value(output.getInt("hour"), "hour");
                Value<Float>    v2 =   new Value(output.getFloat("value"), "value");
                Value<Integer>  v3 =   new Value(output.getString("date"), "date");


                values.add(v1);
                values.add(v2);
                values.add(v3);
                values.wrapTogether();
            }

            data = values.toString();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (data);
    }

    /**
     * @param sensor_id         the sensor_id the data should be read from
     * @param timestamp_start   the timestamp to start looking after the sensor data
     * @param timestamp_end     the timestamp to stop looking after the sensor data
     * @return                  returns a string that contains all the database entries for a specific sensor between
     *                          the two timestamps.
     */
    public static String readSensorValuesDaily(int sensor_id, String timestamp_start, String timestamp_end) {
        String data = "";

        //convert timestamp_start to date
        Date date_start = new Date();
        Date date_end = new Date();

        try{
            date_start = sdf.parse(timestamp_start);
            date_end = sdf.parse(timestamp_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            //select * from measurements where s_sensor_id = 0 and s_timestamp > 2345678 and s_timestamp < 3456789
            //SELECT avg(value),convert(timestamp,date) FROM %s WHERE sensor_id = %d AND timestamp between '%s' and '%s' GROUP BY convert(timestamp,date);
            ResultSet output = stmt.executeQuery(String.format("SELECT avg(value) as value,convert(timestamp,date) as date FROM %s WHERE sensor_id = %d AND timestamp between '%s' and '%s' GROUP BY convert(timestamp,date);", table_measurements,sensor_id, sdf.format(date_start), sdf.format(date_end)));
            Container<Value> values = new JsonDataContainer<>();

            while (output.next()) {
                Value<Float> v1 = new Value(output.getFloat("value"), "value");
                Value<String> v2 = new Value(output.getString("date"), "tag");

                values.add(v1);
                values.add(v2);

                values.wrapTogether();
            }

            data = values.toString();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (data);
    }

    /**
     * @param sensor_id         the sensor_id the data should be read from
     * @param timestamp_start   the timestamp to start looking after the sensor data
     * @param timestamp_end     the timestamp to stop looking after the sensor data
     * @return                  returns a string that contains all the database entries for a specific sensor between
     *                          the two timestamps.
     */
    public static String readSensorValuesWeekly(int sensor_id, String timestamp_start, String timestamp_end) {
        String data = "";

        //convert timestamp_start to date
        Date date_start = new Date();
        Date date_end = new Date();

        try{
            date_start = sdf.parse(timestamp_start);
            date_end = sdf.parse(timestamp_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            //select * from measurements where s_sensor_id = 0 and s_timestamp > 2345678 and s_timestamp < 3456789
            //;
            ResultSet output = stmt.executeQuery(String.format("select avg(value) as value,extract(year from timestamp) as year, extract(week from timestamp) as week from %s WHERE sensor_id = %d AND timestamp between '%s' and '%s' GROUP BY extract(year from timestamp), extract(week from timestamp)", table_measurements,sensor_id, sdf.format(date_start), sdf.format(date_end)));
            Container<Value> values = new JsonDataContainer<>();

            while (output.next()) {
                Value<Float> v1 = new Value(output.getInt("value"), "value");
                Value<Float> v2 = new Value(output.getInt("year"), "year");
                Value<Float> v3 = new Value(output.getInt("week"), "week");

                values.add(v1);
                values.add(v2);
                values.add(v3);

                values.wrapTogether();
            }

            data = values.toString();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (data);
    }

    /**
     * @param sensor_id         the sensor_id the data should be read from
     * @param timestamp_start   the timestamp to start looking after the sensor data
     * @param timestamp_end     the timestamp to stop looking after the sensor data
     * @return                  returns a string that contains all the database entries for a specific sensor between
     *                          the two timestamps.
     */
    public static String readSensorValuesMonthly(int sensor_id, String timestamp_start, String timestamp_end) {
        String data = "";

        //convert timestamp_start to date
        Date date_start = new Date();
        Date date_end = new Date();

        try{
            date_start = sdf.parse(timestamp_start);
            date_end = sdf.parse(timestamp_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            //select * from measurements where s_sensor_id = 0 and s_timestamp > 2345678 and s_timestamp < 3456789
            //SELECT avg(value),extract(year from timestamp), extract(month from timestamp) FROM measurements WHERE sensor_id = %d AND timestamp between '%s' and '%s' GROUP BY extract(year from timestamp), extract(month from timestamp);
            ResultSet output = stmt.executeQuery(String.format("SELECT avg(value) as value,extract(year from timestamp) as year, extract(month from timestamp) as month FROM %s WHERE sensor_id = %d AND timestamp between '%s' and '%s' GROUP BY extract(year from timestamp), extract(month from timestamp)", table_measurements, sensor_id, sdf.format(date_start), sdf.format(date_end)));
            Container<Value> values = new JsonDataContainer<>();
            while (output.next()) {
                Value<Float> v1 =   new Value(output.getFloat("value"), "value");
                Value<String> v2 =  new Value(output.getString("year"), "year");
                Value<String> v3 =  new Value(output.getString("month"), "month");

                values.add(v1);
                values.add(v2);
                values.add(v3);

                values.wrapTogether();
            }

            data = values.toString();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (data);
    }

    /**
     * @param sensor_id         the sensor_id the data should be read from
     * @param timestamp_start   the timestamp to start looking after the sensor data
     * @param timestamp_end     the timestamp to stop looking after the sensor data
     * @return                  returns a string that contains all the database entries for a specific sensor between
     *                          the two timestamps.
     */
    public static String readSensorValuesYearly(int sensor_id, String timestamp_start, String timestamp_end) {
        String data = "";

        //convert timestamp_start to date
        Date date_start = new Date();
        Date date_end = new Date();

        try{
            date_start = sdf.parse(timestamp_start);
            date_end = sdf.parse(timestamp_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            //select * from measurements where s_sensor_id = 0 and s_timestamp > 2345678 and s_timestamp < 3456789
            //SELECT avg(value), extract(year from timestamp) as year FROM %s WHERE sensor_id = %d AND timestamp between '%s' and '%s' GROUP BY extract(year from timestamp);
            ResultSet output = stmt.executeQuery(String.format("SELECT avg(value) as value, extract(year from timestamp) as year FROM %s WHERE sensor_id = %d AND timestamp between '%s' and '%s' GROUP BY extract(year from timestamp)", table_measurements,sensor_id, sdf.format(date_start), sdf.format(date_end)));
            Container<Value> values = new JsonDataContainer<>();

            while (output.next()) {
                Value<Float> v1 = new Value(output.getFloat("value"), "value");
                Value<Integer> v2 = new Value(output.getInt("year"), "year");

                values.add(v1);
                values.add(v2);

                values.wrapTogether();
            }

            data = values.toString();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (data);
    }
































    /** IS GOING TO BE DELETED
     * This method returns the last amount (f.e.: 100) data entrances of a specific sensor, specified by it's ID
     * @param sensor_id which sensor should be read from
     * @param amount    the last x data entrances
     * @return          returns a string containing the last f.e.: 100 data entrances from the given sensor_id [JSON]
     */
    public static String readSensorValuesSingle(int sensor_id, int amount) {
        String data = "";
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            //select * from measurements where s_sensor_id = 0;
            ResultSet output = stmt.executeQuery(String.format("select * from %s where sensor_id = %d", table_measurements, sensor_id));


            Container<Value> values = new JsonDataContainer<>();
            output.last();
            int count = 0;
            while (count < amount && output.previous()) {
                Value<Integer>  v1 =     new Value(output.getInt(s_measurement_id), "measurement_id");
                Value<String>   v2 =     new Value(output.getString(s_timestamp), "timestamp");
                Value<Float>    v3 =     new Value(output.getFloat(s_value), "value");
                Value<Integer>  v4 =     new Value(output.getInt(s_quality_code), "quality_code");

                values.add(v1);
                values.add(v2);
                values.add(v3);
                values.add(v4);
                values.wrapTogether();

                count++;
            }

            data = values.toString();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (data);
    }





    /**
     *   ____         __ _
     *  / ___|  ___  / _| |___      ____ _ _ __ ___
     *  \___ \ / _ \| |_| __\ \ /\ / / _` | '__/ _ \
     *   ___) | (_) |  _| |_ \ V  V / (_| | | |  __/
     *  |____/ \___/|_|  \__| \_/\_/ \__,_|_|  \___|
     *  |_ _|_ __ (_) |_(_) __ _| (_)___(_) ___ _ __ _   _ _ __   __ _
     *   | || '_ \| | __| |/ _` | | / __| |/ _ \ '__| | | | '_ \ / _` |
     *   | || | | | | |_| | (_| | | \__ \ |  __/ |  | |_| | | | | (_| |
     *  |___|_| |_|_|\__|_|\__,_|_|_|___/_|\___|_|   \__,_|_| |_|\__, |
     *                                                           |___/
     */

    /**
     * This method returns an Array of HashMap of a specific sensor specified in the String <sensorClassName>
     *  (1) What is this class doing?
     *      It scans for every type-name. If the type-name equals the content in sensorClassName, we first save the
     *      s_sensor_id. We then scan for all attributes that a database entry with the s_sensor_id has. Afterwards we put
     *      the results in a HashMap and put it inside of an array to be able to differentiate the sensors from each other.
     * (2)  But what exactly stands in the HashMap? Read the Description of the class "readFromSensorParameters"
     * @param sensorClassName The type you want to check for. (FakeSensor, TempSensor, PressureSensor,...)
     * @return  Returns a HashMap containing all data (such as s_sensor_id, s_favourite, location,...) for a sensor with the type 'sensorClassName'
     */
    public static HashMap<String, String>[] readSensorData(String sensorClassName) {
        ArrayList<HashMap<String, String>> resultSensors = new ArrayList<>();
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            //select distinct sensor_id from sensors where attribute = 'type' and value = 'FakeSensor'
            ResultSet result = stmt.executeQuery(String.format("select distinct %s from %s where %s = 'type' and %s ='%s'",
                    s_sensor_id, table_sensors, s_attribute, s_value, sensorClassName));

            while (result.next()) {
                //für jeden Eintrag:
                int id = result.getInt(s_sensor_id);
                resultSensors.add(readFromSensorParameters(id));
            }

            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSensors.toArray(new HashMap[0]);
    }

    /**
     *
     * @param sensor_id gets all the configuration-information (such as location, sensorname, s_favourite, ...) from this specified sensor
     * @return a HashMap of String, String, the first String containing the Attribute and the second one containing the Value for the Attribute.
     */
    private static HashMap<String, String> readFromSensorParameters(int sensor_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("sensor_id", sensor_id + "");
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            //select * from sensors where sensor_id = 0
            ResultSet result = stmt.executeQuery(String.format("select * from %s where %s = %d", table_sensors, s_sensor_id,sensor_id));

            while (result.next()) {
                //result.next() == ein Tupel
                //result.getString(s_attribute) -> value of "attribute"
                //result.getString(s_value) -> value of "value"
                map.put(result.getString(s_attribute), result.getString(s_value));
            }
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }





}
