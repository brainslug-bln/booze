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
import de.booze.tasks.MotorRegulatorTask
import de.booze.backend.grails.MotorTask
import de.booze.backend.grails.MotorDevice

/**
 * Controls motor on/off regulation
 *
 */
class MotorRegulator {
  
  private Integer forcedCyclingMode
  
  private Integer forcedOnInterval
  
  private Integer forcedOffInterval

  /**
   * Regulation cycle timer
   */
  private Timer timer;

  /**
   * Motor device
   */
  private MotorTask motorTask

  
  /**
   * Default logger
   */
  private Logger log = Logger.getLogger(getClass().getName());

  /**
   * Constructor
   */
  public MotorRegulator(MotorTask motorTask) {
    this.motorTask = motorTask;    
  }

  public void forceCyclingMode(Integer mode, Integer onInterval, Integer offInterval) {
    this.forcedCyclingMode = mode
    this.forcedOnInterval = onInterval
    this.forcedOffInterval = offInterval
    
    this.enable()
  }
  
  public void forceCyclingMode(Integer mode) throws IllegalArgumentException {
    if(mode == MotorTask.CYCLING_MODE_INTERVAL) {
      throw new IllegalArgumentException("You must supply an on/off interval for interval cycling mode");
    }
    this.forceCyclingMode(mode, 0, 0)
  }

  /**
   * Clears a forced motor mode
   */
  public void unforceCyclingMode() {
    this.forcedCyclingMode = this.forcedOnInterval = this.forcedOffInterval = null;
    this.enable()
  }

  /**
   * Returns true if the cycling mode is forced
   */
  public boolean forced() {
    return (this.forcedCyclingMode != null);
  }

  /**
   * Enables this motor and starts
   * the MotorRegulatorTask if motor 
   * cycling mode is set to "interval"
   */
  public void enable() {
    log.error("Enabling motor")
    this.disable();

    def dm = this.getActualCyclingMode()
    
    if (dm.mode == MotorTask.CYCLING_MODE_ON) {
      log.debug("enabling motor continuus")
      this.motorTask.enable();
    }
    else if (dm.mode == MotorTask.CYCLING_MODE_INTERVAL) {
      log.debug("enabling motor interval")
      this.timer = new Timer();
      this.timer.schedule(new MotorRegulatorTask(this), 100, 1000);
    }
    else {
      log.debug("disabling motor for cycling mode OFF")
      this.motorTask.disable()
    }

    
  }

  /**
   * Disables the motor
   * Stops any running MotorRegulatorTasks
   */
  public void disable() {
    log.debug("disabling motor")
    this.motorTask.disable();
    
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
  }
  
  /**
   * Returns the motor associated to this regulator
   */
  public MotorDevice getMotor() {
    return this.motorTask.readMotor();
  }
  
  /**
   * Returns the actual (forced or not) cycling
   * mode for this regulator
   */
  public Map getActualCyclingMode() {
    Map cm = [:]
    if(this.forcedCyclingMode) {
      cm = [mode: this.forcedCyclingMode,
            onInterval: this.forcedOnInterval,
            offInterval: this.forcedOffInterval]
    }
    else {
      cm = [mode: this.motorTask.cyclingMode,
            onInterval: this.motorTask.onInterval,
            offInterval: this.motorTask.offInterval]
    }
    return cm
  }
}

