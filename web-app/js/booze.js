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

// First of all remove trailing slash from APPLICATION_ROOT if exists
/*if (APPLICATION_ROOT.charAt(APPLICATION_ROOT.length - 1) == "/") {
    APPLICATION_ROOT = APPLICATION_ROOT.substring(0, APPLICATION_ROOT.length - 1);
}*/

/**
 * Booze namespace container
 *
 * @author Andreas Kotsias <akotsias@esnake.de>
 */
function Booze() {
}

Booze.prototype.showStatusMessage = function(msg) {
    $('#statusMessage').html(msg);
}

var booze = new Booze();

/**
 * Replacement for the .outerHTML property which
 * is not supported by Mozilla browsers
 *
 * @type String
 */
Element.prototype.getOuterHtml = function() {
    var container = new Element("div");
    container.appendChild(this.cloneNode(true));

    return container.innerHTML;
};

String.prototype.startsWith = function(str) {
    return (this.match("^"+str)==str);
};




