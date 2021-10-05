package org.djna.asynch.estate.data;

import java.util.Date;

public class ThermostatReading {
    // add data such temperature here
    public Date date;
    public int temperature;
    public String location;

    public ThermostatReading(Date initDate, int initTemp, String initLocation) {
        this.date = initDate;
        this.temperature = initTemp;
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
}
