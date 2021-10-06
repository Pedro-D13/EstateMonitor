
package org.djna.asynch.estate.data;
import java.util.ArrayList;
import java.util.List;

public class LatestThreeTempBelowFive {
    public List<Integer> listOfTemps = new ArrayList<>();

    public void add(int temp) {
        listOfTemps.add(temp);
        if (listOfTemps.size() == 4) {
            listOfTemps.remove(0);
        }
    }

    public boolean sendWarning() {
        // if there are precisely three
        if (listOfTemps.size() == 3) {
            if (listOfTemps.get(0) < 5 && listOfTemps.get(1) < 5 && listOfTemps.get(2) < 5) {
                return true;
            }
        }
        return false;
    }
}
