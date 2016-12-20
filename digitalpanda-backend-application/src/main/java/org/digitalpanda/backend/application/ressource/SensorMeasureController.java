package org.digitalpanda.backend.application.ressource;

import org.digitalpanda.backend.application.persistence.SensorMeasureRepository;
import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureEnum;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.digitalpanda.backend.data.SensorMeasures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/sensor")
public class SensorMeasureController {

    @Autowired
    SensorMeasureRepository sensorMeasureRepository;


    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET)
    public SensorMeasure getLatestMeasure(@RequestParam(value="type", defaultValue="TEMPERATURE" ) String type,
                                          @RequestParam(value="location", defaultValue="default") String location){
        return sensorMeasureRepository.getMeasure(
                new SensorMeasureMetaData(location, SensorMeasureEnum.valueOf(type)));
    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.POST)
    public void getLatestMeasure(@RequestBody SensorMeasures jsonObject){
        //TODO: continue here...
    }
}
