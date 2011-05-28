package de.booze.backend.grails

import java.lang.Math

class HopService {

  boolean transactional = true

  def recipeService

  public Double estimateIbu(Recipe recipe) {
    return this.calculateIbu(recipe, recipeService.estimateFinalWaterAmount(recipe), this.estimateAverageWort(recipe));
  }

  public Double calculateIbu(Recipe recipe, Double waterAmount, averageWort) {
    Double ibu = 0.0 as Double;

    recipe.hops.each {
      Double mgPerLiter = ((it.percentAlpha / 100) * it.amount * 1000) / waterAmount;
      Double aau = this.calculateAlphaAcidUtilization(averageWort, (recipe.cookingTime - it.time));
      ibu += aau * mgPerLiter;
    }

    return ibu;
  }

  public Double calculateAlphaAcidUtilization(averageWort, cookingTime) {

    Double gravity = 1 + (averageWort / (258.6 - (0.8796 + averageWort)))

    Double bignessFactor = 1.65 * java.lang.Math.pow(0.000125, (gravity - 1));

    Double boilTimeFactor = (1 - java.lang.Math.pow(java.lang.Math.E, (-0.04 * cookingTime))) / 4.15;

    return (bignessFactor * boilTimeFactor) as Double;
  }

  private Double estimateAverageWort(Recipe recipe) {
    Double preCookingWort = recipe.preCookingWort ? recipe.preCookingWort : (recipe.originalWort * 0.8);
    return ((preCookingWort + recipe.originalWort) / 2) as Double;
  }
}