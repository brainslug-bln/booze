/**
 * Booze - Software for micro breweries
 *
 * Copyright (C) 2011  Andreas Kotsias <akotsias@esnake.de>
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
package de.booze.backend.grails
import de.booze.tasks.MotorRegulatorDeviceTask
import de.booze.process.BrewProcessHolder
import de.booze.process.BrewProcess

class MotorTask implements Serializable {

  /**
   * Regulation mode
   * speed / temperature / pressure regulated
   */
  Integer regulationMode = MotorTask.REGULATION_MODE_OFF
  
  /**
   * Target temperature (°C) to achieve by regulating up or
   * down the motor device
   */
  Double targetTemperature
    
  /**
   * Regulate up (true) or down (false) to minimize temperature 
   */
  Integer temperatureRegulationDirection = 1
   
  /**
   * Target pressure (mbar) to achieve by regulating up or
   * down the motor device
   */
  Double targetPressure
    
  /**
   * Regulate up (true) or down (false) to minimize pressure 
   */
  Integer pressureRegulationDirection = 1
  
  /**
   * Target speed
   */
  Integer targetSpeed = 100
  
  Integer onInterval
  Integer offInterval
  
  /**
   * This tasks motor
   */
  MotorDevice motor
  
  /**
   * Operation mode (interval, permanently on)
   */
  Integer cyclingMode
  
  /**
   * Task for softOn upspinning
   */
  Timer motorRegulatorDeviceTask
  
  /**
   */
  boolean enabled = false
  
  public final static Integer CYCLING_MODE_ON = 0
  public final static Integer CYCLING_MODE_INTERVAL = 1
  public final static Integer CYCLING_MODE_OFF = 1
  
  public final static Integer REGULATION_MODE_OFF = 0
  public final static Integer REGULATION_MODE_SPEED = 1
  public final static Integer REGULATION_MODE_TEMPERATURE = 2
  public final static Integer REGULATION_MODE_PRESSURE = 3
  
  public final static Integer REGULATION_DIRECTION_UP = 0
  public final static Integer REGULATION_DIRECTION_DOWN = 1
  
  static transients = ["motorRegulatorDeviceTask", "enabled"]

  static belongsTo = [setting: Setting]
  
  static hasMany = [temperatureSensors: TemperatureSensorDevice, pressureSensors: PressureSensorDevice]
  
  static constraints = {
    targetTemperature(nullable: true, validator: { val, obj ->
        if(obj.regulationMode == MotorTask.REGULATION_MODE_TEMPERATURE) {
          if(val == null) {
            return ['motorTask.targetTemperature.notNullable']
          }
        } 
      })
    targetPressure(nullable: true, min: 0d, max: 10000d, validator: { val, obj ->
        if(obj.regulationMode == MotorTask.REGULATION_MODE_PRESSURE) {
          if(val == null) {
            return ['motorTask.targetPressure.notNullable']
          }
        } 
      })
    targetSpeed(nullable: true, min: 0, max: 100, validator: { val, obj ->
        if(obj.regulationMode == MotorTask.REGULATION_MODE_SPEED) {
          if(val == null) {
            return ['motorTask.targetSpeed.notNullable']
          }
        } 
      })
    motor(nullable: false)
    temperatureRegulationDirection(nullable: true, validator: { val, obj ->
        if(obj.regulationMode == MotorTask.REGULATION_MODE_TEMPERATURE) {
          if(val == null) {
            return ['motorTask.temperatureRegulationDirection.notNullable']
          }
        } 
      })
    pressureRegulationDirection(nullable: true, validator: { val, obj ->
        if(obj.regulationMode == MotorTask.REGULATION_MODE_PRESSURE) {
          if(val == null) {
            return ['motorTask.pressureRegulationDirection.notNullable']
          }
        } 
      })
    cyclingMode(nullable: false, inList:[CYCLING_MODE_ON, CYCLING_MODE_INTERVAL])
    onInterval(nullable: true, validator: { val, obj ->
        if(obj.cyclingMode && obj.cyclingMode == MotorTask.CYCLING_MODE_INTERVAL && val == null) {
          return ['motorTask.onInterval.notNullable']
        }
      })
    offInterval(nullable: true, validator: { val, obj ->
        if(obj.cyclingMode && obj.cyclingMode == MotorTask.CYCLING_MODE_INTERVAL && val == null) {
          return ['motorTask.offInterval.notNullable']
        }
      })
  }
  
  static mapping = {
    pressureSensors cascade: "evict,refresh"
    temperatureSensors cascade: "evict,refresh"
    columns {
      motor lazy: false
      pressureSensors lazy: false
      temperatureSensors lazy: false
    }
  }
  
  
  def checkTargetSpeed = {
    if(regulationMode != REGULATION_MODE_SPEED) {
      targetSpeed = 100;
    }
  }
  
  public void enable() {
    if(this.readMotor().hasRegulator() && this.readMotor().regulator?.softOn && this.readMotor().regulator?.softOn > 0) {
      this.readMotor().writeSpeed(0);
      this.motorRegulatorDeviceTask = new Timer();
      this.motorRegulatorDeviceTask.schedule(new MotorRegulatorDeviceTask(this), 0, 50);
    }
    else {
      this.readMotor().writeSpeed(this.targetSpeed);
    }
    
    this.readMotor().enable();
    this.enabled = true;
  }
  
  public void disable() {
    if(this.motorRegulatorDeviceTask) {
      this.motorRegulatorDeviceTask.cancel();
      this.motorRegulatorDeviceTask = null;
    }
    
    this.readMotor().disable();
    this.enabled = false;
    
    Thread.sleep(100);
    System.out.println("disabled motor, writing motor speed 0!")
    this.readMotor().writeSpeed((int)0);
  }
  
  public boolean enabled() {
    return this.enabled
  }
  
  public Double readAveragePressure() {
    BrewProcessHolder h = BrewProcessHolder.getInstance()
    BrewProcess p = h.getBrewProcess()
    
    Double d = 0
    this.pressureSensors.each() { it ->
      def mySensor = p.getPressureSensor(it.id)
      d = mySensor.readPressure()
    }
    return d/this.pressureSensors.size()
  }
  
  public Double readAverageTemperature() {
    BrewProcessHolder h = BrewProcessHolder.getInstance()
    BrewProcess p = h.getBrewProcess()
    
    Double t = 0
    this.temperatureSensors.each() { it ->
      def mySensor = p.getTemperatureSensor(it.id)
      t = mySensor.readTemperature()
    }
    return t/this.temperatureSensors.size()
  }
  
  public MotorDevice readMotor() {
    BrewProcessHolder h = BrewProcessHolder.getInstance()
    BrewProcess p = h.getBrewProcess()
    
    return p.getMotor(this.motor.id)
  }
}
