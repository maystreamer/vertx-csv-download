package com.vertx.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;

public class DatabaseVerticle extends AbstractVerticle {
	@Override
	public void start(Future<Void> future) throws Exception {
		super.start(future);
		SQLClient client = JDBCClient.createShared(vertx, getMySQLConfigForJDBC());
		DatabaseHandler dbHandler = new DatabaseHandler(client, vertx.eventBus().consumer(DatabaseHandler.ADDRESS), vertx);
		dbHandler.execute();
	}

	private JsonObject getMySQLConfig() {
		JsonObject config = new JsonObject();
		config.put("host", "localhost");
		config.put("port", 3306);
		config.put("database", "greymetrics");
		config.put("username", "root");
		config.put("password", "root@1010");
		config.put("charset", "UTF-8");
		config.put("maxPoolSize", 50);
		return config;
	}

	private JsonObject getMySQLConfigForJDBC() {
		JsonObject config = new JsonObject();
		config.put("url", "jdbc:mysql://localhost:3306/greymetrics");
		config.put("driver_class", "com.mysql.cj.jdbc.Driver");
		config.put("user", "root");
		config.put("password", "root@1010");
		config.put("max_pool_size", 50);
		config.put("row_stream_fetch_size", Integer.MAX_VALUE);
		return config;
	}
}