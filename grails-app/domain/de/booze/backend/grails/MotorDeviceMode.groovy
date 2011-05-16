package de.booze.backend.grails

/**
 * Represents a motor device mode
 * Valid modes are "always on" and "interval".
 * If interval mode is specified you need to set
 * values for on/off interval time in seconds
 */
class MotorDeviceMode {

  final static Integer MODE_ON = 0
  final static Integer MODE_INTERVAL = 1
  
  Integer mode
  
  Integer onInterval
  Integer offInterval
  
  static constraints = {
    mode(nullable: false, inList:[MODE_ON, MODE_INTERVAL])
    onInterval(nullable: true, validator: { val, obj ->
        if(obj.mode && obj.mode == MotorDeviceMode.MODE_INTERVAL && val.isNull()) {
          return ['motorDeviceMode.onInterval.notNullable']
        }
      })
    offInterval(nullable: true, validator: { val, obj ->
        if(obj.mode && obj.mode == MotorDeviceMode.MODE_INTERVAL && val.isNull()) {
          return ['motorDeviceMode.offInterval.notNullable']
        }
      })
  }
}
