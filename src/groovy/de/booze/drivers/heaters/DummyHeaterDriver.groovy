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

package de.booze.drivers.heaters

import org.apache.log4j.Logger
import de.booze.driverInterfaces.AbstractHeaterDriver

/**
 *
 * Driver for a dummy heater device
 *
 * @author akotsias
 */
class DummyHeaterDriver extends AbstractHeaterDriver {

  /**
   * Logger instance
   */
  private Logger log = Logger.getLogger(getClass().getName());

  private boolean enabled = false;
  
  /**
   * Driver options
   */
  public static availableOptions = []


  /**
   * Constructor
   */
  public DummyHeaterDriver(Map o) throws Exception {
    this.setOptions(o);
    
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
   * Device shutdown
   */
  public void shutdown() throws Exception {
    this.disable()
  }
}

