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
 **/

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
 * Controls the brew process
 *
 */
class BrewProcess implements Serializable {
  
  Date initTime = new Date()

  private Logger log = Logger.getLogger(getClass().getName());

  Recipe recipe

  List temperatureSensors = []
  List pressureSensors = []
  List heaters = []
  List motors = []
  
  Setting setting

  DeviceSwitcher devSwitcher
  TemperatureRegulator temperatureRegulator
  
  MotorRegulator mashingPumpRegulator
  MotorRegulator mashingMixerRegulator
  MotorRegulator cookingPumpRegulator
  MotorRegulator cookingMixerRegulator
  MotorRegulator drainPumpRegulator
  
  PressureMonitor pressureMonitor

  Long protocolId
  Timer protocolTimer

  BrewService brewService

  def actualStep

  boolean pause = false

  List events = []

  String processId

  Integer finalCookingTime = 0;
  
  public BrewProcess(Recipe r, Setting s) throws Exception {

    // Generate a process id for this brew process
    this.newProcessId();
    
    log.debug("Found setting with name: ${s.name}")

    // Cache devices
    this.temperatureSensors = s.temperatureSensors.toList()
    this.pressureSensors = s.pressureSensors.toList()
    this.heaters = s.heaters.toList()
    this.motors = s.motors.toList()
    
    this.recipe = r
    this.setting = s

    // Init the temperature regulator
    this.temperatureRegulator = new TemperatureRegulator()
    this.temperatureRegulator.setHysteresis(this.setting.hysteresis)
    this.temperatureRegulator.setHeatingRamp(this.setting.heatingRamp)
    
    // Init heaters
    this.heaters.each {
      log.debug("Init heater: ${it.name}")
      try {
        it.initDevice()
        temperatureRegulator.addHeater(it)
        it.disable()
      }
      catch (Exception e) {
        throw new Exception("Could not initialize heater with name '${it.name}': ${e}")
      }
    }
    
    // Init motors
    this.motors.each {
      log.debug("Init motor: ${it.name}")
      try {
        it.initDevice()
        it.disable()
      }
      catch (Exception e) {
        throw new Exception("Could not initialize motor with name '${it.name}': ${e}")
      }
    }
    
    // Init motor regulators
    ["mashingMixer", "cookingMixer", "mashingPump", "cookingPump", "drainPump"].each() {it ->
      try {
        if(this.setting[it] != null) {
          this[it+"Regulator"] = new MotorRegulator(this.setting[it])
        }
      }
      catch (Exception e) {
        throw new Exception("Could not initialize motor device '${it}': ${e}")
      }
    }

    // Init inner temperature sensors
    this.temperatureSensors.each {
      log.debug("Init temperatureSensor: ${it.name}")
      try {
        it.initDevice()
        temperatureRegulator.addSensor(it)
      }
      catch (Exception e) {
        throw new Exception("Could not initialize temperature sensor with name '${it.name}': ${e}")
      }
    }

    // Init pressure sensors
    this.pressureSensors.each {
      log.debug("Init pressureSensor: ${it.name}")
      try {
        it.initDevice()
      }
      catch (Exception e) {
        throw new Exception("Could not initialize pressure sensor with name '${it.name}': ${e}")
      }
    }

    // Start pressure monitoring
    this.pressureMonitor = new PressureMonitor(this, this.pressureSensors);
    this.pressureMonitor.enable();

    // Fetch a new DeviceSwitcher instance
    this.devSwitcher = DeviceSwitcher.getInstance()

    // Create a new protocol instance
    Protocol protocol = new Protocol([recipeName: this.recipe.name,
            dateStarted: (new Date()),
            recipeDescription: this.recipe.description,
            alcohol: this.recipe.alcohol,
            targetOriginalWort: this.recipe.originalWort,
            targetPreSpargingWort: this.recipe.preSpargingWort,
            targetPostSpargingWort: this.recipe.postSpargingWort,
            targetBottlingWort: this.recipe.bottlingWort,
            mashingWaterVolume: this.recipe.mashingWaterVolume,
            targetSpargingWaterVolume: this.recipe.spargingWaterVolume,
            mashingTemperature: this.recipe.mashingTemperature,
            spargingTemperature: this.recipe.spargingTemperature,
            lauterTemperature: this.recipe.lauterTemperature,
            targetCookingTime: this.recipe.cookingTime,
            targetPostIsomerization: this.recipe.postIsomerization,
            fermentationTemperature: this.recipe.fermentationTemperature,
            storingTime: this.recipe.storingTime,
            storingTemperature: this.recipe.storingTemperature,
            co2Concentration: this.recipe.co2Concentration,
            yeast: this.recipe.yeast]);

    recipe.rests.each {
        ProtocolRest rest = new ProtocolRest(it.properties)
        protocol.addToRests(rest);
    }

    recipe.malts.each {
        ProtocolMalt malt = new ProtocolMalt(it.properties)
        protocol.addToMalts(malt);
    }

    recipe.hops.each {
        ProtocolHop hop = new ProtocolHop(it.properties)
        protocol.addToHops(hop);
    }

    protocol.validate()
    log.error(protocol.errors)
    
    protocol.save(flush: true)

    this.protocolId = protocol.id
    log.error("set protocol id ${protocol.id}")

    this.actualStep = new BrewInitStep();

    log.debug("successfully initialized brew process");
  }

