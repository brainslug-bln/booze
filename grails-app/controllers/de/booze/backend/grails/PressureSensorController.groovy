package de.booze.backend.grails
import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class PressureSensorController {

  def settingService
  
  /**
   * @responseType JSON
   */
  def edit = {
    PressureSensorDevice ts = new PressureSensorDevice()
    Setting setting
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
      
    if(params.pressureSensor?.id && PressureSensorDevice.exists(params.pressureSensor?.id)) {
      ts = PressureSensorDevice.get(params.pressureSensor?.id)
    }
      
    ts.properties = params.pressureSensor
    if(params.driverOptionValues) {
      ts.encodeOptions(params.driverOptionValues)
    }
    
    
    Map model = [setting: setting, pressureSensor: ts, drivers: settingService.getDeviceDrivers("de.booze.drivers.pressureSensors"), driverOptionValues: ts.decodeOptions()]
    render([success: true, html: g.render(template:"edit", model: model)] as JSON)
    
  }
  
  def save = {

    PressureSensorDevice ts = new PressureSensorDevice()
    Setting setting
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    if(params.pressureSensor?.id && PressureSensorDevice.exists(params.pressureSensor?.id)) {
      ts = PressureSensorDevice.get(params.pressureSensor?.id)
    }
    
    ts.properties = params.pressureSensor
    ts.encodeOptions(params.driverOptionValues)
    ts.setting = setting
    
    Map model = [:]
    
    if(ts.validate()) {
      try {
        ts.save(flush: true)
        
        setting.refresh()
        render([success: true, message: g.message(code:"setting.pressureSensor.save.saved"), html:g.render(template:"list", bean: setting)] as JSON)
        return
      }
      catch(Exception e) {
        log.error(e)
        model.error = g.message(code: "setting.temperaturSensor.save.failed")
      }
    }
    

    model.putAll([checkOptions: true, setting: setting, pressureSensor: ts, drivers: settingService.getDeviceDrivers("de.booze.drivers.pressureSensors"), driverOptionValues: ts.decodeOptions()])
    render([success: false, html:g.render(template:"edit", model: model)] as JSON)
  }
  
  def delete = {
    PressureSensorDevice ps = new PressureSensorDevice()
    Setting setting
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    if(params.pressureSensor?.id && PressureSensorDevice.exists(params.pressureSensor?.id)) {
      ps = PressureSensorDevice.get(params.pressureSensor?.id)
    }
    
    def model
    try {
      setting.removeFromPressureSensors(ps)
      ps.delete()
      setting.save(flush: true)
      
      model = [success: true, message: g.message(code:"setting.pressureSensor.delete.deleted"), html:g.render(template:"list", bean: setting)]
    }
    catch(Exception e) {
      log.error(e)
      model = [success: false, error: g.message(code: "setting.temperaturSensor.delete.failed")]
    }
    
    render(model as JSON)
  }
  
}
