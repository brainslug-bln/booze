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

package de.booze.drivers.pressureSensors

import java.util.Random
import de.booze.driverInterfaces.AbstractPressureSensorDriver

/**
 * Driver for an analogue pressure sensor
 * connected via a combination of IOWarrior and
 * a PCF8591 a/d converter
 *
 * @author akotsias
 */
class DummyPressureSensorDriver extends AbstractPressureSensorDriver {

  /**
   * Address pattern
   */
  final static public String ADDRESS_PATTERN = "dummy"

  /**
   * Constructor
   *
   * Gets an IOWController instance and registers the PCF8591 adc
   * If successful a timer is started which adds a pressure value
   * to valueList every 50ms
   */
  public DummyPressureSensorDriver(String address) throws Exception, IllegalArgumentException {
  }

  /**
   * Returns pressure in mbar
   */
  public Double getPressure() throws Exception {
    def rand = new Random()
    return (Double) rand.nextDouble() * 300;
    //return 100.0 as Double
  }

  /**
   * Checks if the given address is valid for this driver
   *
   * iow://pcf/'PCF8591-ADDRESS'/'ANALOG-INPUT-PORT'
   */
  public static boolean checkAddress(String address) {
    return (address ==~ /dummy/) ? true : false
  }

  /**
   * Sets the driver's address
   */
  public void setAddress(String address) throws Exception {
  }
}

