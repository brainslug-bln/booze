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
    if(!params.id || !Setting.exists(params.id)) {
      flash.message = g.message(code:"setting.delete.notFound")
      redirect(action: "list")
      return
    }

    Setting setting = Setting.get(params.id)
    
    setting.heaters.each {
      it.delete(flush: true)
    }
    
    setting.motors.each {
      it.delete(flush: true)
    }
    
    setting.temperatureSensors.each {
      it.delete(flush: true)
    }
    
    setting.pressureSensors.each {
      it.delete(flush: true)
    }
    
    setting.delete(flush: true)
    
    flash.message = g.message(code:"setting.delete.deleted")
    redirect(action:'list')
  }

  /**
   * Display the setting create dialog
   * @responseType HTML
   */
  def create = {
    [settingInstance: new Setting()]
  }

  /**
   * Save a setting
   * @responseType HTML
   */
  def save = {
    Setting setting = new Setting()
    setting.properties = params.setting
    log.error(params.setting)
    if(setting.validate()) {
        
      try {
        setting.save()
        if(setting.active == true) {
          List ss = Setting.findAll()
          for(int i=0; i<ss.size(); i++) {
            if(ss[i].id != setting.id) {
              ss[i].active = false;
              ss[i].save();
            }
          }
        }
        
        flash.message = g.message(code:"setting.save.saved")
        redirect(controller: "setting", action:"edit", id:setting.id)
      }
      catch(Exception e) {
        log.error("Saving setting failed: ${e}")
        flash.message = g.message(code:"setting.save.failed")
        e.printStackTrace()
      }
    } 
    log.error(setting)
    render(view:"create", model:[settingInstance: setting])
  }
  
  def editActive = {
    if(!Setting.findByActive(true)) {
      redirect(controller:'setting', action:'list')
    }
    render(view:"edit", model:[settingInstance: Setting.findByActive(true)])
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
   * Update a setting
   * @responseType JSON
   */
  def update = {
    if(!params?.setting?.id || !Setting.exists(params?.setting?.id)) {
      render([success: false, error: g.message(code: "setting.update.notFound")] as JSON)
      return
    }
      
    Setting setting = Setting.get(params.setting.id)
    
    boolean validationErrors = false
    
    List thingsToRemove = []
    
    if(params.tab == "motorTasks") {
      ['mashingPump', 'mashingMixer', 'cookingPump', 'cookingMixer', 'drainPump'].each() { task ->
        
        thingsToRemove.add(setting[task])
        
        def newTask = null
        
        if(params.setting[task]?.active == "true") {
          newTask = new MotorTask(params.setting[task])
          newTask.setting = setting
          newTask.checkTargetSpeed()
          
          if(!newTask.validate()) {
            validationErrors = true
            newTask.discard()
          }
          
          
        }

        setting[task] = newTask
      }  
    }
    else {
      setting.properties = params.setting
    }
    
      
    if (validationErrors == false && setting.validate()) {
      try {
        setting.save(flush: true)
        
        thingsToRemove.each() { ttr ->
          ttr?.delete(flush:true)
        }
        
        render([success: true, message: g.message(code:"setting.update.saved"), tab: params.tab, html: g.render(template:params.tab, bean: setting)] as JSON)
        return
      }
      catch(Exception e) {
        flash.message = g.message(code:"setting.update.failed")
        log.error("Updating setting failed: ${e}")
      }
    }
    else {
      setting.discard()
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
  def createMotorTask = {
    if(!params?.setting?.id || !Setting.exists(params?.setting?.id)) {
      render([success: false, error: g.message(code: "setting.createMotorTask.notFound")] as JSON)
      return
    }
    
    Setting setting = Setting.read(params.setting.id)
    
    render([success: true, html: g.render(template: 'motorTaskData', model: [type: params.type, setting: setting])] as JSON)
  }
}
