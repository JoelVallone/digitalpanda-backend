package org.digitalpanda.backend.application.ressource;

import org.digitalpanda.backend.application.persistence.sensors.latest.SensorMeasureLatestRepository;
import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.digitalpanda.backend.data.SensorMeasures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/sensor")
public class SensorMeasureController {

    private SensorMeasureLatestRepository sensorMeasureLatestRepository;

    @Autowired
    public SensorMeasureController(SensorMeasureLatestRepository sensorMeasureLatestRepository) {
        this.sensorMeasureLatestRepository = sensorMeasureLatestRepository;
    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, path="/keys")
    public List<SensorMeasureMetaData> getMeasureKeys(){
        return sensorMeasureLatestRepository.getKeys();
    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET)
    public SensorMeasure getLatestMeasure(SensorMeasureMetaData sensorMeasureMetaData){
        return sensorMeasureLatestRepository.getLatestMeasure(sensorMeasureMetaData);
    }

    //FIXME: Add endpoint to retrieve a list of measures between time intervals (MeasureTypesList, location, startDate, endDate).

    @CrossOrigin
    @RequestMapping(method= RequestMethod.POST)
    public void setLatestMeasure(@RequestBody List<SensorMeasures> sensorMeasuresList){
        System.out.println("/sensor POST: " + sensorMeasuresList.stream().map(SensorMeasures::toString).collect(Collectors.joining(",")));
        sensorMeasuresList.forEach(
                (sensorMeasures) ->
                    sensorMeasureLatestRepository.setMeasure(
                            sensorMeasures.getSensorMeasureMetaData(),
                            sensorMeasures.getMeasures().stream().max(SensorMeasure::compareTo).get()));
    }
}
