package com.github.tutertlob.iotgateway;

import com.github.tutertlob.subghz.SubGHzFrame;
import java.lang.Thread;
import java.util.List;

public abstract class Transceiver extends Thread {

	public abstract Transceiver startTransceiver(List<PacketHandler> handlers);

	public abstract void sendAck(String toAddr, String extAddr, byte command, String response);

	public abstract void sendCommand(String toAddr, String extAddr, byte command, String param,
			boolean responseRequested);

	public abstract void sendData(String toAddr, String extAddr, byte[] data);

	public abstract void sendNotice(String toAddr, String extAddr, String notice);

	public abstract void run();

	public abstract void finish();

	public static interface PacketHandler {

		public void handle(SubGHzFrame frame);

	}

}
