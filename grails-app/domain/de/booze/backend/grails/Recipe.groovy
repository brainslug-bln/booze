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

class Recipe implements Serializable {
  
  def recipeService, hopService

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
   * Wort in °Plato before sparging starts
   */
  Double preSpargingWort
  
  /**
   * Wort in °Plato after sparging
   */
  Double postSpargingWort

  /**
   * Wort before bottling in °Plato
   */
  Double bottlingWort

  /**
   * Mashing water amount in l
   */
  Double mashingWaterVolume

  /**
   * Sparging water volume in l
   */
  Double spargingWaterVolume

  /**
   * Cooking time in minutes
   */
  Double cookingTime

  /**
   * Temperature at which the malt is removed from
   * the water (lautering) in °C
   */
  Double lauterTemperature

  /**
   * Temperature at which the malt is added
   * to the water in °C
   */
  Double mashingTemperature

  /**
   * Temperature at which the sparging water
   * is added to the mesh in °C
   */
  Double spargingTemperature
  
  /**
   * Isomerization time after cooking in minutes
   */
  Double postIsomerization
  
  /**
   * Fermentation temperature in °C
   */
  Double fermentationTemperature
  
  /**
   * Storing period in weeks
   */  
  Double storingTime
  
  /**
   * Storing temperature in °C
   */
  Double storingTemperature
    
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
  String author
  
  /**
   * Transient value
   * Do cold mashing (without pre-heating the mashing water) or not
   */
  boolean doColdMashing = true  
    
  /**
   * Transient property
   * Estimated volume of beer output
   */
  Double finalBeerVolume
    
  /**
   * Transient property
   * Estimated IBU bitterness
   */
  Double ibu
    
  /**
   * Transient property
   * Estimated EBU beer color
   */
  Double ebc
  
  Double co2Concentration

  SortedSet rests
  SortedSet malts
  SortedSet hops

  static hasMany = [rests: RecipeRest, hops: RecipeHop, malts: RecipeMalt]

  static transients = ['doColdMashing', 'finalBeerVolume', 'ibu', 'ebc']
  
  static constraints = {
    name(size: 3..255, nullable: false, blank: false)
    globalId(size: 1..255, nullable: false, blank: false, unique: true)
    description(size: 0..5000, nullable: true, blank: true)
    author(size:1..255, nullable: true, blank: true)
    alcohol(min: 0.0 as Double, max: 40.0 as Double, nullable: true)
    preSpargingWort(min: 0.0 as Double, max: 50.0 as Double, nullable: true)
    postSpargingWort(min: 0.0 as Double, max: 50.0 as Double, nullable: true)
    bottlingWort(min: 0.0 as Double, max: 20.0 as Double, nullable: true)
    originalWort(min: 0.0 as Double, max: 50.0 as Double, nullable: false)
    cookingTime(min: 0.0 as Double, max: 1000 as Double, nullable: false)
    mashingTemperature(min: 0.0 as Double, max: 100 as Double, nullable: true, validator: {val, obj ->
        if (val == null && obj.doColdMashing == false) {
          ['recipe.mashingTemperature.nullable']
        }
      })
    mashingWaterVolume(min: 0.0 as Double, max: 1000 as Double, nullable: false)
    spargingWaterVolume(min: 0.0 as Double, max: 1000 as Double, nullable: true)
    spargingTemperature(min: 0.0 as Double, max: 100 as Double, nullable: true,  validator: {val, obj ->
        if (val == null && obj.spargingWaterVolume != null ) {
          ['recipe.spargingTemperature.nullable']
        }
      })
    lauterTemperature(min: 0.0 as Double, max: 100 as Double, nullable: false)
    postIsomerization(min: 0.0 as Double, max: 1000 as Double, nullable: true)
    fermentationTemperature(min: 0.0 as Double, max: 50 as Double, nullable: false)
    storingTime(min: 0.0 as Double, max: 1000 as Double, nullable: true)
    storingTemperature(min: 0.0 as Double, max: 50 as Double, nullable: true)
    yeast(nullable: false, blank: false, size: 3..2000)
    co2Concentration(nullable: true, min: 0d, max: 1000d)
  }

  static mapping = {
    malts cascade: "all-delete-orphan"
    hops cascade: "all-delete-orphan"
    rests cascade: "all-delete-orphan"
    columns {
      malts lazy: false
      hops lazy: false
      rests lazy: false
    }
  }

  def calculateData = {
    if(malts && malts.size() > 0) {
      try {
        ebc = recipeService.calculateBeerColor(this)
      }
      catch(Exception e) {
        log.error("Could not calculate beer color: ${e}")
      }
    }
    
    if(mashingWaterVolume) {
      try {
        finalBeerVolume = recipeService.estimateFinalWaterAmount(this)
      }
      catch(Exception e) {
        log.error("Could not estimate final beer amount: ${e}")
      }
    }
    
    if(hops && hops.size() > 0) {
      try {
        ibu = hopService.estimateIbu(this)
      }
      catch(Exception e) {
        log.error("Could not estimate IBU: ${e}")
      }
    }
  }
  
  def afterLoad = {
    if(!mashingTemperature) {
      doColdMashing = true
    }
    else {
      doColdMashing = false
    }
    this.calculateData()
  }
  
  def afterUpdate = {
    this.calculateData()
  }
  
  def beforeInsert = {
    if(doColdMashing == true) {
      mashingTemperature = null
    }
  }
  
  def beforeUpdate = {
    if(doColdMashing == true) {
      mashingTemperature = null
    }
  }
}
