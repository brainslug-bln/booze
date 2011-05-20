/**
 * Booze - Software for micro breweries
 *
 * Copyright (C) 2011  Andreas Kotsias <akotsias@esnake.de>
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
package de.booze.backend.grails
import de.booze.tasks.MotorDeviceSoftOnTask

/**
 *
 */
class MotorRegulatorDevice extends Device {
  
   /**
   * Target temperature (°C) to achieve by regulating up or
   * down the motor device
   */
  Double targetTemperature
    
  /**
   * Regulate up (true) or down (false) to minimize temperature 
   */
  boolean temperatureRegulationDirection = false
   
  /**
   * Target pressure (mbar) to achieve by regulating up or
   * down the motor device
   */
  Double targetPressure
    
  /**
   * Regulate up (true) or down (false) to minimize pressure 
   */
  boolean pressureRegulationDirection = false
  
  /**
   * Target speed
   */
  Integer targetSpeed = 100
  
  /**
   * Set this value in milliseconds to enable soft upspinning
   */
  Integer softOn
  
  /**
   * Task for softOn upspinning
   */
  MotorDeviceSoftOnTask softOnTimer
  
  static transients = ["softOnTimer"]
  
  static hasMany = [temperatureSensors: TemperatureSensorDevice,
    pressureSensors: PressureSensorDevice]

  static constraints = {
    softOn(nullable: true, min: 0, max: 5000)
  }

  public void enable() {
    if(this.softOn && this.softOn > 0) {
      this.setSpeed(0);
      this.softOnTimer = new Timer();
      this.softOnTimer.schedule(new MotorDeviceSoftOnTask(this), 0, 50);
    }
    else {
      this.setSpeed(100);
    }
  }
  
  public void disable() {
    if(this.softOnTimer) {
      this.softOnTimer.cancel();
      this.softOnTimer = null;
    }
    this.setSpeed(0);
  }
  
  /**
   * Sets the motor device speed in percent
   * of the maximum value
   */
  public void setSpeed(int speed) {
    driverInstance.setSpeed(speed);
  }

  /**
   * Returns the motor device speed in percent
   * of the maximum value
   */
  public int getSpeed() {
    return driverInstance.getSpeed();
  }
}
