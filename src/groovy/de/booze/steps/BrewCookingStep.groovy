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
import de.booze.events.BrewAddHopEvent
import de.booze.events.BrewEvent
import de.booze.backend.grails.Recipe
import de.booze.tasks.CheckStepTask

/**
 *
 * @author akotsias
 */
class BrewCookingStep extends AbstractBrewStep {

  /**
   * Recipe
   */
  public Recipe recipe;

  /**
   * This step's type
   */
  public BrewProcess brewProcess;

  /**
   * Time when this step was started
   */
  public Date stepStartTime

  /**
   * The timer for this step
   */
  public Timer timer

  /**
   * Cooking temperature
   */
  public Double targetTemperature

  /**
   * Indicates whether the cooking temperature was reached
   */
  public boolean targetTemperatureReached = false

  /**
   * Time when the cooking temperature was reached
   */
  public Date targetTemperatureReachedTime

  /**
   * Last hop which has been added
   */
  public int hopAdded = -1;

  /**
   * Constructor
   */
  public BrewCookingStep(BrewProcess bp, Recipe r, Double t) {
    this.brewProcess = bp;
    this.recipe = r
    this.targetTemperature = t

    this.stepStartTime = new Date()

    this.startMotors();

    // Set the target temperature
    this.brewProcess.temperatureRegulator.setTargetTemperature(this.targetTemperature);
    
    // Use the mashing sensors as reference
    this.brewProcess.temperatureRegulator.setCookingReferenceSensors();
    
    // Start the temperatureRegulator
    this.brewProcess.temperatureRegulator.start();

    // Start the timer for step checking
    this.timer = new Timer();
    this.timer.schedule(new CheckStepTask(this), 100, 1000);

    this.brewProcess.addEvent(new BrewEvent('brew.brewProcess.startedCooking'));
  }

  /**
   * Checks if this step is finished or if the
   * target temperature is reached
   */
  public void checkStep() {
    if (!this.targetTemperatureReached) {
      if (this.brewProcess.temperatureRegulator.getActualTemperature() >= this.targetTemperature) {
        this.targetTemperatureReached = true;
        this.targetTemperatureReachedTime = new Date();
        this.brewProcess.addEvent(new BrewEvent('brew.brewProcess.cookingTemperatureReached'));
      }
    }
    else {
      if ((new Date()).getTime() > (this.targetTemperatureReachedTime.getTime() + (this.recipe.cookingTime * 60000))) {
        this.timer.cancel();
        this.stopMotors();
        this.brewProcess.temperatureRegulator.stop()
        this.brewProcess.nextStep();
      }
      else {
        def hops = this.recipe.hops.toList().sort {it.time};
        // Check if a hop has to be added
        if (hops.size() > (this.hopAdded + 1)) {
          if ((new Date()).getTime() > (this.targetTemperatureReachedTime.getTime() + hops[this.hopAdded + 1].time * 60000)) {
            this.brewProcess.addEvent(new BrewAddHopEvent('brew.brewProcess.addHop', hops[this.hopAdded + 1]));
            this.hopAdded++;
          }
        }
      }
    }
  }

  /**
   * Returns the time to go for cooking in seconds
   */
  public Long getTimeToGo() {
    if (!this.targetTemperatureReached) {
      return this.recipe.cookingTime * 60;
    }
    else {
      Long stepEndTime = this.targetTemperatureReachedTime.getTime() + (this.recipe.cookingTime * 60000);
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
   * Returns the target temperature
   */
  public Double getTargetTemperature() {
    return this.targetTemperature;
  }

  /**
   * Setter for targetTemperature
   */
  public void setTargetTemperature(Double t) {
    this.targetTemperature = t;
    this.brewProcess.temperatureRegulator.setTargetTemperature(this.targetTemperature);
  }

  /**
   * Returns temperatureReached
   */
  public boolean getTargetTemperatureReached() {
    return this.targetTemperatureReached;
  }

  /**
   * Returns the cooking time
   */
  public Integer getCookingTime() {
    return Math.round(((new Date()).getTime() - this.targetTemperatureReachedTime.getTime()) / 60000)
  }

  /**
   * Returns temperatureReachedTime
   */
  public Date getTargetTemperatureReachedTime() {
    return this.targetTemperatureReachedTime;
  }

  public void pause() {
    this.stopMotors();
    this.brewProcess.temperatureRegulator.stop();
    this.timer.cancel();
  }

  public void resume() {
    this.startMotors();
    this.brewProcess.temperatureRegulator.start();
    this.timer = new Timer();
    this.timer.schedule(new CheckStepTask(this), 100, 1000);
  }

  public Map getInfo(taglib) {
    return [type: grails.util.GrailsNameUtils.getShortName(this.getClass()),
            headline: taglib.message(code: 'brew.step.cooking'),
            targetTemperature: taglib.message(code: 'default.formatter.degrees.celsius', args: [taglib.formatNumber(format: '##0.0', number: this.targetTemperature)]),
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
    if(this.brewProcess.cookingPumpRegulator) {
      this.brewProcess.cookingPumpRegulator.enable();
    }
    
    if(this.brewProcess.cookingMixerRegulator) {
      this.brewProcess.cookingMixerRegulator.enable();
    }
  }
 
  /**
   * Stop all associated motors for this step
   */
  private void stopMotors() {
    // Start the mashing pump and mixer
    if(this.brewProcess.cookingPumpRegulator) {
      this.brewProcess.cookingPumpRegulator.disable();
    }
    
    if(this.brewProcess.cookingMixerRegulator) {
      this.brewProcess.cookingMixerRegulator.disable();
    }
  }
}

