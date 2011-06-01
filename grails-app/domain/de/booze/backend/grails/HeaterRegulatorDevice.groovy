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

class HeaterRegulatorDevice extends Device {

  static constraints = {
  }
  
  static belongsTo = [heater:HeaterDevice]

  /**
   * Sets the heater power in percent
   * of the maximum value
   */
  public void writePower(int power) {
    driverInstance.setPower(power);
  }

  /**
   * Returns the heater power in percent
   * of the maximum value
   */
  public int readPower() {
    return driverInstance.getPower();
  }
}
