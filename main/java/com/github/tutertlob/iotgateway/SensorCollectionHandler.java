package com.github.tutertlob.iotgateway;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.NullPointerException;

import com.github.tutertlob.subghz.NoticePacketInterface;
import com.github.tutertlob.subghz.PacketImplementation;
import com.github.tutertlob.subghz.SubGHzFrame;

public final class SensorCollectionHandler implements Transceiver.PacketHandler {

	private static final Logger logger = Logger.getLogger(SensorCollectionHandler.class.getName());

	public SensorCollectionHandler() {
	}

	@Override
	public void handle(SubGHzFrame frame) {
		SensorEntity sensor;
		try {
			sensor = SensorEntity.lookUpSensor(frame.getSenderAddr());
			if (sensor.getPanid() != frame.getSenderExtAddr()) {
				sensor.setPanid(frame.getSenderExtAddr());
				SensorEntity.updateSensor(sensor);
			}
		} catch (NullPointerException e) {
			logger.log(Level.INFO, "No sensor is hit.", e);
			sensor = new SensorEntity().setAddr(frame.getSenderAddr()).setPanid(frame.getSenderExtAddr());
			SensorEntity.registerSensor(sensor);
		}
	}

}
