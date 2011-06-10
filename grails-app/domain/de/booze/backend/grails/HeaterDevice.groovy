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
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import grails.converters.JSON

/**
 * Basic actions are enable/disable.
 * Optionally heating power may be regulated by a MotorRegulatorDevice.
 */
class HeaterDevice extends Device {

  Long secondsOn = 0
  Date lastEnableTime
  
  boolean forced = false
  
  HeaterRegulatorDevice regulator

  static transients = ["secondsOn", "lastEnableTime", "forced"]
  
  static constraints = {
    regulator(nullable: true)
  }
  
  static mapping = {
    columns {
      regulator lazy: false
    }
  }

  /**
   * Enables the heater
   */
  public void enable() {
    if (!this.enabled()) {
      driverInstance.enable()
      this.lastEnableTime = new Date()
    }
  }

  /**
   * Disabled the heater
   */
  public void disable() {
    if (this.enabled()) {
      driverInstance.disable();
      this.secondsOn += Math.round((new Date().getTime()) - this.lastEnableTime.getTime())
    }
  }

  /**
   * Returns true if the heater is enabled, false if not
   */
  public boolean enabled() {
    return driverInstance.enabled();
  }
  
  /**
   * Sets the device power if a regulator is available
   */
  public void writePower(int s) {
    if(this.hasRegulator()) {
      this.regulator.writePower(s);
    }
  }
  
  /**
   * Returns the device power if a regulator is available
   */
  public int readPower() {
    if(this.hasRegulator()) {
      return this.regulator.readPower()
    }
  }
   
  /**
   * Checks if this device has a power regulator
   */
  public boolean hasRegulator() {
    return (this.regulator != null)
  }
  
  /**
   * Toggles the force mode for this heater
   */
  public void toggleForce() {
    // STUB
    if(!this.forced) this.forced = true
    else this.forced = false
  }
  
  /**
   * Returns heater force status
   */
  public boolean forced() {
    return this.forced
  }
  
  /**
   * Init the device driver
   * Store an instance of it in the transient driverInstance
   */
  def initDevice() {
    def myClassLoader = AH.application.mainContext.getClassLoader()
    def myClass = Class.forName(driver, false, myClassLoader)
    driverInstance = myClass.newInstance(JSON.parse(options));
    
    if(this.hasRegulator()) {
      this.regulator.initDevice()
    }
  }

  /**
   * Gracefully shut down a driver instance
   */
  def shutdown() {
    if(this.hasRegulator()) {
      this.regulator.shutdown()
    }
    
    driverInstance.shutdown()
    driverInstance = null
  }
}
