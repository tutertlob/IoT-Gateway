package com.github.tutertlob.iotgateway;

import java.net.UnknownHostException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.NullPointerException;

import com.github.tutertlob.subghz.SubGHzFrame;
import com.github.tutertlob.subghz.PacketImplementation;
import com.github.tutertlob.subghz.NoticePacketInterface;
import com.mongodb.util.JSON;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class MongoUtil implements DatabaseUtil {
	private static final Logger logger = Logger.getLogger(MongoUtil.class.getName());

	private static final MongoUtil INSTANCE = new MongoUtil();

	private final MongoClient mongoClient;

	private final DB database;

	private final DBCollection collection;

	private final DBCollection sensorCollection;

	private MongoUtil() {
		String host;
		int port;
		String db;
		String dbcollection;
		String dbsensorcollection;

		try {
			host = AppProperties.getInstance().getProperty("database.host");
		} catch (IllegalArgumentException | NullPointerException e) {
			host = "localhost";
		}
		try {
			port = Integer.parseInt(AppProperties.getInstance().getProperty("database.port"));
		} catch (IllegalArgumentException | NullPointerException e) {
			port = 27017;
		}

		try {
			db = AppProperties.getInstance().getProperty("database.db");
		} catch (IllegalArgumentException | NullPointerException e) {
			db = "iot";
		}

		try {
			dbcollection = AppProperties.getInstance().getProperty("database.collection");
			dbsensorcollection = AppProperties.getInstance().getProperty("database.sensorcollection");
		} catch (IllegalArgumentException | NullPointerException e) {
			dbcollection = "sensor_records";
			dbsensorcollection = "sensor_entities";
		}

		MongoClient temp = null;
		try {
			temp = new MongoClient(host, port);
		} catch (UnknownHostException e) {
			logger.log(Level.SEVERE, String.format("Unknown host '%s:%d'.", host, port), e);
			System.exit(-1);
		}
		mongoClient = temp;
		database = mongoClient.getDB(db);
		collection = database.getCollection(dbcollection);
		sensorCollection = database.getCollection(dbsensorcollection);
	}

	public static MongoUtil getInstance() {
		return INSTANCE;
	}

	@Override
	public void insertSensorRecord(SensorRecord<?> record) {
		DBObject sensorRecord = (DBObject) JSON.parse(record.toJson());
		try {
			collection.insert(sensorRecord);
		} catch (MongoException e) {
			logger.log(Level.WARNING, "Inserting a document to collection failed.", e);
		}
	}

	@Override
	public SensorEntity lookUpSensor(String key, Object value) {
		BasicDBObject query = new BasicDBObject();

		query.append(key, value);
		DBObject projection = (DBObject) JSON.parse("{'_id': 0}");
		DBCursor cursor = sensorCollection.find(query, projection);
		if (!cursor.hasNext()) {
			throw new NullPointerException("No sensor entries.");
		}

		if (cursor.count() > 1) {
			logger.log(Level.WARNING, "Multiple sensors were found.");
		}

		DBObject json = cursor.next();
		logger.log(Level.INFO, "Sensor: " + json);

		SensorEntity sensor;
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
		sensor = gson.fromJson(json.toString(), SensorEntity.class);
		return sensor;
	}

	@Override
	public void insertSensor(SensorEntity sensor) {
		try {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
			DBObject doc = (DBObject) JSON.parse(gson.toJson(sensor));
			sensorCollection.insert(doc);
			logger.log(Level.INFO, "Insert: " + gson.toJson(sensor));
		} catch (MongoException e) {
			logger.log(Level.WARNING, e.toString(), e);
		}
	}

	@Override
	public void saveSensor(SensorEntity sensor) {
		try {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
			DBObject doc = (DBObject) JSON.parse(gson.toJson(sensor));
			sensorCollection.save(doc);
			logger.log(Level.INFO, "Save: " + gson.toJson(sensor));
		} catch (MongoException e) {
			logger.log(Level.WARNING, e.toString(), e);
		}
	}

}
