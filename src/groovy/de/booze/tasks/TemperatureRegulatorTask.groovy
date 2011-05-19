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

import org.apache.log4j.Logger;


import de.booze.backend.grails.Recipe
import de.booze.regulation.DeviceSwitcher
import de.booze.regulation.TemperatureRegulator

class TemperatureRegulatorTask extends TimerTask {

  /**
   * Temperature controller
   */
  private TemperatureRegulator tempRegulator

  /**
   * Device switching controller
   */
  private DeviceSwitcher devSwitcher

  /**
   * Default logger
   */
  private Logger log = Logger.getLogger(getClass().getName());
  
  /**
   * Constructor
   */
  public TemperatureRegulatorTask(TemperatureRegulator t) {
    this.tempRegulator = t;
    this.devSwitcher = DeviceSwitcher.getInstance()
  }

  /**
   * Returns the average reference temperature from the
   * selected reference sensors
   */
  public Double getReferenceTemperature() {
    Double rt = 0.0d;
    int rtc = 0;
    
    this.tempRegulator.getReferenceSensorDevices().each() {
      try {
          rt += it.readTemperatureImmediate();;
          rtc++;
      }
      catch (Exception e) {
        log.error("Could not read temperature from sensor ${it.name}")
      }
    }

    if (rtc < 1) {
      throw new Exception('No valid reference temperature sensors found');
    }

    return (rt / rtc) as Double
  }

  /**
   * Checks if the desired temperature is reached and
   *
   */
  public void run() {
    List heaters = this.tempRegulator.getHeaters();

    Double targetTemperature = this.tempRegulator.getTargetTemperature();
    Double referenceTemperature = 0.0 as Double;

    try {
      referenceTemperature = this.getReferenceTemperature();
    }
    catch (Exception e) {
      // Could not read a valid temperature value, shutdown all heaters
      // for security reasons
      for (int i = 0; i < heaters.size(); i++) {
        try {
          heaters[i].disable();
        }
        catch (Exception p) {
          log.error("Could not disable heater ${heaters[i].name}: ${p}")
        }
      }
      log.error("Reference temperature could not be read: ${e}")
      return;
    }

    // Tell the actual temperature to the tempRegulator
    this.tempRegulator.setActualTemperature(referenceTemperature);

    if (referenceTemperature < targetTemperature) {
      Double tempDifference = targetTemperature - referenceTemperature
      Double hysteresis = this.tempRegulator.getHysteresis()

      if (tempDifference >= hysteresis) {
        // Set all heaters to full power because it's way too cold
        try {
          for (int i = 0; i < heaters.size(); i++) {
            if(heaters[i].hasRegulator()) {
              heaters[i].setPower(100)
            }
            
            if (!heaters[i].enabled()) {
              // Check if we are allowed to switch
              if (this.devSwitcher.getNextSwitchingSlot() == 0) {
                heaters[i].enable()
              }
            }
          }
        }
        catch (Exception e) {
          log.error("Could not set heaters: ${e}")
        }
      }
      else {
        // Check only the first for a regulator, assume they all got
        // one. That should be assured by the Setting domain class
        if(heaters[0].hasRegulator()) {
          Integer power = new Integer(Math.round(tempDifference / ((hysteresis) / 100)))
          try {
            for(int i = 0; i< heaters.size(); i++) {
              heaters[i].setPower(power)
              if(!heaters[i].enabled()) {
                // Check if we are allowed to switch
                if (this.devSwitcher.getNextSwitchingSlot() == 0) {
                  heaters[i].enable()
                }
              }
            }
          }
          catch (Exception e) {
            log.error("Could not set heaters: ${e}")
          }
        }
        else {
          // No regulators, use on/off switching for hysteresis
          Double o = tempDifference / offset * 0.7

          int enableCount = Math.round(heaters.size() * o) as int
          try {
            for (int i = 0; i < enableCount; i++) {
              if (!heaters[i].enabled()) {
                // Check if we are allowed to switch
                if (this.devSwitcher.getNextSwitchingSlot() == 0) {
                  heaters[i].enable()
                }
              }
            }
            for (int i = enableCount; i < heaters.size(); i++) {
              heaters[i].disable();
            }
          }
          catch (Exception e) {
            log.error("Could not set heaters: ${e}")
          }
        }
        
      }
    }
    else {
      try {
        for (int i = 0; i < heaters.size(); i++) {
          if(heaters[i].hasRegulator()) {
            heaters[i].setPower(0)
          }
          heaters[i].disable();
        }
      }
      catch (Exception e) {
        log.error("Could not set heaters: ${e}")
      }
    }
  }
}


