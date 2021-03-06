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

package de.booze.driverInterfaces

/**
 * Interface for motor regulator drivers
 * 
 * Used to control motor speed
 * The speed value is always given in percent of the 
 * maximum value.
 */
abstract class AbstractMotorRegulatorDriver extends AbstractDriver {

  public Integer speed

  /**
   * Sets the motor speed in percent
   */
  abstract public void setSpeed(Integer speed) throws Exception

  ;

  /**
   * Returns the actual motor speed
   */
  abstract public Integer getSpeed() throws Exception

  ;
}