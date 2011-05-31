package de.booze.backend.grails
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
    
class SettingTagLib {
  
  static namespace = "setting"
  
    def checkDriverOption = { attrs, body ->
      Class driverClass
      
      try {
        def myClassLoader = AH.application.mainContext.getClassLoader()
        driverClass = Class.forName(params.driver, false, myClassLoader)
      }
      catch(Exception e) {
        log.error(e)
        return
      }
      
      if(!driverClass.checkOption(driverClass, attrs.option.name, attrs.optionValue)) {
        out << g.message(code:"setting.driver.invalidValue")
      }
    }
}
