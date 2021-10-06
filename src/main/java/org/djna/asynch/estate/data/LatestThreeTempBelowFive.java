
package org.djna.asynch.estate.data;
import java.util.ArrayList;
import java.util.List;

public class LatestThreeTempBelowFive {
    List<Integer> listOfTemps = new ArrayList<>();

    public boolean add(int temp) {
        // add fourth temp reading onto list
        if (listOfTemps.size() <= 4) {
            listOfTemps.add(temp);
            listOfTemps.remove(0);
        }
        // if there are precisely three
        if (listOfTemps.size() == 3) {
            if (listOfTemps.get(0) < 5 && listOfTemps.get(1) < 5 && listOfTemps.get(2) < 5) {
                return true;
            }
        }
        return false;
    }
}
