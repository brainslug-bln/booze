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

import grails.util.GrailsNameUtils

/**
 * Triggers a dialog which informs
 * about finished meshing step
 */
class BrewMeshingTemperatureReachedEvent extends BrewEvent {

  public BrewMeshingTemperatureReachedEvent(String message) {
    super(message);
  }

  public Map getEventDataForFrontend(g) {
    return [message: g.message(code: 'brew.brewProcess.event', args: [(g.formatDate(format: g.message(code: 'default.time.formatter'), date: this.created)), g.message(code: this.message)]),
            dialog: 'meshingTemperatureReached',
            playSound: true]
  }

  public Map getEventDataForProtocol() {
    this.savedToProtocol = true;
    return [message: this.message,
            created: this.created.getTime(),
            type: grails.util.GrailsNameUtils.getShortName(this.getClass())];
  }
}