package org.djna.asynch.estate.webapp;


import org.apache.log4j.Logger;
import org.djna.asynch.estate.data.ThermoRead;
import org.djna.asynch.estate.data.ThermostatReading;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeMonitorController {
    static private Logger LOGGER = Logger.getLogger(HomeMonitorController.class);
    // enable simple server test
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    // display page that subscribes to one topic
    @GetMapping("/monitor")
    List<ThermoRead> all(@RequestParam(name="property", required=false, defaultValue="101") String property,
                          @RequestParam(name="locationSelected", required=false, defaultValue="hall") String location,
                          Model model) {
        List<ThermoRead> result = new ArrayList<>();
        ThermoRead jsonObj = new ThermoRead(location);
        result.add(jsonObj);
        return result;
    }
}
