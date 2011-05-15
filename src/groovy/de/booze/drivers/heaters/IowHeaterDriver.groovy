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

package de.booze.drivers.heaters

import org.apache.log4j.Logger
import de.booze.controllers.IoWarriorController
import de.booze.driverInterfaces.AbstractHeaterDriver

/**
 *
 * Driver for a heater device connect via
 * an IOWarrior usb controller
 *
 * @author akotsias
 */
class BoozeHeaterDriver extends AbstractHeaterDriver {

  /**
   * Controller instance
   */
  IoWarriorController iow;

  /**
   * Logger instance
   */
  private Logger log = Logger.getLogger(getClass().getName());

  /**
   * Device port
   */
  Integer port

  /**
   * Device bit
   */
  Integer bit

  /**
   * Address pattern
   */
  final static public String ADDRESS_PATTERN = "iow://PORT/BIT"

  /**
   * Constructor
   */
  public IowHeaterDriver(String address) throws Exception {
    iow = IoWarriorController.getInstance()
    if (!IowHeaterDriver.checkAddress(address)) {
      throw new Exception("Invalid address for this IowHeaterDriver: ${address}")
    }
    this.setAddress(address)
  }

  /**
   * Returns true if the device is enabled
   */
  public boolean enabled() throws Exception {
    return !iow.isBitSet(port, bit)
  }

  /**
   * Enables the device
   */
  public void enable() throws Exception {
    iow.clearBit(port, bit)
  }

  /**
   * Disables the device
   */
  public void disable() throws Exception {
    iow.setBit(port, bit)
  }

  /**
   * Checks if the given address is valid for this driver
   *
   * iow://PORT/BIT
   *
   * @param String Address to check
   */
  public static boolean checkAddress(String address) {
    return (address ==~ /iow:\/\/\d+\/\d+/) ? true : false
  }

  /**
   * Sets the driver's address
   *
   * @param String Address to set
   */
  public void setAddress(String address) throws Exception {
    def tokens = address.tokenize("/")
    port = Integer.parseInt(tokens[1])
    bit = Integer.parseInt(tokens[2])
  }
}

