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

import java.net.URI;

import car.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class CarController {

	private Logger logger = LoggerFactory.getLogger(CarController.class);

	private WebClient carLocationClient = WebClient.create("http://localhost:8081");

	private WebClient carRequestClient = WebClient.create("http://localhost:8082");


	@PostMapping("/booking")
	public Mono<ResponseEntity<Void>> book() {

		return carLocationClient.get()
				.uri("/cars")
				.retrieve()
				.bodyToFlux(Car.class)
				.take(5)
				.flatMap(car -> {
					logger.debug("Requesting " + car);
					return carRequestClient.post()
							.uri("/cars/" + car.getId() + "/booking")
							.exchange()
							.map(this::toBookingResponseEntity);
				})
				.next();
	}

	private ResponseEntity<Void> toBookingResponseEntity(ClientResponse response) {
		HttpStatus status = response.statusCode();
		Assert.state(status.equals(HttpStatus.CREATED), "Booking failed: " + status);
		URI location = response.headers().asHttpHeaders().getLocation();
		return ResponseEntity.created(location).build();
	}

}
