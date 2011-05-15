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

/**
 *
 * @author akotsias
 */
class PumpRegulatorTask extends TimerTask {

  /**
   * Logger instance
   */
  private Logger log = Logger.getLogger(getClass().getName());

  /**
   * ON-interval (seconds)
   * (for interval pump mode)
   */
  private Integer onInterval;

  /**
   * OFF-interval (seconds)
   * (for interval pump mode)
   */
  private Integer offInterval;

  /**
   * Pump device
   */
  def pump

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
  public PumpRegulatorTask(pump, Integer on, Integer off) {

    this.pump = pump;
    this.onInterval = on;
    this.offInterval = off;
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

      if (this.pump.enabled()) {
        if ((this.actualIntervalStart.getTime() + (this.onInterval * 1000)) < (new Date().getTime())) {
          this.pump.disable();
          this.actualIntervalStart = new Date();
        }
      }
      else {
        if ((this.actualIntervalStart.getTime() + (this.offInterval * 1000)) < (new Date().getTime())) {
          this.pump.enable();
          this.actualIntervalStart = new Date();
        }
      }
    }
    catch (Exception e) {
      log.error("Could not access pump");
    }
  }
}

