package de.booze.backend.grails
import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class HeaterController {

  def settingService
  
  /**
   * @responseType JSON
   */
  def edit = {
    HeaterDevice heater
    Setting setting
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
      
    if(params.heater?.id && HeaterDevice.exists(params.heater?.id)) {
      heater = HeaterDevice.get(params.heater?.id)
    }
    else {
      heater = new HeaterDevice()   
    }
      
    heater.properties = params
    if(params.driverOptionValues) {
      heater.encodeOptions(params.driverOptionValues)
    }
    
    
    Map model = [setting: setting, heater: heater, drivers: settingService.getDeviceDrivers("de.booze.drivers.heaters"), driverOptionValues: heater.decodeOptions()]
    render([success: true, html: g.render(template:"edit", model: model)] as JSON)
    
  }
  
  def save = {
    
    HeaterDevice heater = new HeaterDevice()
    Setting setting
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    if(params.heater?.id && HeaterDevice.exists(params.heater?.id)) {
      heater = HeaterDevice.get(params.heater?.id)
    }
    
    heater.properties = params.heater
    heater.encodeOptions(params.driverOptionValues)
    heater.setting = setting
    
    Map model = [:]
    
    if(heater.validate()) {
      try {
        heater.save(flush: true)
        render([success: true, message: g.message(code:"setting.heater.save.saved"), html:g.render(template:"list", bean: setting)] as JSON)
        return
      }
      catch(Exception e) {
        model.error = g.message(code: "setting.heater.save.failed")
      }
    }

    model.putAll([checkOptions: true, setting: setting, heater: heater, drivers: settingService.getDeviceDrivers("de.booze.drivers.heaters"), driverOptionValues: heater.decodeOptions()])
    render([success: false, html:g.render(template:"edit", model: model)] as JSON)
  }
  
  def editHeaterRegulator = {
    
  }
  
  def saveHeaterRegulator = {
    
  }
}
