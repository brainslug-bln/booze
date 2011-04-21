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
 **/
package de.booze.backend.grails

import java.util.Random

class Recipe {

  def toString = { name }

  String name
  String description

  /**
   * Alcolhol in vol%
   */
  Double alcohol

  /**
   * Final (after cooking) wort
   * in °Plato
   */
  Double originalWort

  /**
   * Wort in °Plato before cooking starts
   */
  Double preCookingWort

  /**
   * Wort before bottling in °Plato
   */
  Double bottlingWort

  /**
   * Main water amount in l
   */
  Double mainWaterVolume

  /**
   * Second water volume in l
   */
  Double secondWaterVolume

  /**
   * Cooking time in minutes
   */
  Integer cookingTime

  /**
   * Temperature at which the malt is removed from
   * the water (clarification) in °C
   */
  Double meshTemperature

  /**
   * Temperature at which the malt is added
   * to the water in °C
   */
  Double fillTemperature

  /**
   * Temperature at which the second water
   * is added to the mesh in °C
   */
  Double secondWaterTemperature

  /**
   * Yeast to use for fermentation
   */
  String yeast

  Date dateCreated
  Date lastUpdated

  /**
   * The global ID is used for identification of shared recipes
   * on the community server
   */
  String globalId

  /**
   * Recipe owner
   */
  String owner

  SortedSet rests

  static hasMany = [rests: RecipeRest, hops: RecipeHop, malts: RecipeMalt, additives: RecipeAdditive]

  static constraints = {
    name(size: 3..255, nullable: false, blank: false)
    globalId(size: 1..255, nullable: false, blank: false, unique: true)
    description(size: 0..5000, nullable: true, blank: true)
    alcohol(min: 0.0 as Double, max: 20.0 as Double, nullable: true)
    preCookingWort(min: 0.0 as Double, max: 20.0 as Double, nullable: true)
    bottlingWort(min: 0.0 as Double, max: 20.0 as Double, nullable: true)
    originalWort(min: 0.0 as Double, max: 20.0 as Double, nullable: false)
    meshTemperature(min: 0.0 as Double, max: 100 as Double, nullable: false)
    mainWaterVolume(min: 0.0 as Double, max: 1000 as Double, nullable: false)
    secondWaterVolume(min: 0.0 as Double, max: 1000 as Double, nullable: true, validator: {val, obj ->
      if (val != null && obj.secondWaterTemperature == null) {
        ['recipe.secondWaterTemperature.nullable']
      }
    })
    yeast(nullable: false, size: 5..2000)
  }

  static mapping = {
    malts cascade: "all-delete-orphan"
    hops cascade: "all-delete-orphan"
    rests cascade: "all-delete-orphan"
    additives cascade: "all-delete-orphan"
    columns {
      malts lazy: false
      hops lazy: false
      rests lazy: false
      additives lazy: false
    }
  }

  def beforeSave = {
      // Generate a unique id for this recipe from various variables
      Random random = new Random((new Date()).getTime())
      globalId = (random.next(20) + (new Date().getTime()) + name + description + cookingTime + originalWort).encodeAsMD5();
  }
}

class RecipeGeneralCommand {
    String name
    String description
    Double alcohol
    Double originalWort

    static constraints = {
        name(size: 3..255, nullable: false, blank: false)
        description(size: 0..5000, nullable: true, blank: true)
        alcohol(min: 0.0 as Double, max: 20.0 as Double, nullable: true)
        originalWort(min: 0.0 as Double, max: 20.0 as Double, nullable: false)
    }
}

class RecipeFillsCommand {
    
}

class RecipeRestsCommand {
    
}

class RecipeCookingCommand {

}