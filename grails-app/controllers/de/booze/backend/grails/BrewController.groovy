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

import de.booze.events.BrewAddCommentEvent

class BrewController {

  def brewService, protocolService, hopService, recipeService

  /**
   * Inits the brew process
   * Before rendering the brew window all devices are initialized
   * and stored into a BrewProcess object in the session.
   */
  def init = {

    def s = Setting.findByIsDefault(true)
    if (!s) {
      render(view: "initError", model: [msg: message(code: 'brew.init.noSettings')])
      return
    }


    if (!params.recipe || !Recipe.exists(params.recipe)) {
      render(view: "initError", model: [msg: message(code: 'brew.init.recipeNotFound')])
      return
    }

    // Check if there is a running brew process
    if (brewService.brewProcess != null) {
      forward(action: "resumeLostSession", params: params)
      return
    }

    // Create a new brew process instance which holds
    // all information related to this brew
    try {
      def settings = Setting.read(s.id)
      def recipe = Recipe.read(params.recipe)

      // Check if the sensors defined in the recipe are all present
      /*if(brewService.checkInvalidSensorsForRecipe(recipe, settings)) {
          flash.message = message(code:"brew.init.invalidSensorsDefinedInRecipe")
          redirect(controller:'recipe', action:'edit', id: recipe.id)
          return
      }*/

      brewService.initBrewProcess(recipe, settings, g)
    }
    catch (Exception e) {
      render(view: "initError", model: [msg: e.toString(), recipe: params.recipe])
      return
    }
    catch (Error er) {
      render(view: "initError", model: [msg: er.toString(), recipe: params.recipe])
      return
    }

    // Render template
    [brewProcess: brewService.brewProcess, pumpModes: PumpMode.findAll()]
  }

  /**
   * Starts the brew process
   */
  def start = {
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
      brewService.brewProcess.start()
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
      log.debug("Commiting fill...")
      brewService.brewProcess.commitFill()
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
      Long elongate = Long.parseLong(params.time)
      brewService.brewProcess.elongateMashing(elongate * 60)
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
    if (!brewService.brewProcess || brewService.brewProcess == null) {
      response.sendError(503)
    }

    if (brewService.brewProcess?.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      //Protocol p = Protocol.get(brewService.brewProcess.protocolId);
      //bindData(p, params);

      //p.save()
      brewService.brewProcess.startCooking()

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
      Long elongate = Long.parseLong(params.time)
      Double temperature = Double.parseDouble(params.temperature)
      brewService.brewProcess.elongateCooking(elongate * 60, temperature)
      render([success: true] as JSON)
    }
    catch (Exception e) {
      log.error("Could not elongate cooking, passed time value is not valid: ${e}")
      render([success: false, message: g.message(code: 'brew.elongateCooking.failed', args: [e])] as JSON)
    }
  }

  /**
   * Reads the status for the actual brew process
   */
  def readStatus = {
    if (!brewService.brewProcess || brewService.brewProcess == null) {
      response.sendError(503)
      return
    }

    if (brewService.brewProcess?.processId != params.processId) {
      log.error("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    try {
      def status = brewService.updateStatus()
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
      if (!brewService.brewProcess.isPaused()) {
        brewService.brewProcess.pause()
        render([success: true] as JSON)
      }
    }
    catch (Exception e) {
      log.error("Unable to pause: ${e}")
      render([success: false, message: g.message(code: 'brew.pause.failed', args: [e])] as JSON)
    }
  }

  /**
   * Resumes the actual brew process
   */
  def resume = {
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
      if (brewService.brewProcess.isPaused()) {
        brewService.brewProcess.resume()
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
      brewService.brewProcess.cancel()
      brewService.brewProcess = null
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
      def hysteresis = Double.parseDouble(params.hysteresis)
      brewService.brewProcess.setHysteresis(hysteresis)
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
    if (!brewService.brewProcess || brewService.brewProcess == null) {
      response.sendError(503)
      return
    }

    if (brewService.brewProcess?.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    def actualStep = brewService.brewProcess.getActualStep();
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
    if (!brewService.brewProcess || brewService.brewProcess == null || !params.comment) {
      response.sendError(503)
      return
    }

    if (brewService.brewProcess?.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    brewService.brewProcess.addEvent(new BrewAddCommentEvent(params.comment));
    render([success: true] as JSON)
  }

  /**
   * Forces the pump to a given pump mode
   */
//  def forcePumpMode = {
//    if (!brewService.brewProcess || brewService.brewProcess == null || !params.pumpMode) {
//      response.sendError(503)
//      return
//    }
//
//    if (brewService.brewProcess?.processId != params.processId) {
//      log.debug("Given process id does not match the processe's id")
//      response.sendError(503)
//      return
//    }
//
//    try {
//      brewService.brewProcess.forcePumpMode(PumpMode.get(params.pumpMode));
//      render([success: true] as JSON)
//    }
//    catch (Exception e) {
//      render([success: false, message: e])
//    }
//  }
//
//  /**
//   * Un-forces the pump
//   */
//  def unforcePumpMode = {
//    if (!brewService.brewProcess || brewService.brewProcess == null) {
//      response.sendError(503)
//      return
//    }
//
//    if (brewService.brewProcess?.processId != params.processId) {
//      log.debug("Given process id does not match the processe's id")
//      response.sendError(503)
//      return
//    }
//
//    try {
//      brewService.brewProcess.unforcePumpMode();
//      render([success: true] as JSON)
//    }
//    catch (Exception e) {
//      render([success: false, message: e])
//    }
//  }

  /**
   * Finishes the brew process
   */
  def finish = {
    if (!brewService.brewProcess || brewService.brewProcess == null) {
      response.sendError(503)
      return
    }

    if (brewService.brewProcess?.processId != params.processId) {
      log.debug("Given process id does not match the processe's id")
      response.sendError(503)
      return
    }

    def actualStep = brewService.brewProcess.getActualStep();
    if (actualStep.getClass().getName() != "de.booze.steps.BrewCookingFinishedStep") {
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
      brewService.brewProcess.cancel()

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

      brewService.brewProcess = null

      [success: true/*, protocolInstance: myp*/]
    }
    catch (Exception e) {
      log.error("Failed to finish brew process: ${e}")
      response.sendError(503)
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
}
