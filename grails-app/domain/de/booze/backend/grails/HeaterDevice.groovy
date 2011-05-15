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

package de.booze.backend.grails

class HeaterDevice extends Device {

  Long secondsOn = 0
  Date lastEnableTime
  
  HeaterRegulatorDevice regulator

  static transients = ["secondsOn", "lastEnableTime"]

  static constraints = {
      regulator(nullable: true)
  }

  public void enable() {
    if (!this.enabled()) {
      driverInstance.enable()
      this.lastEnableTime = new Date()
    }
  }

  public void disable() {
    if (this.enabled()) {
      driverInstance.disable();
      this.secondsOn += Math.round((new Date().getTime()) - this.lastEnableTime.getTime())
    }
  }

  public boolean enabled() {
    return driverInstance.enabled();
  }
}
