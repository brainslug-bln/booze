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
  this.tabs = $("#settingNav").children("ul").first().children();
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
    $(this.tabs[i]).click({
      tab: this.tabs[i]
      }, this.tabClick);
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
      booze.clearStatusMessage();
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
          booze.showStatusMessage(data.error);
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
  booze.setting.update($("#"+at+"Form"), {
    tabToShow: event.data.tab
    });
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

BoozeSetting.prototype.formSubmit = function(event) {
  event.stopPropagation();
  event.preventDefault();
  
  booze.setting.update(event.data.form);
}

BoozeSetting.prototype.fetchDriverOptions = function(event) {
  try { prefix = event.data.prefix } catch(e) { prefix = ""}
  
  var driver = $('#'+booze.setting.activeTab.id+'_'+prefix+'driverSelector').val()
  if(driver == "") {
    $('#'+booze.setting.activeTab.id+'_'+prefix+'driverOptions').slideUp();
  }
  
  $.get(APPLICATION_ROOT+"/setting/getDriverOptions", {
    driver: driver
  }, 
  function(data) {
    booze.clearStatusMessage();
    if(data.message) {
      booze.showStatusMessage(data.message);
    }
    if(data.success) {
      $('#'+booze.setting.activeTab.id+'_'+prefix+'driverOptions').html(data.html);
      $('#'+booze.setting.activeTab.id+'_'+prefix+'driverOptions').slideDown();
    }
    else {
      if(data.error) {
        booze.showStatusMessage(data.error);
        console.log(data.error)
      }
    }
  }, "json")
}

BoozeSetting.prototype.editRegulator = function(type, options) {
  if(!options) options = {};
  
  $.post(APPLICATION_ROOT+"/"+type+"Regulator/edit", options, 
    function(data) {
      booze.clearStatusMessage();
      if(data.message) {
        booze.showStatusMessage(data.message);
      }
      if(data.success) {
        $('#'+booze.setting.activeTab.id+'_regulatorEditor').html(data.html);
        $('#'+booze.setting.activeTab.id+'_regulatorEditor').slideDown();
      }
      else {
        if(data.error) {
          booze.showStatusMessage(data.error);
          console.log(data.error)
        }
      }
    }, "json")
}

BoozeSetting.prototype.cancelEditRegulator = function() {
  $('#'+booze.setting.activeTab.id+'_regulatorEditor').slideUp();
}

BoozeSetting.prototype.deleteRegulator = function() {
  $('#'+booze.setting.activeTab.id+'_regulatorEditor').slideUp();
  $('#'+booze.setting.activeTab.id+'_hasRegulatorField').val(0);
  $('#'+booze.setting.activeTab.id+'_regulatorIdField').val("");
  $('#'+booze.setting.activeTab.id+'_regulatorNameField').val("");
  $('#'+booze.setting.activeTab.id+'_regulatorOptionsField').val("");
  $('#'+booze.setting.activeTab.id+'_regulatorDriverField').val("");
  $('#'+booze.setting.activeTab.id+'_regulatorNameHref').hide();
  $('#'+booze.setting.activeTab.id+'_noRegulatorHref').show();
}

BoozeSetting.prototype.saveRegulator = function(event) {
  event.stopPropagation();
  event.preventDefault();
  
  $.post(APPLICATION_ROOT+"/"+event.data.type+"Regulator/save", $('#'+booze.setting.activeTab.id+'_regulatorEditorForm').serialize(), 
    function(data) {
      booze.clearStatusMessage();
      if(data.message) {
        booze.showStatusMessage(data.message);
      }
      if(data.success) {
        $('#'+booze.setting.activeTab.id+'_hasRegulatorField').val(1);
        $('#'+booze.setting.activeTab.id+'_regulatorNameField').val(data.regulator.name)
        $('#'+booze.setting.activeTab.id+'_regulatorOptionsField').val(data.regulator.options)
        $('#'+booze.setting.activeTab.id+'_regulatorDriverField').val(data.regulator.driver)
        $('#'+booze.setting.activeTab.id+'_regulatorNameHref').html(data.regulator.name)
        $('#'+booze.setting.activeTab.id+'_regulatorNameHref').show();
        $('#'+booze.setting.activeTab.id+'_noRegulatorHref').hide();
        $('#'+booze.setting.activeTab.id+'_regulatorEditor').slideUp();
      }
      else {
        $('#'+booze.setting.activeTab.id+'_regulatorEditor').html(data.html);
        
        if(data.error) {
          booze.showStatusMessage(data.error);
          console.log(data.error)
        }
      }
    }, "json")
}

BoozeSetting.prototype.editDevice = function(type, options) {
  if(!options) options = {};
    
  $.post(APPLICATION_ROOT+"/"+type+"/edit", options, 
    function(data) {
      booze.clearStatusMessage();
      if(data.message) {
        booze.showStatusMessage(data.message);
      }
      if(data.success) {
        $('#'+booze.setting.activeTab.id+'_deviceEditor').html(data.html);
        $('#'+booze.setting.activeTab.id+'_deviceList').slideUp()
        $('#'+booze.setting.activeTab.id+'_deviceEditor').slideDown();
      }
      else {
        if(data.error) {
          booze.showStatusMessage(data.error);
          console.log(data.error)
        }
      }
    }, "json")
}

BoozeSetting.prototype.cancelEditDevice = function() {
  $('#'+booze.setting.activeTab.id+'_deviceEditor').slideUp();
  $('#'+booze.setting.activeTab.id+'_deviceList').slideDown();
}

BoozeSetting.prototype.saveDevice = function(event) {

  event.stopPropagation();
  event.preventDefault();
  
  $.post(APPLICATION_ROOT+"/"+event.data.type+"/save", $('#'+booze.setting.activeTab.id+'_deviceEditorForm').serialize(), 
    function(data) {
      booze.clearStatusMessage();
      if(data.message) {
        booze.showStatusMessage(data.message);
      }
      if(data.success) {
        $('#'+booze.setting.activeTab.id+'_deviceList').html(data.html);
        $('#'+booze.setting.activeTab.id+'_deviceEditor').slideUp();
        $('#'+booze.setting.activeTab.id+'_deviceList').slideDown();
      }
      else {
        $('#'+booze.setting.activeTab.id+'_deviceEditor').html(data.html);
        
        if(data.error) {
          booze.showStatusMessage(data.error);
          console.log(data.error)
        }
      }
    }, "json")
}

BoozeSetting.prototype.deleteDevice = function(type, options) {
  if(!options) options = {};
    
  $.post(APPLICATION_ROOT+"/"+type+"/delete", options, 
    function(data) {
      booze.clearStatusMessage();
      if(data.message) {
        booze.showStatusMessage(data.message);
      }
      if(data.success) {
        $('#'+booze.setting.activeTab.id+'_deviceEditor').slideUp();
        $('#'+booze.setting.activeTab.id+'_deviceList').html(data.html)
        $('#'+booze.setting.activeTab.id+'_deviceList').slideDown()
      }
      else {
        if(data.error) {
          booze.showStatusMessage(data.error);
          console.log(data.error)
        }
      }
    }, "json")
}


$(document).ready(function() {
  booze.setting = new BoozeSetting();
});