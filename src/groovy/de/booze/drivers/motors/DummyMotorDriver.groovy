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

package de.booze.drivers.motors;

import de.booze.driverInterfaces.AbstractMotorDriver;

/**
 *
 * Driver a pump device connect via
 * an IOWarrior usb controller
 *
 * @author akotsias
 */
class DummyMotorDriver extends AbstractMotorDriver {

  private boolean enabled = false

  final static public String ADDRESS_PATTERN = "dummy"

  /**
   * Constructor
   */
  public DummyMotorDriver(String address) throws Exception {
  }

  /**
   * Returns true if the device is enabled
   */
  public boolean enabled() throws Exception {
    return this.enabled;
  }

  /**
   * Enables the device
   */
  public void enable() throws Exception {
    this.enabled = true;
  }

  /**
   * Disables the device
   */
  public void disable() throws Exception {
    this.enabled = false;
  }

  /**
   * Checks if the given address is valid for this driver
   *
   * iow://PORT/BIT
   */
  public static boolean checkAddress(String address) {
    return (address ==~ /dummy/) ? true : false
  }

  /**
   * Sets the driver's address
   *
   * @param String Address to set
   */
  private void setAddress(String address) throws Exception {
  }
}
