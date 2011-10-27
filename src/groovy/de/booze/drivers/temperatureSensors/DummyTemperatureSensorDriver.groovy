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
 * */

package de.booze.drivers.temperatureSensors

import org.apache.log4j.Logger
import de.booze.driverInterfaces.AbstractTemperatureSensorDriver
import de.booze.regulation.DeviceSwitcher
import de.booze.regulation.*

/**
 * Driver for a dummy temperature sensor
 *
 * @author akotsias
 */
class DummyTemperatureSensorDriver extends AbstractTemperatureSensorDriver {

    /**
     * Logger instance
     */
    private Logger log = Logger.getLogger(getClass().getName());

    /**
     * Driver options
     */
    public static availableOptions = []
    
    private Double lastTemperature = 28.0 as Double;

    private Double threshold = 5.0

    /**
     * Constructor
     * Gets an IOWController instance and registers the PCF8591 adc
     *
     * @param String Address for this device
     * @param Double Minimum temperature value
     * @param Double Maximum temperature value
     */
    public DummyTemperatureSensorDriver(Map o) throws Exception, IllegalArgumentException {
        this.setOptions(o);
    }

    /**
     * Returns temperature in degrees celsius
     */
    public Double getTemperature() throws Exception {
      DeviceSwitcher d = DeviceSwitcher.getInstance();
      if (this.lastTemperature < (d.getTargetTemperature() + 2.0 as Double)) {
        this.lastTemperature += 0.002 * this.threshold as Double;

        if (this.threshold > 1.1) {
          // this.threshold -= (0.000005 as Double)
        }
      }
      return this.lastTemperature;
    }
}

