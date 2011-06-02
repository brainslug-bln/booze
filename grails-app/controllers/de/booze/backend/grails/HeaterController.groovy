package de.booze.backend.grails
import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class HeaterController {

  def settingService
  
  /**
   * @responseType JSON
   */
  def edit = {
    HeaterDevice heater = new HeaterDevice()
    Setting setting
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
      
    if(params.heater?.id && HeaterDevice.exists(params.heater?.id)) {
      heater = HeaterDevice.get(params.heater?.id)
    }
      
    heater.properties = params
    if(params.driverOptionValues) {
      heater.encodeOptions(params.driverOptionValues)
    }
    
    
    Map model = [setting: setting, heater: heater, drivers: settingService.getDeviceDrivers("de.booze.drivers.heaters"), driverOptionValues: heater.decodeOptions()]
    render([success: true, html: g.render(template:"edit", model: model)] as JSON)
    
  }
  
  def save = {
    log.error("Regler: "+HeaterRegulatorDevice.list())

    HeaterDevice heater = new HeaterDevice()
    Setting setting
    HeaterRegulatorDevice oldRegulator
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    if(params.heater?.id && HeaterDevice.exists(params.heater?.id)) {
      heater = HeaterDevice.get(params.heater?.id)
    }
    
    heater.properties = params.heater
    heater.encodeOptions(params.driverOptionValues)
    heater.setting = setting
    
    if(params.heater?.hasRegulator == "1") {
      if(!heater?.regulator) {
        heater.regulator = new HeaterRegulatorDevice()
      }
      heater.regulator.properties = params.regulator
    }
    else {
      oldRegulator = heater.regulator
      heater.regulator = null
    }
    
    Map model = [:]
    
    if(heater.validate()) {
      try {
        // First save the device
        heater.save()
        
        // Now save the regulator association
        if(heater.regulator) {
          heater.regulator.heater = heater
          heater.regulator.save()
        }
        
        // Finally delete the old regulator association
        if(oldRegulator) {
          oldRegulator.delete()
        }
        render([success: true, message: g.message(code:"setting.heater.save.saved"), html:g.render(template:"list", bean: setting)] as JSON)
        return
      }
      catch(Exception e) {
        log.error(e)
        model.error = g.message(code: "setting.heater.save.failed")
      }
    }
    
    model.putAll([checkOptions: true, setting: setting, heater: heater, drivers: settingService.getDeviceDrivers("de.booze.drivers.heaters"), driverOptionValues: heater.decodeOptions()])
    render([success: false, html:g.render(template:"edit", model: model)] as JSON)
  }
  
  def delete = {
    HeaterDevice heater = new HeaterDevice()
    Setting setting
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    if(params.heater?.id && HeaterDevice.exists(params.heater?.id)) {
      heater = HeaterDevice.get(params.heater?.id)
    }
    
    def model
    try {
      setting.removeFromHeaters(heater)
      setting.save()
      heater.delete()
      model = [success: true, message: g.message(code:"setting.heater.delete.deleted"), html:g.render(template:"list", bean: setting)]
    }
    catch(Exception e) {
      log.error(e)
      model = [success: false, error: g.message(code: "setting.temperaturSensor.delete.failed")]
    }
    
    render(model as JSON)
  }
}
