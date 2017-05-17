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

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;

public class LocationGenerator {

	private static final MathContext mathContext = new MathContext(8);

	private static final Random random = new Random();


	private final BigDecimal longitude;

	private final BigDecimal latitude;


	public LocationGenerator(double longitude, double latitude) {
		this.longitude = new BigDecimal(longitude, mathContext);
		this.latitude = new BigDecimal(latitude, mathContext);
	}


	public Location location() {
		return new Location(longitude.add(randomDeviation(), mathContext),
				latitude.add(randomDeviation(), mathContext));
	}

	private BigDecimal randomDeviation() {
		return new BigDecimal((double) random.nextLong() % 100 / 1000000, mathContext);
	}
}
