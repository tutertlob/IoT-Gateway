package com.github.tutertlob.iotgateway;

import java.util.Date;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SensorRecord<T> {

    @SerializedName("sensor")
    @Expose
    private SensorEntity sensor;

    @SerializedName("received_at")
    @Expose
    private Date receivedAt = new Date();

    @SerializedName("packet_type")
    @Expose
    private String packetType;

    @SerializedName("content_type")
    @Expose
    private String contentType;

    @SerializedName("rssi")
    @Expose
    private Integer rssi;

    private T data;

    public SensorRecord() {

    }

    public SensorRecord(SensorEntity sensor, T data) {
        this.sensor = sensor;
        this.data = data;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public SensorRecord<T> setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
        return this;
    }

    public String getPacketType() {
        return packetType;
    }

    public SensorRecord<T> setPacketType(String packetType) {
        this.packetType = packetType;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public SensorRecord<T> setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Integer getRssi() {
        return rssi;
    }

    public SensorRecord<T> setRssi(Integer rssi) {
        this.rssi = rssi;
        return this;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return toJson();
    }

}