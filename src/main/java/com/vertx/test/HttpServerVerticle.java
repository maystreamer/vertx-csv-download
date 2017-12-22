package com.vertx.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class HttpServerVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> future) throws Exception {
		final int PORT = 8181;
		RequestHandler requestHandler = new RequestHandler(vertx);
		Router mainRouter = Router.router(vertx);
		mainRouter.route().consumes("application/json");
		mainRouter.route().produces("application/json");

		mainRouter.route().handler(BodyHandler.create());
		mainRouter.get("/api/csv").handler(requestHandler::handle);
		vertx.createHttpServer().requestHandler(mainRouter::accept).listen(PORT, res -> {
			if (res.succeeded()) {
				System.out.println("Server listening on port " + PORT);
				future.complete();
			} else {
				System.out.println("Failed to launch server");
				future.fail(res.cause());
			}
		});
	}
}
