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
 * Task which periodically runs the step's
 * checkStep method
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
   */
  final static Integer SPEED_CHANGE = 2

  /**
   * Constructor
   */
  public MotorRegulatorDeviceTask(MotorTask m) {
    this.mt = m;
    this.adaptedSpeed = this.mt.targetSpeed
  }

  /**
   * Default run method
   */
  public void run() {
    if(!this.startDate) {
      this.startDate = new Date()
    }
    
    if(this.mt.targetPressure) {
      if(this.mt.readAveragePressure() > (this.mt.targetPressure + 100)) {
        if(this.mt.pressureRegulationDirection) {
          this.decreaseSpeed()
        }
        else {
          this.increaseSpeed()
        }
      }
      else if(this.mt.readAveragePressure() < (this.mt.targetPressure - 100)) {
        if(this.mt.pressureRegulationDirection) {
          this.increaseSpeed()
        }
        else {
          this.decreaseSpeed()
        }
      }
    }
    else (this.mt.targetTemperature) {
      if(this.mt.readAverageTemperature() > (this.mt.targetTemperature + 1)) {
        if(this.mt.temperatureRegulationDirection) {
          this.decreaseSpeed()
        }
        else {
          this.increaseSpeed()
        }
      }
      else if(this.mt.readAverageTemperature() < (this.mt.targetTemperature - 1)) {
        if(this.mt.temperatureRegulationDirection) {
          this.increaseSpeed()
        }
        else {
          this.decreaseSpeed()
        }
      }
    }
    
    if(this.mt.motor.hasRegulator() && this.mt.motor.regulator?.softOn) {
      Integer timeRun = (new Date()).getTime() - this.startDate.getTime();
      if(this.timeRun < this.mt.motor.regulator?.softOn) {
        this.adaptedSpeed = Math.round(this.spinupTime/timeRun * this.adaptedSpeed);
      }
    }
    
    this.mt.motor.writeSpeed(this.adaptedSpeed);
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