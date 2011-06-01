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

package de.booze.drivers.motors

import org.apache.log4j.Logger
import de.booze.controllers.IoWarriorController
import de.booze.driverInterfaces.AbstractMotorDriver
import de.booze.driverInterfaces.DriverOption

/**
 *
 * Driver for a motor device connected via
 * an IOWarrior usb controller
 * 
 * Only for switching a connected motor on and off
 *
 * @author akotsias
 */
class BoozeMotorDriver extends AbstractMotorDriver {

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
   * Driver options
   */
  public static availableOptions = [new DriverOption("port", 
                                                 "de.booze.drivers.heaters.BoozeMotor.port.description",
                                                 /[0-9]/),
                                     new DriverOption("bit", 
                                                 "de.booze.drivers.heaters.BoozeMotor.bit.description",
                                                 /[0-9]/)]
    
    
    /**
     * Constructor
     */
    public BoozeMotorDriver(Map o) throws Exception {
        
        // Try to get an IoWarrior instance
        this.iow = IoWarriorController.getInstance()
        
        this.setOptions(o);
        
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
   * Device shutdown
   */
  public void shutdown() throws Exception {
    this.disable()
  }
}

