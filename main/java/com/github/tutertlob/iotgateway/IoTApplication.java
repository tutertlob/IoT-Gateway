package com.github.tutertlob.iotgateway;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.List;
import java.lang.StackTraceElement;
import java.lang.Package;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;
import java.lang.NoSuchMethodException;
import java.lang.NullPointerException;
import java.lang.reflect.InvocationTargetException;
import java.lang.Class;

public abstract class IoTApplication {

    private static Package userAppPackage;

    public static void launch(List<Transceiver.PacketHandler> handlers) {
        final Logger logger = Logger.getLogger(IoTApplication.class.getName());
        logger.info("Starting the sensor system receiver...");

        String appClassName = null;
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        for (StackTraceElement trace : traces) {
            if ("main".equals(trace.getMethodName())) {
                appClassName = trace.getClassName();
                break;
            }
        }

        Class<?> appClass = null;
        try {
            appClass = Class.forName(appClassName);
            userAppPackage = appClass.getPackage();
        } catch (ClassNotFoundException | LinkageError e) {
            logger.log(Level.WARNING, "Application main class was not found.", e);
            System.exit(-1);
        }

        String[] parts = userAppPackage.getName().split("\\.");
        try {
            final Logger tutertlobLogger = Logger.getLogger("com.github.tutertlob");
            Handler handler = new FileHandler(parts[parts.length - 1] + ".log");
            tutertlobLogger.addHandler(handler);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.warning("The sensor system receiver is exiting since the signal was received.");
                TransceiverFactory.getTransceiver().finish();
                ReceiverRestServer.finish();
            }));

            IoTApplication app = (IoTApplication) appClass.getDeclaredConstructor().newInstance();

            ReceiverRestServer.startRestServer(userAppPackage);
            TransceiverFactory.getTransceiver().startTransceiver(handlers).join();
            app.start();
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Couldn't create log file.", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.log(Level.INFO, "Caught InterruptedException then exitting the system.", e);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException
                | NullPointerException e) {
            logger.log(Level.SEVERE, "The child class of IoTApplication has no nullary constructor.", e);
        } finally {
            TransceiverFactory.getTransceiver().finish();
            ReceiverRestServer.finish();
            logger.info("Exited");
        }
        logger.info("The sensor system receiver exited.");
    }

    public static Package getUserApplicationPackage() {
        return userAppPackage;
    }

    public abstract void start();

    public abstract void finish();
}