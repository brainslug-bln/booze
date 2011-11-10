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

package de.booze.events

import grails.util.GrailsNameUtils


/**
 * Default brew event which holds
 * only a message for the brewProtocol list
 */
class BrewEvent {

  /**
   * Event message
   */
  String message;

  /**
   * Indicates whether this event was delivered
   * to the frontend
   */
  boolean delivered = false;

  /**
   * Date when the event was created
   */
  Date created = new Date()

  /**
   * Indicates wheter the event was persisted
   * to the protocol
   */
  boolean savedToProtocol = false

  /**
   * Returns true if the message is already delivered
   * @return
   */
  public boolean delivered() {
    return this.delivered;
  }

  /**
   * Tell the event that is has been delivered
   * @return
   */
  public boolean deliver() {
    this.delivered = true;
  }

  /**
   * Returns true if the event has already been saved
   * to the protocol
   * @return
   */
  public boolean getSavedToProtocol() {
    return this.savedToProtocol;
  }

  /**
   * Constructor
   * @param message
   */
  public BrewEvent(String message) {
    this.message = message;
  }

  /**
   * Returns an object with data for the
   * frontend
   * 
   * @param g
   * @return
   */
  public Map getEventDataForFrontend(g) {
    return [message: g.message(code: 'brew.brewProcess.event', args: [(g.formatDate(format: g.message(code: 'default.time.formatter'), date: this.created)), g.message(code: this.message)])]
  }

  /**
   * Returns an object with data for the
   * protocol
   * 
   * @return
   */
  public Map getEventDataForProtocol() {
    this.savedToProtocol = true;
    return [message: this.message,
            created: this.created.getTime(),
            type: grails.util.GrailsNameUtils.getShortName(this.getClass())];
  }
}