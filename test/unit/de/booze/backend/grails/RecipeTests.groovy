package de.booze.backend.grails

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainUnitTestMixin} for usage instructions
 */
@TestFor(Recipe)
class RecipeTests {
	
	void testCreateRecipe() {
		
		Recipe r = new Recipe([
			name: "testRecipe",
			description: "empty",
			alcohol: 5.0d,
			originalWort: 12.0d,
			preSpargingWort: 10.0d,
			postSpargingWort: 10.5d,
			bottlingWort: 3.5d,
			mashingWaterVolume: 50.0d,
			spargingWaterVolume: 20.0d,
			cookingTime: 90.0d,
			lauterTemperature: 78.0d,
			mashingTemperature: 38.0d,
			spargingTemperature: 78.0d,
			postIsomerization: 5.0d,
			fermentationTemperature: 10.0d,
			storingTime: 5.0d,
			storingTemperature: 1.0d,
			yeast: "simpleTestYeast",
			globalId: "ad772jd29jhs87g237r",
			author: "testAuthor",
			co2Concentration: 5.5d
			]);
		
		r.save(flush:true)
		
		assert Recipe.list().size() == 1
	}
}