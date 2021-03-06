package de.booze.backend.grails
import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class MotorRegulatorController {
  
  def settingService

  def edit = {
    MotorDevice motor
    Setting setting
    MotorRegulatorDevice regulator = new MotorRegulatorDevice()
     
    if(params.regulator?.id && MotorRegulatorDevice.exists(params.regulator?.id)) {
      regulator = MotorRegulatorDevice.get(params.regulator?.id)
    }
      
    if(params.motor?.id && MotorDevice.exists(params.motor?.id)) {
      motor = MotorDevice.get(params.motor?.id)
    }
    
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    regulator.properties = params.regulator
    regulator.setting = setting
    
    Map model = [regulator: regulator, motor: motor, drivers: settingService.getDeviceDrivers("de.booze.drivers.motorRegulators"), driverOptionValues: regulator.decodeOptions()]
    render([success: true, html: g.render(template:"edit", model: model)] as JSON)
  }
  
  def save = {
    MotorDevice motor = new MotorDevice()
    Setting setting
    MotorRegulatorDevice regulator = new MotorRegulatorDevice()
     
    if(params.regulator?.id && MotorRegulatorDevice.exists(params.regulator?.id)) {
      regulator = MotorRegulatorDevice.get(params.regulator?.id)
    }
    
    if(params.motor?.id && MotorDevice.exists(params.motor?.id)) {
      motor = MotorDevice.get(params.motor?.id)
    }
    
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    regulator.properties = params.regulator
    regulator.encodeOptions(params.driverOptionValues)
    regulator.motor = motor
    regulator.setting = setting
    
    Map model = [:]
    
    if(regulator.validate()) {
      render([success: true, message: g.message(code:"setting.motorRegulator.save.saved"), regulator: regulator] as JSON)
      return
    }
    
    model.putAll([checkOptions: true, motor: motor, regulator: regulator, drivers: settingService.getDeviceDrivers("de.booze.drivers.motorRegulators"), driverOptionValues: regulator.decodeOptions()])
    render([success: false, html:g.render(template:"edit", model: model)] as JSON)
  }
}
