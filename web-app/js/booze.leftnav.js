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

/**
 * Booze left navigation class
 * Handles the loading and displaying of the tabs
 * on the left navigation bar
 *
 * @author Andreas Kotsias <akotsias@esnake.de>
 * @copyright Andreas Kotsias <akotsias@esnake.de>
 */
function BoozeLeftNav(navId) {
    this.tabs = $("#"+navId).children("ul").first().children();
    for(var i=0; i<this.tabs.length; i++) {
        $(this.tabs[i]).click({nav: this, tab: this.tabs[i]}, this.tabClick);
    }
}

BoozeLeftNav.prototype.tabClick = function(event) {
    event.stopPropagation();
    event.preventDefault();
    for(var i=0; i<event.data.nav.tabs.length; i++) {
        $(event.data.nav.tabs[i]).removeClass("active");
    }
    $(event.data.tab).addClass("active");

    var tabContents = $($(event.data.tab).children("a").first().attr("rel")).parent().children();
    
    for(var i=0; i<tabContents.length; i++) {
        $(tabContents[i]).hide();
    }

    $($(event.data.tab).children("a").first().attr("rel")).show();
}