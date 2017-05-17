/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package car.app;

import java.time.Duration;

import car.Car;
import car.LocationGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class ClientUploadApp {

	private static Logger logger = LoggerFactory.getLogger(ClientUploadApp.class);


	public static void main(String[] args) {

		WebClient client = WebClient.create("http://localhost:8081");

		LocationGenerator gen = new LocationGenerator(40.740900, -73.988000);
		Flux<Car> cars = Flux.interval(Duration.ofSeconds(2)).map(i -> new Car(i + 200, gen.location()));

		client.post()
				.uri("/cars")
				.contentType(MediaType.APPLICATION_STREAM_JSON)
				.body(cars, Car.class)
				.retrieve()
				.bodyToFlux(Car.class)
				.doOnNext(car -> logger.debug("Uploaded: " + car))
				.blockLast();

	}

}
