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
  private Double targetTemperature = 0d
  
  /**
   * Temperature thats lies on the heating ramp
   */
  private Double rampTemperature = 0d
  
  /**
   * Ramp start temperature
   */
  private Double rampStartTemperature = 0d
  
  /**
   * Heating ramp
   */
  private Double heatingRamp = 1d
  
  /**
   * Heating ramp start date
   */
  private Date rampStartTime = new Date()

  /**
   * Actual reference temperature
   * */
  private Double actualTemperature = 0d
  
  /**
   * Which reference sensors to use
   * Default: TemperatureRegulator.REFERENCE_SENSORS_MASHING
   * 
   * @see TemperatureRegulator.REFERENCE_SENSORS_*
   */
  private int referenceSensors = 0
  
  /**
   * Wheter to use the heating ramp or not
   */
  private boolean useRamp = true;

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
    
    
    Double at = this.getActualReferenceTemperature();
    
    this.rampTemperature = at;
    this.rampStartTemperature = at
    this.rampStartTime = new Date()
    
    DeviceSwitcher d = DeviceSwitcher.getInstance();
    d.setTargetTemperature(t);
  }
  
  public Double getActualReferenceTemperature() {
    Double rt = 0.0d;
    int rtc = 0;
    
    this.getReferenceSensorDevices().each() {
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
   * Returns the actual ramp temperature
   */
  public Double getRampTemperature() {
    if(!this.useRamp) {
      log.debug("not using the heating ramp")
      return this.targetTemperature
    }
    
    Long timeElapsed = (new Date()).getTime() - this.rampStartTime.getTime()
    if((this.rampStartTemperature + (timeElapsed / 60000) * (this.heatingRamp)) <= this.targetTemperature) {
      this.rampTemperature = this.rampStartTemperature + (timeElapsed / 60000) * (this.heatingRamp)
    }
    else {
      this.rampTemperature = this.targetTemperature;
    }
    
    log.debug("actual ramp temperature is ${this.rampTemperature}°C")
    return this.rampTemperature;
  }
  
  
  public void setHeatingRamp(Double r) {
    log.debug("setting heating ramp to ${this.heatingRamp}°C/min")
    this.heatingRamp = r;
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
  
  /**
   * Do not use the heating ramp
   */
  public void disableRamp() {
    this.useRamp = false;
  }
  
  
  /**
   * Use the heating ramp
   */
  public void enableRamp() {
    this.useRamp = true;
  }
}

