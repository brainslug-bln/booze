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

package de.booze.steps

import java.lang.Math.*

import grails.util.GrailsNameUtils
import de.booze.process.BrewProcess

/**
 *
 * @author akotsias
 */
class BrewCoolingStep extends AbstractBrewStep {

  Date stepStartTime
  
  /**
   * The brewProcess
   */
  public BrewProcess brewProcess;

  public BrewCoolingFinishedStep(BrewProcess bp) {   
    this.brewProcess = bp;
    
    this.stepStartTime = new Date()

    this.startMotors();

  }

  public void pause() {
    this.stopMotors()
  }

  public void resume() {
    this.startMotors()
  }

  public Map getInfo(taglib) {
    return [type: grails.util.GrailsNameUtils.getShortName(this.getClass()),
            headline: taglib.message(code: 'brew.step.coolingFinished'),
            stepStartTime: taglib.formatDate(formatName: 'default.time.formatter', date: this.stepStartTime),]
  }
  
  public void startMotors() {
    if(this.brewProcess.drainPump) {
      this.brewProcess.drainPump.enable()
    }
  }
  
  public void stopMotors() {
    if(this.brewProcess.drainPump) {
      this.brewProcess.drainPump.disable()
    }
  }
}

