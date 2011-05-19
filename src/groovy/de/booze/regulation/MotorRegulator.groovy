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
 **/

package de.booze.regulation

import org.apache.log4j.Logger
import de.booze.backend.grails.MotorDeviceMode
import de.booze.tasks.MotorRegulatorTask

/**
 * Controls motor on/off regulation
 *
 */
class MotorRegulator {

  /**
   * Regulation cycle timer
   */
  private Timer timer;

  /**
   * Motor device
   */
  def motor

  /**
   * Default logger
   */
  private Logger log = Logger.getLogger(getClass().getName());

  /**
   * Constructor
   */
  public MotorRegulator(def motor) {
    this.motor = motor;
  }


  /**
   * Returns the actual motor Mode
   */
  public MotorDeviceMode getMotorMode() {
    return this.motor.mode;
  }

  /**
   * Overrides the actual motor mode
   *
   * @param MotorMode
   */
  public void forceMode(MotorDeviceMode dm) {
    this.forcedMode = dm;
    this.enable();
  }

  /**
   * Clears a forced motor mode
   */
  public void unforceMode() {
    this.forcedMode = null;
    this.enable()
  }

  /**
   * Returns true if the actual motor mode is forced
   */
  public MotorDeviceMode getForcedMode() {
    return this.forcedMode;
  }

  /**
   * Returns true if the device mode is forced
   */
  public boolean forced() {
    return (this.forcedMode != null);
  }

  /**
   * Enables this motor and starts
   * the MotorRegulatorTask if motor mode is interval
   */
  public void enable() {

    this.disable();

    def dm = this.forcedMode ?: this.motor.mode;

    if (pm && pm?.mode == MotorDeviceMode.MODE_ON) {
      log.debug("enabling motor continuus")
      this.motor.enable();
    }
    else if (pm && pm?.mode == MotorDeviceMode.MODE_INTERVAL) {
      log.debug("enabling motor interval")
      this.timer = new Timer();
      this.timer.schedule(new MotorRegulatorTask(this.motor), 100, 1000);
    }

  }

  /**
   * Disables the motor
   * Stops any running MotorRegulatorTasks
   */
  public void disable() {
    log.debug("disabling motor")
    if (this.timer) {
      try {
        this.timer.cancel();
        this.timer = null;
      }
      catch (Exception e) {
        log.error("could not cancel motor timer: ${e}")
        // pass
      }
    }

    this.motor.disable();
  }
}

