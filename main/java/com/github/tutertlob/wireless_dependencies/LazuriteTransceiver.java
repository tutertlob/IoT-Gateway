package com.github.tutertlob.wireless_dependencies;

import com.github.tutertlob.iotgateway.Transceiver;
import com.github.tutertlob.iotgateway.Transceiver.PacketHandler;
import com.github.tutertlob.iotgateway.AppProperties;
import com.github.tutertlob.iotgateway.SensorCollectionHandler;
import com.github.tutertlob.lazurite.LazuriteParams;
import com.github.tutertlob.lazurite.LazuriteUtils;
import com.github.tutertlob.subghz.SubGHzFrame;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.InterruptedException;

public final class LazuriteTransceiver extends Transceiver {

	private static final Logger logger = Logger.getLogger(LazuriteTransceiver.class.getName());

	private static final LazuriteTransceiver INSTANCE = new LazuriteTransceiver();

	private boolean running = false;

	private List<PacketHandler> handlers;

	public static LazuriteTransceiver getInstance() {
		return INSTANCE;
	}

	private LazuriteTransceiver() {

	}

	@Override
	public LazuriteTransceiver startTransceiver(List<PacketHandler> handlers) {
		if (!running) {
			logger.info("Transceiver is starting...");
			LazuriteParams lazParams = AppProperties.getInstance().getLazuriteParams();
			LazuriteUtils.begin(lazParams);
			this.handlers = new ArrayList<>(Arrays.asList(new SensorCollectionHandler()));
			this.handlers.addAll(handlers);
			running = true;
			this.start();
		}
		return INSTANCE;
	}

	@Override
	public void sendAck(String toAddr, String extAddr, byte command, String response) {
		Long panid = Long.valueOf(extAddr);
		Long addr = Long.valueOf(toAddr);

		LazuriteUtils.sendAck(addr.shortValue(), panid.shortValue(), command, response);
	}

	@Override
	public void sendCommand(String toAddr, String extAddr, byte command, String param, boolean responseRequested) {
		Long panid = Long.valueOf(extAddr);
		Long addr = Long.valueOf(toAddr);

		if (responseRequested) {
			LazuriteUtils.sendCommandWithAck(addr.shortValue(), panid.shortValue(), command, param);
		} else {
			LazuriteUtils.sendCommand(addr.shortValue(), panid.shortValue(), command, param);
		}
	}

	@Override
	public void sendData(String toAddr, String extAddr, byte[] data) {
		Long panid = Long.valueOf(extAddr);
		Long addr = Long.valueOf(toAddr);

		LazuriteUtils.sendData(addr.shortValue(), panid.shortValue(), data);
	}

	@Override
	public void sendNotice(String toAddr, String extAddr, String notice) {
		Long panid = Long.valueOf(extAddr);
		Long addr = Long.valueOf(toAddr);

		LazuriteUtils.sendNotice(addr.shortValue(), panid.shortValue(), notice);
	}

	@Override
	public void run() {
		logger.info("Transceiver running...");

		try {
			while (running) {
				SubGHzFrame frame = LazuriteUtils.readFrame();
				logger.log(Level.INFO, frame.toString());
				for (PacketHandler hnd : handlers)
					hnd.handle(frame);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			logger.log(Level.WARNING, "Caught an unexpected exception: ", e);
			System.exit(-1);
		} finally {
			running = false;
			logger.warning("Transceiver exited");
		}
	}

	@Override
	public void finish() {
		if (running) {
			running = false;
			this.interrupt();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Do nothing
			}
			LazuriteUtils.close();
			this.handlers = null;
			logger.info("Transceiver exited.");
		}
	}

}
