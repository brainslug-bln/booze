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

import org.apache.log4j.Logger
import de.booze.grails.PumpMode
import de.booze.tasks.PumpRegulatorTask

/**
 * Controls pump on/off regulation
 *
 */
class PumpRegulator {

  /**
   * Regulation cycle timer
   */
  private Timer timer;

  /**
   * Pump mode
   * Default to "off" if not set
   */
  private PumpMode pumpMode

  private PumpMode forcedPumpMode

  /**
   * Pump device
   */
  def pump

  /**
   * Default logger
   */
  private Logger log = Logger.getLogger(getClass().getName());

  /**
   * Constructor
   */
  public PumpRegulator(def pump) {
    this.pump = pump;
  }

  /**
   * Sets a new pump mode
   * Warning: Clears forced pump modes
   *
   * @param PumpMode
   */
  public void setPumpMode(PumpMode pumpMode) {
    log.debug("setting pumpMode to ${pumpMode.mode}")
    this.unforcePumpMode();

    this.pumpMode = pumpMode;
  }

  /**
   * Clears the pump mode ^= off
   */
  public void clearPumpMode() {
    this.pumpMode = null;
  }

  /**
   * Returns the actual pump Mode
   */
  public PumpMode getPumpMode() {
    return this.pumpMode;
  }

  /**
   * Overrides the actual pump mode
   *
   * @param PumpMode
   */
  public void forcePumpMode(PumpMode pumpMode) {
    this.forcedPumpMode = pumpMode;
    this.enable();
  }

  /**
   * Clears a forced pump mode
   */
  public void unforcePumpMode() {
    this.forcedPumpMode = null;
    this.enable()
  }

  /**
   * Returns true if the actual pump mode is forced
   */
  public PumpMode getForcedPumpMode() {
    return this.forcedPumpMode;
  }

  public boolean forced() {
    return (this.forcedPumpMode != null);
  }

  /**
   * Enables this pump and starts
   * the PumpRegulatorTask if pump mode is interval
   */
  public void enable() {

    this.disable();

    def pm = this.forcedPumpMode ?: this.pumpMode;

    if (pm && pm?.mode == PumpMode.MODE_CONTINUUS) {
      log.debug("enabling pump continuus")
      this.pump.enable();
    }
    else if (pm && pm?.mode == PumpMode.MODE_INTERVAL) {
      log.debug("enabling pump interval")
      this.timer = new Timer();
      this.timer.schedule(new PumpRegulatorTask(this.pump, pm.onInterval, pm.offInterval), 100, 1000);
    }

  }

  /**
   * Disables the pump
   * Stops any running PumpRegulatorTasks
   */
  public void disable() {
    log.debug("disabling pump")
    if (this.timer) {
      try {
        this.timer.cancel();
        this.timer = null;
      }
      catch (Exception e) {
        log.error("could not cancel pump timer: ${e}")
        // pass
      }
    }

    this.pump.disable();
  }
}

