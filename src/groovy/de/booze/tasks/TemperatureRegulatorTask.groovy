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


import de.booze.grails.Recipe
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
    Double rt = 0;
    int rtc = 0;

    if (this.tempRegulator.getReferenceSensors() == Recipe.TEMPERATURE_REFERENCE_INNER
            || this.tempRegulator.getReferenceSensors() == Recipe.TEMPERATURE_REFERENCE_BOTH) {
      this.tempRegulator.getInnerSensors().each {
        try {
          Double itsT = it.readTemperatureImmediate();
          rt += itsT;
          rtc++;
        }
        catch (Exception e) {
          log.error("Could not read temperature from sensor ${it.name}")
        }
      }
    }
    else if (this.tempRegulator.getReferenceSensors() == Recipe.TEMPERATURE_REFERENCE_OUTER
            || this.tempRegulator.getReferenceSensors() == Recipe.TEMPERATURE_REFERENCE_BOTH) {
      this.tempRegulator.getOuterSensors().each {
        try {
          Double itsT = it.readTemperatureImmediate();
          rt += itsT;
          rtc++;
        }
        catch (Exception e) {
          log.error("Could not read temperature from sensor ${it.name}")
        }
      }
    }

    if (rtc < 1) {
      throw new Exception('No valid reference temperature sensors found');
    }

    return (rt / rtc) as Double
  }

  /**
   * Returns the average temperature for additional (^= non-reference)
   * temperature sensors
   */
  public Double getAdditionalTemperature() {
    Double at = 0;
    int atc = 0;

    if (this.tempRegulator.getReferenceSensors() == Recipe.TEMPERATURE_REFERENCE_INNER) {
      this.tempRegulator.getOuterSensors().each {
        try {
          Double itsT = it.readTemperatureImmediate();
          at += itsT;
          atc++;
        }
        catch (Exception e) {
          log.error("Could not read temperature from sensor ${it.name}")
        }
      }
    }
    else if (this.tempRegulator.getReferenceSensors() == Recipe.TEMPERATURE_REFERENCE_OUTER) {
      this.tempRegulator.getInnerSensors().each {
        try {
          Double itsT = it.readTemperatureImmediate();
          at += itsT;
          atc++;
        }
        catch (Exception e) {
          log.error("Could not read temperature from sensor ${it.name}")
        }
      }
    }

    if (atc < 1) {
      throw new Exception('No valid reference temperature sensors found');
    }

    return (at / atc) as Double
  }

  /**
   * Checks if the desired temperature is reached and
   *
   */
  public void run() {
    List heaters = this.tempRegulator.getHeaters();

    Double targetTemperature = this.tempRegulator.getTemperature();
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
      Double offset = this.tempRegulator.getOffset()

      if (this.tempRegulator.getReferenceSensors() != Recipe.TEMPERATURE_REFERENCE_BOTH) {
        try {
          Double additionalTemperature = this.getAdditionalTemperature();
          if (additionalTemperature > (referenceTemperature + TemperatureRegulator.INNER_OUTER_MAX_DIFFERENCE)) {
            offset = offset + 2;
          }
        }
        catch (Exception e) {
          log.error("Additional temperature sensors could not be read: ${e}")
        }
      }

      if (tempDifference > offset) {
        // Enable all heaters
        try {
          for (int i = 0; i < heaters.size(); i++) {
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
        Double o = tempDifference / offset * 0.7
        // This is magic until now
        // Should be re-done with a good hysteresis algorithm
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
    else {
      try {
        for (int i = 0; i < heaters.size(); i++) {
          heaters[i].disable();
        }
      }
      catch (Exception e) {
        log.error("Could not set heaters: ${e}")
      }
    }
  }
}


