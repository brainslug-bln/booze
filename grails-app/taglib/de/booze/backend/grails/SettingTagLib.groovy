package de.booze.backend.grails
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
    
class SettingTagLib {
  
  static namespace = "setting"
  
  def checkDriverOption = { attrs, body ->
    Class driverClass
      
    try {
      def myClassLoader = AH.application.mainContext.getClassLoader()
      driverClass = Class.forName(attrs.driver, false, myClassLoader)
    }
    catch(Exception e) {
      log.error(e)
      return
    }
    def value = (attrs.optionValues.containsKey(attrs.option.name))?attrs.optionValues[attrs.option.name]:""

    if(!driverClass.checkOption(driverClass, attrs.option.name, value)) {
      out << g.message(code:"setting.driver.invalidValue")
    }
  }
    
  def eachDriverOption = { attrs, body ->
    Class driverClass
      
    try {
      def myClassLoader = AH.application.mainContext.getClassLoader()
      driverClass = Class.forName(attrs.driver, false, myClassLoader)
    }
    catch(Exception e) {
      log.error(e)
      return
    }
      
    driverClass.availableOptions.each { option ->
      out << body(option)
    }
  }
  
  def driverOptionValue = { attrs, body ->
    if(!attrs.values) return
    if(!attrs.option) return
    
    if(attrs.values.containsKey(attrs.option.name)) {
      out << attrs.values[attrs.option.name]
    }
    else if(attrs.option.defaultValue) {
      out << attrs.option.defaultValue
    }
  }

  def sensorSelected = { attrs, body ->
    if(!attrs.selectedSensors) return
    
    attrs.selectedSensors.each() {it ->
      if(it.id == attrs.sensor.id) {
        out << " selected='selected'"
      }
    }
  }
  
  def activeSettingName = { attrs, body ->
    def s = Setting.findByActive(true)
    if(s) {
      out << "<a href='${createLink(controller:'setting', action:'edit', id:s.id)}'>${s.name.encodeAsHTML()}</a>"
    }
    else {
      out << message(code:"setting.noActiveAvailable")
    }
  }
  
  def activeSettingExists = { attrs, body ->
    if(Setting.findByActive(true)) {
      out << body()
    }
  }
}
