package de.booze.backend.grails
import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class TemperatureSensorController {

  def settingService
  
  /**
   * @responseType JSON
   */
  def edit = {
    TemperatureSensorDevice ts = new TemperatureSensorDevice()
    Setting setting
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
      
    if(params.temperatureSensor?.id && TemperatureSensorDevice.exists(params.temperatureSensor?.id)) {
      ts = TemperatureSensorDevice.get(params.temperatureSensor?.id)
    }
      
    ts.properties = params.temperatureSensor
    if(params.driverOptionValues) {
      ts.encodeOptions(params.driverOptionValues)
    }
    
    
    Map model = [setting: setting, temperatureSensor: ts, drivers: settingService.getDeviceDrivers("de.booze.drivers.temperatureSensors"), driverOptionValues: ts.decodeOptions()]
    render([success: true, html: g.render(template:"edit", model: model)] as JSON)
    
  }
  
  def save = {

    TemperatureSensorDevice ts = new TemperatureSensorDevice()
    Setting setting
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    if(params.temperatureSensor?.id && TemperatureSensorDevice.exists(params.temperatureSensor?.id)) {
      ts = TemperatureSensorDevice.get(params.temperatureSensor?.id)
    }
    
    ts.properties = params.temperatureSensor
    ts.encodeOptions(params.driverOptionValues)
    ts.setting = setting
    
    Map model = [:]
    
    if(ts.validate()) {
      try {
        ts.save(flush: true)
        setting.refresh()
        
        render([success: true, message: g.message(code:"setting.temperatureSensor.save.saved"), html:g.render(template:"list", bean: setting)] as JSON)
        return
      }
      catch(Exception e) {
        log.error(e)
        model.error = g.message(code: "setting.temperaturSensor.save.failed")
      }
    }
    

    model.putAll([checkOptions: true, setting: setting, temperatureSensor: ts, drivers: settingService.getDeviceDrivers("de.booze.drivers.temperatureSensors"), driverOptionValues: ts.decodeOptions()])
    render([success: false, html:g.render(template:"edit", model: model)] as JSON)
  }
  
  def delete = {
    TemperatureSensorDevice ts = new TemperatureSensorDevice()
    Setting setting
     
    if(params.setting?.id && Setting.exists(params.setting?.id)) {
      setting = Setting.get(params.setting?.id)
    }
    
    if(params.temperatureSensor?.id && TemperatureSensorDevice.exists(params.temperatureSensor?.id)) {
      ts = TemperatureSensorDevice.get(params.temperatureSensor?.id)
    }
    
    def model
    try {
      setting.removeFromTemperatureSensors(ts)
      ts.delete()
      setting.save(flush: true)
      model = [success: true, message: g.message(code:"setting.temperatureSensor.delete.deleted"), html:g.render(template:"list", bean: setting)]
    }
    catch(Exception e) {
      log.error(e)
      model = [success: false, error: g.message(code: "setting.temperaturSensor.delete.failed")]
    }
    
    render(model as JSON)
  }
}
