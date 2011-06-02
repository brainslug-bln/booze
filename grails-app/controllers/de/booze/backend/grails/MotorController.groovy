package de.booze.backend.grails
import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class MotorController {

  def settingService
  
  /**
   * @responseType JSON
   */
  def edit = {
    MotorDevice motor = new MotorDevice()
    Setting setting
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
      
    if(params.motor?.id && MotorDevice.exists(params.motor?.id)) {
      motor = MotorDevice.get(params.motor?.id)
    }
      
    motor.properties = params
    if(params.driverOptionValues) {
      motor.encodeOptions(params.driverOptionValues)
    }
    
    
    Map model = [setting: setting, motor: motor, drivers: settingService.getDeviceDrivers("de.booze.drivers.motors"), driverOptionValues: motor.decodeOptions()]
    render([success: true, html: g.render(template:"edit", model: model)] as JSON)
    
  }
  
  def save = {
    log.error("Regler: "+MotorRegulatorDevice.list())

    MotorDevice motor = new MotorDevice()
    Setting setting
    MotorRegulatorDevice oldRegulator
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    if(params.motor?.id && MotorDevice.exists(params.motor?.id)) {
      motor = MotorDevice.get(params.motor?.id)
    }
    
    motor.properties = params.motor
    motor.encodeOptions(params.driverOptionValues)
    motor.setting = setting
    
    if(params.motor?.hasRegulator == "1") {
      if(!motor?.regulator) {
        motor.regulator = new MotorRegulatorDevice()
      }
      motor.regulator.properties = params.regulator
    }
    else {
      oldRegulator = motor.regulator
      motor.regulator = null
    }
    
    Map model = [:]
    
    if(motor.validate()) {
      try {
        // First save the device
        motor.save()
        
        // Now save the regulator association
        if(motor.regulator) {
          motor.regulator.motor = motor
          motor.regulator.save()
        }
        
        // Finally delete the old regulator association
        if(oldRegulator) {
          oldRegulator.delete()
        }
        render([success: true, message: g.message(code:"setting.motor.save.saved"), html:g.render(template:"list", bean: setting)] as JSON)
        return
      }
      catch(Exception e) {
        log.error(e)
        model.error = g.message(code: "setting.motor.save.failed")
      }
    }
    log.error(motor.errors)
    model.putAll([checkOptions: true, setting: setting, motor: motor, drivers: settingService.getDeviceDrivers("de.booze.drivers.motors"), driverOptionValues: motor.decodeOptions()])
    render([success: false, html:g.render(template:"edit", model: model)] as JSON)
  }
  
  def delete = {
    MotorDevice motor = new MotorDevice()
    Setting setting
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    if(params.motor?.id && MotorDevice.exists(params.motor?.id)) {
      motor = MotorDevice.get(params.motor?.id)
    }
    
    def model
    try {
      setting.removeFromMotors(motor)
      setting.save()
      motor.delete()
      model = [success: true, message: g.message(code:"setting.motor.delete.deleted"), html:g.render(template:"list", bean: setting)]
    }
    catch(Exception e) {
      log.error(e)
      model = [success: false, error: g.message(code: "setting.temperaturSensor.delete.failed")]
    }
    
    render(model as JSON)
  }
}
