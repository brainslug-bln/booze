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
 * */

package de.booze.backend.grails

class Setting {

  String name

  String description

  boolean active

  Date dateCreated
  Date lastUpdated

  PumpDevice pump

  Double cookingTemperature

  Double heatingTemperatureOffset

  static hasMany = [heaters: HeaterDevice,
          pressureSensors: PressureSensorDevice,
          innerTemperatureSensors: TemperatureSensorDevice,
          outerTemperatureSensors: TemperatureSensorDevice]

  static constraints = {
    name(nullable: false, blank: false, size: 1..255)
    description(nullable: true, size: 0..5000)
    pump(nullable: false)
    heaters(validator: {val, obj ->
      if (!val || val.size() < 1) {
        return ['setting.heaters.notEnough']
      }
    })
    innerTemperatureSensors(validator: {val, obj ->
      if (!val || val.size() < 1) {
        return ['setting.temperatureSensors.notEnough']
      }
    })
    cookingTemperature(min: 95.0 as Double, max: 110.0 as Double, nullable: false)
    heatingTemperatureOffset(min: 0.0 as Double, max: 8.0 as Double, nullable: false)

  }

  static mapping = {
    heaters cascade: "evict,refresh"
    pressureSensors cascade: "evict,refresh"
    temperatureSensors cascade: "evict,refresh"
  }
}
