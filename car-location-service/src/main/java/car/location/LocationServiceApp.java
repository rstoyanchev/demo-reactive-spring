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
package car.location;


import java.time.Duration;

import car.Car;
import car.LocationGenerator;
import reactor.core.publisher.Flux;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;

@SpringBootApplication
public class LocationServiceApp {


	public static void main(String[] args) {
		SpringApplication.run(LocationServiceApp.class, args);
	}


	@Bean
	public CommandLineRunner initData(MongoOperations mongo) {
		return (String... args) -> {

			mongo.dropCollection(Car.class);
			mongo.createCollection(Car.class, CollectionOptions.empty().size(1000000).capped());

			LocationGenerator gen = new LocationGenerator(40.740900, -73.988000);
			Flux.range(1, 100)
					.map(i -> new Car(i.longValue(), gen.location()))
					.doOnNext(mongo::save)
					.blockLast(Duration.ofSeconds(5));
		};
	}

}