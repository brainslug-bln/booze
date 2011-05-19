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
import de.booze.backend.grails.Hop

/**
 * Event which reminds the user to add
 * hop
 */
class BrewAddHopEvent extends BrewEvent {

  private Hop hop

  public BrewAddHopEvent(String message, Hop hop) {
    super(message);
    this.hop = hop;
  }

  public Hop getHop() {
    return this.hop;
  }

  public Map getEventDataForFrontend(g) {
    def args = [time: this.hop.time,
            name: this.hop.name,
            percentAlpha: this.hop.percentAlpha,
            amount: this.hop.amount,
            id: this.hop.id]


    return [message: g.message(code: 'brew.brewProcess.event', args: [(g.formatDate(format: g.message(code: 'default.time.formatter'), date: this.created)), g.message(code: this.message, args: args.collect {key, value -> value })]),
            dialog: "addHop",
            args: args,
            playSound: true];
  }

  public Map getEventDataForProtocol() {
    this.savedToProtocol = true;
    return [message: this.message,
            created: this.created.getTime(),
            type: grails.util.GrailsNameUtils.getShortName(this.getClass()),
            data: (new JSON([this.hop.time, this.hop.name, this.hop.percentAlpha, this.hop.amount, this.hop.id]).toString())];
  }
}