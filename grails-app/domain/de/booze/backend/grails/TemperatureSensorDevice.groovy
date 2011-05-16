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

/**
 * Represents a temperature sensor
 */
class TemperatureSensorDevice extends Device {

  static transients = ['lastValue']

  Double lastValue = 0.0 as Double;
  
  /**
   * Is this a referenceSensor for mashing?
   */
  boolean referenceForMashing = false
  
  /**
   * Is this a referenceSensor for cooking?
   */
  boolean referenceForCooking = false
  
  static belongsTo = [setting: Setting]

  /**
   * Returns the last measured (cached) temperature in °C
   * Does NOT actually read a value from the sensor
   */
  public Double readTemperature() throws Exception {
    return lastValue;
  }

  /**
   * Reads the temperature in °C directly from the sensor
   */
  public Double readTemperatureImmediate() throws Exception {
    if (!driverInstance) {
      throw new Exception("Temperature sensor ${this.getClass().getName()}:${this.name} could not read temperature from driver: no driver instance")
    }

    this.lastValue = driverInstance.getTemperature() as Double
    return this.lastValue;
  }
}