  /**
   * Starts the brew process
   */
  public void start() {
    log.debug("starting brew process")

    this.protocolTimer = new Timer();
    this.protocolTimer.schedule(new ProtocolTask(this), 100, 30000);

    this.nextStep();

  }

  /**
   * Pause the brew process
   */
  public void pause() {
    this.actualStep.pause();
    this.addEvent(new BrewEvent('brew.brewProcess.paused'));
    this.pause = true;
  }

  /**
   * Resume a paused brew process
   */
  public void resume() {
    this.actualStep.resume();
    this.addEvent(new BrewEvent('brew.brewProcess.resumed'));
    this.pause = false;
  }

  /**
   * Proceed to next step
   */
  public void nextStep() {

    // Refresh the recipe instance
    //this.recipe.refresh()

    switch (this.actualStep.getClass().getName()) {
      case "de.booze.steps.BrewInitStep":
        this.addEvent(new BrewEvent('brew.brewProcess.startedBrewing'));
        if (this.recipe.mashingTemperature) {
          this.actualStep = new BrewTargetMashingTemperatureStep(this, this.recipe);
        }
        else {
          this.actualStep = new BrewRestStep(this, this.recipe.rests.first(), 0);
        }
        break;

      case "de.booze.steps.BrewTargetMashingTemperatureStep":
        this.actualStep = new BrewMashingTemperatureReachedStep();
        break;

      case "de.booze.steps.BrewMashingTemperatureReachedStep":
        this.actualStep = new BrewRestStep(this, this.recipe.rests.first(), 0);
        break;

      case "de.booze.steps.BrewRestStep":
        // Check if this was the last mesh step
        if (this.actualStep.restIndex < (this.recipe.rests.size() - 1)) {
          // Proceed one mash step
          int restIndex = ++this.actualStep.restIndex;
          this.actualStep = new BrewRestStep(this, this.recipe.rests.toList().getAt(restIndex), restIndex);
        }
        else {
          // Proceed to "mashing finished"
          this.addEvent(new BrewEvent('brew.brewProcess.finalRestFinished'));
          this.actualStep = new BrewTargetLauterTemperatureStep(this, this.recipe.lauterTemperature);
        }
        break;

      case "de.booze.steps.BrewTargetLauterTemperatureStep":
        this.actualStep = new BrewInitCookingStep();
        break;

      case "de.booze.steps.BrewElongateMashingStep":
        this.actualStep = new BrewInitCookingStep();
        break;

      case "de.booze.steps.BrewInitCookingStep":
        this.addEvent(new BrewEvent('brew.brewProcess.initCooking'));
        this.actualStep = new BrewCookingStep(this, this.recipe, this.setting.cookingTemperature);
        break;

      case "de.booze.steps.BrewCookingStep":
        this.finalCookingTime = this.actualStep.getCookingTime();
        this.actualStep = new BrewCookingFinishedStep()
        this.addEvent(new BrewCookingFinishedEvent('brew.brewProcess.cookingFinished'));
        break;

      case "de.booze.steps.BrewElongateCookingStep":
        this.finalCookingTime = this.finalCookingTime + this.actualStep.getCookingTime();
        this.actualStep = new BrewCookingFinishedStep()
        break;
        
      case "de.booze.steps.BrewCookingFinishedStep":
        this.actualStep = new BrewCoolingStep(this)
        break;
    }

  }

  /**
   * Proceeds to cooking
   */
  public void startCooking() {
    this.nextStep();
  }
  
  /**
   * Proceeds to cooking
   */
  public void startCooling() {
    this.nextStep();
  }

  /**
   * Elongates the meshing step by a given time
   */
  public void elongateMashing(Long time) throws Exception {
    log.debug("elongate mashing for ${time} seconds;")
    if (this.actualStep.getClass().getName() == "de.booze.steps.BrewInitCookingStep") {
      this.actualStep = new BrewElongateMashingStep(this, time, this.recipe.lauterTemperature)
    }
  }

  /**
   * Elongates the cooking step by a given time
   */
  public void elongateCooking(Long time, Double temperature) throws Exception {
    log.debug("elongate cooking for ${time} seconds;")
    if (this.actualStep.getClass().getName() == "de.booze.steps.BrewCookingFinishedStep") {
      this.actualStep = new BrewElongateCookingStep(this, time, temperature)
    }
  }

  /**
   * Proceeds to meshing after fill completion
   */
  public void commitFill() {
    this.nextStep();
  }

  /**
   * Returns the actual brew step
   */
  public getActualStep() {
    return this.actualStep;
  }

  /**
   * Adds an event to deliver to the frontend
   */
  public void addEvent(event) {
    this.events.add(event)
  }

  /**
   * Returns true if the brew process is paused
   */
  public boolean isPaused() {
    return this.pause;
  }

