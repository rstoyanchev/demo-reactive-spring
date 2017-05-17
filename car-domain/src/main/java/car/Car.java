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
package car;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Car {

	private final Long id;

	private final Location location;


	@JsonCreator
	public Car(@JsonProperty("id") Long id, @JsonProperty("location") Location location) {
		this.id = id;
		this.location = location;
	}


	public Long getId() {
		return id;
	}

	public Location getLocation() {
		return location;
	}

	@Override
	public String toString() {
		return "Car{id=" + id + ", location=" + location + '}';
	}
}
