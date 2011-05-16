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
import de.booze.controllers.IoWarriorController
import de.booze.driverInterfaces.AbstractTemperatureSensorDriver
import de.booze.driverInterfaces.DriverOption


/**
 * Driver for an analogue temperature sensor
 * connected via a combination of IOWarrior and
 * a PCF8591 a/d converter
 *
 * @author akotsias
 */
class BoozeTemperatureSensorDriver extends AbstractTemperatureSensorDriver {

    /**
     * Minimum temperature value
     */
    private Double minimum;

    /**
     * Maximum temperature value
     */
    private Double maximum;

    /**
     * Logger instance
     */
    private Logger log = Logger.getLogger(getClass().getName());

    /**
     * Controller instance
     */
    private IoWarriorController iow;

    /**
     * PCF8591 address (0..7)
     */
    private Integer pcf8591Address;

    /**
     * PCF8591 Channel
     */
    private Integer pcf8591Channel;

    /**
     * Driver options
     */
    private static availableOptions = [new DriverOption("pcf8591Address", 
                                             "de.booze.drivers.heaters.BoozeTemperaturSensor.pcf8591Address.description",
                                                 "/d/"),
                                       new DriverOption("pcf8591Channel", 
                                                 "de.booze.drivers.heaters.BoozeTemperaturSensor.pcf8591Channel.description",
                                                 "/d/"),
                                       new DriverOption("minimum", 
                                                 "de.booze.drivers.heaters.BoozeTemperaturSensor.minimum.description",
                                                 "/d+/",
                                                 "0"),
                                       new DriverOption("maximum", 
                                                 "de.booze.drivers.heaters.BoozeTemperaturSensor.maximum.description",
                                                 "/d+/",
                                                 "110")]

    /**
     * Constructor
     * Gets an IOWController instance and registers the PCF8591 adc
     *
     * @param String Address for this device
     * @param Double Minimum temperature value
     * @param Double Maximum temperature value
     */
    public BoozeTemperatureSensorDriver(String address) throws Exception, IllegalArgumentException {

        this.iow = IoWarriorController.getInstance()

        this.setOptions(o);

        this.setAddress(address);
    }

    /**
     * Returns temperature in degrees celsius
     */
    public Double getTemperature() throws Exception {

        Double value = 0.0 as Double;

        try {
            value = this.iow.readPCF8592Channel(this.pcf8591Address, this.pcf8591Channel);
        }
        catch (Exception e) {
            log.error("Could not read temperature from PT100 sensor through IOWarrior/PCF8591 at address: ${address}: ${e}")
        }

        return (value * ((this.maximum - this.minimum) / 254) + this.minimum) as Double;
    }
}

