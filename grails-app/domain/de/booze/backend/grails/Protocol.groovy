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

class Protocol implements Serializable {
  
  final static Integer SUGAR_TYPE_GLUCOSE = 0
  final static Integer SUGAR_TYPE_SACCHAROSE = 1

  String recipeName

  String recipeDescription

  /**
   * Bitterness in IBU
   */
  Double ibu

  /**
   * Alcohol in Vol/0%
   */
  Double alcohol

  /**
   * Beer color in ebc
   */
  Double ebc

  /**
   * Targeted and final original wort
   */
  Double targetOriginalWort
  Double finalOriginalWort

  /**
   * Targeted and final pre-cooking wort
   */
  Double targetPostSpargingWort
  Double finalPostSpargingWort

  /**
   * Measured meshing wort (after mashing, before sparging)
   */
  Double targetPreSpargingWort
  Double finalPreSpargingWort

  /**
   * Targeted and final bottling wort
   */
  Double targetBottlingWort
  Double finalBottlingWort

  /**
   * Main water volume
   */
  Double mashingWaterVolume

  /**
   * Temperature for the main water
   */
  Double mashingTemperature
  

  /**
   * Targeted and final second water volume
   */
  Double targetSpargingWaterVolume
  Double finalSpargingWaterVolume

  /**
   * Temperature in °C for the second water
   */
  Double spargingTemperature

  /**
   * Targeted and final beer volume
   */
  Double finalBeerVolume
  Double targetFinalBeerVolume

  /**
   * Dilution water volume in l
   */
  Double dilutionWaterVolume


  /**
   * Targeted and final cooking time in minutes
   */
  Integer targetCookingTime
  Integer finalCookingTime

  /**
   * Lauter temperature in °C
   */
  Double lauterTemperature
  
  Double targetPostIsomerization
  Double finalPostIsomerization
  
  /**
   * Yeast info
   */
  String yeast
  
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
   * Amount of fare for storing in ml/l
   */
  Double fareVolume
  
  /**
   * Fare concentration in °Plato
   */
  Double fareConcentration
  
  /**
   * Amount of sugar added to the bottles or cans in g/l
   */
  Double fareSugar
  
  /**
   * CO2 concentration
   */
  Double co2Concentration
  
  /**
   * Glucose or saccharose
   */
  Integer fareSugarType = SUGAR_TYPE_GLUCOSE

  Date dateStarted
  Date dateFinished

  static hasMany = [temperatureValues: ProtocolTemperatureValue,
          pressureValues: ProtocolPressureValue,
          targetTemperatureValues: ProtocolTargetTemperatureValue,
          events: ProtocolEvent,
          rests: ProtocolRest,
          malts: ProtocolMalt,
          hops: ProtocolHop]

  static constraints = {
    recipeName(nullable: false, blank: false)
    recipeDescription(nullable: true, blank: true)

    dateFinished(nullable: true)

    ibu(min: 0.0 as Double, max: 2000 as Double, nullable: true)
    ebc(min: 0.0 as Double, max: 2000 as Double, nullable: true)

    alcohol(min: 0.0 as Double, max: 20.0 as Double, nullable: true)

    targetPostSpargingWort(min: 0.0 as Double, max: 40.0 as Double, nullable: true)
    finalPostSpargingWort(min: 0.0 as Double, max: 40.0 as Double, nullable: true)
    
    targetPreSpargingWort(min: 0.0 as Double, max: 40.0 as Double, nullable: true)
    finalPreSpargingWort(min: 0.0 as Double, max: 40.0 as Double, nullable: true)

    targetBottlingWort(min: 0.0 as Double, max: 40.0 as Double, nullable: true)
    finalBottlingWort(min: 0.0 as Double, max: 40.0 as Double, nullable: true)

    targetOriginalWort(min: 0.0 as Double, max: 40.0 as Double, nullable: false)
    finalOriginalWort(min: 0.0 as Double, max: 40.0 as Double, nullable: true)

    mashingWaterVolume(min: 0.0 as Double, max: 1000 as Double, nullable: false)

    targetSpargingWaterVolume(min: 0.0 as Double, max: 1000 as Double, nullable: true)
    finalSpargingWaterVolume(min: 0.0 as Double, max: 1000 as Double, nullable: true)
    spargingTemperature(min: 0.0 as Double, max: 1000.0 as Double, nullable: true)
    
    targetPostIsomerization(min: 0d, max: 1000d, nullable: true)
    finalPostIsomerization(min: 0d, max: 1000d, nullable: true)

    dilutionWaterVolume(min: 0.0 as Double, max: 1000 as Double, nullable: true)

    finalBeerVolume(nullable: true, min: 0.0 as Double, max: 1000 as Double)
    targetFinalBeerVolume(nullable: true, min: 0.0 as Double, max: 1000 as Double)

    lauterTemperature(min: 0.0 as Double, max: 100.0 as Double, nullable: true)
    mashingTemperature(min: 0.0 as Double, max: 100.0 as Double, nullable: true)
    spargingTemperature(min: 0.0 as Double, max: 100.0 as Double, nullable: true)

    yeast(nullable: false, blank: false)

    targetCookingTime(nullable: false, min: 0, max: 1000)
    finalCookingTime(nullable: true, min: 0, max: 1000)

    fareVolume(min: 0.0 as Double, max: 1000.0 as Double, nullable: true)
    fareConcentration(min: 0.0 as Double, max: 40.0 as Double, nullable: true)
    fareSugar(min: 0.0 as Double, max: 1000.0 as Double, nullable: true)
    fareSugarType(nullable: true)
    
    storingTime(nullable: true, min: 0d, max: 2000d)
    storingTemperature(nullable: true, min: 0d, max: 100d)
    
    fermentationTemperature(nullable: true, min: 0d, max: 100d)
    
    co2Concentration(nullable: true, min: 0d, max: 1000d)
  }

  static mapping = {
    temperatureValues cascade: "delete-orphan,all"
    pressureValues cascade: "delete-orphan,all"
    events cascade: "delete-orphan,all"
    /*columns {
        temperatureValues lazy: false
        pressureValues lazy: false
        events lazy: false
    }*/
  }
}
