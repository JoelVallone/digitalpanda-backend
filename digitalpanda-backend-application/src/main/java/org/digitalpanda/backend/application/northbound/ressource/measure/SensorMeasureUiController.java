package org.digitalpanda.backend.application.northbound.ressource.measure;

import org.digitalpanda.backend.application.persistence.measure.latest.SensorMeasureLatestRepository;
import org.digitalpanda.backend.application.northbound.service.SensorMeasureHistoryService;
import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.digitalpanda.backend.data.SensorMeasureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("ui/sensor")
public class SensorMeasureUiController {

    private SensorMeasureLatestRepository sensorMeasureLatestRepository;

    private SensorMeasureHistoryService sensorMeasureHistoryService;
    @Autowired
    public SensorMeasureUiController(SensorMeasureLatestRepository sensorMeasureLatestRepository, SensorMeasureHistoryService sensorMeasureHistoryService) {
        this.sensorMeasureLatestRepository = sensorMeasureLatestRepository;
        this.sensorMeasureHistoryService = sensorMeasureHistoryService;
    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, path="/keys")
    public List<SensorMeasureMetaData> getMeasureKeys(){
        return sensorMeasureLatestRepository.getKeys();
    }


    //TODO: interface change on frontend side
    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET)
    public SensorMeasureDTO getLatestMeasure(String location, String type){
        return Optional.of(sensorMeasureLatestRepository.getLatestMeasure(new SensorMeasureMetaData(location, SensorMeasureType.valueOf(type))))
                .map( sensorMeasure -> new SensorMeasureDTO(sensorMeasure.getTimestamp(), sensorMeasure.getValue()))
                .orElse(null);
    }

    /*
    TODO: getMeasureInterval
    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, path="/history")
    public ??? getMeasureInterval(SensorMeasureMetaData sensorMeasureMetaData, String startDateTime, String endDateTime, Integer dataPointCount){
        //"2018-05-05T11:50:55"
        return sensorMeasureHistoryService.getMeasureInterval(sensorMeasureMetaData, LocalDateTime.parse(startDateTime), LocalDateTime.parse(endDateTime), dataPointCount);
    }
    */
}
