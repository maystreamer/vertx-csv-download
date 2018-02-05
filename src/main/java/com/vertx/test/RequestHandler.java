package com.vertx.test;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.streams.Pump;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.web.RoutingContext;

public class RequestHandler implements Handler<RoutingContext> {
	private Vertx vertx;

	public RequestHandler(Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	public void handle(RoutingContext event) {
		DeliveryOptions options = new DeliveryOptions();
		HttpServerResponse response = event.response();
		response.putHeader(HttpHeaders.CONTENT_TYPE, "application/csv")
				.putHeader("Content-Disposition", "attachment; filename=user_details.csv")
				.putHeader(HttpHeaders.TRANSFER_ENCODING, "chunked").setChunked(true);

		// this.vertx.eventBus().consumer("4f127476-41c8-4d55-97fa-6e0c98f27e74", result
		// -> {
		// JsonArray array = (JsonArray) result.body();
		// response.write(toString(array), "UTF-8");
		// });

		ReadStream<Buffer> consumer = this.vertx.eventBus().<Buffer>consumer("4f127476-41c8-4d55-97fa-6e0c98f27e74")
				.bodyStream();
		consumer.handler(result -> {
			JsonArray array = (JsonArray) result;
			response.write(toString(array), "UTF-8");
		});

		Pump pump = Pump.pump(consumer, response);
		pump.start();
		this.vertx.eventBus().send(DatabaseHandler.ADDRESS, "hi, from RequestHandler", options, result -> {
			response.end();
			response.close();
		});
	}

	public String toString(final JsonArray data) {
		return String.join(",", "" + data.getValue(0), "" + data.getValue(1), "" + data.getValue(2),
				"" + data.getValue(3), "" + data.getValue(4), "" + data.getValue(5), "" + data.getValue(6), "\n");
	}
}