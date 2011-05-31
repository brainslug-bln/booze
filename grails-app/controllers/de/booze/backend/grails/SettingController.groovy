package de.booze.backend.grails
import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class SettingController {
  
  def settingService

  /**
   * List available settings
   * @responseType HTML
   */
  def list = { 
    [settings: Setting.list()]
  }

  /**
   * Delete a setting
   * @responseType HTML
   */
  def delete = {

  }

  /**
   * Delete a setting
   * @responseType HTML
   */
  def create = {
    [settingInstance: new Setting()]
  }

  /**
   * Delete a setting
   * @responseType HTML
   */
  def save = {
    Setting setting = new Setting()
    setting.properties = params.setting
    log.error(params.setting)
    if(setting.validate()) {
        
      try {
        setting.save()
        flash.message = g.message(code:"setting.save.saved")
        redirect(controller: "setting", action:"edit", id:setting.id)
      }
      catch(Exception e) {
        log.error("Saving setting failed: ${e}")
        flash.message = g.message(code:"setting.save.failed")
      }
    } 
    log.error(setting)
    render(view:"create", model:[settingInstance: setting])
  }
    
  def edit = {

    if(params.id  && Setting.exists(params.id)) {
      try {
        return [settingInstance: Setting.get(params.id)]
      }
      catch(Exception e) { }
    }
      
    flash.message = message(code:"setting.edit.notFound")
    redirect(action:"list")
  }

  /**
   * Delete a setting
   * @responseType JSON
   */
  def update = {
    if(!params?.setting?.id || !Setting.exists(params?.setting?.id)) {
      render([success: false, error: g.message(code: "setting.update.notFound")] as JSON)
      return
    }
      
    Setting setting = Setting.get(params.setting.id)
    setting.properties = params.setting
    if(setting.validate()) {
      try {
        setting.save()
        render([success: true, message: g.message(code:"setting.update.saved")] as JSON)
      }
      catch(Exception e) {
        flash.message = g.message(code:"setting.update.failed")
        log.error("Updating setting failed: ${e}")
      }
    }
    render([success: false, tab: params.tab, html: g.render(template:params.tab, bean: setting)] as JSON)
  }

  /**
   * @responseType JSON
   */
  def getDriverOptions = {
    Class driverClass
      
    try {
      def myClassLoader = AH.application.mainContext.getClassLoader()
      driverClass = Class.forName(params.driver, false, myClassLoader)
    }
    catch(Exception e) {
      log.error(e)
      render([success: false, error: g.message(code:"setting.driver.notFound")] as JSON)
      return
    }
    
    render([success: true, html: g.render( template:"driverOptions", model: [options: driverClass.availableOptions, driver: params.driver] )] as JSON)
  }
    
  /**
   * @responseType JSON
   */
  def editHeater = {
    HeaterDevice heater
    Setting setting
     
    if(params.setting.id && Setting.exists(params.setting.id)) {
      setting = Setting.get(params.setting.id)
    }
      
    if(params.id && HeaterDevice.exists(params.id)) {
      heater = HeaterDevice.get(params.id)
    }
    else {
      heater = new HeaterDevice()   
    }
      
    heater.properties = params
          
    Map model = [setting: setting, heater: heater, drivers: settingService.getDeviceDrivers("de.booze.drivers.heaters"), driverOptionValues: params.driverOptionValues]
    render([success: true, html: g.render(template:"editHeater", model: model)] as JSON)
    
  }
  
  def saveHeater = {
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
    
    Map model = [:]
    
    if(heater.validate()) {
      try {
        heater.save(flush: true)
        setting.addToHeaters(heater)
        setting.save(flush: true)
        render([success: true, message: g.message(code:"setting.heater.save.saved"), html:g.render(template:"listHeaters", bean: setting)])
      }
      catch(Exception e) {
        model.error = g.message(code: "setting.heater.save.failed")
      }
    }
    log.error(heater.errors)
    model.putAll([checkOptions: true, setting: setting, heater: heater, drivers: settingService.getDeviceDrivers("de.booze.drivers.heaters"), driverOptionValues: heater.decodeOptions()])
    render([success: false, html:g.render(template:"editHeater", model: model)] as JSON)
  }
}
