package de.booze.backend.grails
import de.booze.tasks.MotorRegulatorDeviceTask

class MotorTask {

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
  MotorRegulatorDeviceTask motorRegulatorDeviceTask
  
  public final static Integer CYCLING_MODE_ON = 0
  public final static Integer CYCLING_MODE_INTERVAL = 1
  public final static Integer CYCLING_MODE_OFF = 1
  
  public final static Integer REGULATION_MODE_OFF = 0
  public final static Integer REGULATION_MODE_SPEED = 1
  public final static Integer REGULATION_MODE_TEMPERATURE = 2
  public final static Integer REGULATION_MODE_PRESSURE = 3
  
  public final static Integer REGULATION_DIRECTION_UP = 0
  public final static Integer REGULATION_DIRECTION_DOWN = 1
  
  static transients = ["motorRegulatorDeviceTask"]

  static belongsTo = [setting: Setting]
  
  static hasMany = [temperatureSensors: TemperatureSensorDevice, pressureSensors: PressureSensorDevice]
  
  static constraints = {
    targetTemperature(nullable: true)
    targetPressure(nullable: true)
    targetSpeed(nullable: true)
    motor(nullable: false)
    temperatureRegulationDirection(nullable: true)
    pressureRegulationDirection(nullable: true)
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
