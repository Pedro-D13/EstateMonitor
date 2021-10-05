package org.djna.asynch.estate.data;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ThermoRead {
    // add data such temperature here
    public Date date;
    public int temperature;
    public String location;


    public ThermoRead(String initLocation) {
        this.date = Calendar.getInstance().getTime();
        this.temperature = new Random().nextInt(10) + 10;
        this.location = initLocation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ThermostatReading{" +
                "date=" + date +
                ", temperature=" + temperature +
                ", location='" + location + '\'' +
                '}';
    }
}
