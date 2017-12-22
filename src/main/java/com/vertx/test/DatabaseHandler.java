package com.vertx.test;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.EncodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.SQLRowStream;

public class DatabaseHandler {
	private final SQLClient sqlClient;
	private final MessageConsumer<String> consumer;

	private static final String SELECT_USER_DETAILS = "SELECT `id`, `username`, `first_name`, `last_name`, `gender`, `password`, `status` FROM `user_details` limit 1, 10000";

	public static final String ADDRESS = "db_queue";

	public DatabaseHandler(SQLClient client, MessageConsumer<String> consumer) {
		this.sqlClient = client;
		this.consumer = consumer;
	}

	public void execute() {
		consumer.handler(m -> {
			try {
				sqlClient.getConnection(conn -> {
					if (conn.failed()) {
						conn.cause().printStackTrace();
						return;
					} else {
						SQLConnection connection = conn.result();
						JsonArray array = new JsonArray();
						connection.queryStream(SELECT_USER_DETAILS, stream -> {
							if (stream.succeeded()) {
								SQLRowStream sqlRowStream = stream.result();
								sqlRowStream.resultSetClosedHandler(v -> {
									sqlRowStream.moreResults();
								}).handler(row -> {
									array.add(row);
								}).endHandler(v -> {
									JsonObject resp = new JsonObject();
									resp.put("data", array);
									sqlRowStream.close();
									connection.close();
									m.reply(resp);
								});
							} else {
								stream.cause().printStackTrace();
							}
						});
					}
				});
			} catch (EncodeException ex) {
				ex.printStackTrace();
				m.fail(HttpResponseStatus.BAD_REQUEST.code(), "Failed to encode data.");
			} catch (Exception ex) {
				ex.printStackTrace();
				m.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), ex.getMessage());
			}
		}).exceptionHandler(ex -> {
			ex.printStackTrace();
			throw new RuntimeException("Error while fetching records");
		});
	}
}
