package de.booze.backend.grails
import de.booze.tasks.MotorRegulatorDeviceTask

class MotorTask {

  /**
   * Target temperature (°C) to achieve by regulating up or
   * down the motor device
   */
  Double targetTemperature
    
  /**
   * Regulate up (true) or down (false) to minimize temperature 
   */
  boolean temperatureRegulationDirection = false
   
  /**
   * Target pressure (mbar) to achieve by regulating up or
   * down the motor device
   */
  Double targetPressure
    
  /**
   * Regulate up (true) or down (false) to minimize pressure 
   */
  boolean pressureRegulationDirection = false
  
  /**
   * Target speed
   */
  Integer targetSpeed = 100
  
  /**
   * This tasks motor
   */
  MotorDevice motor
  
  /**
   * Operation mode (interval, permanently on)
   */
  MotorDeviceMode mode
  
  /**
   * Task for softOn upspinning
   */
  MotorRegulatorDeviceTask motorRegulatorDeviceTask
  
  static transients = ["motorRegulatorDeviceTask"]

  static belongsTo = [setting: Setting]
  
  static hasMany = [temperatureSensors: TemperatureSensorDevice, pressureSensors: PressureSensorDevice]
  
  static constraints = {
    targetTemperature(nullable: true)
    targetPressure(nullable: true)
    motor(nullable: false)
  }
  
  public void enable() {
    if(this.motor.softOn && this.motor.softOn > 0) {
      this.motor.writeSpeed(0);
      this.motorRegulatorDeviceTask = new Timer();
      this.motorRegulatorDeviceTask.schedule(new MotorRegulatorDeviceTask(this), 0, 50);
    }
    else {
      this.motor.writeSpeed(100);
    }
  }
  
  public void disable() {
    if(this.motorRegulatorDeviceTask) {
      this.motorRegulatorDeviceTask.cancel();
      this.motorRegulatorDeviceTask = null;
    }
    this.motor.writeSpeed(0);
  }
  
  public Double readAveragePressure() {
    Double p = 0
    this.pressureSensors.each() { it ->
      p = it.readPressure()
    }
    return p/this.pressureSensors.size()
  }
  
  public Double readAverageTemperature() {
    Double t = 0
    this.temperatureSensors.each() { it ->
      t = it.readTemperature()
    }
    return t/this.temperatureSensors.size()
  }
}
