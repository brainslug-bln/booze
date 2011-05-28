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
import de.booze.regulation.PressureMonitor
import de.booze.regulation.MotorRegulator
import de.booze.regulation.TemperatureRegulator
import de.booze.tasks.ProtocolTask
import de.booze.backend.grails.*
import de.booze.steps.*

/**
 * Controls the manual  mode process
 *
 */
class ManualModeProcess implements Serializable {
//
//  Date initTime = new Date()
//
//  private Logger log = Logger.getLogger(getClass().getName());
//
//  List temperatureSensors = []
//  List pressureSensors = []
//  List heaters = []
//  
//  MotorDevice mashingMixer
//  MotorDevice cookingMixer
//  MotorDevice mashingPump
//  MotorDevice cookingPump
//  MotorDevice whirlpoolPump
//  MotorDevice drainPump
//  
//  Setting setting
//
//  DeviceSwitcher devSwitcher
//  TemperatureRegulator temperatureRegulator
//  
//  MotorRegulator mashingPumpRegulator
//  MotorRegulator mashingMixerRegulator
//  
//  PressureRegulator pressureMonitor
//  
//  String processId
//
//  public ManualModeProcess(Setting s) throws Exception {
//
//    // Generate a process id for this brew process
//    this.newProcessId();
//
//    // Cache devices
//    this.temperatureSensors = s.temperatureSensors.toList()
//    this.pressureSensors = s.pressureSensors.toList()
//    
//    this.heaters = s.heaters.toList()
//    this.mashingMixer = s.mashingMixer
//    this.cookingMixer = s.cookingMixer
//    this.mashingPump = s.mashingPump
//    this.cookingPump = s.cookingPump
//    this.whirlpoolPump = s.whirpoolPump
//    this.drainPump = s.drainPump
//
//    this.recipe = r
//    
//    this.setting = s
//
//    // Init motor devices
//    [this.mashingMixer, this.cookingMixer, this.mashingPump, this.cookingPump, this.whirpoolPump, this.drainPump].each() {it ->
//      try {
//        it.initDevice()
//        it.disable()
//      }
//      catch (Exception e) {
//        throw new Exception("Could not initialize motor device '${it.name}': ${e}")
//      }
//    }
//
//    // Init the temperature regulator
//    this.temperatureRegulator = new TemperatureRegulator()
//
//    // Init heaters
//    this.heaters.each {
//      try {
//        it.initDevice()
//        temperatureRegulator.addHeater(it)
//        it.disable()
//      }
//      catch (Exception e) {
//        throw new Exception("Could not initialize heater with name '${it.name}': ${e}")
//      }
//    }
//
//    // Init inner temperature sensors
//    this.temperatureSensors.each {
//      try {
//        it.initDevice()
//        temperatureRegulator.addSensor(it)
//      }
//      catch (Exception e) {
//        throw new Exception("Could not initialize temperature sensor with name '${it.name}': ${e}")
//      }
//    }
//
//    // Init pressure sensors
//    this.pressureSensors.each {
//      try {
//        it.initDevice()
//      }
//      catch (Exception e) {
//        throw new Exception("Could not initialize pressure sensor with name '${it.name}': ${e}")
//      }
//    }
//
//    // Start pressure monitoring
//    this.pressureMonitor = new PressureMonitor(this, this.pressureSensors);
//    this.pressureMonitor.enable();
//
//    // Fetch a new DeviceSwitcher instance
//    this.devSwitcher = DeviceSwitcher.getInstance()
//
//    // Init the pump and mixer regulators for mashing if set
//    if(this.mashingPump) {
//      this.mashingPumpRegulator = new MotorRegulator(this.mashingPump);
//    }
//    if(this.mashingMixer) {
//      this.mashingMixerRegulator = new MotorRegulator(this.mashingMixer);
//    }
//    
//  }
//
//
//  /**
//   * Stops the manual moe process
//   */
//  public void stop() throws Exception {
//    log.debug("cancelling manual mode process")
//    this.pumpRegulator.disable();
//    this.heaters.each {
//        it.disable();
//        it.shutdown()
//    }
//    this.pressureSensors.each {
//        it.shutdown();
//    }
//    this.innerTemperatureSensors.each {
//        it.shutdown()
//    }
//    this.outerTemperatureSensors.each {
//        it.shutdown()
//    }
//  }
//
//  /**
//   * Generates a new processId for this
//   * manual mode process
//   */
//  public void newProcessId() {
//    this.processId = (new Date()).getTime().encodeAsMD5();
//  }
//
//  /**
//   * Set the actual pump mode
//   */
//  public void setPumpMode(PumpMode pumpMode) {
//    this.pumpRegulator.setPumpMode(pumpMode);
//    this.pumpRegulator.enable();
//  }
//
//  /**
//   * Toggle heater on/off status
//   */
//  public void toggleHeater(Long heaterId) throws Exception {
//    this.heaters.each {
//        if(it.id == heaterId) {
//            if(it.enabled()) {
//                it.disable()
//            }
//            else {
//                it.enable()
//            }
//        }
//    }
//  }
}