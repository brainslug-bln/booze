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

package de.booze.events;


import grails.converters.JSON

/**
 *
 * @author akotsias
 */
class BrewMashingTemperatureStartedEvent extends BrewEvent {

  Double mashingTemperature

  public BrewMashingTemperatureStartedEvent(String message, Double mashingTemperature) {
    super(message);
    this.mashingTemperature = mashingTemperature;
  }

  public Map getEventDataForFrontend(g) {
    def args = [this.MashingTemperature]
    return [message: g.message(code: 'brew.brewProcess.event', args: [(g.formatDate(format: g.message(code: 'default.time.formatter'), date: this.created)), g.message(code: this.message, args: args)])]
  }

  public Map getEventDataForProtocol() {
    this.savedToProtocol = true;
    return [message: this.message,
            created: this.created.getTime(),
            type: grails.util.GrailsNameUtils.getShortName(this.getClass()),
            data: new JSON([this.mashingTemperature]).toString()];
  }
}

