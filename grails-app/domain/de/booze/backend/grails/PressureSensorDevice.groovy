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

class PressureSensorDevice extends Device {

  /**
   * Pressure in mbar at which the whole brew process is paused
   */
  Double pressureMaxLimit

  static constraints = {
    pressureMaxLimit(nullable: false, min: 0.0 as Double, max: 10000.0 as Double)
  }

  final static int MAX_AVERAGE_VALUES = 50

  public Double readPressure() throws Exception {
    if (!driverInstance) {
      throw new Exception("Could not read pressure from driver: no driver instance")
    }

    return driverInstance.getPressure() as Double
  }

}
