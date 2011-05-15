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


import de.booze.grails.Recipe
import de.booze.tasks.TemperatureRegulatorTask

/**
 * Controls temperature regulation
 *
 * @author akotsias
 */
class TemperatureRegulator {

  /**
   * Maximal temperature difference between
   * inner and outer sensors
   */
  final static Double INNER_OUTER_MAX_DIFFERENCE = 5.0 as Double

  /**
   * Offset at which to start to shut down heaters before
   * reaching the desired temperature
   * Default value is 4.0
   */
  private Double destinationTemperatureOffset = 4.0 as Double

  /**
   * List of available heaters
   */
  private List heaters = []

  /**
   * List of available inner sensors
   * i.e. sensors which measure the real mesh temperature
   */
  private List innerSensors = []

  /**
   * List of available outer sensors
   * e.g. sensors which measure the temperature
   * of the outer beholder (usually higher then inner
   * temperature)
   */
  private List outerSensors = []

  /**
   * Temperature to achieve/hold
   */
  private Double temperature = 0.0 as Double

  /**
   * Actual reference temperature
   * */
  private Double actualTemperature = 0.0 as Double

  /**
   * Specifies which sensors to use as reference
   */
  private int referenceSensors = Recipe.TEMPERATURE_REFERENCE_INNER;

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
  public void addInnerSensor(s) {
    this.innerSensors.add(s)
  }

  /**
   * Returns the list of inner sensors
   */
  public List getInnerSensors() {
    return this.innerSensors
  }

  /**
   * Adds an outer sensor to the list
   */
  public void addOuterSensor(s) {
    this.outerSensors.add(s)
  }

  /**
   * Returns the list of outer sensors
   */
  public List getOuterSensors() {
    return this.outerSensors
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
  public void setTemperature(Double t) {
    this.temperature = t
    DeviceSwitcher d = DeviceSwitcher.getInstance();
    d.setTargetTemperature(t);
  }

  /**
   * Returns the temperature which is actually set
   */
  public Double getTemperature() {
    return this.temperature;
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
   * Setter for referenceSensors
   *
   * @see Recipe.TEMPERATURE_REFERENCE_*
   */
  public void setReferenceSensors(int reference) {
    this.referenceSensors = reference;
    log.debug("Using reference sensors: ${reference}")
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
  public void start() {
    this.timer = new Timer();
    this.timer.schedule(new TemperatureRegulatorTask(this), 1000, 3000);
  }

  /**
   * Stops temperature controlling
   */
  public void stop() {
    if (!this.timer != null) {
      try {
        this.timer.cancel();
      }
      catch (Exception e) {}
    }

    this.heaters.each {
      it.disable();
    }
  }

  /**
   * Changes the offset for temperature hysteresis
   */
  public void setOffset(Double offset) {
    this.destinationTemperatureOffset = offset;
  }

  /**
   * Getter for destinationTemperatureOffset
   */
  public Double getOffset() {
    return this.destinationTemperatureOffset;
  }
}

