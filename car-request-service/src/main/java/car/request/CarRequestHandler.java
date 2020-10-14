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
package car.request;

import java.net.URI;
import java.time.Duration;
import java.util.Random;

import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;


@Component
public class CarRequestHandler {

	private static final Random random = new Random();


	public Mono<ServerResponse> createBooking(ServerRequest request) {
		return Mono.delay(randomThinkTime())
				.then(ServerResponse.created(bookingUrl(request)).build());
	}

	/**
	 * Simulate driver accepting the request after "think time" of 2-5 secs.
	 */
	private static Duration randomThinkTime() {
		return Duration.ofSeconds(random.nextInt(5 - 2) + 2);
	}

	private static URI bookingUrl(ServerRequest request) {
		long id = Long.parseLong(request.pathVariable("id"));
		return URI.create("/car/" + id + "/booking/" + Math.abs(random.nextInt()));
	}

}
