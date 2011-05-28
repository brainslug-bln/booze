/**
 * Booze - Software for micro breweries
 *
 * Copyright (C) 2011  Andreas Kotsias <akotsias@esnake.de>
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

class Setting {

  String name

  String description

  boolean active = false

  Date dateCreated
  Date lastUpdated

  /**
   * Cooking temperature
   * Usually a little bit above 100°C
   */
  Double cookingTemperature = 102.0d
  
  /**
   * Hysteresis 
   */
  Double hysteresis = 5.0d
  
  /**
   * Heating ramp in °C/minute
   */
  Double heatingRamp = 1.0d
  
  /**
   * Font size for the frontend
   */
  Integer frontendFontSize = 0;
  
  MotorDevice mashingMixer
  MotorDevice cookingMixer
  MotorDevice mashingPump
  MotorDevice cookingPump
  MotorDevice drainPump
  
  static hasMany = [heaters: HeaterDevice,
                    pressureSensors: PressureSensorDevice,
                    temperatureSensors: TemperatureSensorDevice]

  static constraints = {
    name(nullable: false, blank: false, size: 1..255, unique: true)
    description(nullable: true, blank: true, size: 0..5000)
    cookingTemperature(min: 95.0d, max: 110.0d, nullable: false)
    hysteresis(min: 0.5d, max: 10.0d, nullable: false)
    heatingRamp(min: 0.1d, max: 6.0d, nullable: false)
    frontendFontSize(min: 0, max: 5, nullable: false)
    mashingMixer(nullable: true)
    cookingMixer(nullable: true)
    mashingPump(nullable: true)
    cookingPump(nullable: true)
    drainPump(nullable: true)
  }

  static mapping = {
    heaters cascade: "evict,refresh"
    pressureSensors cascade: "evict,refresh"
    temperatureSensors cascade: "evict,refresh"
  }
}
