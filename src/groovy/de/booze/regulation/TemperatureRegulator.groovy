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

import java.lang.Math

import org.apache.log4j.Logger;

import de.booze.backend.grails.Recipe
import de.booze.tasks.TemperatureRegulatorTask

/**
 * Controls temperature regulation
 *
 * @author akotsias
 */
class TemperatureRegulator {
  
  /**
   * Use the sensors marked as "mashing" as reference
   * sensors for temperature
   */
  final static int REFERENCE_SENSORS_MASHING = 0
  
  /**
   * Use the sensors marked as "cooking" as reference
   * sensors for temperature
   */
  final static int REFERENCE_SENSORS_COOKING = 1

  /**
   * Hysteresis threshold
   */
  private Double hysteresis = 1.0 as Double

  /**
   * List of available heaters
   */
  private List heaters = []

  /**
   * List of available temperature sensors
   */
  private List sensors = []

  /**
   * Target temperature to achieve/hold
   */
  private Double targetTemperature = 0.0 as Double

  /**
   * Actual reference temperature
   * */
  private Double actualTemperature = 0.0 as Double
  
  /**
   * Which reference sensors to use
   * Default: TemperatureRegulator.REFERENCE_SENSORS_MASHING
   * 
   * @see TemperatureRegulator.REFERENCE_SENSORS_*
   */
  private int referenceSensors = 0

  /**
   * Timer for temperature regulation cycles
   */
  private Timer timer;

  /**
   * Default logger
   */
  private Logger log = Logger.getLogger(getClass().getName());



  public TemperatureRegulator() {

  }

  /**
   * Adds an inner sensor to the list
   */
  public void addSensor(s) {
    this.sensors.add(s)
  }

  /**
   * Returns the list of inner sensors
   */
  public List getSensors() {
    return this.sensors
  }
  
  /** 
   * Returns a list with all actual reference sensor devices
   */
  public List getReferenceSensorDevices() {
    List mySensors = []
    this.sensors.each() { it ->
      if(this.referenceSensors == TemperatureRegulator.REFERENCE_SENSORS_MASHING
         && it.referenceForMashing == true) {
        mySensors.add(it)
      }
      else if(this.referenceSensors == TemperatureRegulator.REFERENCE_SENSORS_COOKING
              && it.referenceForCooking == true) {
        mySensors.add(it)
      }
    }
    return mySensors
  }

  /**
   * Adds a heater to the list of available heaters
   */
  public void addHeater(heater) {
    this.heaters.add(heater)
  }

  /**
   * Returns all heaters
   */
  public List getHeaters() {
    return this.heaters
  }

  /**
   * Sets the desired temperature
   */
  public void setTargetTemperature(Double t) {
    this.targetTemperature = t
    DeviceSwitcher d = DeviceSwitcher.getInstance();
    d.setTargetTemperature(t);
  }

  /**
   * Returns the temperature which is actually set
   */
  public Double getTargetTemperature() {
    return this.targetTemperature;
  }

  /**
   * Sets the actural reference temperature
   */
  public void setActualTemperature(Double t) {
    this.actualTemperature = t
  }

  /**
   * Returns the actual reference temperature
   */
  public Double getActualTemperature() {
    return this.actualTemperature;
  }

  /**
   * Select sensors for reference which are
   * marked as "mashing" reference sensors
   */
  public void setMashingReferenceSensors() {
    this.referenceSensors = REFERENCE_SENSORS_MASHING
  }
  
  /**
   * Select sensors for reference which are
   * marked as "cooking" reference sensors
   */
  public void setCookingReferenceSensors() {
    this.referenceSensors = REFERENCE_SENSORS_COOKING
  }
  
  /**
   * Getter for referenceSensors
   */
  public int getReferenceSensors() {
    return this.referenceSensors;
  }

  /**
   * Starts temperature controlling
   */
  public void start() throws Exception {
    if(this.heaters.size() < 1) {
      throw new Exception("A minimum of 1 heater has to be set")
    }
    this.timer = new Timer();
    this.timer.schedule(new TemperatureRegulatorTask(this), 1000, 3000);
  }

  /**
   * Stops temperature controlling
   */
  public void stop() {
    if (this.timer != null) {
      try {
        this.timer.cancel();
      }
      catch (Exception e) {}
    }

    try {
      this.heaters.each {
        it.disable();
      }
    }
    catch(Exception e) {
      log.error("Failed to disable heater: ${e}")
    }
  }

  /**
   * Changes the offset for temperature hysteresis
   */
  public void setHysteresis(Double h) {
    this.hysteresis = h;
  }

  /**
   * Getter for destinationTemperatureOffset
   */
  public Double getHysteresis() {
    return this.hysteresis;
  }
}

