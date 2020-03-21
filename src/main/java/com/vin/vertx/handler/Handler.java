package com.vin.vertx.handler;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import com.vin.vertx.model.Tracker;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;



public class Handler {

	public static void showIndex(RoutingContext routingContext) {

		System.out.println("Thread running on "+Thread.currentThread().getName());
		Calendar today = Calendar.getInstance();
		String URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/03-10-2020.csv";
		final SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("MM-dd-yyyy"); 
		
		
		// In order to use a Thymeleaf template we first need to create an engine
		final ThymeleafTemplateEngine engine = ThymeleafTemplateEngine.create(routingContext.vertx());

		routingContext.vertx().executeBlocking(promise -> {
			String[] splitString = URL.split("/");
		
		
			for(int i = 0; i<10; i++) {
				today.add(Calendar.DATE, -i);
				splitString[splitString.length - 1] = simpleDateFormatter.format(today.getTime());
				System.out.println("Index "+i);
				Future<String> future = getResponseBody(routingContext,splitString.toString());
				future.onSuccess(event -> {
					if()
				});
				future.compose(result -> getTrackerList(result))
				.setHandler(ar -> {
					if(ar.succeeded()) {
						// and now delegate to the engine to render it.
						engine.render(ar.result(), "index.html", res -> {
							if (res.succeeded()) {
								routingContext.response().end(res.result());
							} else {
								routingContext.fail(res.cause());
							}
						});
					}
				});			
			}
		}
		,true, resultHandler -> {
			if(resultHandler.succeeded()) {
				resultHandler.result();
			}
		});
	}


	private static  Future<JsonObject> getTrackerList(String body) {
		System.out.println("Second");
		List<Tracker> trackerList = new ArrayList<>();
		Promise<JsonObject> promise = Promise.promise();
		System.out.println("After response");
		System.out.println(" body " +body);

		if(body != null && body.trim().length() > 1) {
			StringReader stringReader = new StringReader(body);
			Iterable<CSVRecord> records = null;
			try {
				records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);
			} catch (IOException e) {
				e.printStackTrace();
			}

			JsonObject trackerJson = new JsonObject();
			for (CSVRecord record : records) {
				Tracker tracker = new Tracker();
				tracker.setCountry(record.get("Country/Region"));
				tracker.setState(record.get("Province/State"));

				tracker.setConfirmed(
						record.get("Confirmed").length() > 0?
								Integer.parseInt(record.get("Confirmed")): 0);

				tracker.setDeaths(record.get("Deaths").length() > 0?
						Integer.parseInt(record.get("Deaths")): 0);

				tracker.setRecovered(record.get("Recovered").length() > 0?
						Integer.parseInt(record.get("Recovered")): 0);			
				trackerList.add(tracker);
			}

			trackerJson.put("tracker", trackerList);
			System.out.println("Thread running on "+Thread.currentThread().getName());
			promise.complete(trackerJson);
		}
		return promise.future();

	}


	private static Future<String> getResponseBody(RoutingContext routingContext, String url) {
		System.out.println("First");
		Promise<String> promise = Promise.promise();
		System.out.println("Thread from getResponseBody "+Thread.currentThread().getName());
		System.out.println("In Body");
		WebClient webClient = WebClient.create(routingContext.vertx());
		HttpRequest<Buffer> httpRequest = webClient.getAbs(url);
		System.out.println("Thread from "+Thread.currentThread().getName());
		httpRequest.send(ar -> {
			if(ar.succeeded()) {
				System.out.println("Added data");
				promise.complete(ar.result().bodyAsString());
			}else {
				System.out.println(ar.cause());
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}
}

