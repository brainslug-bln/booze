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
 *
 */
class MotorRegulatorDevice extends Device {
  
  /**
   * Set this value in milliseconds to enable soft upspinning
   */
  Integer softOn
  
  static belongsTo = [motor: MotorDevice]

  static constraints = {
    softOn(nullable: true, min: 0, max: 5000)
  }
  
  /**
   * Sets the motor device speed in percent
   * of the maximum value
   */
  public void writeSpeed(int speed) {
    driverInstance.setSpeed(speed);
  }

  /**
   * Returns the motor device speed in percent
   * of the maximum value
   */
  public int readSpeed() {
    return driverInstance.getSpeed();
  }
}
