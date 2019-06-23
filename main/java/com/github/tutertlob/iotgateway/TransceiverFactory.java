package com.github.tutertlob.iotgateway;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.tutertlob.wireless_dependencies.Im920Transceiver;
import com.github.tutertlob.wireless_dependencies.LazuriteTransceiver;

public class TransceiverFactory {

	private static final Logger logger = Logger.getLogger(TransceiverFactory.class.getName());

	private TransceiverFactory() {

	}

	public static Transceiver getTransceiver() {
		String rf = AppProperties.getInstance().getProperty("rf.interface");
		return getTransceiver(rf);
	}

	public static Transceiver getTransceiver(String rfModule) {
		rfModule = rfModule.toLowerCase();
		Transceiver transceiver = null;
		if ("lazurite".equals(rfModule)) {
			transceiver = LazuriteTransceiver.getInstance();
			logger.log(Level.INFO, "Lazurite interface was specified.");
		} else if ("im920".equals(rfModule)) {
			transceiver = Im920Transceiver.getInstance();
			logger.log(Level.INFO, "IM920 interface was specified.");
		} else {
			logger.log(Level.WARNING,
					"No RF interface was specified in the property file. rf.interface mush be specified.");
			System.exit(-1);
		}
		return transceiver;
	}

}
