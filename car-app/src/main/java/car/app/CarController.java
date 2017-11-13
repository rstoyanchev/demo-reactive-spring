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

import car.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class CarController {

	private static final Logger logger = LoggerFactory.getLogger(CarController.class);


	private final WebClient carLocationClient;

	private final WebClient carRequestClient;


	public CarController() {

		ReactorClientHttpConnector connector = new ReactorClientHttpConnector();

		carLocationClient = WebClient.builder()
				.baseUrl("http://localhost:8081")
				.clientConnector(connector)
				.build();

		carRequestClient = WebClient.builder()
				.baseUrl("http://localhost:8082")
				.clientConnector(connector)
				.build();
	}


	@PostMapping("/booking")
	public Mono<ResponseEntity<Void>> book() {

		logger.debug("Processing booking request..");

		return carLocationClient.get()
				.uri("/cars")
				.retrieve()
				.bodyToFlux(Car.class)
				.doOnNext(car -> logger.debug("Requesting to book " + car))
				.take(5)
				.flatMap(car ->
						carRequestClient.post()
								.uri("/cars/{id}/booking", car.getId())
								.exchange()
								.flatMap(response -> response.toEntity(Void.class)))
				.next()
				.doOnNext(car -> logger.debug("Booked car " + car));
	}

}
