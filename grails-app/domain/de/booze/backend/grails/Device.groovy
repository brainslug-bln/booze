/**
 * Booze - Software for micro breweries
 *
 * Copyright (C) 2011  Andreas Kotsias <akotsias@esnake.de>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/
package de.booze.backend.grails

import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

/**
 * Represents an (abstract) device.
 * 
 */
class Device implements Serializable {

  /**
   * Device name
   */
  String name

  /**
   * Device driver class name
   */
  String driver

  /**
   * Device options
   */
  String options
  
  static belongsTo = [setting: Setting]


  static constraints = {
    name(nullable: false, blank: false, size: 1..255)
    driver(nullable: false, blank: false, size: 1..500)
    options(nullable: false, blank: false, size: 1..5000, validator: { val, obj ->
        if(!obj.driver) return true

        def myClassLoader = AH.application.mainContext.getClassLoader()
        def optionMap = obj.decodeOptions()
        List error = []
        
        optionMap.each() { key, value ->
          def myClass = Class.forName(obj.driver, false, myClassLoader)
          if(!myClass.checkOption(myClass, key, value)) {
            error.add("driver."+obj.driver+"."+key+".invalid")
          }
        }
        
        if(error.size() > 0) {
          return error
        }
        return true
      })
    setting(nullable: false)
  }

  /**
   * (Transient) driver instance
   */
  def driverInstance

  static transients = ['driverInstance']

  /**
   * Init the device driver
   * Store an instance of it in the transient driverInstance
   */
  def initDevice() {
    def myClassLoader = AH.application.mainContext.getClassLoader()
    def myClass = Class.forName(driver, false, myClassLoader)
    driverInstance = myClass.newInstance(JSON.parse(options));
  }

  /**
   * Gracefully shut down a driver instance
   */
  def shutdown() {
    driverInstance.shutdown()
    driverInstance = null
  }
    
  def decodeOptions() {
    if(!options) {
      return [:]
    }
    return JSON.parse(options)
  }
  
  def encodeOptions(params) {
    if(!driver || driver == "") {
      options = [:]
      return
    }
    
    Map checkedOptions = [:]
 
    def myClassLoader = AH.application.mainContext.getClassLoader()
    def myClass = Class.forName(driver, false, myClassLoader)
    
    params.each() { key, value ->
      if(myClass.hasOption(myClass, key)) {
        checkedOptions[key] = value
      }
    }
    options = checkedOptions.encodeAsJSON()
  }
  
}