  /**
   * Returns all undelivered events
   */
  public synchronized List getEvents() {
    List etd = []
    for(int i=0; i<this.events.size(); i++) {
      if(!this.events[i].delivered()) {
        etd.add(this.events[i]);
        this.events[i].deliver();
      }
    }

    return etd;
  }

  /**
   * Returns all events not yet persisted
   * to the protocol
   */
  public List getEventsForProtocol() {
    List etd = []
    for(int i=0; i<this.events.size(); i++) {
      if (this.events[i].getSavedToProtocol() != true) {
        etd.add(this.events[i]);
      }
    }
    return etd;
  }

  /**
   * Returns all events (even delivered ones)
   */
  public List getAllEvents() {
    return this.events;
  }

  /**
   * Cancels the brew process
   */
  public void cancel() throws Exception {
    // TODO: implement real cancel actions for
    // all regulators
    log.debug("cancelling brew process")

    try {
      if (this.protocolTimer) {
        this.protocolTimer.cancel()
      }

      def pt = new ProtocolTask(this)
      pt.run()
    }
    catch(Exception e) {
      log.error("could not save protocol, cancelling anyway")
    }
    
   
    this.shutdownDevices();
  }
  
  /** 
   * Shut down all devices and regulators
   */
  private void shutdownDevices() {
    this.actualStep.pause();
    
    // Disable regulators and monitors
    this.pressureMonitor.disable();
    this.temperatureRegulator.stop();
    
    // Disable all motor regulators
    ["mashingPump", "mashingMixer", "cookingPump", "cookingMixer", "drainPump"].each() { it ->
      if(this[it+"Regulator"] != null) {
        this[it+"Regulator"].disable()
      }
    }
    
    // Shut down heaters
    this.heaters.each() { it ->
      it.shutdown();
    }
    
    // Shut down pressure sensors
    this.pressureSensors.each() { it ->
      it.shutdown();
    }
    
    // Shut down temperature sensors
    this.temperatureSensors.each() { it ->
      it.shutdown();
    }    
  }

  /**
   * Generates a new processId for this
   * brew process
   */
  public void newProcessId() {
    this.processId = (new Date()).getTime().encodeAsMD5();
  }

  /**
   * Changes the temperature offset for temperature regulator
   */
  public void setHysteresis(Double h) {
    this.temperatureRegulator.setHysteresis(h);
  }

  /**
   * Toggles the force mode for a heater
   */
  public void toggleForceHeater(Long heater) {
    for(int i=0; i<this.heaters.size(); i++) {
      if(this.heaters[i].id == heater) {
		  if(this.heaters[i].forced()) {
			  this.heaters[i].unforce()
		  }
		  else {
			  this.heaters[i].force()
		  }
      }
    }
  }
  
  /**
   * Sets a forced heater's power value
   * 
   * @param heater Heater ID
   * @param power In percent of the maximal power value
   * @throws IllegalArgumentException
   */
  
  public void setForcedHeaterPower(Long heater, Integer power) throws IllegalArgumentException {
	  for(int i=0; i<this.heaters.size(); i++) {
		  if(this.heaters[i].id == heater) {
			  if(!this.heaters[i].hasRegulator()) {
				  throw new IllegalArgumentException("you are trying modify a heater regulator where there is none")
			  }
			  this.heaters[i].writeForcedPower(power);
		  }
	  }
  }
  
  /**
   * Toggles a forced heater's enabled/disabled status
   * @param heater Heater ID
   */
  public void toggleForcedHeaterStatus(Long heater) {
	  for(int i=0; i<this.heaters.size(); i++) {
		  if(this.heaters[i].id == heater) {
			  if(this.heaters[i].forced()) {	
				  if(this.heaters[i].enabled()) this.heaters[i].forceDisable()
				  else this.heaters[i].forceEnable()
			  }
		  }
	  }
  }
  
  /**
   * Forces a motor to a particular cycling mode
   * overriding the setting's cycling mode
   */
  public void forceCyclingMode(params) {
    try {
      this[params.motor+"Regulator"].forceCyclingMode(params.cyclingMode, params.onInterval, params.offInterval)
    }
    catch(Exception e) {
      log.error("Could not force mode for motor: ${motor}")
    }
  }

  /**
   * Removes a forced motor mode
   */
  public void unforceCyclingMode(String motor) {
    try {
      this[motor+"Regulator"].unforceCyclingMode()
    }
    catch(Exception e) {
      log.error("Could not unforce mode for motor: ${motor}")
    }
  }
  
  public MotorDevice getMotor(Long id) {
    for(int i=0; i<this.motors.size(); i++) {
      if(this.motors[i].id == id) return this.motors[i]
    } 
  }
  
  public TemperatureSensorDevice getTemperatureSensor(Long id) {
    for(int i=0; i<this.temperatureSensors.size(); i++) {
      if(this.temperatureSensors[i].id == id) return this.temperatureSensors[i]
    } 
  }
  
  public PressureSensorDevice getPressureSensor(Long id) {
    for(int i=0; i<this.pressureSensors.size(); i++) {
      if(this.pressureSensors[i].id == id) return this.pressureSensors[i]
    } 
  }
}




















