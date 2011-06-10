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
import java.util.Random

import org.apache.log4j.Logger
import de.booze.driverInterfaces.AbstractPressureSensorDriver

/**
 * Driver for a dummy pressure sensor
 *
 * @author akotsias
 */
class DummyPressureSensorDriver extends AbstractPressureSensorDriver {
    
    /**
     * Logger instance
     */
    private Logger log = Logger.getLogger(getClass().getName());

  
    /**
     * Driver options
     */
    public static availableOptions = []

    /**
     * Constructor
     */
    public DummyPressureSensorDriver(Map o) throws Exception, IllegalArgumentException {
        this.setOptions(o);
    }

    /**
     * Returns a random pressure in mbar
     */
    public Double getPressure() throws Exception {
      def rand = new Random()
      if(rand.nextBoolean()) {
        return (Double)(200d + rand.nextDouble() * 150);
      }
      else {
        return (Double)(200d + rand.nextDouble() - 150);
      }
    }

    protected void finalize() throws Exception {
        this.shutdown();
    }

    public void shutdown() throws Exception {

    }
}

