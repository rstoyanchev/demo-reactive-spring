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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class CarController {

	private static final Logger logger = LoggerFactory.getLogger(CarController.class);


	private final WebClient carsClient = WebClient.create("http://localhost:8081");

	private final WebClient bookClient = WebClient.create("http://localhost:8082");


	@PostMapping("/booking")
	public Mono<ResponseEntity<Void>> book() {
		logger.debug("Processing booking request..");
		return carsClient.get().uri("/cars")
				.retrieve()
				.bodyToFlux(Car.class)
				.doOnNext(car -> logger.debug("Trying to book " + car))
				.take(5)
				.flatMap(this::requestCar)
				.next()
				.doOnNext(car -> logger.debug("Booked car " + car));
	}

	private Mono<ResponseEntity<Void>> requestCar(Car car) {
		return bookClient.post()
				.uri("/cars/{id}/booking", car.getId())
				.exchange()
				.flatMap(response -> response.toEntity(Void.class));
	}

}
