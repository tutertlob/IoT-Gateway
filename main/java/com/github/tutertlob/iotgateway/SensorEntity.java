package com.github.tutertlob.iotgateway;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SensorEntity {

    private final float version = 1.0f;

    @SerializedName("radio_interface")
    @Expose
    private String radioInterface;

    @SerializedName("product_number")
    @Expose
    private String productNumber;

    @SerializedName("serial_number")
    @Expose
    private String serialNumber;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("uuid")
    @Expose
    private String uuid;

    @SerializedName("panid")
    @Expose
    private Long panid;

    @SerializedName("addr")
    @Expose
    private Long addr;

    @SerializedName("coordinate")
    @Expose
    private String coordinate;

    @SerializedName("latitude")
    @Expose
    private Double latitude;

    @SerializedName("longitude")
    @Expose
    private Double longitude;

    @SerializedName("placedAddress")
    @Expose
    private String placedAddress;

    private static final Map<Long, SensorEntity> sensors = new HashMap<>();

    public static SensorEntity lookUpSensor(Long key) {
        SensorEntity sensor = sensors.get(key);
        if (Objects.nonNull(sensor)) {
            return sensor;
        }

        try {
            sensor = DatabaseUtilFactory.getDatabaseUtil().lookUpSensor("addr", key);
        } catch (NullPointerException e) {
            throw e;
        }

        return sensor;
    }

    synchronized public static void registerSensor(SensorEntity entity) {
        DatabaseUtilFactory.getDatabaseUtil().insertSensor(entity);
        sensors.put(entity.getAddr(), entity);
    }

    synchronized public static void updateSensor(SensorEntity entity) {
        DatabaseUtilFactory.getDatabaseUtil().saveSensor(entity);
        sensors.replace(entity.getAddr(), entity);
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public SensorEntity() {
    }

    /**
     * 
     * @param coordinate
     * @param serialNumber
     * @param addr
     * @param radioInterface
     * @param version
     * @param productNumber
     * @param name
     * @param placedAddress
     * @param longitude
     * @param uuid
     * @param panid
     * @param latitude
     */
    public SensorEntity(Double version, String radioInterface, String productNumber, String serialNumber, String name,
            String uuid, Long panid, Long addr, String coordinate, Double latitude, Double longitude,
            String placedAddress) {
        super();
        this.radioInterface = radioInterface;
        this.productNumber = productNumber;
        this.serialNumber = serialNumber;
        this.name = name;
        this.uuid = uuid;
        this.panid = panid;
        this.addr = addr;
        this.coordinate = coordinate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placedAddress = placedAddress;
    }

    public float getVersion() {
        return version;
    }

    public String getInterface() {
        return radioInterface;
    }

    public SensorEntity setInterface(String radioInterface) {
        this.radioInterface = radioInterface;
        return this;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public SensorEntity setProductNumber(String productNumber) {
        this.productNumber = productNumber;
        return this;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public SensorEntity setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public String getName() {
        return name;
    }

    public SensorEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public SensorEntity setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Long getPanid() {
        return panid;
    }

    public SensorEntity setPanid(Long panid) {
        this.panid = panid;
        return this;
    }

    public Long getAddr() {
        return addr;
    }

    public SensorEntity setAddr(Long addr) {
        this.addr = addr;
        return this;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public SensorEntity setCoordinate(String coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public SensorEntity setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public SensorEntity setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getPlacedAddress() {
        return placedAddress;
    }

    public SensorEntity setPlacedAddress(String placedAddress) {
        this.placedAddress = placedAddress;
        return this;
    }

}
