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
import de.booze.grails.RecipeRest

/**
 * Event for rest messages
 */
class BrewRestEvent extends BrewEvent {

  private RecipeRest rest;

  public BrewRestEvent(String message, RecipeRest rest) {
    super(message);
    this.rest = rest;
  }

  public RecipeRest getRest() {
    return this.rest;
  }

  public Map getEventDataForFrontend(g) {
    def args = [temperature: this.rest.temperature,
            index: (this.rest.indexInRests + 1),
            comment: this.rest.comment ?: g.message(code: 'brew.brewProcess.noRestComment'),
            duration: this.rest.duration];

    return [message: g.message(code: 'brew.brewProcess.event', args: [(g.formatDate(format: g.message(code: 'default.time.formatter'), date: this.created)), g.message(code: this.message, args: args.collect {key, value -> value })])]
  }

  public Map getEventDataForProtocol() {
    this.savedToProtocol = true;
    return [message: this.message,
            created: this.created.getTime(),
            type: grails.util.GrailsNameUtils.getShortName(this.getClass()),
            data: new JSON([this.rest.temperature, (this.rest.indexInRests + 1), this.rest.comment, this.rest.duration]).toString()]
  }

}