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
      
    if(!driverClass.checkOption(driverClass, attrs.option.name, attrs.optionValue)) {
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
}
