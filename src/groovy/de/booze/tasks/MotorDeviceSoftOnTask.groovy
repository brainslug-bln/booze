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
import de.booze.backend.grails.MotorRegulatorDevice

/**
 * Task which periodically runs the step's
 * checkStep method
 */
class MotorDeviceSoftOnTask extends TimerTask {
  
  /**
   * Actual speed in percent
   */
  Integer speed = 0;
  
  /**
   * Time the spinup started
   */
  Date startDate
  
  MotorRegulatorDevice mr
  
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
  public MotorDeviceSoftOnTask(MotorRegulatorDevice r) {
    this.mr = r;
    this.adaptedSpeed = this.mr.targetSpeed
  }

  /**
   * Default run method
   */
  public void run() {
    if(!this.startDate) {
      this.startDate = new Date()
    }

    
    if(this.mr.targetPressure) {
        if(this.mr.getAveragePressure > (this.mr.targetPressure + 100)) {
            if(this.mr.pressureRegulationDirection) {
                this.decreaseSpeed()
            }
            else {
                this.increaseSpeed()
            }
        }
        else if(this.mr.getAveragePressure < (this.mr.targetPressure - 100)) {
            if(this.mr.pressureRegulationDirection) {
                this.increaseSpeed()
            }
            else {
                this.decreaseSpeed()
            }
        }
        this.mr.setSpeed(this.adaptedSpeed);
    }
    else (this.mr.targetTemperature) {
        if(this.mr.getAverageTemperature > (this.mr.targetTemperature + 1)) {
            if(this.mr.temperatureRegulationDirection) {
                this.decreaseSpeed()
            }
            else {
                this.increaseSpeed()
            }
        }
        else if(this.mr.getAveragePressure < (this.mr.targetTemperature - 1)) {
            if(this.mr.temperatureRegulationDirection) {
                this.increaseSpeed()
            }
            else {
                this.decreaseSpeed()
            }
        }
        this.mr.setSpeed(this.adaptedSpeed);
    }
    
    if(this.mr.softOn) {
        Integer timeRun = (new Date()).getTime() - this.startDate.getTime();
        if(this.timeRun >= this.mr.softOn) {
          this.mr.setSpeed(this.adaptedSpeed);
        }
        else {
          this.mr.setSpeed(Math.round(this.spinupTime/timeRun * this.adaptedSpeed));
        }
    }
  }
  
  public void increaseSpeed() {
      if(this.adaptedSpeed+MotorDeviceSoftOnTask.SPEED_CHANGE <= 100) {
          this.adaptedSpeed = this.adaptedSpeed + MotorDeviceSoftOnTask.SPEED_CHANGE
      }
      else {
          this.adaptedSpeed = 100
      }
  }
  
  public void decreaseSpeed() {
      if(this.adaptedSpeed-MotorDeviceSoftOnTask.SPEED_CHANGE >= 0) {
          this.adaptedSpeed = this.adaptedSpeed - MotorDeviceSoftOnTask.SPEED_CHANGE
      }
      else {
          this.adaptedSpeed = 0
      }
  }
}