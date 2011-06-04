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
import de.booze.backend.grails.MotorTask

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
  MotorTask motorTask

  /**
   * Start time for the actual interval
   */
  private Date actualIntervalStart

  /**
   * True if the task is not run yet
   */
  private boolean virgin = true;

  /**
   * Constructor
   */
  public MotorRegulatorTask(MotorTask motorTask) {
    this.motorTask = motorTask;
  }

  /**
   * Run method for this task
   */
  public void run() {
    try {
      // Save the first run time, start disabled
      if (this.virgin) {
        this.virgin = false;
        this.actualIntervalStart = new Date();
        return
      }

      if (this.motorTask.motor.enabled()) {
        if ((this.actualIntervalStart.getTime() + (this.motorTask.onInterval * 1000)) < (new Date().getTime())) {
          this.motorTask.motor.disable();
          this.actualIntervalStart = new Date();
        }
      }
      else {
        if ((this.actualIntervalStart.getTime() + (this.motorTask.offInterval * 1000)) < (new Date().getTime())) {
          this.motorTask.motor.enable();
          this.actualIntervalStart = new Date();
        }
      }
    }
    catch (Exception e) {
      log.error("Could not access motor");
    }
  }
}

