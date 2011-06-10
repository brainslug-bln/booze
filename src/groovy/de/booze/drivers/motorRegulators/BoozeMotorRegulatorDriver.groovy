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
 **/

package de.booze.drivers.motorRegulators

import java.util.*;

import org.apache.log4j.Logger
import de.booze.controllers.IoWarriorController
import de.booze.driverInterfaces.AbstractMotorRegulatorDriver
import de.booze.driverInterfaces.DriverOption

/**
 * Driver for an analogue pressure sensor
 * connected via a combination of IOWarrior and
 * a PCF8591 a/d converter
 *
 * @author akotsias
 */
class BoozeMotorRegulatorDriver extends AbstractMotorRegulatorDriver {

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
   * Acutal speed in percent
   */
  private Integer speed = 0;
  
  /**
   * Driver options
   */
  public static availableOptions = [new DriverOption("pcf8591Address", 
                                             "de.booze.drivers.motorRegulators.BoozeMotorRegulator.pcf8591Address.description",
                                             /[0-9]/)]

  /**
   * Constructor
   *
   * Gets an IOWController instance and registers the PCF8591 adc
   *
   */
  public BoozeMotorRegulatorDriver(Map o) throws Exception, IllegalArgumentException {
        
    // Try to get an IoWarrior instance
    this.iow = IoWarriorController.getInstance()
        
    this.setOptions(o);

    // Try to initially set the output value to 0
    this.iow.setPcf8591OutputValue(pcf8591Address, 0)
  }

  /**
   * Set the speed for this controller in percent
   *
   * @param int speed
   */
  public void setSpeed(Integer speed) throws IllegalArgumentException {
    if(speed >= 0 && speed <= 100) {
      this.iow.setPcf8591OutputValue(pcf8591Address, Math.round(speed * 2.54))
      this.speed = speed;
    }
    else {
      throw new IllegalArgumentException("Motor speed must be between 0 and 100");
    }
  }

  /**
   * Returns the actual speed
   */
  public Integer getSpeed() {
    return this.speed;
  }
    
}

