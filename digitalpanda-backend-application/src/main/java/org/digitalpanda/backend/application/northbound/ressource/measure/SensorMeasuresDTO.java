package org.digitalpanda.backend.application.northbound.ressource.measure;

import org.digitalpanda.backend.data.SensorMeasureMetaData;

import java.util.List;

public class SensorMeasuresDTO {

    String startDate;
    String endDate;
    SensorMeasureMetaData SensorMeasureMetaData;
    List<Double> values;
}
