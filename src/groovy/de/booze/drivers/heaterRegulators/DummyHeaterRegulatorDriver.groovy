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

package de.booze.drivers.heaterRegulators

import java.util.*;

import org.apache.log4j.Logger
import de.booze.driverInterfaces.AbstractHeaterRegulatorDriver

/**
 * 
 *
 * @author akotsias
 */
class DummyHeaterRegulatorDriver extends AbstractHeaterRegulatorDriver {

    /**
     * Logger instance
     */
    private Logger log = Logger.getLogger(getClass().getName());

    /**
     * Acutal speed in percent
     */
    private Integer power = 0;
  
    /**
     * Driver options
     */
    public static availableOptions = []

    /**
     * Constructor
     *
     * Gets an IOWController instance and registers the PCF8591 adc
     *
     */
    public DummyHeaterRegulatorDriver(Map o) throws Exception, IllegalArgumentException {
        this.setOptions(o);
    }

    /**
     * Set the power for this heater regulator in percent
     *
     * @param int speed
     */
    public void setPower(Integer power) throws Exception, IllegalArgumentException {
        if(power >= 0 && power <= 100) {
            this.power = power;
        }
        else {
            throw new IllegalArgumentException("Heater power must be between 0 and 100");
        }
    }

    /**
     * Returns the actual speed
     */
    public Integer getPower() {
        return this.power;
    }
}

