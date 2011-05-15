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

package de.booze.process

import org.apache.log4j.Logger
import de.booze.events.BrewCookingFinishedEvent
import de.booze.events.BrewEvent
import de.booze.regulation.DeviceSwitcher
import de.booze.regulation.PressureRegulator
import de.booze.regulation.PumpRegulator
import de.booze.regulation.TemperatureRegulator
import de.booze.tasks.ProtocolTask
import de.booze.grails.*
import de.booze.steps.*

/**
 * Controls the manual  mode process
 *
 */
class ManualModeProcess implements Serializable {

  Date initTime = new Date()

  private Logger log = Logger.getLogger(getClass().getName());

  List innerTemperatureSensors = []
  List outerTemperatureSensors = []
  List pressureSensors = []
  List heaters = []
  PumpDevice pump
  Setting setting

  PumpRegulator pumpRegulator

  String processId

  public ManualModeProcess(Setting s) throws Exception {

    this.newProcessId();

    // Cache devices
    this.innerTemperatureSensors = s.innerTemperatureSensors.toList()
    this.outerTemperatureSensors = s.outerTemperatureSensors.toList()
    this.pressureSensors = s.pressureSensors.toList()
    this.heaters = s.heaters.toList()
    this.pump = s.pump

    this.setting = s

    // Init Pump
    try {
      this.pump.initDevice()
      this.pump.disable()
    }
    catch (Exception e) {
      throw new Exception("Could not initialize pump with name '${this.pump.name}': ${e}")
    }

    // Init heaters
    this.heaters.each {
      try {
        it.initDevice()
        it.disable()
      }
      catch (Exception e) {
        throw new Exception("Could not initialize heater with name '${it.name}': ${e}")
      }
    }

    // Init inner temperature sensors
    this.innerTemperatureSensors.each {
      try {
        it.initDevice()
      }
      catch (Exception e) {
        throw new Exception("Could not initialize temperature sensor with name '${it.name}': ${e}")
      }
    }

    // Init outer temperature sensors
    this.outerTemperatureSensors.each {
      try {
        it.initDevice()
      }
      catch (Exception e) {
        throw new Exception("Could not initialize temperature sensor with name '${it.name}': ${e}")
      }
    }

    // Init pressure sensors
    this.pressureSensors.each {
      try {
        it.initDevice()
      }
      catch (Exception e) {
        throw new Exception("Could not initialize pressure sensor with name '${it.name}': ${e}")
      }
    }

    // Init the pump regulator
    this.pumpRegulator = new PumpRegulator(this.pump);
  }


  /**
   * Stops the manual moe process
   */
  public void stop() throws Exception {
    log.debug("cancelling manual mode process")
    this.pumpRegulator.disable();
    this.heaters.each {
        it.disable();
        it.shutdown()
    }
    this.pressureSensors.each {
        it.shutdown();
    }
    this.innerTemperatureSensors.each {
        it.shutdown()
    }
    this.outerTemperatureSensors.each {
        it.shutdown()
    }
  }

  /**
   * Generates a new processId for this
   * manual mode process
   */
  public void newProcessId() {
    this.processId = (new Date()).getTime().encodeAsMD5();
  }

  /**
   * Set the actual pump mode
   */
  public void setPumpMode(PumpMode pumpMode) {
    this.pumpRegulator.setPumpMode(pumpMode);
    this.pumpRegulator.enable();
  }

  /**
   * Toggle heater on/off status
   */
  public void toggleHeater(Long heaterId) throws Exception {
    this.heaters.each {
        if(it.id == heaterId) {
            if(it.enabled()) {
                it.disable()
            }
            else {
                it.enable()
            }
        }
    }
  }
}




















