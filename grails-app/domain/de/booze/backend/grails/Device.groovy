/**
 * Booze - Software for micro breweries
 *
 * Copyright (C) 2010  Andreas Kotsias <akotsias@esnake.de>
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


    static constraints = {
        name(nullable: false, blank: false, size: 1..255)
        driver(nullable: false, blank: false, size: 1..500)
        options(nullable: false, blank: false, size: 1..5000, validator: { val, obj ->
            if(!obj.driver) return true

            def myClassLoader = AH.application.mainContext.getClassLoader()

            def optionMap = JSON.parse(val)
            optionMap.each() { key, value ->
                def myClass = Class.forName(obj.driver, false, myClassLoader)
                if(myClass.checkOption(key, value)) {
                    return true
                }
                else {
                    return ["driver.${obj.driver}.${key}.invalid"]
                }
            }
        })
    }

    /**
     * (Transient) driver instance
     */
    def driverInstance

    static transients = ['driverInstance']

    /**
     * Init the device driver
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
}
