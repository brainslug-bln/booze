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
import de.booze.process.BrewProcess
import de.booze.events.BrewPressureExceededEvent

/**
 *
 * @author akotsias
 */
class PressureMonitorTask extends TimerTask {

  /**
   * Default logger
   */
  private Logger log = Logger.getLogger(getClass().getName());

  /**
   * Available pressure sensors
   */
  private List pressureSensors;

  /**
   * Brew process instance
   */
  private BrewProcess brewProcess;

  /**
   * Date the last pressure exceedance occured
   */
  private Date lastExceedanceEvent = new Date();

  public PressureMonitorTask(BrewProcess brewProcess, List pressureSensors) {
    this.pressureSensors = pressureSensors;
    this.brewProcess = brewProcess;
  }

  public void run() {
    this.pressureSensors.each {
      try {
        Double pressure = it.readPressure();
        if (pressure > it.pressureMaxLimit) {
          //if((this.lastExceedanceEvent.getTime() + PressureRegulator.exceedanceTimeout) < (new Date().getTime())) {
          brewProcess.pause()
          brewProcess.addEvent(new BrewPressureExceededEvent('brew.brewProcess.pressureExceeded', pressure, it.name))
          //}
        }
      }
      catch (Exception e) {
        log.error("Could not read pressure ${e}");
      }
    }
  }
}

