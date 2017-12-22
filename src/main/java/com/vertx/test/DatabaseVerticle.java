package com.vertx.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;

public class DatabaseVerticle extends AbstractVerticle {
	@Override
	public void start(Future<Void> future) throws Exception {
		super.start(future);
		SQLClient client = MySQLClient.createShared(vertx, getMySQLConfig());
		DatabaseHandler dbHandler = new DatabaseHandler(client, vertx.eventBus().consumer(DatabaseHandler.ADDRESS));
		dbHandler.execute();
	}

	private JsonObject getMySQLConfig() {
		JsonObject config = new JsonObject();
		config.put("host", "localhost");
		config.put("port", 3306);
		config.put("username", "root");
		config.put("password", "root@1010");
		config.put("database", "greymetrics");
		config.put("charset", "UTF-8");
		config.put("password", "root@1010");
		config.put("maxPoolSize", 50);
		return config;
	}
}