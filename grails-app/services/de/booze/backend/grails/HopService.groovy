package de.booze.backend.grails

import java.lang.Math

class HopService {

  boolean transactional = true

  def recipeService

  /**
   * Estimates the IBU for a given protocol
   * @param protocol
   * @return
   */
  public Double estimateIbu(Protocol protocol) {
	  Double cookingTime = protocol.finalCookingTime?protocol.finalCookingTime:protocol.targetCookingTime
	  Double finalWaterAmount = protocol.finalBeerVolume?protocol.finalBeerVolume:recipeService.estimateFinalWaterAmount(protocol)
	  return this.calculateIbu(protocol.hops, cookingTime, finalWaterAmount, this.estimateAverageWort(protocol));
  }
  
  /**
   * Estimates the IBU for a given recipe
   * @param recipe
   * @return
   */
  public Double estimateIbu(Recipe recipe) {
    return this.calculateIbu(recipe.hops, recipe.cookingTime, recipeService.estimateFinalWaterAmount(recipe), this.estimateAverageWort(recipe));
  }
  
  /**
   * Convenience method
   * @param recipe
   * @param waterAmount
   * @param averageWort
   * @return
   */
  public Double calculateIbu(Recipe recipe, Double waterAmount, averageWort) {
    return this.calculateIbu(recipe.hops, recipe.cookingTime, waterAmount, averageWort);

	}

  /**
   * Calculates the IBU for given values for hops, cooking time, water amount and average wort
   * 
   * @param hops
   * @param cookingTime
   * @param waterAmount
   * @param averageWort
   * @return
   */
  public Double calculateIbu(hops, Double cookingTime, Double waterAmount, averageWort) {
    Double ibu = 0.0 as Double;

    hops.each {
      Double mgPerLiter = ((it.percentAlpha / 100) * it.amount * 1000) / waterAmount;
      Double aau = this.calculateAlphaAcidUtilization(averageWort, (cookingTime - it.time));
      ibu += aau * mgPerLiter;
    }

    return ibu;
  }

  /**
   * Calculates the alpha acid utilization
   * 
   * @param averageWort
   * @param cookingTime
   * @return
   */
  public Double calculateAlphaAcidUtilization(averageWort, cookingTime) {

    Double gravity = 1 + (averageWort / (258.6 - (0.8796 + averageWort)))

    Double bignessFactor = 1.65 * java.lang.Math.pow(0.000125, (gravity - 1));

    Double boilTimeFactor = (1 - java.lang.Math.pow(java.lang.Math.E, (-0.04 * cookingTime))) / 4.15;

    return (bignessFactor * boilTimeFactor) as Double;
  }

  /**
   * Estimates the average wort value in °Plato for a given recipe
   * @param recipe
   * @return
   */
  private Double estimateAverageWort(Recipe recipe) {
    Double preSpargingWort = recipe.preSpargingWort ? recipe.preSpargingWort : (recipe.originalWort * 0.8);
    return ((preSpargingWort + recipe.originalWort) / 2) as Double;
  }
  
  /**
   * Estimates the average wort value in °Plato for a given protocol
   * @param protocol
   * @return
   */
  private Double estimateAverageWort(Protocol protocol) {
	  Double preSpargingWort = 0
	  Double originalWort = protocol.finalOriginalWort?protocol.finalOriginalWort:protocol.targetOriginalWort
	  
	  if(protocol.finalPreSpargingWort) { preSpargingWort = protocol.finalPreSpargingWort }
	  else if(protocol.targetPreSpargingWort) { preSpargingWort = protocol.targetPreSpargingWort }
	  else {  preSpargingWort = originalWort * 0.8 }
	  
	  return ((preSpargingWort + originalWort) / 2) as Double;
  }
}