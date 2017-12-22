package com.vertx.test;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class RequestHandler implements Handler<RoutingContext> {
	private Vertx vertx;

	public RequestHandler(Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	public void handle(RoutingContext event) {
		DeliveryOptions options = new DeliveryOptions();
		this.vertx.eventBus().send(DatabaseHandler.ADDRESS, "hi, from RequestHandler", options, result -> {
			HttpServerResponse response = event.response();
			if (result.succeeded()) {
				String formatted = "";
				JsonObject json = (JsonObject) result.result().body();
				for (Object array : json.getJsonArray("data")) {
					JsonArray data = (JsonArray) array;
					formatted = toString(formatted, data);
				}
				response.putHeader(HttpHeaders.CONTENT_TYPE, "application/csv")
						.putHeader("Content-Disposition", "attachment; filename=user_details.csv")
						.putHeader("Content-Length", "" + formatted.length())
						.putHeader(HttpHeaders.TRANSFER_ENCODING, "chunked").setChunked(true).write(formatted, "UTF-8")
						.end();
			} else {
				event.response().setStatusCode(500).end(result.cause().getMessage());
			}
		});
	}

	public String toString(final String formatted, final JsonArray data) {
		return String.join(",", formatted, "" + data.getValue(0), "" + data.getValue(1), "" + data.getValue(2),
				"" + data.getValue(3), "" + data.getValue(4), "" + data.getValue(5), "" + data.getValue(6), "\n");
	}

}
