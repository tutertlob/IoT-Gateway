# IoT Gateway

A gateway library for wireless sensor networks using the ISM band radio.
The library allows you to develop your own gateway with this library, which can receive packets from sernsors and control them by sending control packets.

## Dependencies

This library depends on following packages.
- [subghz-wireless-java](https://github.com/tutertlob/subghz-wireless-java)

## Install

Built and install the jar package with Maven

```bash
mvn install
```

## Develop your gateway

```java
package Your-Package
import com.github.tutertlob.iotgateway.IoTApplication;
import com.github.tutertlob.iotgateway.Transceiver.PacketHandler;

public final class Your-Main-Class extends IoTApplication {
    public static void main(String[] args) throws IOException {
        ...
        // Instantiate your own packet handlers as a list and pass it to the launch as a argument.
        List<PacketHandler> handlers = Arrays.asList(
            new Your-Packet-Handler() 
            new Your-Another-Packet-Handler()
            );

        // Start the iot gateway
        launch(handlers);
        ...
    }

    @Override
    public void start() {
        // This is called once after starting the gateway.
        ...
    }

    @Override
    public void finish() {
        // This is called once before exiting.
        ...
    }
}
```

## Develop your own packet handler

A handler is used to handle received packet from sensors.

```java
package Your-Package;

import com.github.tutertlob.iotgateway.*;
import com.github.tutertlob.iotgateway.Transceiver.PacketHandler;
import com.github.tutertlob.subghz.*;

public class Your-Packet-Handler implements PacketHandler {
    @Override
    public void handle(SubGHzFrame frame) {
        // Put your packet handling code here.
    }
}
```
