package org.digitalpanda.backend.application.data;

public class SensorMeasureMetaData {

    private final String location;
    private final SensorMeasureEnum type;

    public SensorMeasureMetaData(String location, SensorMeasureEnum type) {
        this.location = location;
        this.type = type;
    }

    @Override
    public int hashCode(){
        return (location + type).hashCode();
    }

    @Override
    public boolean equals(Object o){
        if(! (o instanceof SensorMeasureMetaData)) return false;
        SensorMeasureMetaData object = (SensorMeasureMetaData) o;
        return  ((object.location == null && this.location == null) || (object.location != null && object.location.equals(this.location)))
                &&
                ((object.type == null && this.type == null) || (object.type != null && object.type.equals(this.type)));
    }
}
