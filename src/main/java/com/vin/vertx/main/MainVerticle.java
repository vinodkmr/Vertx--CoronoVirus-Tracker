package com.vin.vertx.main;

import com.vin.vertx.Util.Utility;
import com.vin.vertx.handler.Handler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
	  
	Router router = Router.router(vertx);
	
	router.get().blockingHandler(Handler::showIndex);
	
	vertx.createHttpServer().requestHandler(router).listen(8082,
			ar -> {
				if(ar.succeeded()) {
					System.out.println("Server Started");
					startPromise.complete();
				}else {
					startPromise.fail(ar.cause());
					System.out.println("Server Could not start "+Utility.getStackTrace(ar.cause().fillInStackTrace()));
				}
			});
	  
	  Future<String> future = anAsyncAction();
	    future.compose( out -> anotherAsyncAction(out))
	      .setHandler(ar -> {
	        if (ar.failed()) {
	          System.out.println("Something bad happened");
	          ar.cause().printStackTrace();
	        } else {
	          System.out.println("Result: " + ar.result());
	        }
	      });
  }
  
  //Convenience method so you can run it in your IDE
  public static void main(String[] arg) { 
	  Vertx vertx = Vertx.vertx();
	  vertx.deployVerticle(new MainVerticle()); 
  }
  
  
  private Future<String> anAsyncAction() {
	  Promise<String> promise = Promise.promise();
	  vertx.setTimer(1000, id -> promise.complete("Stranger!!"));
	    System.out.println("F1");
	  return promise.future();
	 }

	  private Future<String> anotherAsyncAction(String name) {
	    Promise<String> promise = Promise.promise();
	    // mimic something that take times
	    vertx.setTimer(100, l -> promise.complete("Hello " + name));
	    System.out.println("F2");
	    return promise.future();
	  }
	 
}
