package de.booze.backend.grails
import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class HeaterRegulatorController {
  
  def settingService

  def edit = {
    HeaterDevice heater
    Setting setting
    HeaterRegulatorDevice regulator = new HeaterRegulatorDevice()
     
    if(params.regulator?.id && HeaterRegulatorDevice.exists(params.regulator?.id)) {
      regulator = HeaterRegulatorDevice.get(params.regulator?.id)
    }
      
    if(params.heater?.id && HeaterDevice.exists(params.heater?.id)) {
      heater = HeaterDevice.get(params.heater?.id)
    }
    
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    regulator.properties = params.regulator
    regulator.setting = setting
    
    Map model = [regulator: regulator, heater: heater, drivers: settingService.getDeviceDrivers("de.booze.drivers.heaterRegulators"), driverOptionValues: regulator.decodeOptions()]
    render([success: true, html: g.render(template:"edit", model: model)] as JSON)
  }
  
  def save = {
    HeaterDevice heater = new HeaterDevice()
    Setting setting
    HeaterRegulatorDevice regulator = new HeaterRegulatorDevice()
     
    if(params.regulator?.id && HeaterRegulatorDevice.exists(params.regulator?.id)) {
      regulator = HeaterRegulatorDevice.get(params.regulator?.id)
    }
    
    if(params.heater?.id && HeaterDevice.exists(params.heater?.id)) {
      heater = HeaterDevice.get(params.heater?.id)
    }
    
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    regulator.properties = params.regulator
    regulator.encodeOptions(params.driverOptionValues)
    regulator.heater = heater
    regulator.setting = setting
    
    Map model = [:]
    
    if(regulator.validate()) {
      render([success: true, message: g.message(code:"setting.heaterRegulator.save.saved"), regulator: regulator] as JSON)
      return
    }
    
    model.putAll([checkOptions: true, heater: heater, regulator: regulator, drivers: settingService.getDeviceDrivers("de.booze.drivers.heaterRegulators"), driverOptionValues: regulator.decodeOptions()])
    render([success: false, html:g.render(template:"edit", model: model)] as JSON)
  }
}
