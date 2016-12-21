package org.digitalpanda.backend.application.ressource;

import org.digitalpanda.backend.application.persistence.SensorMeasureRepository;
import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureEnum;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.digitalpanda.backend.data.SensorMeasures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensor")
public class SensorMeasureController {

    private SensorMeasureRepository sensorMeasureRepository;

    @Autowired
    public SensorMeasureController(SensorMeasureRepository sensorMeasureRepository) {
        this.sensorMeasureRepository = sensorMeasureRepository;
    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET)
    public SensorMeasure getLatestMeasure(SensorMeasureMetaData sensorMeasureMetaData){
        return sensorMeasureRepository.getMeasure(sensorMeasureMetaData);
    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.POST)
    public void setLatestMeasure(@RequestBody SensorMeasures sensorMeasures){
        sensorMeasures.getMeasures().forEach(
                (sensorMeasureMetaData, sensorMeasureList) ->
                    sensorMeasureRepository.setMeasure(
                            sensorMeasureMetaData,
                            sensorMeasureList.stream().max(SensorMeasure::compareTo).get()));
    }
}
