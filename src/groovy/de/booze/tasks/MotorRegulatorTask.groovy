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

package de.booze.tasks

import org.apache.log4j.Logger
import de.booze.regulation.MotorRegulator
import de.booze.backend.grails.MotorDevice

/**
 *
 * @author akotsias
 */
class MotorRegulatorTask extends TimerTask {

  /**
   * Logger instance
   */
  private Logger log = Logger.getLogger(getClass().getName());
 
  /**
   * Motor device
   */
  MotorRegulator motorRegulator

  /**
   * Start time for the actual interval
   */
  private Date actualIntervalStart

  /**
   * True if the task has not run yet
   */
  private boolean virgin = true;

  /**
   * Constructor
   */
  public MotorRegulatorTask(MotorRegulator mr) {
    this.motorRegulator = mr;
  }

  /**
   * Run method
   * If motor is in interval cycling mode this
   * task controls the periodical on/off switching
   */
  public void run() {
    try {
      log.debug("running regulation for motor cycling mode")
      
      MotorDevice motor = this.motorRegulator.getMotor(); 
      Map cm = this.motorRegulator.getActualCyclingMode();
      
      // Save the first run time, start disabled
      if (this.virgin) {
        log.debug("first run for MotorRegulatorTask")
        this.virgin = false;
        this.actualIntervalStart = new Date();
        return
      }

      if (motor.enabled()) {
        log.debug("motor is actually enabled")
        if ((this.actualIntervalStart.getTime() + (cm.onInterval * 1000)) < (new Date().getTime())) {
          log.debug("disabling motor, actual offInterval passed by")
          this.motorRegulator.motorTask.disable();
          this.actualIntervalStart = new Date();
        }
      }
      else {
        log.debug("motor is actually disabled")
        if ((this.actualIntervalStart.getTime() + (cm.offInterval * 1000)) < (new Date().getTime())) {
          log.debug("enabling motor, actual offInterval passed by")
          this.motorRegulator.motorTask.enable();
          this.actualIntervalStart = new Date();
        }
      }
    }
    catch (Exception e) {
      log.error("Could not access motor ${e}");
    }
  }
}

