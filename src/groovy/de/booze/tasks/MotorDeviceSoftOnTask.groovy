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
   * Spin-up time in milliseconds
   */
  Integer spinupTime;
  
  /**
   * Actual speed in percent
   */
  Integer speed = 0;
  
  /**
   * Time the spinup started
   */
  Date startDate
  
  MotorRegulatorDevice motorRegulator

  /**
   * Constructor
   */
  public MotorDeviceSoftOnTask(MotorRegulatorDevice r) {
    this.motorRegulator = r;
    this.spinupTime = r.softOn;
  }

  /**
   * Default run method
   */
  public void run() {
    if(!this.startDate) {
      this.startDate = new Date()
    }
    
    Integer timeRun = (new Date()).getTime() - this.startDate.getTime();
    if(this.timeRun >= this.spinupTime) {
      this.motorRegulator.setSpeed(100);
      this.cancel();
    }
    else {
      this.motorRegulator.setSpeed(Math.round(this.spinupTime/timeRun * 100));
    }
  }
}