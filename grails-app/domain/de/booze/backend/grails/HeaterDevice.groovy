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
  
  /**
   * True if the heater is controlled by the user
   */
  boolean forced = false
  
  /**
   * If the heater is forced (^= manually controlled by the user)
   * the programmatically assigned status is saved in this
   * variable
   */
  boolean realEnabled = false
 
  HeaterRegulatorDevice regulator

  static transients = ["secondsOn", "lastEnableTime", "forced", "realEnabled"]
  
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
    // Write the programmatically assigned status
    // to a variable, don't really change anything
    if(this.forced) {
      this.realEnabled = true;
      return;
    }
    
    if (!this.enabled()) {
      driverInstance.enable()
      this.lastEnableTime = new Date()
    }
  }
  
  /**
   * Enables the heater in forced mode
   */
  public void forceEnable() {
    driverInstance.enable()
    this.lastEnableTime = new Date()
  }

  /**
   * Disabled the heater
   */
  public void disable() {
    // Write the programmatically assigned status
    // to a variable, don't really change anything
    if(this.forced) {
      this.realEnabled = false;
      return;
    }
    
    if (this.enabled()) {
      driverInstance.disable();
      this.secondsOn += Math.round((new Date().getTime()) - this.lastEnableTime.getTime())
    }
  }
  
  /**
   * Disables the heater in forced mode
   */
  public void forceDisable() {
    driverInstance.disable();
    this.secondsOn += Math.round((new Date().getTime()) - this.lastEnableTime.getTime())
  }
 
  /**
   * Returns true if the heater is enabled, false if not
   */
  public boolean enabled() {
    return driverInstance.enabled();
  }
  
  /**
   * Sets the device power if a regulator is available
   * @param s Power in percent
   */
  public void writePower(int s) {
    if(this.hasRegulator()) {
      this.regulator.writePower(s);
    }
  }
  
  /**
   * Writes the device power if a regulator is
   * available and in forced mode
   * @param s Power in percent
   */
  public void writeForcedPower(int s) {
	if(this.hasRegulator()) {
		this.regulator.writeForcedPower(s);
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
   * Activates the forced mode for this heater.
   * "Forced" means that the heater can only be
   * modified via the "forced" methods, calls to
   * the standard enable/disable methods are ignored
   */
  public void force() {
	if(!this.forced) {
	  this.realEnabled = this.enabled()
	  if(this.hasRegulator()) {
		  this.regulator.force()
	  }
	  this.forced = true
	}
  }
  
  /**
   * Disables the forced mode for this heater
   */
  public void unforce() {
    if(this.forced) {
      this.forced = false
	  if(this.hasRegulator()) {
		  this.regulator.unforce()
	  }
      if(this.realEnabled) {
        this.enable()
      }
      else {
        this.disable()
      }
    }
  }
  
  /**
   * Returns true if the heater is in forced mode
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
