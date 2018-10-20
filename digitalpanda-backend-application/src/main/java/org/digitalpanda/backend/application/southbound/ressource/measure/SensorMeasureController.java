package org.digitalpanda.backend.application.southbound.ressource.measure;

import org.digitalpanda.backend.application.persistence.measure.latest.SensorMeasureLatestRepository;
import org.digitalpanda.backend.data.SensorMeasure;
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
    @RequestMapping(method= RequestMethod.POST)
    public void setLatestMeasure(@RequestBody List<SensorMeasures> sensorMeasuresList){
        System.out.println("/sensor POST: " + sensorMeasuresList.stream().map(SensorMeasures::toString).collect(Collectors.joining(",")));
        sensorMeasuresList.forEach(
                (sensorMeasures) ->
                    sensorMeasureLatestRepository.setMeasure(
                            sensorMeasures.getSensorMeasureMetaData(),
                            sensorMeasures.getMeasures().stream().max(SensorMeasure::compareTo).orElse(null)));
    }
}
