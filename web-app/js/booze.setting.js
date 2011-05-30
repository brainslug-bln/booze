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
 * Helper methods for the setting editor
 *
 * @author Andreas Kotsias <akotsias@esnake.de>
 * @copyright Andreas Kotsias <akotsias@esnake.de>
 */
function BoozeSetting() {
  this.tabs = $("#leftnav").children("ul").first().children();
}

/**
 * Inits the setting/create environment
 */
BoozeSetting.prototype.initCreate = function() {
  for(var i=1; i<this.tabs.length; i++) {
      $(this.tabs[i]).addClass("ui-state-disabled");
  }
}

/**
 * Inits the setting/edit environment
 */
BoozeSetting.prototype.initEdit = function() {
  for(var i=0; i<this.tabs.length; i++) {
    $(this.tabs[i]).click({tab: this.tabs[i]}, this.tabClick);
  }
  this.activeTab = this.tabs[0];
}

/**
 * Updates setting data and optionally displays a new tab
 * Options: {tabToShow: LI-Element}
 */
BoozeSetting.prototype.update = function(form, options) {

  $.post(APPLICATION_ROOT+"/setting/update", $(form).serialize(), 
    function(data) {
      if(data.message) {
          booze.showStatusMessage(data.message);
      }
      if(data.success) {
        if(options && options.tabToShow) {
          booze.setting.displayTab(options.tabToShow);
        }
      }
      else {
        if(data.tab && data.html) {
          $("#"+data.tab+"TabContent").html(data.html);
        }
        if(data.error) {
          // TODO: Implement error reporting to the user
          console.log(data.error)
        }
      }
    }, "json")
}

/**
 * Callback for tab click event
 */
BoozeSetting.prototype.tabClick = function(event) {
  event.stopPropagation();
  event.preventDefault();
    
  // Identify active tab form and update
  var at = $(booze.setting.activeTab).children("a").first().attr("rel");
  booze.setting.update($("#"+at+"Form"), {tabToShow: event.data.tab});
}

/**
 * Displays a tab and hides all other tabs
 */
BoozeSetting.prototype.displayTab = function(tabToShow) {
  
    for(var i=0; i<booze.setting.tabs.length; i++) {
        $(booze.setting.tabs[i]).removeClass("active");
    }
    $(tabToShow).addClass("active");

    var tabContents = $("#settingTabsContent").children();
    
    for(var i=0; i<tabContents.length; i++) {
        $(tabContents[i]).hide();
    }

    $("#" + $(tabToShow).children("a").first().attr("rel") + "TabContent").show();
    
    booze.setting.activeTab = tabToShow;
}


$(document).ready(function() {
    booze.setting = new BoozeSetting();
});