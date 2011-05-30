package de.booze.backend.grails
import grails.converters.JSON

class SettingController {

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
          log.error(e)
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
          log.error("Saving setting failed: ${e}")
        }
      }
      render([success: false, tab: params.tab, html: g.render(template:params.tab, bean: setting)] as JSON)
    }

    /**
     * Test a setting
     * @responseType JSON
     */
    def test = {

    }
}
