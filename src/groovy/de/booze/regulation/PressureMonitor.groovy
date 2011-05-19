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


package de.booze.regulation

import de.booze.process.BrewProcess
import de.booze.tasks.PressureRegulatorTask

/**
 *
 * @author akotsias
 */
class PressureMonitor {

  /**
   * Timeout between pressure exceedance events in ms
   */
  final static int exceedanceTimeout = 1000;

  /**
   * Brew process instance
   */
  BrewProcess brewProcess;

  /**
   * List of registered pressure sensors
   */
  List pressureSensors = []

  /**
   * Task timer
   */
  Timer timer;

  public PressureMonitor(BrewProcess brewProcess, List pressureSensors) {
    this.brewProcess = brewProcess;
    this.pressureSensors = pressureSensors;
  }

  public void enable() {
    this.timer = new Timer();
    this.timer.schedule(new PressureMonitorTask(this.brewProcess, this.pressureSensors), 100, 500);
  }

  /**
   * Disables the pump
   * Stops any running PumpRegulatorTasks
   */
  public void disable() {
    this.timer.cancel();
  }
}

