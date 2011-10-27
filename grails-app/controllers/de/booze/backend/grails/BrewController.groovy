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
      forward(action: "resumeLostSession", params: params)
      return
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
      return
    }

    try {
      p.start()
    }
    catch (Exception e) {
      e.printStackTrace();
      log.error("Could not start brew process: ${e}")
      render([success: false, error: g.message(code: 'brew.start.failed', args: [e])] as JSON)
      return
    }

    render([success: true] as JSON)
  }

  /**
   * Commits the fill completion and inits mashing process
   */
  def commitFill = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()
    
    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }
    
    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
   * Extends the mashing for a given amount of
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
      return
    }

    try {     
      p.startCooking()

      Protocol proto = Protocol.get(p.protocolId);
      proto.properties = params

      proto.save()
      
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
      return
    }

    def actualStep = p.getActualStep();
    if (actualStep.getClass().getName() != "de.booze.steps.BrewCookingFinishedStep" && actualStep.getClass().getName() != "de.booze.steps.BrewCoolingStep") {
      response.sendError(503)
      return
    }


    try {
      def myp = Protocol.get(p.protocolId)

      myp.properties["finalBeerVolume", "finalOriginalWort"] = params

      myp.dateFinished = new Date()
      myp.finalCookingTime = p.finalCookingTime
      
      myp.targetFinalBeerVolume = recipeService.estimateFinalWaterAmount(p.recipe)
      
      Double originalWort = myp.finalOriginalWort?myp.finalOriginalWort:myp.targetOriginalWort
      myp.ebc = recipeService.calculateBeerColor(p.recipe, originalWort)

      def finalBeerVolume = myp.finalBeerVolume ? myp.finalBeerVolume : myp.targetFinalBeerVolume

      // Calculate IBU
      Double averageWort
      if (myp.finalPostSpargingWort && myp.finalOriginalWort) {
        averageWort = (myp.finalPostSpargingWort + myp.finalOriginalWort) / 2;
      }
      else {
        averageWort = hopService.estimateAverageWort(p.recipe)
      }

      myp.ibu = hopService.calculateIbu(p.recipe, finalBeerVolume, averageWort)

      
      myp.save()

      p.cancel()
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
    BrewProcessHolder f = BrewProcessHolder.getInstance()

    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }

    BrewProcess p = f.getBrewProcess()
    
    p.newProcessId();
    render(view: 'init', model: [brewProcess: p, resume: true, events: p.getAllEvents()])
  }

  def editProtocolData = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()

    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }

    BrewProcess p = f.getBrewProcess()
    
    render([success: true, html: g.render(template:"editProtocolData", model:[protocol: Protocol.get(p.protocolId)])] as JSON)
  }

  def saveProtocolData = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()

    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }

    BrewProcess p = f.getBrewProcess()
    
    try {
      Protocol proto = Protocol.get(p.protocolId);
      proto.properties = params
      proto.validate()
      
      if(!proto.hasErrors()) {
        proto.save()
        render([success: true, close: true] as JSON)
        return
      }
      
      render([success: true, close: false, html: g.render(template:"editProtocolData", model:[protocol: proto])] as JSON)
    }
    catch(Exception e) {
      render([success: false, close: false, message: e] as JSON)
    }
  }

  def toggleForceHeater = {
    BrewProcessHolder f = BrewProcessHolder.getInstance()

    if (!f.hasBrewProcess()) {
      response.sendError(503)
      return
    }

    BrewProcess p = f.getBrewProcess()

    if (p.processId != params.processId) {
      render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
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
  
  /**
   * Set power value for a forced heater
   */
  def setForcedHeaterPower = {
	  BrewProcessHolder f = BrewProcessHolder.getInstance()
	  
	  if (!f.hasBrewProcess()) {
		response.sendError(503)
		return
	  }
  
	  BrewProcess p = f.getBrewProcess()
  
	  if (p.processId != params.processId) {
		render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
		return
	  }
  
	  try {
		Long heater = Long.parseLong(params.heater)
		Integer power = Integer.parseInt(params.power)
		p.setForcedHeaterPower(heater, power);
		render([success: true] as JSON)
	  }
	  catch (Exception e) {
		render([success: false, message: e])
	  }
  }
  
  /**
   * Enable or disable a forced heater
   */
  def toggleForcedHeaterStatus = {
	  BrewProcessHolder f = BrewProcessHolder.getInstance()
	  
	  if (!f.hasBrewProcess()) {
		response.sendError(503)
		return
	  }
  
	  BrewProcess p = f.getBrewProcess()
  
	  if (p.processId != params.processId) {
		render([success: false, error: g.message(code: 'brew.brewProcess.processId.mismatch')] as JSON)
		return
	  }
  
	  try {
		Long heater = Long.parseLong(params.heater)
		p.toggleForcedHeaterStatus(heater);
		render([success: true] as JSON)
	  }
	  catch (Exception e) {
		render([success: false, message: e])
	  }
  }
}
