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

package de.booze.backend.grails

import de.booze.process.BrewProcess
import de.booze.regulation.MotorRegulator

class BrewService {

  boolean transactional = true;
  static scope = "singleton";

  BrewProcess brewProcess;

  def taglib;

  public BrewProcess initBrewProcess(Recipe recipe, Setting settings, taglib) throws Exception {
    this.taglib = taglib;
    this.brewProcess = new BrewProcess(recipe, settings);
  }

  /**
   * Builds a map with status information for the actual brew process
   */
  public Map updateStatus() throws Exception {

    log.debug("Collecting status data...")
    Date startTime = new Date();

    def status = [temperatureSensors: [],
                  pressureSensors: [],
                  heaters: []];

    try {
      log.debug("Reading temperature sensors")
      this.brewProcess.temperatureSensors.each {
        // Round temperature to .5 steps
        def t = Math.round(it.readTemperatureImmediate()*2d)/2d;
        status.temperatureSensors.add([id: it.id,
                temperature: t,
                label: taglib.message(code: "default.formatter.degrees.celsius", args: [taglib.formatNumber(number: t, format: "##0.0")])]);
      }
    }
    catch(Exception e) {
      log.error("Error reading temperature sensors: ${e}");
      e.printStackTrace()
    }


    try {
      log.debug("Reading pressure sensors")
      this.brewProcess.pressureSensors.each {
        def pressure = Math.round(it.readPressure()*2d)/2d;
        status.pressureSensors.add([id: it.id,
                pressure: pressure,
                label: taglib.message(code: "default.formatter.mbar", args: [taglib.formatNumber(number: pressure, format: "###0.0")])]);
      }
    }
    catch(Exception e) {
      log.error("Error reading pressure sensors: ${e}");
      e.printStackTrace()
    }

    try {
      log.debug("Reading heater status")
      this.brewProcess.heaters.each {
        Map h = [id: it.id, enabled: it.enabled()];
        if(it.hasRegulator()) {
          h.power = it.readPower();
        }
        status.heaters.add(h);
      }
    }
    catch(Exception e) {
      log.error("Error reading heater status: ${e}");
      e.printStackTrace()
    }

    status.motors = []
    ["mashingMixer", "mashingPump", "cookingMixer", "cookingPump", "drainPump"].each() { ->
      MotorRegulator mr = this.brewProcess[it+"Regulator"] 
      if(mr) {
        try {
          log.debug("Reading motor status: ${mr.motor}")

          def myMotor = [id: it, 
                         enabled: mr.motorTask.motor.enabled(),
                         hasForcedCyclingMode:mr.forcedCyclingMode(), 
                         cyclingMode: mr.getActualCyclingMode,
                         message: taglib.message(code: "motorDeviceMode.mode.${MotorDeviceMode.MODE_OFF}")]
                       
          if(mr.motorTask.motor.hasRegulator()) {
            myMotor.power = mr.motorTask.motor.readPower();
          }

          status.motors.add(myMotor);
        }
        catch(Exception e) {
          log.error("Error reading motor status: ${e}");
          e.printStackTrace()
        }
      }
     }


    // Read actual used reference sensors
    status.referenceSensors = this.brewProcess.temperatureRegulator.getReferenceSensors()

    status.targetTemperature = this.brewProcess.temperatureRegulator.getTemperature();

    status.actualStep = this.brewProcess.getActualStep().getInfo(this.taglib);

    status.events = this.getActualEvents();

    status.pause = this.brewProcess.isPaused();

    // Elapsed time in minutes
    status.timeElapsed = Math.round(((new Date()).getTime() - this.brewProcess.initTime.getTime()) / 60000)

    status.success = true;

    Long collectionTime = (new Date().getTime()) - startTime.getTime();
    log.debug("Finished collection of status data, ${collectionTime} ms")
    log.debug("------------------------------------------------------------")
    return status;
  }

  /**
   * Generates a list of events suitable for
   * delivery to the frontend
   */
  public List getActualEvents() {

    List events = [];
    this.brewProcess.getEvents().each {
      events.add(it.getEventDataForFrontend(this.taglib));
    }
    return events;
  }
}
