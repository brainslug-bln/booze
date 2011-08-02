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
import de.booze.events.BrewCookingElongationFinishedEvent
import de.booze.events.BrewElongateCookingEvent
import de.booze.tasks.CheckStepTask

/**
 *
 * @author akotsias
 */
class BrewElongateCookingStep extends AbstractBrewStep {

  /**
   * Cooking temperature
   */
  public Double targetTemperature;

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
   * Time by which to elongate the meshing step
   */
  public Long time

  /**
   * Constructor
   */
  public BrewElongateCookingStep(BrewProcess bp, Long ti, Double t) {

    this.brewProcess = bp;
    this.targetTemperature = t
    this.time = ti;

    this.stepStartTime = new Date();

    this.startMotors();

    // Set the target temperature
    this.brewProcess.temperatureRegulator.setTargetTemperature(this.targetTemperature);
    
    // Use the mashing sensors as reference
    this.brewProcess.temperatureRegulator.setCookingReferenceSensors();
    
    // Disable the heating ramp for this step
    this.brewProcess.temperatureRegulator.disableRamp();
    
    // Start the temperatureRegulator
    this.brewProcess.temperatureRegulator.start();

    // Start the timer for step checking
    this.timer = new Timer();
    this.timer.schedule(new CheckStepTask(this), 100, 1000);

    this.brewProcess.addEvent(new BrewElongateCookingEvent('brew.brewProcess.elongateCooking', ti));
  }

  /**
   * Checks if this step is finished or if the
   * target temperature is reached
   */
  public void checkStep() {
    if ((this.stepStartTime.getTime() + this.time * 1000) < (new Date()).getTime()) {
      this.timer.cancel();
      this.stopMotors();
      this.brewProcess.temperatureRegulator.stop()
      this.brewProcess.addEvent(new BrewCookingElongationFinishedEvent('brew.brewProcess.cookingElongationFinished'));
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
   * Returns the time to go for meshing elongation in seconds
   */
  public Long getTimeToGo() {
    Long stepEndTime = this.stepStartTime.getTime() + (this.time * 1000);
    return Math.round((stepEndTime - (new Date().getTime())) / 1000);
  }

  /**
   * Returns the cooking time
   */
  public Integer getCookingTime() {
    return Math.round(((new Date()).getTime() - this.stepStartTime.getTime()) / 60000)
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
            headline: taglib.message(code: 'brew.step.elongateCooking'),
            stepStartTime: taglib.formatDate(formatName: 'default.time.formatter', date: this.stepStartTime),
            targetTemperature: taglib.message(code: 'default.formatter.degrees.celsius', args: [taglib.formatNumber(format: '##0.0', number: this.targetTemperature)]),
            timeToGo: taglib.message(code: 'default.formatter.minutes', args: [Math.round(this.getTimeToGo() / 60)])]
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
    if(this.brewProcess.cookingPumpRegulator) {
      this.brewProcess.cookingPumpRegulator.disable();
    }
    
    if(this.brewProcess.cookingMixerRegulator) {
      this.brewProcess.cookingMixerRegulator.disable();
    }
  }
}

