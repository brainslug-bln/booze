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

package de.booze.tasks
import de.booze.backend.grails.MotorTask

/**
 * Task which periodically adapts
 * the motor speed
 */
class MotorRegulatorDeviceTask extends TimerTask {
  
  /**
   * Actual speed in percent
   */
  Integer speed = 0;
  
  /**
   * Time the spinup started
   */
  Date startDate
  
  /**
   * Calling motor task
   */
  MotorTask mt
  
  /**
   * Pressure/temperature adapted speed
   */
  Integer adaptedSpeed
  
  
  /**
   * Speed change amount
   * Speed is changed by this percentage 
   * upon increaseSpeed() or decreaseSpeed() call
   */
  final static Integer SPEED_CHANGE = 2

  /**
   * Constructor
   */
  public MotorRegulatorDeviceTask(MotorTask m) {
    this.mt = m;
    this.adaptedSpeed = this.mt.targetSpeed;
  }

  /**
   * Run method
   *
   * The motor speed is adapted to either
   * the target pressure, temperature or
   * the speed given by the soft-on feature
   */
  public void run() {
    if(!this.startDate) {
      this.startDate = new Date()
    }
    
    log.debug("adapted speed before motor regulation is ${this.adaptedSpeed}")
    
    if(this.mt.targetPressure) {
	  this.adaptSpeedToTargetPressure();
    }
    else if(this.mt.targetTemperature) {
      this.adaptSpeedToTargetTemperature();
    }

    // If the soft-on feature is enabled the motor speed
    // is increased slowly instead of switching to full power 
    // immediately
    if(this.mt.readMotor().regulator?.softOn) {
      log.debug("motor softOn running")
      Integer timeRun = (new Date()).getTime() - this.startDate.getTime();
      if(timeRun < this.mt.readMotor().regulator?.softOn) {
        log.debug("softOn ran ${timeRun}ms")
        // Yes, we overwrite the value for this.adaptedSpeed as long as 
        // soft-on time is not elapsed.
        this.adaptedSpeed = Math.round(  (timeRun / this.mt.readMotor().regulator?.softOn) * this.mt.targetSpeed);
      }
    }
    
    
    log.debug("adapted speed is now ${this.adaptedSpeed}, setting it...")
    this.mt.readMotor().writeSpeed(this.adaptedSpeed);
  }

  /**
   * Adapts the motor speed to the target temperature value
   */
  private void adaptSpeedToTargetTemperature() {
	Double ap = this.mt.readAveragePressure()
	
    log.debug("Regulating motor by pressure")
    if(ap > (this.mt.targetPressure + 100)) {
      log.debug("actual pressure (${ap}}) is higher than target pressure ${(this.mt.targetPressure + 100)}")
      if(this.mt.pressureRegulationDirection) {
        log.debug("decreasing speed")
        this.decreaseSpeed()
      }
      else {
        log.debug("increasing speed")
        this.increaseSpeed()
      }
    }
    else if(ap < (this.mt.targetPressure - 100)) {
      log.debug("actual pressure (${ap}) is lower than target pressure ${(this.mt.targetPressure - 100)}")
      if(this.mt.pressureRegulationDirection) {
        log.debug("increasing speed")
        this.increaseSpeed()
      }
      else {
        log.debug("decreasing speed")
        this.decreaseSpeed()
      }
    }
  }

  /**
   * Adapts the motor speed to the target pressure value
   */
  private void adaptSpeedToTargetPressure() {
	Double at = this.mt.readAverageTemperature()
	
    log.debug("Regulating motor by temperature")
    if(at > (this.mt.targetTemperature + 1)) {
      log.debug("actual temperature (${at}}) is higher than target temperature ${(this.mt.targetTemperature + 1)}")
      if(this.mt.temperatureRegulationDirection) {
        log.debug("decreasing speed")
        this.decreaseSpeed()
      }
      else {
        log.debug("increasing speed")
        this.increaseSpeed()
      }
    }
    else if(at < (this.mt.targetTemperature - 1)) {
      log.debug("actual temperature (${at}}) is lower than target temperature ${(this.mt.targetTemperature - 1)}")
      if(this.mt.temperatureRegulationDirection) {
        log.debug("increasing speed")
        this.increaseSpeed()
      }
      else {
        log.debug("decreasing speed")
        this.decreaseSpeed()
      }
    }
  }
  
  /**
   * Increases the speed by the value defined in MotorRegulatorDeviceTask.SPEED_CHANGE
   */
  public void increaseSpeed() {
    if(this.adaptedSpeed+MotorRegulatorDeviceTask.SPEED_CHANGE <= 100) {
      this.adaptedSpeed = this.adaptedSpeed + MotorRegulatorDeviceTask.SPEED_CHANGE
    }
    else {
      this.adaptedSpeed = 100
    }
  }
  
  /**
   * Decreases the speed by the value defined in MotorRegulatorDeviceTask.SPEED_CHANGE
   */
  public void decreaseSpeed() {
    if(this.adaptedSpeed-MotorRegulatorDeviceTask.SPEED_CHANGE >= 0) {
      this.adaptedSpeed = this.adaptedSpeed - MotorRegulatorDeviceTask.SPEED_CHANGE
    }
    else {
      this.adaptedSpeed = 0
    }
  }
}