package org.djna.asynch.estate.data;

import java.util.ArrayList;
import java.util.List;

public class Courtyard {
    // each apartment house num / property num stored as a string
    public List<String> apartmentList = new ArrayList<>();
    public String courtyardName;

    public Courtyard(int initStart, int initEnd) {
        for (int i = initStart; i <= initEnd; i++) {
            apartmentList.add(""+i);
        }
    }
}
