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

package de.booze.backend.grails

import grails.converters.*
import de.booze.process.BrewProcess
import de.booze.process.BrewProcessHolder
import de.booze.events.BrewAddCommentEvent

class BrewController {

  def brewService, protocolService, hopService, recipeService

  /**
   * Inits the brew process
   * Before rendering the brew window all devices are initialized
   * and stored into a BrewProcess object in the session.
   */
  def init = {

    def s = Setting.findByActive(true)
    if (!s) {
      render(view: "initError", model: [msg: message(code: 'brew.init.noSettings')])
      return
    }


    if (!params.recipe || !Recipe.exists(params.recipe)) {
      render(view: "initError", model: [msg: message(code: 'brew.init.recipeNotFound')])
      return
    }
    
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    // Check if there is a running brew process
    if (f.hasBrewProcess()) {
      f.getBrewProcess().cancel();
      f.flushBrewProcess()
      //forward(action: "resumeLostSession", params: params)
      //return
    }

    // Create a new brew process instance which holds
    // all information related to this brew
    try {
      def settings = Setting.read(s.id)
      def recipe = Recipe.read(params.recipe)

      f.setBrewProcess(new BrewProcess(recipe, settings))
    }
    catch (Exception e) {
      render(view: "initError", model: [msg: e.toString(), recipe: params.recipe])
      return
    }
    catch (Error er) {
      render(view: "initError", model: [msg: er.toString(), recipe: params.recipe])
      return
    }

    log.error(f.getBrewProcess())
    // Render template
    [brewProcess: f.getBrewProcess()]
  }

  /**
   * Starts the brew process
   */
  def start = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      p.start()
    }
    catch (Exception e) {
      e.printStackTrace();
      log.error("Could not start brew process: ${e}")
      render([success: false, message: g.message(code: 'brew.start.failed', args: [e])] as JSON)
      return
    }

    render([success: true] as JSON)
  }

  /**
   * Commits the fill completion and inits meshing process
   */
  def commitFill = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      log.debug("Commiting fill...")
      p.commitFill()
    }
    catch (Exception e) {
      log.error("Could not commit fill: ${e}")
      render([success: false, message: g.message(code: 'brew.fillCommit.failed', args: [e])] as JSON)
      return
    }

    render([success: true] as JSON)
  }

  /**
   * Extends the meshing for a given amount of
   * minutes
   */
  def elongateMashing = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      Long elongate = Long.parseLong(params.time)
      p.elongateMashing(elongate * 60)
      render([success: true] as JSON)
    }
    catch (Exception e) {
      log.error("Could not elongate mashing, passed time value is not valid: ${e}")
      render([success: false, message: g.message(code: 'brew.elongateMashing.failed', args: [e])] as JSON)
    }
  }

  /**
   * Starts the cooking process after meshing
   * was finished
   */
  def startCooking = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      //Protocol p = Protocol.get(brewService.brewProcess.protocolId);
      //bindData(p, params);

      //p.save()
      p.startCooking()

      render([success: true] as JSON)
    }
    catch (Exception e) {
      render([success: false, message: g.message(code: 'brew.startCooking.failed', args: [e])] as JSON)
    }
  }

  /**
   * Extends the cooking step for a given amount of
   * minutes
   */
  def elongateCooking = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      Long elongate = Long.parseLong(params.time)
      Double temperature = Double.parseDouble(params.temperature)
      p.elongateCooking(elongate * 60, temperature)
      render([success: true] as JSON)
    }
    catch (Exception e) {
      log.error("Could not elongate cooking, passed time value is not valid: ${e}")
      render([success: false, message: g.message(code: 'brew.elongateCooking.failed', args: [e])] as JSON)
    }
  }
  
  /**
   * Starts the cooling step
   */
  def startCooling = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      p.startCooling()
      render([success: true] as JSON)
    }
    catch (Exception e) {
      log.error("Could not start cooling: ${e}")
      render([success: false, message: g.message(code: 'brew.startCooling.failed', args: [e])] as JSON)
    }
  }

  /**
   * Reads the status for the actual brew process
   */
  def readStatus = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.error("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      def status = brewService.updateStatus(g)
      render([success: true, status: status] as JSON)
    }
    catch (Exception e) {
      log.error("Unable to read status: ${e}")
      e.printStackTrace()
      render([success: false, message: g.message(code: 'brew.readStatus.failed', args: [e])] as JSON)
    }
  }

  /**
   * Pauses the actual brew process
   */
  def pause = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      if (!p.isPaused()) {
        p.pause()
        render([success: true] as JSON)
      }
    }
    catch (Exception e) {
      log.error("Unable to pause: ${e}")
      render([success: false, message: g.message(code: 'brew.pause.failed', args: [e])] as JSON)
      e.printStackTrace();
    }
  }

  /**
   * Resumes the actual brew process
   */
  def resume = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      if (p.isPaused()) {
        p.resume()
        render([success: true] as JSON)
      }
    }
    catch (Exception e) {
      log.error("Unable to resume: ${e}")
      render([success: false, message: g.message(code: 'brew.resume.failed', args: [e])] as JSON)
    }

  }

  /**
   * Cancels the actual brew process
   */
  def cancel = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      p.cancel()
      f.flushBrewProcess()
      render([success: true] as JSON)
    }
    catch (Exception e) {
      render([success: false, message: g.message(code: 'brew.cancel.failed', args: [e])] as JSON)
      return
    }
  }

  /**
   * Sets the value for hysteresis adaptation
   */
  def setHysteresis = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      def hysteresis = Double.parseDouble(params.hysteresis)
      p.setHysteresis(hysteresis)
    }
    catch (Exception e) {
      log.error("Unable to set temperature offset: ${e}")
      render([success: false, message: g.message(code: 'brew.setHysteresis.failed', args: [e])] as JSON)
    }
    render([success: true] as JSON)
  }

  /**
   * Sets the cooking temperature
   */
  def setCookingTemperature = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    def actualStep = p.getActualStep();
    if (actualStep.getClass().getName() != "de.booze.steps.BrewCookingStep" && actualStep.getClass().getName() != "de.booze.steps.BrewElongateCookingStep") {
      response.sendError(503)
      return
    }

    try {
      Double temp = Double.parseDouble(params.cookingTemperature)
      log.debug("setting cooking temperature: ${temp}")
      actualStep.setTargetTemperature(temp)
    }
    catch (Exception e) {
      log.error("Unable to set cooking temperature: ${e}")
      render([success: false, message: g.message(code: 'brew.setCookingTemperature.failed', args: [e])] as JSON)
    }

    render([success: true] as JSON)
  }

  /**
   * Adds a comment to the event list
   */
  def addComment = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    p.addEvent(new BrewAddCommentEvent(params.comment));
    render([success: true] as JSON)
  }

  /**
   * Forces a motor to a cycling mode
   */
  def forceCyclingMode = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      p.forceCyclingMode(params);
      render([success: true] as JSON)
    }
    catch (Exception e) {
      render([success: false, message: e])
    }
  }

  /**
   * Un-forces the pump
   */
  def unforcePumpMode = {
    if (!brewService.brewProcess || brewService.brewProcess == null) {
      response.sendError(503)
      return
    }

    if (brewService.brewProcess?.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      brewService.brewProcess.unforcePumpMode();
      render([success: true] as JSON)
    }
    catch (Exception e) {
      render([success: false, message: e])
    }
  }

  /**
   * Finishes the brew process
   */
  def finish = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    def actualStep = p.getActualStep();
    if (actualStep.getClass().getName() != "de.booze.steps.BrewCookingFinishedStep" && actualStep.getClass().getName() != "de.booze.steps.BrewCoolingStep") {
      response.sendError(503)
      return
    }

