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
import de.booze.events.BrewEvent
import de.booze.events.BrewMeshingTemperatureReachedEvent
import de.booze.tasks.CheckStepTask

/**
 *
 * @author akotsias
 */
class BrewMeshingFinishedStep extends AbstractBrewStep {

  /**
   * Meshing temperature
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
   * Constructor
   */
  public BrewMeshingFinishedStep(BrewProcess bp, Double t) {
    this.brewProcess = bp;
    this.targetTemperature = t

    this.stepStartTime = new Date()

    // Set actual pump mode
    // Note: we keep the actual pumpMode from the last step
    //this.brewProcess.pumpRegulator.setPumpMode(pumpMode)

    // Start the pump
    this.brewProcess.pumpRegulator.enable();

    // Start the temperature regulator
    this.brewProcess.temperatureRegulator.setTemperature(this.targetTemperature);
    this.brewProcess.temperatureRegulator.start();

    // Start the timer for step checking
    this.timer = new Timer();
    this.timer.schedule(new CheckStepTask(this), 100, 1000);

    this.brewProcess.addEvent(new BrewEvent('brew.brewProcess.targetingMeshingTemperature'));
  }

  /**
   * Checks if this step is finished or if the
   * target temperature is reached
   */
  public void checkStep() {
    if (this.brewProcess.temperatureRegulator.getActualTemperature() >= this.targetTemperature) {
      this.timer.cancel();
      this.brewProcess.pumpRegulator.disable()
      this.brewProcess.temperatureRegulator.stop()
      this.brewProcess.addEvent(new BrewMeshingTemperatureReachedEvent('brew.brewProcess.meshingTemperatureReached'));
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


  public void pause() {
    this.brewProcess.pumpRegulator.disable();
    this.brewProcess.temperatureRegulator.stop();
    this.timer.cancel();
  }

  public void resume() {
    this.brewProcess.pumpRegulator.enable();
    this.brewProcess.temperatureRegulator.start();
    this.timer = new Timer();
    this.timer.schedule(new CheckStepTask(this), 100, 1000);
  }

  public Map getInfo(taglib) {
    return [type: grails.util.GrailsNameUtils.getShortName(this.getClass()),
            headline: taglib.message(code: 'brew.step.finalRestFinished'),
            stepStartTime: taglib.formatDate(formatName: 'default.time.formatter', date: this.stepStartTime),
            targetTemperature: taglib.message(code: 'default.formatter.degrees.celsius', args: [taglib.formatNumber(format: '##0.0', number: this.targetTemperature)])]
  }
}

