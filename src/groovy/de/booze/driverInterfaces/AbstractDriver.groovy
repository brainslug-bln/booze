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
 * */

package de.booze.driverInterfaces

/**
 * Abstract driver interface
 * 
 */
abstract class AbstractDriver {
    
    /**
     * Map that contains the names of variables
     * which contain driver options
     */
    abstract private static DriverOption[] availableOptions;
    
    /**
     * Getter for options
     */
    public DriverOption[] getAvailableOptions() {
        return this.availableOptions;
    }
    
    public void shutdown() {
      
    }
    
    /**
     * Sets driver options
     * 
     * Options are given as key:value pairs in a map
     */
    public void setOptions(Map o) throws IllegalArgumentException {
        
        // Check every single option
        o.each() { it ->
            if(!getClass().checkOption(it)) {
                throw new IllegalArgumentException("Invalid value '${it.value}' for option ${it.name}");
            }
            else {
                this[it.name] = it.value;
            }
        }
    }
    
    /**
     * Checks if a single option is valid for this driver
     */
    public static boolean checkOption(String name, String value) throws IllegalArgumentException {
        options.each() { it ->
            if(it.name == name) {
                if(value ==~ it.validator) {
                    return true
                }
                return false
            }
        }
        throw IllegalArgumentException("Driver option '$name' not found")
    }
}