//    Double totalPowerConsumption = 0.0 as Double
//
//    brewService.brewProcess.heaters.each {
//      totalPowerConsumption += it.readTotalPowerConsumption()
//    }
//
//    totalPowerConsumption += brewService.brewProcess.pump.readTotalPowerConsumption()


    try {
      p.cancel()

//      def myp = Protocol.get(brewService.brewProcess.protocolId)
//
//      myp.properties["finalVolume", "finalOriginalWort"] = params
//
//      myp.powerConsumption = totalPowerConsumption
//      myp.dateFinished = new Date()
//      myp.finalCookingTime = brewService.brewProcess.finalCookingTime
//      myp.targetFinalVolume = recipeService.estimateFinalWaterAmount(brewService.brewProcess.recipe)
//      
//      Double originalWort = myp.finalOriginalWort?myp.finalOriginalWort:myp.targetOriginalWort
//      myp.ebc = recipeService.calculateBeerColor(brewService.brewProcess.recipe, originalWort)
//
//      def finalVolume = myp.finalVolume ? myp.finalVolume : myp.targetFinalVolume
//
//      // Calculate IBU
//      Double averageWort
//      if (myp.finalPreCookingWort && myp.finalOriginalWort) {
//        averageWort = (myp.finalPreCookingWort + myp.finalOriginalWort) / 2;
//      }
//      else {
//        averageWort = hopService.estimateAverageWort(brewService.brewProcess.recipe)
//      }
//
//      myp.ibu = hopService.calculateIbu(brewService.brewProcess.recipe, finalVolume, averageWort)
//
//      myp.save()

      f.flushBrewProcess()

      render([success: true, redirect: g.createLink(controller:'recipe', action:'edit', id:p.recipe.id)/*, protocolInstance: myp*/] as JSON)
      return
    }
    catch (Exception e) {
      log.error("Failed to finish brew process: ${e}")
      render([success: false, error: e] as JSON)
    }
  }

  /**
   * Resumes a brew process after the client
   * connection was los
   */
  def resumeLostSession = {
    brewService.brewProcess.newProcessId();
    render(view: 'init', model: [brewProcess: brewService.brewProcess, resume: true, events: brewService.brewProcess.getAllEvents(), pumpModes: PumpMode.findAll()])
  }

//  def editProtocolData = {
//    render(template:"editProtocolData", model:[protocol: Protocol.get(brewService.brewProcess.protocolId)])
//  }
//
//  def saveProtocolData = {
//    try {
//      Protocol p = Protocol.get(brewService.brewProcess.protocolId);
//      p.properties = params
//      if(!p.validate()) {
//        render([success: true, close: false, html: g.render(template:"editProtocolData", model:[protocol: p])] as JSON)
//      }
//      p.save()
//      render([success: true, close: true] as JSON)
//    }
//    catch(Exception e) {
//      render([success: false, close: false, message: e] as JSON)
//    }
//  }

  def toggleForceHeater = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()

    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }

    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      Long heater = Long.parseLong(params.heater)
      p.toggleForceHeater(heater);
      render([success: true] as JSON)
    }
    catch (Exception e) {
      render([success: false, message: e])
    }
  }
  
}
