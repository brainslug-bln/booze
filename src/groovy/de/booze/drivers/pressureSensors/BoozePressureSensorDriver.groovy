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

package de.booze.drivers.pressureSensors

import java.util.*;


import org.apache.log4j.Logger
import de.booze.controllers.IoWarriorController
import de.booze.driverInterfaces.AbstractPressureSensorDriver
import de.booze.driverInterfaces.DriverOption
import de.booze.tasks.QueryPressureTask

/**
 * Driver for an analogue pressure sensor
 * connected via a combination of IOWarrior and
 * a PCF8591 a/d converter
 *
 * @author akotsias
 */
class BoozePressureSensorDriver extends AbstractPressureSensorDriver {
    
    /**
     * Logger instance
     */
    private Logger log = Logger.getLogger(getClass().getName());

    /**
     * Controller instance
     */
    private IoWarriorController iow;
    
    /**
     * Minimum pressure value in mbar
     */
    private Double minimum;

    /**
     * Maximum pressure value in mbar
     */
    private Double maximum;

    /**
     * PCF8591 address (0..7)
     */
    private Integer pcf8591Address;

    /**
     * PCF8591 Channel
     */
    private Integer pcf8591Channel;

    /**
     * Average value from the last
     * 10 measurements
     */
    private Double lastAverage = 0.0 as Double;

    /**
     * List with last measurements
     */
    private List valueList = [];

    /**
     * Timer for timed measurements
     */
    private Timer measureTimer;

    /**
     * Delay between measurements in ms
     */
    public int measurementTimeout = 100;
  
    /**
     * Driver options
     */
    public static availableOptions = [new DriverOption("pcf8591Address", 
                                                    "de.booze.drivers.heaters.BoozePressureSensor.pcf8591Address.description",
                                                     "/d/"),
                                       new DriverOption("pcf8591Channel", 
                                                     "de.booze.drivers.heaters.BoozePressureSensor.pcf8591Channel.description",
                                                     "/d/"),
                                       new DriverOption("minimum", 
                                                     "de.booze.drivers.heaters.BoozePressureSensor.minimum.description",
                                                     "/d+/"),
                                       new DriverOption("maximum", 
                                                     "de.booze.drivers.heaters.BoozePressureSensor.maximum.description",
                                                     "/d+/"),
                                       new DriverOption("measurementTimeout", 
                                                     "de.booze.drivers.heaters.BoozePressureSensor.measurementTimeout.description",
                                                     "/d/",
                                                     "100")]

    /**
     * Constructor
     *
     * Gets an IOWController instance and registers the PCF8591 adc
     *
     * If successful a timer is started which adds a pressure value
     * to valueList every 50ms to produce a flattened output
     */
    public BoozePressureSensorDriver(Map o) throws Exception, IllegalArgumentException {
        this.iow = IoWarriorController.getInstance()

        this.setOptions(o);

        // Start the timer for measurement which
        // adds a pressure value to the valueList every 50ms
        this.measureTimer = new Timer();
        this.measureTimer.schedule(new QueryPressureTask(this), 1000, this.measurementTimeout);
    }

    /**
     * Returns pressure in mbar
     */
    public Double getPressure() throws Exception {
        // Return 0 on startup to smoothen possible peek values
        if(this.valueList.size() < 10) {
            return 0
        }
        return (this.lastAverage * ((this.maximum - this.minimum) / 254) + this.minimum) as Double;
    }

    /**
     * Adds a pressure value to the valueList
     * Writes the average of the last 10 values
     * to this.lastAverage
     */
    public synchronized void addPressureValue() {

        int value
        try {
            value = this.iow.readPCF8592Channel(this.pcf8591Address, this.pcf8591Channel);
        }
        catch (Exception e) {
            log.error("Could not read pressure value from sensor: ${e}")
        }

        if (this.valueList.size() > 19) {
            this.valueList.remove(0);
        }

        this.valueList.add(value);

        this.lastAverage = (this.valueList.sum() / this.valueList.size()) as Double;

    }

    protected void finalize() throws Exception {
        this.shutdown();
    }

    public void shutdown() throws Exception {
        if(this.measureTimer) {
            this.measureTimer.cancel();
        }
    }
}

