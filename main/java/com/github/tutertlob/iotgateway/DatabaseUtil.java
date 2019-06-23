package com.github.tutertlob.iotgateway;

import java.nio.file.Path;
import com.github.tutertlob.subghz.NoticePacketInterface;
import com.github.tutertlob.subghz.SubGHzFrame;

public interface DatabaseUtil {
	public void insertSensorRecord(SensorRecord<?> record);

	public SensorEntity lookUpSensor(String key, Object value);

	public void insertSensor(SensorEntity sensor);

	public void saveSensor(SensorEntity sensor);
}