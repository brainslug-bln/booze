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
  List heaterRegulator = []
  List motors = []
  List motorRegulators = []
  
  Setting setting

  DeviceSwitcher devSwitcher
  TemperatureRegulator temperatureRegulator
  
  PumpRegulator pumpRegulator
  PressureRegulator pressureRegulator

  Long protocolId
  Timer protocolTimer

  BrewService brewService

  def actualStep

  boolean pause = false

  List events = []

  String processId

  Integer finalCookingTime = 0;

  public BrewProcess(Recipe r, Setting s) throws Exception {

    this.newProcessId();

    // Cache devices
    this.temperatureSensors = s.temperatureSensors.toList()
    this.pressureSensors = s.pressureSensors.toList()
    
    this.heaters = s.heaters.toList()
    this.pump = s.pump

    this.recipe = r
    this.setting = s

    // Init Pump
    try {
      this.pump.initDevice()
      this.pump.disable()
    }
    catch (Exception e) {
      throw new Exception("Could not initialize pump with name '${this.pump.name}': ${e}")
    }

    // Init the temperature regulator
    this.temperatureRegulator = new TemperatureRegulator()

    // Init heaters
    this.heaters.each {
      try {
        it.initDevice()
        temperatureRegulator.addHeater(it)
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
        temperatureRegulator.addInnerSensor(it)
      }
      catch (Exception e) {
        throw new Exception("Could not initialize temperature sensor with name '${it.name}': ${e}")
      }
    }

    // Init outer temperature sensors
    this.outerTemperatureSensors.each {
      try {
        it.initDevice()
        temperatureRegulator.addOuterSensor(it)
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

    // Start pressure inspection
    this.pressureRegulator = new PressureRegulator(this, this.pressureSensors);
    this.pressureRegulator.enable();

    // Fetch a new DeviceSwitcher instance
    this.devSwitcher = DeviceSwitcher.getInstance()

    // Init the pump regulator
    this.pumpRegulator = new PumpRegulator(this.pump);

    // Create a new protocol instance
    Protocol protocol = new Protocol([recipeName: this.recipe.name,
            dateStarted: (new Date()),
            recipeDescription: this.recipe.description,
            alcohol: this.recipe.alcohol,
            targetOriginalWort: this.recipe.originalWort,
            targetPreCookingWort: this.recipe.preCookingWort,
            targetBottlingWort: this.recipe.bottlingWort,
            mainWaterVolume: this.recipe.mainWaterVolume,
            targetSecondWaterVolume: this.recipe.secondWaterVolume,
            mainWaterTemperature: this.recipe.fillTemperature,
            secondWaterTemperature: this.recipe.secondWaterTemperature,
            targetCookingTime: this.recipe.cookingTime,
            yeastName: this.recipe.yeast.name,
            yeastDescription: this.recipe.yeast.description,
            meshTemperature: this.recipe.meshTemperature]);

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

    recipe.additives.each {
        ProtocolAdditive ad = new ProtocolAdditive(it.properties)
        protocol.addToAdditives(ad);
    }

    protocol.validate()
    
    protocol.save(flush: true)

    this.protocolId = protocol.id

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
    this.recipe.refresh()

    switch (this.actualStep.getClass().getName()) {
      case "de.booze.steps.BrewInitStep":
        this.addEvent(new BrewEvent('brew.brewProcess.startedBrewing'));
        if (this.recipe.fillTemperature) {
          this.actualStep = new BrewFillTemperatureStep(this, this.recipe);
        }
        else {
          this.actualStep = new BrewMeshStep(this, this.recipe.rests.first(), 0);
        }
        break;

      case "de.booze.steps.BrewFillTemperatureStep":
        this.actualStep = new BrewFillTemperatureReachedStep();
        break;

      case "de.booze.steps.BrewFillTemperatureReachedStep":
        this.actualStep = new BrewMeshStep(this, this.recipe.rests.first(), 0);
        break;

      case "de.booze.steps.BrewMeshStep":
        // Check if this was the last mesh step
        if (this.actualStep.restIndex < (this.recipe.rests.size() - 1)) {
          // Proceed one mesh step
          int restIndex = ++this.actualStep.restIndex;
          this.actualStep = new BrewMeshStep(this, this.recipe.rests.toList().getAt(restIndex), restIndex);
        }
        else {
          // Proceed to "meshing finished"
          this.addEvent(new BrewEvent('brew.brewProcess.finalRestFinished'));
          this.actualStep = new BrewMeshingFinishedStep(this, this.recipe.meshTemperature);
        }
        break;

      case "de.booze.steps.BrewMeshingFinishedStep":
        this.actualStep = new BrewInitCookingStep();
        break;

      case "de.booze.steps.BrewElongateMeshingStep":
        this.actualStep = new BrewInitCookingStep();
        this.addEvent(new BrewEvent('brew.brewProcess.initCooking'));
        break;

      case "de.booze.steps.BrewInitCookingStep":
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
    }

  }

  /**
   * Proceeds to cooking
   */
  public void startCooking() {
    this.nextStep();
  }

  /**
   * Elongates the meshing step by a given time
   */
  public void elongateMeshing(Long time) throws Exception {
    log.debug("elongate meshing for ${time} seconds;")
    if (this.actualStep.getClass().getName() == "de.booze.steps.BrewInitCookingStep") {
      this.actualStep = new BrewElongateMeshingStep(this, time, this.recipe.meshTemperature)
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
  public List getEvents() {
    List etd = []
    this.events.each {
      if (!it.delivered()) {
        etd.add(it);
        it.deliver();
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
    this.events.each {
      if (it.getSavedToProtocol() != true) {
        etd.add(it);
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

    if (this.protocolTimer) {
      this.protocolTimer.cancel()
    }

    def pt = new ProtocolTask(this)
    pt.run()

    this.actualStep.pause();

    this.pumpRegulator.disable();

    this.temperatureRegulator.stop();
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
  public void setTemperatureOffset(Double offset) {
    this.temperatureRegulator.setOffset(offset);
  }

  /**
   * Forces the pump to a particular pump mode
   * overriding the recipe's pump mode
   */
  public void forcePumpMode(PumpMode pumpMode) {
    this.pumpRegulator.forcePumpMode(pumpMode);
  }

  /**
   * Removes any forced pump mode
   */
  public void unforcePumpMode() {
    this.pumpRegulator.unforcePumpMode();
  }
}



















