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

package de.booze.steps

import java.lang.Math.*

import grails.util.GrailsNameUtils
import de.booze.process.BrewProcess
import de.booze.events.BrewRestEvent
import de.booze.grails.RecipeRest
import de.booze.tasks.CheckStepTask

/**
 *
 * @author akotsias
 */
class BrewMashingStep extends AbstractBrewStep {

  /**
   * This steps rest
   */
  public RecipeRest rest;

  /**
   * This step's type
   */
  public BrewProcess brewProcess;

  /**
   * The actual rest's index
   */
  public Integer restIndex;

  /**
   * Indicates if the desired temperature was reached
   */
  public boolean targetTemperatureReached = false;

  /**
   * Time when the desired temperatured was reached
   */
  public Date targetTemperatureReachedTime

  /**
   * Time when this step was started
   */
  public Date stepStartTime

  /**
   * The timer for this step
   */
  public Timer timer


  public BrewMeshStep(BrewProcess bp, RecipeRest r, Integer ri) {
    this.brewProcess = bp;
    this.rest = r;
    this.restIndex = ri;
    this.stepStartTime = new Date()

    this.startMotors();

    // Set the target temperature
    this.brewProcess.temperatureRegulator.setTemperature(this.rest.temperature);
    
    // Use the mashing sensors as reference
    this.brewProcess.temperatureRegulator.setMashingReferenceSensors();
    
    // Start the temperatureRegulator
    this.brewProcess.temperatureRegulator.start();

    // Start the timer for step checking
    this.timer = new Timer();
    this.timer.schedule(new CheckStepTask(this), 100, 1000);

    this.brewProcess.addEvent(new BrewRestEvent('brew.brewProcess.restStarted', this.rest));
  }

  /**
   * Checks if this step is finished or if the
   * target temperature is reached
   */
  public void checkStep() {
    if (!this.targetTemperatureReached) {
      if (this.brewProcess.temperatureRegulator.getActualTemperature() >= this.rest.temperature) {
        this.targetTemperatureReached = true;
        this.targetTemperatureReachedTime = new Date();
        this.brewProcess.addEvent(new BrewRestEvent('brew.brewProcess.restTemperatureReached', this.rest));
      }
    }
    else {
      if ((new Date()).getTime() > (this.targetTemperatureReachedTime.getTime() + (this.rest.duration * 60000))) {
        this.timer.cancel();
        this.motors.stop();
        this.brewProcess.temperatureRegulator.stop()
        this.brewProcess.addEvent(new BrewRestEvent('brew.brewProcess.restFinished', this.rest));
        this.brewProcess.nextStep();
      }
    }
  }

  /**
   * Returns the time to go for this step in seconds
   */
  public Long getTimeToGo() {
    if (!this.targetTemperatureReached) {
      return this.rest.duration * 60;
    }
    else {
      Long stepEndTime = this.targetTemperatureReachedTime.getTime() + (this.rest.duration * 60000);
      return Math.round((stepEndTime - (new Date().getTime())) / 1000);
    }
  }

  /**
   * Returns the time as Date object when
   * this step was initialized
   */
  public Date getStepStartTime() {
    return this.stepStartTime;
  }

  /**
   * Returns this step's rest index
   */
  public Integer getRestIndex() {
    return this.restIndex;
  }

  /**
   * Returns this step's rest
   */
  public RecipeRest getRest() {
    return this.rest;
  }

  /**
   * Returns temperatureReached
   */
  public boolean getTargetTemperatureReached() {
    return this.targetTemperatureReached;
  }

  /**
   * Returns temperatureReachedTime
   */
  public Date getTargetTemperatureReachedTime() {
    return this.targetTemperatureReachedTime;
  }

  public void pause() {
    this.motors.stop();
    this.brewProcess.temperatureRegulator.stop();
    this.timer.cancel();
  }

  public void resume() {
    this.motors.start();
    this.brewProcess.temperatureRegulator.start();
    this.timer = new Timer();
    this.timer.schedule(new CheckStepTask(this), 100, 1000);
  }

  public Map getInfo(taglib) {
    return [type: grails.util.GrailsNameUtils.getShortName(this.getClass()),
            headline: taglib.message(code: 'brew.step.rest', args: [(this.rest.indexInRests + 1), taglib.formatNumber(format: '##0.0', number: this.rest.temperature)]),
            targetTemperature: taglib.message(code: 'default.formatter.degrees.celsius', args: [taglib.formatNumber(format: '##0.0', number: this.rest.temperature)]),
            timeToGo: taglib.message(code: 'default.formatter.minutes', args: [Math.round(this.getTimeToGo() / 60)]),
            stepStartTime: taglib.formatDate(formatName: 'default.time.formatter', date: this.stepStartTime),
            targetTemperatureReachedTime: taglib.formatDate(formatName: 'default.time.formatter', date: this.targetTemperatureReachedTime),
            targetTemperatureReached: this.targetTemperatureReached]
  }
  
  /**
   * Start all associated motors for this step
   */
  private void startMotors() {
    // Start the mashing pump and mixer
    if(this.brewProcess.mashingPumpRegulator) {
      this.brewProcess.mashingPumpRegulator.enable();
    }
    
    if(this.brewProcess.mashingMixerRegulator) {
      this.brewProcess.mashingMixerRegulator.enable();
    }
  }
 
  /**
   * Stop all associated motors for this step
   */
  private void stopMotors() {
    // Start the mashing pump and mixer
    if(this.brewProcess.mashingPumpRegulator) {
      this.brewProcess.mashingPumpRegulator.disable()();
    }
    
    if(this.brewProcess.mashingMixerRegulator) {
      this.brewProcess.mashingMixerRegulator.disable();
    }
  }
}

