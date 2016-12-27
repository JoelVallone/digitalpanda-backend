package org.digitalpanda.backend.data;

public enum SensorMeasureType {

    TEMPERATURE("Degree Celccius", "°C"),
    HUMIDITY("Percentage", "%"),
    PRESSURE("Hecto-Pascal","hPa");

    private final String unit;
    private final String description;

    SensorMeasureType(String description, String unit) {
        this.description = description;
        this.unit = unit;
    }
}
