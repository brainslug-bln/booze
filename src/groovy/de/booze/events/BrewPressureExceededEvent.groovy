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

package de.booze.events

import grails.converters.*
import grails.util.GrailsNameUtils

/**
 * Event which signals a pressure exceedance
 */
class BrewPressureExceededEvent extends BrewEvent {

  /**
   * Pressure value which lead to the event
   */
  Double pressure;

  /**
   * Signalig sensor name
   */
  String sensorName;

  public BrewPressureExceededEvent(String message, Double pressure, String sensorName) {
    super(message);
    this.pressure = pressure;
    this.sensorName = sensorName;
  }

  public Map getEventDataForFrontend(g) {

    Map args = [pressure: g.formatNumber(format: "###0", number: this.pressure), sensorName: this.sensorName]

    return [message: g.message(code: 'brew.brewProcess.event', args: [(g.formatDate(format: g.message(code: 'default.time.formatter'), date: this.created)), g.message(code: this.message, args: args.collect {key, value -> value })]),
            dialog: "pressureExceeded",
            args: args,
            playSound: true];
  }

  public Map getEventDataForProtocol() {
    this.savedToProtocol = true;
    return [message: this.message,
            created: this.created.getTime(),
            type: grails.util.GrailsNameUtils.getShortName(this.getClass()),
            data: new JSON([this.pressure, this.sensorName]).toString()];
  }
}