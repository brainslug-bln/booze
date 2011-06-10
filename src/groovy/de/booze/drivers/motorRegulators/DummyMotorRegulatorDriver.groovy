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
import de.booze.driverInterfaces.AbstractMotorRegulatorDriver

/**
 * Driver for an dummy motor regulator
 *
 * @author akotsias
 */
class DummyMotorRegulatorDriver extends AbstractMotorRegulatorDriver {

  /**
   * Logger instance
   */
  private Logger log = Logger.getLogger(getClass().getName());

  /**
   * Acutal speed in percent
   */
  private Integer speed = 0;
  
  /**
   * Driver options
   */
  public static availableOptions = []

  /**
   * Constructor
   */
  public DummyMotorRegulatorDriver(Map o) throws Exception, IllegalArgumentException {
    this.setOptions(o);
  }

  /**
   * Set the speed for this controller in percent
   *
   * @param int speed
   */
  public void setSpeed(Integer speed) throws IllegalArgumentException {
    if(speed >= 0 && speed <= 100) {
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

