package com.vertx.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> done) {
		int WORKER_POOL_SIZE = 200;
		DeploymentOptions serverOpts = new DeploymentOptions().setWorkerPoolSize(WORKER_POOL_SIZE);

		DeploymentOptions workerOpts = new DeploymentOptions().setWorker(true).setMultiThreaded(true)
				.setWorkerPoolSize(WORKER_POOL_SIZE);

		CompositeFuture.all(deploy(HttpServerVerticle.class.getName(), serverOpts),
				deploy(DatabaseVerticle.class.getName(), workerOpts)).setHandler(r -> {
					if (r.succeeded()) {
						done.complete();
					} else {
						done.fail(r.cause());
					}
				});
	}

	private Future<Void> deploy(String name, DeploymentOptions opts) {
		Future<Void> done = Future.future();

		vertx.deployVerticle(name, opts, res -> {
			if (res.failed()) {
				System.out.println("Failed to deploy verticle " + name);
				done.fail(res.cause());
			} else {
				System.out.println("Deployed verticle " + name);
				done.complete();
			}
		});

		return done;
	}
}