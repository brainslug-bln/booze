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
package de.booze.backend.grails

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtilities

class ProtocolController {

  static allowedMethods = [delete: "GET", edit: 'GET', update: 'POST', list: 'GET']

  def protocolService

  def index = {
    redirect(action: "list", params: params)
  }

  def list = {
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    params.sort = params.sort ?: "dateStarted"
    params.order = params.order ?: "desc"
    [protocolInstanceList: Protocol.list(params), protocolInstanceTotal: Protocol.count()]
  }

  def editBrewData = {
    def protocolInstance = Protocol.get(params.id)
	
    if (!protocolInstance) {
      flash.message = message(code: 'protocol.notFound', args: [params.id])
      redirect(action: "list")
    }
    else {
      [protocolInstance: protocolInstance]
    }
  }
  
  def editFermentationData = {
	  def protocolInstance = Protocol.get(params.id)
	  if (!protocolInstance) {
		flash.message = message(code: 'protocol.notFound', args: [params.id])
		redirect(action: "list")
	  }
	  else {
		[protocolInstance: protocolInstance]
	  }
	}
  
  def temperatureChart = {
    def protocolInstance = Protocol.get(params.id)
    if (!protocolInstance) {
      flash.message = message(code: 'protocol.notFound', args: [params.id])
      redirect(action: "list")
    }
    else {
      [protocolInstance: protocolInstance]
    }
  }
  
  def pressureChart = {
    def protocolInstance = Protocol.get(params.id)
    if (!protocolInstance) {
      flash.message = message(code: 'protocol.notFound', args: [params.id])
      redirect(action: "list")
    }
    else {
      [protocolInstance: protocolInstance]
    }
  }

  def showTemperatureChart = {
    def protocolInstance = Protocol.get(params.id)
    if (!protocolInstance) {
      log.error("Protocol not found with ID: ${params.id}")
      response.sendError(503)
    }

    Map size;
    try {
      size = [width: Integer.parseInt(params.width), height: Integer.parseInt(params.height)]
    }
    catch (Exception e) {
      log.error("No valid size specified for protocol temperature chart creation")
      response.sendError(503)
      return
    }

    JFreeChart tchart = protocolService.createTemperatureChart(protocolInstance, g);

    response.setContentType("image/jpeg")
    OutputStream out = response.getOutputStream();
    ChartUtilities.writeChartAsJPEG(out, tchart, size.width, size.height)
    out.close();
  }

  def showPressureChart = {
    def protocolInstance = Protocol.get(params.id)
    if (!protocolInstance) {
      log.error("Chart not found with ID: ${params.id}")
      response.sendError(503)
    }

    Map size;
    try {
      size = [width: Integer.parseInt(params.width), height: Integer.parseInt(params.height)]
    }
    catch (Exception e) {
      log.error("No valid size specified for protocol pressure chart creation")
      response.sendError(503)
      return
    }

    def pchart = protocolService.createPressureChart(protocolInstance, g);

    response.setContentType("image/jpeg")
    OutputStream out = response.getOutputStream();
    ChartUtilities.writeChartAsJPEG(out, pchart, size.width, size.height)
    out.close();
  }

  def delete = {
    def protocolInstance = Protocol.get(params.id)
    if (protocolInstance) {
      try {
        protocolInstance.delete(flush: true)
        flash.message = message(code: "protocol.delete.deleted", args: [protocolInstance.recipeName])
        redirect(action: "list")
      }
      catch (org.springframework.dao.DataIntegrityViolationException e) {
        flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'protocol.label', default: 'Protocol'), params.id])}"
        redirect(action: "show", id: params.id)
      }
    }
    else {
      flash.message = message(code: 'protocol.notFound', args: [params.id])
      redirect(action: "list")
    }
  }

  def update = {
    def protocolInstance = Protocol.get(params.id)
    if (protocolInstance) {
      if (params.version) {
        def version = params.version.toLong()
        if (protocolInstance.version > version) {
          protocolInstance.errors.rejectValue("version", "protocol.optimistic.locking.failure", "Another user has updated this Protocol while you were editing.")
          render(view: 'editBrewData', model: [protocolInstance: protocolInstance])
          return
        }
      }

      protocolInstance.properties = params

      if (!protocolInstance.hasErrors() && protocolInstance.save(flush: true)) {
        flash.message = message(code: 'protocol.edit.saved')
      }

      render(view: "editBrewData", model: [protocolInstance: protocolInstance])
    }
    else {
      flash.message = message(code: 'protocol.edit.notFound')
      redirect(action: "list")
    }
  }
}
