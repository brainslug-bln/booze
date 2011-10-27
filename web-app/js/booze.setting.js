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
 * This class handles the displaying of tabs
 * and all ajax requests to the server
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
 * 
 * @param {Form Element} form Form to submit
 * @param {Object} options Hashmap with options
 */
BoozeSetting.prototype.update = function(form, options) {
  
  $.post(APPLICATION_ROOT+"/setting/update", $(form).serialize(), 
    function(data) {
      if(data.message) {
        booze.notifier.statusMessage(data.message);
      }
      if(data.success) {
        if(options && options.tabToShow) {
          booze.setting.displayTab(options.tabToShow);
        }
      }
      else {
        if(data.error) {
          booze.notifier.statusMessage(data.error);
        }
      }
      
      if(data.tab && data.html) {
        $("#"+data.tab+"TabContent").html(data.html);
      }
    }, "json")
}

/**
 * Callback for tab click event
 * 
 * @param {Event} event Calling event
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
 * 
 * @param {Element} tabToShow Tab to show
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

/**
 * Submits the settings form
 * 
 * @param {Event} event Calling event
 */
BoozeSetting.prototype.formSubmit = function(event) {
  event.stopPropagation();
  event.preventDefault();
  
  booze.setting.update(event.data.form);
}

/**
 * Fetches available options for a driver from the server
 * and displays them in the driverOptions div
 * 
 * @param {Event} event Calling event
 */
BoozeSetting.prototype.fetchDriverOptions = function(event) {
  var prefix;
  try { prefix = event.data.prefix } catch(e) { prefix = ""}
  
  var driver = $('#'+booze.setting.activeTab.id+'_'+prefix+'driverSelector').val()
  if(driver == "") {
    $('#'+booze.setting.activeTab.id+'_'+prefix+'driverOptions').hide();
  }
  
  $.get(APPLICATION_ROOT+"/setting/getDriverOptions", {
    driver: driver
  }, 
  function(data) {
    if(data.message) {
      booze.notifier.statusMessage(data.message);
    }
    if(data.success) {
      $('#'+booze.setting.activeTab.id+'_'+prefix+'driverOptions').html(data.html);
      $('#'+booze.setting.activeTab.id+'_'+prefix+'driverOptions').show();
    }
    else {
      if(data.error) {
        booze.notifier.statusMessage(data.error);
      }
    }
  }, "json")
}

/**
 * Displays an edit dialog for a regulator
 * 
 * @param {String} type Regulator type
 * @param {Object} options Hashmap with options that are submitted to the server
 */
BoozeSetting.prototype.editRegulator = function(type, options) {
  if(!options) options = {};
  
  $.post(APPLICATION_ROOT+"/"+type+"Regulator/edit", options, 
    function(data) {
      if(data.message) {
        booze.notifier.statusMessage(data.message);
      }
      if(data.success) {
        $('#'+booze.setting.activeTab.id+'_regulatorEditor').html(data.html);
        $('#'+booze.setting.activeTab.id+'_tabOptions').hide();
        $('#'+booze.setting.activeTab.id+'_regulatorEditor').show();
      }
      else {
        if(data.error) {
          booze.notifier.statusMessage(data.error);
        }
      }
    }, "json")
}

/**
 * Cancels a regulator/edit dialog
 */
BoozeSetting.prototype.cancelEditRegulator = function() {
  $('#'+booze.setting.activeTab.id+'_regulatorEditor').slideUp();
        $('#'+booze.setting.activeTab.id+'_tabOptions').show();
}

/**
 * Deletes a regulator by hiding the editor and removing the 
 * regulator data from the hidden input fields in the device dialog
 */
BoozeSetting.prototype.deleteRegulator = function() {
  $('#'+booze.setting.activeTab.id+'_regulatorEditor').hide();
  $('#'+booze.setting.activeTab.id+'_hasRegulatorField').val(0);
  $('#'+booze.setting.activeTab.id+'_regulatorIdField').val("");
  $('#'+booze.setting.activeTab.id+'_regulatorNameField').val("");
  $('#'+booze.setting.activeTab.id+'_regulatorOptionsField').val("");
  $('#'+booze.setting.activeTab.id+'_regulatorDriverField').val("");
  $('#'+booze.setting.activeTab.id+'_regulatorNameHref').hide();
  $('#'+booze.setting.activeTab.id+'_noRegulatorHref').show();
  $('#'+booze.setting.activeTab.id+'_tabOptions').show();
  $('#'+booze.setting.activeTab.id+'_regulatorSoftOnField').val("");
}

/**
 * Saves a regulator
 * - submit data to the server and validate the data
 * - write the data into the device's hidden input field on success
 * 
 * @param {Event} event Calling event
 */
BoozeSetting.prototype.saveRegulator = function(event) {
  event.stopPropagation();
  event.preventDefault();
  
  $.post(APPLICATION_ROOT+"/"+event.data.type+"Regulator/save", $('#'+booze.setting.activeTab.id+'_regulatorEditorForm').serialize(), 
    function(data) {
      if(data.message) {
        booze.notifier.statusMessage(data.message);
      }
      if(data.success) {
        $('#'+booze.setting.activeTab.id+'_hasRegulatorField').val(1);
        $('#'+booze.setting.activeTab.id+'_regulatorNameField').val(data.regulator.name)
        $('#'+booze.setting.activeTab.id+'_regulatorOptionsField').val(data.regulator.options)
        $('#'+booze.setting.activeTab.id+'_regulatorDriverField').val(data.regulator.driver)
        $('#'+booze.setting.activeTab.id+'_regulatorNameHref').html(data.regulator.name)
        $('#'+booze.setting.activeTab.id+'_regulatorNameHref').show();
        $('#'+booze.setting.activeTab.id+'_noRegulatorHref').hide();
        $('#'+booze.setting.activeTab.id+'_regulatorEditor').hide();
        $('#'+booze.setting.activeTab.id+'_tabOptions').show();
        $('#'+booze.setting.activeTab.id+'_regulatorSoftOnField').val(data.regulator.softOn);
      }
      else {
        $('#'+booze.setting.activeTab.id+'_regulatorEditor').html(data.html);
        
        if(data.error) {
          booze.notifier.statusMessage(data.error);
        }
      }
    }, "json")
}

