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
import de.booze.events.BrewMashingTemperatureReachedEvent
import de.booze.events.BrewTargetMashingTemperatureStartedEvent
import de.booze.backend.grails.Recipe
import de.booze.tasks.CheckStepTask

/**
 *
 * @author akotsias
 */
class BrewTargetMashingTemperatureStep extends AbstractBrewStep {

  /**
   * This step's type
   */
  public BrewProcess brewProcess;

  /**
   * Time when this step was started
   */
  public Date stepStartTime

  /**
   * Target temperature
   */
  public Recipe recipe

  /**
   * The timer for this step
   */
  public Timer timer


  public BrewTargetMashingTemperatureStep(BrewProcess bp, Recipe recipe) {
    this.brewProcess = bp;
    this.recipe = recipe;

    this.stepStartTime = new Date()

    this.startMotors();

    // Set the target temperature
    this.brewProcess.temperatureRegulator.setTargetTemperature(this.recipe.mashingTemperature);
    
    // Use the mashing sensors as reference
    this.brewProcess.temperatureRegulator.setMashingReferenceSensors();
    
    // Disable the heating ramp for this step
    this.brewProcess.temperatureRegulator.disableRamp();
    
    // Start the temperatureRegulator
    this.brewProcess.temperatureRegulator.start();

    // Start the timer for step checking
    this.timer = new Timer();
    this.timer.schedule(new CheckStepTask(this), 100, 1000);

    this.brewProcess.addEvent(new BrewTargetMashingTemperatureStartedEvent('brew.brewProcess.targetingMashingTemperature', this.recipe.mashingTemperature));
  }

  /**
   * Checks if this step is finished
   */
  public void checkStep() {

    if (this.brewProcess.temperatureRegulator.getActualTemperature() >= this.recipe.mashingTemperature) {
      this.brewProcess.addEvent(new BrewMashingTemperatureReachedEvent('brew.brewProcess.mashingTemperatureReached', this.recipe.mashingTemperature));

      this.pause();
      this.brewProcess.nextStep();
    }
  }

  /**
   * Returns the time as Date object when
   * this step was initialized
   */
  public Date getStepStartTime() {
    return this.stepStartTime;
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
            headline: taglib.message(code: 'brew.step.targetMashingTemperature'),
            targetTemperature: taglib.message(code: 'default.formatter.degrees.celsius', args: [taglib.formatNumber(format: '##0.0', number: this.recipe.mashingTemperature)]),
            stepStartTime: taglib.formatDate(formatName: 'default.time.formatter', date: this.stepStartTime)]
  }
  
  /**
   * Start all associated motors for this step
   */
  private void startMotors() {
    // Start the mashing pump and mixer
    if(this.brewProcess.mashingPumpRegulator != null) {
      this.brewProcess.mashingPumpRegulator.enable();
    }
    
    if(this.brewProcess.mashingMixerRegulator != null) {
      this.brewProcess.mashingMixerRegulator.enable();
    }
  }
 
  /**
   * Stop all associated motors for this step
   */
  private void stopMotors() {
    // Start the mashing pump and mixer
    if(this.brewProcess.mashingPumpRegulator != null) {
      this.brewProcess.mashingPumpRegulator.disable();
    }
    
    if(this.brewProcess.mashingMixerRegulator != null) {
      this.brewProcess.mashingMixerRegulator.disable();
    }
  }
}

