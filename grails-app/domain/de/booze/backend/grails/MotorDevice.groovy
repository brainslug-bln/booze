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
 * A motor device may be any device with a motor
 * 
 * Basic actions are enable/disable.
 * Optionally speed can be regulated by a MotorRegulatorDevice.
 * 
 * If regulated, target pressure or temperature may be defined
 * to automatically adapt the speed to a sensor value
 */
class MotorDevice extends Device {

  Long secondsOn = 0
  Date lastEnableTime
  
  /**
   * Regulator device to regulate the motor's speed
   */
  MotorRegulatorDevice regulator
  
  static transients = ["secondsOn", "lastEnableTime"]
                  
  static constraints = {
    regulator(nullable: true)
  }
  
  static mapping = {
    columns {
      regulator lazy: false
    }
  }

  /** 
   * Enables the motor device
   */
  public void enable() {
    if (!this.enabled()) {
      
      driverInstance.enable()
      this.lastEnableTime = new Date()
    }
  }

  /**
   * Disables the motor device
   */
  public void disable() {
    if (this.enabled()) {
      
      
      driverInstance.disable();
      this.secondsOn += Math.round((new Date().getTime()) - this.lastEnableTime.getTime())
    }
  }
  
  /**
   * Sets the device speed if a regulator is available
   */
  public void writeSpeed(int s) {
    if(this.hasRegulator()) {
      this.regulator.writeSpeed(s);
    }
  }
  
  /**
   * Returns the device speed if a regulator is available
   */
  public int readSpeed() {
    if(this.hasRegulator()) {
      return this.regulator.readSpeed()
    }
  }
   
  /**
   * Checks if this device has a speed regulator
   */
  public boolean hasRegulator() {
    return (this.regulator != null)
  }

  /**
   * Returns true if the motor device is enabled, false if not
   */
  public boolean enabled() {
    return driverInstance.enabled();
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
