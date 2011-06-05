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
 **/

/**
 * BoozeLogger class
 * Handles notification and error logging
 *
 * @author Andreas Kotsias <akotsias@esnake.de>
 */
function BoozeLogger() {
    //this.messages = {warn: [], error: [], info: []};

    /**
     * Log level:
     * - 0: Log nothing
     * - 1: Log errors only
     * - 2: Log errors and warnings
     * - 3: Log everything
     */
    this.loglevel = 3;

    try {
        if (console) {
            console.log("LOGLEVEL is " + this.loglevel);
        }
    }
    catch(e) {
    }
}

/**
 * Logs an error message
 *
 * @param {String} message
 * @type void
 */
BoozeLogger.prototype.error = function(message) {
    if (this.loglevel < 1) {
        return;
    }

    if (console && console.log) {
        console.log("ERROR: " + message);
    }
    //this.messages.error.push(message);
};

/**
 * Logs an informational message
 *
 * @param {String} message
 * @type void
 */
BoozeLogger.prototype.info = function(message) {
    if (this.loglevel < 3) {
        return;
    }

    if (console && console.log) {
        console.log("INFO: " + message);
    }
    //this.messages.info.push(message);
};

/**
 * Logs a warning message
 *
 * @param {String} message
 * @type void
 */
BoozeLogger.prototype.warn = function(message) {
    if (this.loglevel < 2) {
        return;
    }

    if (console && console.log) {
        console.log("WARNING: " + message);
    }
    //this.messages.warn.push(message);
};

booze.log = new BoozeLogger();
