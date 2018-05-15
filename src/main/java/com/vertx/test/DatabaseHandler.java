package com.vertx.test;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.EncodeException;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.SQLRowStream;

public class DatabaseHandler {
	private final SQLClient sqlClient;
	private final MessageConsumer<String> consumer;
	private Vertx vertx;

	private static final String SELECT_USER_DETAILS = "SELECT `id`, `first_name`, `last_name`, `gender`, `password`, `status` FROM `user_details` "; // limit
																																									// 1,
																																									// 20000

	public static final String ADDRESS = "db_queue";

	public DatabaseHandler(SQLClient client, MessageConsumer<String> consumer, Vertx vertx) {
		this.sqlClient = client;
		this.consumer = consumer;
		this.vertx = vertx;
	}

	public void execute() {
		consumer.handler(m -> {
			try {
				System.out.println(m.body());
				sqlClient.getConnection(conn -> {
					if (conn.failed()) {
						conn.cause().printStackTrace();
						return;
					} else {
						SQLConnection connection = conn.result();
						connection.queryStream(SELECT_USER_DETAILS, stream -> {
							if (stream.succeeded()) {
								SQLRowStream sqlRowStream = stream.result();
								sqlRowStream.resultSetClosedHandler(v -> {
									sqlRowStream.moreResults();
								}).handler(row -> {
									// array.add(row);
									this.vertx.eventBus().send("4f127476-41c8-4d55-97fa-6e0c98f27e74", Buffer.buffer(row.encode()));
								}).endHandler(v -> {
									sqlRowStream.close();
									connection.close();
									m.reply("done");
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
