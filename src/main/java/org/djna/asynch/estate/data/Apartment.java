package org.djna.asynch.estate.data;

import java.util.ArrayList;
import java.util.List;

public class Apartment {
//    public int apartmentNumber;
    public List<ThermostatReading> tempReadings = new ArrayList<>();
    public List<String> topicAddress = new ArrayList<>();
    public List<String> topicAddressWarn = new ArrayList<>();
    public String base;
    public String propertyNumber;

    public Apartment(String initBase, String initPropertyNumber) {
        this.base = initBase;
        this.propertyNumber = initPropertyNumber;
        topicAddress.add(base + propertyNumber + "/living");
        topicAddress.add(base + propertyNumber + "/bedroom");
        topicAddress.add(base + propertyNumber + "/kitchen");

        topicAddressWarn.add(base +"warn/"+ propertyNumber + "/living");
        topicAddressWarn.add(base +"warn/"+ propertyNumber + "/bedroom");
        topicAddressWarn.add(base +"warn/"+ propertyNumber + "/kitchen");
    }
}
