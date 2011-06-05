/**
 * Booze - Software for micro breweries
 *
 * Copyright (C) 2010  Andreas Kotsias <akotsias@esnake.de>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * */

package de.booze.backend.grails

class RecipeService {

  boolean transactional = true

  /**
   * Evaporation per minute default factor
   */
  final static Double DEFAULT_EVAPORATION_FACTOR = 0.0155;

  /**
   * Calculates the EBU for a given
   * recipe
   *
   * @param recipe
   * @return Double
   */
  public Double calculateEBC(Recipe recipe) {
    def malts = recipe.malts
    return (malts.collect{it.amount * it.ebc}.sum())/(malts.collect{it.amount}.sum()) * (recipe.originalWort / 10)
  }

  /**
   * Estimates the final water amount for a given recipe
   */
  public Double estimateFinalWaterAmount(Recipe recipe) {

    Double water = recipe.mashingWaterVolume
    if (recipe.spargingWaterVolume) {
      water += recipe.spargingWaterVolume
    }

    return (water * (1 / (this.calculateAverageEvaporationFactor() * recipe.cookingTime)))
  }

  /**
   * Tries to "learn" the average evaporation volume during cooking from
   * old protocols.
   * Takes the default value if there are less then 2 protocols with valid
   * finalVolume
   */
  public Double calculateAverageEvaporationFactor() {

    return RecipeService.DEFAULT_EVAPORATION_FACTOR

//    def c = Protocol.createCriteria()
//    def results = c.list {
//      isNotNull("finalVolume")
//    }
//    
//    Double avgEvaporationFactor;
//    if (results.size() < 2) {
//      log.debug("found less then 2 valid protocol for evaporation learning")
//      return RecipeService.DEFAULT_EVAPORATION_FACTOR
//    }
//    else {
//      def factors = []
//      results.each {
//        def v1 = it.mainWaterVolume + (it.finalSecondWaterVolume ? it.finalSecondWaterVolume : 0) + (it.dilutionWaterVolume ? it.dilutionWaterVolume : 0)
//        def cookingTime = it.finalCookingTime ? it.finalCookingTime : it.targetCookingTime
//        factors.add(v1 / (it.finalVolume * cookingTime))
//      }
//
//      return (factors.sum() / factors.size()) as Double
//    }
  }

  /**
   * Calculate the beer color in ebc for the given recipe
   * @param recipe
   * @param originalWort
   * @return Double
   */
  public Double calculateBeerColor(Recipe recipe, Double originalWort) {
    Double totalFillWeight = 0.0D;
    Double colorWeightSum = 0.0D;

    recipe.malts.each {
      colorWeightSum += it.ebc * it.amount
      totalFillWeight += it.amount;
    }

    if(totalFillWeight == 0 || colorWeightSum == 0) {
      throw new Exception('Cannot calculate EBC without malt')
    }

    return 2 + (colorWeightSum/totalFillWeight) * originalWort / 10
  }

  /**
   * Wrapper for calculateBeerColor which uses the original
   * wort value from recipe
   *
   * @param recipe
   * @return Double
   */
  public Double calculateBeerColor(Recipe recipe) {
    return calculateBeerColor(recipe, recipe.originalWort)
  }

  /**
   * Validates only the properties corresponding to the 
   * submitted tab
   * 
   * Returns true if no validation errors emerge, 
   * otherwise false
   */
  public boolean validateRecipe(Recipe recipe, String tab) {
      
    boolean clean = true
    
    recipe.validate()
    switch(tab) {
      case 'mainData':
        ['name', 'description'].each() { it ->
          if(recipe.errors.getFieldErrorCount(it) > 0) {
            clean = false
          }
        }
        break;
      
      
      case 'mashing':
        ['mashingWaterVolume', 'postSpargingWort', 'lauterTemperature', 
         'mashingTemperature', 'doColdMashing', 'preSpargingWort', 'spargingWaterVolume'].each() { it ->
          if(recipe.errors.getFieldErrorCount(it) > 0) {
            clean = false
          }
        }
        recipe.rests?.each() { it ->
          if(!it.validate()) {
            clean = false
          }
        }
        recipe.malts?.each() { it ->
          if(!it.validate()) {
            clean = false
          }
        }
        break;
        
      case 'cooking':
        ['cookingTime', 'originalWort', 'postIsomerization'].each() { it ->
          if(recipe.errors.getFieldErrorCount(it) > 0) {
            clean = false
          }
        }
        recipe.hops?.each() { it ->
          if(!it.validate()) {
            clean = false
          }
        }
        break;
        
      case 'fermentation':
        ['yeast', 'fermentationTemperature', 'alcohol',
         'bottlingWort', 'storingTime', 'storingTemperature'].each() { it ->
          if(recipe.errors.getFieldErrorCount(it) > 0) {
            clean = false
          }
        }
    }
    
    return clean
  }
}
