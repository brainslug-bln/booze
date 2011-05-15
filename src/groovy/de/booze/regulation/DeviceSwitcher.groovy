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

package de.booze.regulation

import java.util.Date

/**
 * Controller for switching devices with
 * high power consumption.
 * Before each switching call getNextSwitchingSlot() to
 * avoid simultaneous switching of multiple devices
 *
 * @author akotsias
 */
class DeviceSwitcher {

  /**
   * Singleton instance
   */
  private static DeviceSwitcher instance = new DeviceSwitcher();

  /**
   * Switching delay (milliseconds)
   */
  private static delay = 2000;

  /**
   * Stores the last switching timestamp
   */
  Date lastSwitch = new Date()

  Double targetTemperature = 1000;


  public static DeviceSwitcher getInstance() {
    return instance;

  }

  /**
   * Returns the time in ms to wait until
   * next switching is allowed
   */
  public Integer getNextSwitchingSlot() {
    Integer wait = 0
    if ((new Date()).getTime() - lastSwitch.getTime() < delay) {
      wait = (delay - (new Date()).getTime() - lastSwitch.getTime())
    }
    else {
      // Cache last switch (now)
      lastSwitch = new Date()
    }

    return wait
  }

  public void setTargetTemperature(Double t) {
    this.targetTemperature = t;
  }

  public Double getTargetTemperature() {
    return this.targetTemperature;
  }
}

