package org.digitalpanda.backend.application.ressource;

import org.digitalpanda.backend.application.persistence.sensors.SensorMeasureRepository;
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

    private SensorMeasureRepository sensorMeasureRepository;

    @Autowired
    public SensorMeasureController(SensorMeasureRepository sensorMeasureRepository) {
        this.sensorMeasureRepository = sensorMeasureRepository;
    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, path="/keys")
    public List<SensorMeasureMetaData> getMeasureKeys(){
        return sensorMeasureRepository.getKeys();
    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET)
    public SensorMeasure getLatestMeasure(SensorMeasureMetaData sensorMeasureMetaData){
        return sensorMeasureRepository.getLatestMeasure(sensorMeasureMetaData);
    }

    //FIXME: Add endpoint to retrieve a list of measures between time intervals (MeasureTypesList, location, startDate, endDate).

    @CrossOrigin
    @RequestMapping(method= RequestMethod.POST)
    public void setLatestMeasure(@RequestBody List<SensorMeasures> sensorMeasuresList){
        System.out.println("/sensor POST: " + sensorMeasuresList.stream().map(SensorMeasures::toString).collect(Collectors.joining(",")));
        sensorMeasuresList.forEach(
                (sensorMeasures) ->
                    sensorMeasureRepository.setMeasure(
                            sensorMeasures.getSensorMeasureMetaData(),
                            sensorMeasures.getMeasures().stream().max(SensorMeasure::compareTo).get()));
    }
}
