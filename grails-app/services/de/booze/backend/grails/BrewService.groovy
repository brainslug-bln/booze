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
import de.booze.process.BrewProcessHolder
import de.booze.regulation.MotorRegulator
import de.booze.regulation.TemperatureRegulator

class BrewService {

  //boolean transactional = true;
  //static scope = "singleton";


  /**
   * Builds a map with status information for the actual brew process
   */
  public Map updateStatus(taglib) throws Exception {

    BrewProcessHolder f = BrewProcessHolder.getInstance()
    BrewProcess p = f.getBrewProcess()
    
    log.debug("Collecting status data...")
    Date startTime = new Date();

    def status = [temperatureSensors: [],
                  pressureSensors: [],
                  heaters: []];

    try {
      log.debug("Reading temperature sensors")
      p.temperatureSensors.each {
        // Round temperature to .5 steps
        def t = Math.round(it.readTemperatureImmediate()*2d)/2d;
        Map ts = [id: it.id,
                temperature: t,
                label: taglib.message(code: "default.formatter.degrees.celsius", args: [taglib.formatNumber(number: t, format: "##0.0")])]
             
        if(p.temperatureRegulator.getReferenceSensors() == TemperatureRegulator.REFERENCE_SENSORS_MASHING && it.referenceForMashing == true) {
          ts.reference = true
        }
        else if(p.temperatureRegulator.getReferenceSensors() == TemperatureRegulator.REFERENCE_SENSORS_COOKING && it.referenceForCooking == true) {
          ts.reference = true
        }
        else {
          ts.reference = false
        }
        
        status.temperatureSensors.add(ts);
      }
    }
    catch(Exception e) {
      log.debug("Error reading temperature sensors: ${e}");
      e.printStackTrace()
    }


    try {
      log.debug("Reading pressure sensors")
      p.pressureSensors.each {
        def pressure = Math.round(it.readPressure()*2d)/2d;
        status.pressureSensors.add([id: it.id,
                pressure: pressure,
                label: taglib.message(code: "default.formatter.mbar", args: [taglib.formatNumber(number: pressure, format: "###0.0")])]);
      }
    }
    catch(Exception e) {
      log.debug("Error reading pressure sensors: ${e}");
      e.printStackTrace()
    }

    try {
      log.debug("Reading heater status")
      p.heaters.each {
        Map h = [id: it.id, enabled: it.enabled(), forced: it.forced()];
        if(it.hasRegulator()) {
          h.power = it.readPower();
        }
        status.heaters.add(h);
      }
    }
    catch(Exception e) {
      log.debug("Error reading heater status: ${e}");
      e.printStackTrace()
    }

    status.motors = []
    ["mashingMixerRegulator", "mashingPumpRegulator", "cookingMixerRegulator", "cookingPumpRegulator", "drainPumpRegulator"].each() {
      MotorRegulator mr = p[it] 
      if(mr) {
        try {
          log.debug("Reading motor status: ${mr.motor}")

          def myMotor = [id: it, 
                         enabled: mr.motorTask.enabled(),
                         hasForcedCyclingMode:mr.forced(), 
                         cyclingMode: mr.getActualCyclingMode(),
                         message: taglib.message(code: "motorDeviceMode.mode.${MotorTask.CYCLING_MODE_OFF}")]
                       
          if(mr.motorTask.motor.hasRegulator()) {
            myMotor.speed = mr.motorTask.motor.readSpeed();
          }

          status.motors.add(myMotor);
        }
        catch(Exception e) {
          log.debug("Error reading motor status: ${e}");
          e.printStackTrace()
        }
      }
     }


    // Read actual used reference sensors
    status.referenceSensors = p.temperatureRegulator.getReferenceSensors()

    status.targetTemperature = p.temperatureRegulator.getTargetTemperature();

    status.actualStep = p.getActualStep().getInfo(taglib);

    status.events = this.getActualEvents(taglib);

    status.pause = p.isPaused();

    // Elapsed time in minutes
    status.timeElapsed = Math.round(((new Date()).getTime() - p.initTime.getTime()) / 60000)

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
  public List getActualEvents(taglib) {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    BrewProcess p = f.getBrewProcess()
    
    List events = [];
    p.getEvents().each {
      events.add(it.getEventDataForFrontend(taglib));
    }
    return events;
  }
}
