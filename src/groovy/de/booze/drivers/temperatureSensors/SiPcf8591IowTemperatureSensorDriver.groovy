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

import org.apache.log4j.Logger
import de.booze.controllers.IoWarriorController
import de.booze.driverInterfaces.AbstractTemperatureSensorDriver

/**
 * Driver for an analogue temperature sensor
 * connected via a combination of IOWarrior and
 * a PCF8591 a/d converter
 *
 * @author akotsias
 */
class SiPcf8591IowTemperatureSensorDriver extends AbstractTemperatureSensorDriver {

  /**
   * Minimum temperature value
   */
  final private Double minimum = 10.0 as Double;

  /**
   * Maximum temperature value
   */
  final private Double maximum = 110.0 as Double;

  /**
   * Logger instance
   */
  private Logger log = Logger.getLogger(getClass().getName());

  /**
   * Controller instance
   */
  private IoWarriorController iow;

  /**
   * PCF8591 address (0..7)
   */
  private int pcf8591Address;

  /**
   * PCF8591 Channel
   */
  private int pcf8591Channel;

  final static public String ADDRESS_PATTERN = "iow://pcf/PCF-ADDRESS/CHANNEL"

  /**
   * Constructor
   * Gets an IOWController instance and registers the PCF8591 adc
   *
   * @param String Address for this device
   * @param Double Minimum temperature value
   * @param Double Maximum temperature value
   */
  public SiPcf8591IowTemperatureSensorDriver(String address) throws Exception, IllegalArgumentException {

    this.iow = IoWarriorController.getInstance()

    if (!SiPcf8591IowTemperatureSensorDriver.checkAddress(address)) {
      throw new IllegalArgumentException("Invalid address for this PCF8591TemperatureSensor: ${address}")
    }

    this.setAddress(address);

    this.iow.registerPCF8591(this.pcf8591Address);
  }

  /**
   * Returns temperature in degrees celsius
   */
  public Double getTemperature() throws Exception {

    Double value = 0.0 as Double;

    try {
      if (!this.iow.isConnected()) {
        this.iow.connect();
      }
      if (!this.iow.isPCF8591Registered(this.pcf8591Address)) {
        this.iow.registerPCF8591(this.pcf8591Address)
      }
      value = this.iow.getPCF8591(this.pcf8591Address).getChannel(this.pcf8591Channel)
    }
    catch (Exception e) {
      log.error("Could not read temperature from sensor ${e}")
    }

    return (value * ((this.maximum - this.minimum) / 256) + this.minimum) as Double;
  }

  /**
   * Checks if the given address is valid for this driver
   *
   * iow://pcf/'PCF8591-ADDRESS'/'ANALOG-INPUT-PORT'
   *
   * @param String Address string for checking
   */
  public static boolean checkAddress(String address) {
    return (address ==~ /iow:\/\/pcf\/\d+\/\d+/) ? true : false
  }

  /**
   * Sets the driver's address
   *
   * @param String Address to set
   */
  public void setAddress(String address) throws Exception {
    def tokens = address.tokenize("/")
    pcf8591Address = Integer.parseInt(tokens[2])
    pcf8591Channel = Integer.parseInt(tokens[3])
  }
}

