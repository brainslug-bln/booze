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

package de.booze.drivers.temperatureSensors

import de.booze.driverInterfaces.AbstractTemperatureSensorDriver
import de.booze.regulation.DeviceSwitcher
import de.booze.regulation.*

/**
 * Dummy driver
 * Increases the temperature +0.01 every read
 *
 * @author akotsias
 */
class DummyTemperatureSensorDriver extends AbstractTemperatureSensorDriver {

  private Double lastTemperature = 48.0 as Double;

  private Double threshold = 5.0

  final static public String ADDRESS_PATTERN = "dummy"

  /**
   * Constructor
   *
   * @param String Address for this device
   */
  public DummyTemperatureSensorDriver(String address) throws Exception, IllegalArgumentException {

  }

  /**
   * Returns temperature in degrees celsius
   */
  public Double getTemperature() throws Exception {
    DeviceSwitcher d = DeviceSwitcher.getInstance();
    if (this.lastTemperature < (d.getTargetTemperature() + 2.0 as Double)) {
      this.lastTemperature += 0.002 * this.threshold as Double;

      if (this.threshold > 1.1) {
//        this.threshold -= (0.000005 as Double)
      }
    }
    return this.lastTemperature;
  }

  /**
   * Checks if the given address is valid for this driver
   *
   * dummy
   *
   * @param String Address string for checking
   */
  public static boolean checkAddress(String address) {
    return (address ==~ /dummy/) ? true : false
  }

  /**
   * Sets the driver's address
   *
   * @param String Address to set
   */
  public void setAddress(String address) throws Exception {

  }
}