/**
 * Displays an edit dialog for a device
 * 
 * @param {String} type Device type
 * @param {Object} options Hashmap with options that are submitted to the server
 */
BoozeSetting.prototype.editDevice = function(type, options) {
  if(!options) options = {};
    
  $.post(APPLICATION_ROOT+"/"+type+"/edit", options, 
    function(data) {
      if(data.message) {
        booze.notifier.statusMessage(data.message);
      }
      if(data.success) {
        $('#'+booze.setting.activeTab.id+'_deviceEditor').html(data.html);
        $('#'+booze.setting.activeTab.id+'_deviceList').hide()
        $('#'+booze.setting.activeTab.id+'_deviceEditor').show();
      }
      else {
        if(data.error) {
          booze.notifier.statusMessage(data.error);
        }
      }
    }, "json")
}

/**
 * Cancels a device/edit dialog
 */
BoozeSetting.prototype.cancelEditDevice = function() {
  $('#'+booze.setting.activeTab.id+'_tabOptions').show();
  $('#'+booze.setting.activeTab.id+'_deviceEditor').hide();
  $('#'+booze.setting.activeTab.id+'_deviceList').show();
  $('#'+booze.setting.activeTab.id+'_regulatorEditor').hide();
}

/**
 * Saves a device and hides the edit dialog on success
 * 
 * @param {Event} event Calling event
 */
BoozeSetting.prototype.saveDevice = function(event) {

  event.stopPropagation();
  event.preventDefault();
  
  $.post(APPLICATION_ROOT+"/"+event.data.type+"/save", $('#'+booze.setting.activeTab.id+'_deviceEditorForm').serialize(), 
    function(data) {
      if(data.message) {
        booze.notifier.statusMessage(data.message);
      }
      if(data.success) {
        $('#'+booze.setting.activeTab.id+'_deviceList').html(data.html);
        $('#'+booze.setting.activeTab.id+'_deviceEditor').hide();
        $('#'+booze.setting.activeTab.id+'_deviceList').show();
        $('#'+booze.setting.activeTab.id+'_tabOptions').show();
      }
      else {
        $('#'+booze.setting.activeTab.id+'_deviceEditor').html(data.html);
        
        if(data.error) {
          booze.notifier.statusMessage(data.error);
        }
      }
    }, "json")
}
/**
 * Deletes a device and reloads the device list
 * 
 * @param {String} type Device type
 * @param {Object} options Hashmap with options that are submitted to the server
 */
BoozeSetting.prototype.deleteDevice = function(type, options) {
  if(!options) options = {};
    
  $.post(APPLICATION_ROOT+"/"+type+"/delete", options, 
    function(data) {
      if(data.message) {
        booze.notifier.statusMessage(data.message);
      }
      if(data.success) {
        $('#'+booze.setting.activeTab.id+'_deviceEditor').slideUp();
        $('#'+booze.setting.activeTab.id+'_deviceList').html(data.html)
        $('#'+booze.setting.activeTab.id+'_deviceList').slideDown()
      }
      else {
        if(data.error) {
          booze.notifier.statusMessage(data.error);
        }
      }
    }, "json")
}

/**
 * Loads the motorTask/edit dialog from the server
 * 
 * @param {Object} options Hashmap with options that are submitted to the server
 */
BoozeSetting.prototype.createMotorTask = function(options) {
  if(!options) options = {};
  
  $.get(APPLICATION_ROOT+"/setting/createMotorTask", options, 
    function(data) {
      if(data.message) {
        booze.notifier.statusMessage(data.message);
      }
      if(data.success) {
        $("#motorTask_"+options.type+"_data").html(data.html);
        $("#motorTask_"+options.type+"_data").show();
      }
      else {
        if(data.error) {
          booze.notifier.statusMessage(data.error);
        }
      }
    }, "json")
}

/**
 * Hide/show regulation mode options corresponding to the
 * selected regulation mode
 * 
 * @param {String} type Regulation mode type
 * @param {String} mode Regulation mode (temperature/speed/pressure/off)
 */
BoozeSetting.prototype.selectMotorTaskRegulationMode = function(type, mode) {
  if(mode == "temperature") {
    $('#motorTask_'+type+'_regulationModeData_speed').hide();
    $('#motorTask_'+type+'_regulationModeData_temperature').show();
    $('#motorTask_'+type+'_regulationModeData_pressure').hide();
  }
  else if(mode == "speed") {
    $('#motorTask_'+type+'_regulationModeData_speed').show();
    $('#motorTask_'+type+'_regulationModeData_temperature').hide();
    $('#motorTask_'+type+'_regulationModeData_pressure').hide();
  }
  else if(mode == "pressure") {
    $('#motorTask_'+type+'_regulationModeData_speed').hide();
    $('#motorTask_'+type+'_regulationModeData_temperature').hide();
    $('#motorTask_'+type+'_regulationModeData_pressure').show();
  }
  else {
    $('#motorTask_'+type+'_regulationModeData_speed').hide();
    $('#motorTask_'+type+'_regulationModeData_temperature').hide();
    $('#motorTask_'+type+'_regulationModeData_pressure').hide();
  }
}

$(document).ready(function() {
  booze.setting = new BoozeSetting();
});