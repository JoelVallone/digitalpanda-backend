package org.digitalpanda.backend.application.data;

public enum SensorMeasureEnum {

    TEMPERATURE("Degree Celccius", "°C"),
    HUMIDITY("Percentage", "%"),
    PRESSURE("Hecto-Pascal","hPa");

    private final String unit;
    private final String description;

    SensorMeasureEnum(String description, String unit) {
        this.description = description;
        this.unit = unit;
    }
}
