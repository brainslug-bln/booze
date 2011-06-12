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
 * BoozeBrew class containing all methods
 * for brew process handling
 *
 */
function BoozeBrew() {

  /**
     * Indicates if the last update call
     * is already finished
     *
     * 0: Not finished yet
     * 1: Finished
     *
     * @type Integer
     */
  this.updateStatus = 1;

  /**
     * Handle for updateStatus timeout
     *
     * @type {setTimeoutHandle}
     */
  this.updateTimeoutHandle = null;

  /**
     * Timeout for update calls (ms)
     *
     * @type Integer
     */
  this.updateTimeout = 1000;

  /**
     * Maximal omits to permit for update
     *
     * @type Integer
     */
  this.maxUpdateOmits = 15;

  /**
     * Actually ommitet updates count
     *
     * @type Integer
     */
  this.updateOmits = 0;

  /**
     * AliveThrobber status count
     *
     * @type Integer
     */
  this.aliveThrobber = 0;

  /**
     * Open dialogs
     *
     * @type Array
     */
  this.dialogs = [];

  /**
     * Unique process id to identify ourselfs
     * to the server
     *
     * @type String
     */
  this.processId = "";

  /**
   * Set to true if updating shall be stopped
   *
   * @type boolean
   */
  this.stopUpdating = false;

  /**
   * Handle for pump mode selector event
   */
  this.pmsClickHandle = null;

  /**
   * Brew calculator window
   */
  this.showCalculatorWindow = null;
  
  /**
   * Perform a cooling step after cooking or not
   */
  this.coolingStep = false;
}

/**
 * Inits the brew process
 *
 * @param {String} processId
 * @param {Object} options
 * @type void
 */
BoozeBrew.prototype.init = function(processId, options) {
  booze.log.info("Init new brew process")
  
  if(!options) options = {};
  if(options.updateTimeout) this.updateTimeout = options.updateTimeout;
  if(options.coolingStep) this.coolingStep = true;
  
  this.processId = processId;
  
  this.dialogs.initDialog = booze.notifier.confirm($('#initDialogTemplate').tmpl({}),
    {
      title: booze.messageSource.message("js.booze.brew.initWindow.title"),
      proceedCallback: booze.brew.startBrewing,
      cancelCallback: booze.brew.cancel,
      cancelCallbackOptions: {force: true}
    }
  );
  
  // Start status updates
  this.update();
};

/**
 * Resumes a lost brew session
 *
 * @param {String} processId
 * @param {Object} options
 * @type void
 */
BoozeBrew.prototype.resumeLostSession = function(processId, options) {

  if(!options) options = {};
  if(options.updateTimeout) this.updateTimeout = options.updateTimeout;
  
  this.processId = processId;

  // Start status updates
  this.update();
};

/**
 * Init server call to start brew process
 *
 * @type void
 */
BoozeBrew.prototype.startBrewing = function() {
  booze.log.info("Calling brew/start");
    
  $.get(APPLICATION_ROOT + '/brew/start', {
    processId: booze.brew.processId
    }) 
  .success(function(data) {
    if(data.success) {
      booze.brew.dialogs.initDialog.dialog("destroy");
      booze.brew.dialogs.initDialog = null;
      
      booze.log.info("Brew process successfully started");
    }
    else {
      booze.notifier.error(data.message);
    }
  })
  .error(booze.brew.ajaxError);
};


/**
 * Default callback for ajax errors
 *
 * @param {Object} jqxhr  jqXHR object
 * @type void
 */
BoozeBrew.prototype.ajaxError = function(jqxhr) {
  booze.log.error("Ajax error");
  booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationError"));
    
  jqxhr.abort();
  delete jqxhr;
};

/**
 * Inits an AJAX call to the server to update
 * the current brew process status
 *
 * Sets a new timeout for the next update call
 *
 * @type void
 */
BoozeBrew.prototype.update = function() {

  // Sets a new timeout for the next update call
  if (booze.brew.stopUpdating === true) {
    return;
  }
  booze.brew.updateTimeoutHandle = setTimeout(booze.brew.update, booze.brew.updateTimeout);

  // Don't proceed if there is a still unfinished update call
  // Omit this turn and wait for the next
  if (booze.brew.updateStatus == 0) {
    if (booze.brew.updateOmits < booze.brew.maxUpdateOmits) {
      booze.log.info("Omitting update");
      booze.brew.updateOmits++;
      return;
    }
    else {
      booze.log.info("Omitted " + booze.brew.maxUpdateOmits + " updates, proceeding");
      if (!booze.brew.dialogs.stalled) {
        booze.brew.dialogs.stalled = booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationStalled"),
          {
            callback: function() { $(booze.brew.dialogs.stalled).dialog("destroy"); booze.brew.dialogs.stalled = null; }
          }
        );
      }
      booze.brew.updateOmits = 0;
    }
  }
  else {
    booze.brew.updateOmits = 0;
  }


  booze.brew.updateStatus = 0;

  // Init AJAX call
  $.get(APPLICATION_ROOT + '/brew/readStatus', {
    processId: booze.brew.processId
    }) 
  .success(booze.brew.updateCallback)
  .error(booze.brew.updateError);
};

/**
 * Callback for update server calls
 * Updates all fields reported by the server
 *
 * @param {Object} response  Server response
 * @type void
 */
BoozeBrew.prototype.updateCallback = function(response) {

  if (response.success === false) {
    booze.log.error("Update status failed");
    return;
  }

  try {
  booze.brew.spinAliveThrobber();

  var data = response.status;


  var i, sensor, temperature, pressure, heater, motor;

  for (i = 0; i < data.temperatureSensors.length; i++) {
    sensor = $('#temperatureSensor_' + data.temperatureSensors[i].id);
    $(sensor).find('.temperatureSensorValue').first().html(data.temperatureSensors[i].temperature);
    $(sensor).find('.progressbar').first().progressbar({
      value: Math.round(data.temperatureSensors[i].temperature/1.1)
      });
      
    if(data.temperatureSensors[i].reference == true) {
      $(sensor).find('.reference').first().show();
    }
    else {
      $(sensor).find('.reference').first().hide();
    }
        
  // TODO: use calculateTemperatureColor for progress bar color modification
  }

  for (i = 0; i < data.pressureSensors.length; i++) {
    sensor = $('#pressureSensor_' + data.pressureSensors[i].id);
    $(sensor).find('.pressureSensorValue').first().html(data.pressureSensors[i].pressure);
    $(sensor).find('.progressbar').first().progressbar({
      value: Math.round(data.pressureSensors[i].pressure/20)
      });
  }

  for (i = 0; i < data.heaters.length; i++) {
    if(data.heaters[i].forced == true) {
      $('#heater_'+data.heaters[i].id+'_forced').show();
      $('#heater_'+data.heaters[i].id+'_regular').hide();
      
      // Set power value
      if(data.heaters[i].power != undefined) {
        $('#heater_'+data.heaters[i].id+'_forced').find('.indicator').first().html(data.heaters[i].power);
      }
      
      // Set status icon (on/off)
      if(data.heaters[i].enabled === true) {
        $('#heater_'+data.heaters[i].id+'_forced').find('.statusIcon').first().addClass("ui-state-active");
      }
      else {
        $('#heater_'+data.heaters[i].id+'_forced').find('.statusIcon').first().removeClass("ui-state-active");
      }
    }
    else {
      $('#heater_'+data.heaters[i].id+'_forced').hide();
      $('#heater_'+data.heaters[i].id+'_regular').show();
      
      // Set power value
      if(data.heaters[i].power != undefined) {
        $('#heater_'+data.heaters[i].id+'_progressbar').find('.progressbar').first().progressbar("value", data.heaters[i].power);
      }
      
      if(data.heaters[i].speed != undefined) {
        if(data.heaters[i].enabled === true) {
        $('#heater_'+data.heaters[i].id+'_progressbar').find('.progressbar').first().progressbar("value", data.heaters[i].power);
        }
        else {
        $('#heater_'+data.heaters[i].id+'_progressbar').find('.progressbar').first().progressbar("value", 0);
        }
      }
      
      // Set status icon (on/off)
      if(data.heaters[i].enabled === true) {
        $('#heater_'+data.heaters[i].id+'_regular').find('.statusIcon').first().addClass("ui-state-active");
      }
      else {
        $('#heater_'+data.heaters[i].id+'_regular').find('.statusIcon').first().removeClass("ui-state-active");
      }
    }
  }
  
  for (i = 0; i < data.motors.length; i++) {
    if(data.motors[i].forced == true) {
      $('#motor_'+data.motors[i].id+'_forced').show();
      $('#motor_'+data.motors[i].id+'_regular').hide();
      
      // Set power value
      if(data.motors[i].speed != undefined) {
        $('#motor_'+data.motors[i].id+'_forced').find('.indicator').first().html(data.motors[i].speed);
      }
      
      // Set status icon (on/off)
      if(data.motors[i].enabled === true) {
        $('#motor_'+data.motors[i].id+'_forced').find('.statusIcon').first().addClass("ui-state-active");
      }
      else {
        $('#motor_'+data.motors[i].id+'_forced').find('.statusIcon').first().removeClass("ui-state-active");
      }
    }
    else {
      $('#motor_'+data.motors[i].id+'_forced').hide();
      $('#motor_'+data.motors[i].id+'_regular').show();
      
      // Set power value
      if(data.motors[i].speed != undefined) {
        if(data.motors[i].enabled === true) {
          $('#motor_'+data.motors[i].id+'_progressbar').find('.progressbar').first().progressbar("value", data.motors[i].speed);
        }
        else {
          $('#motor_'+data.motors[i].id+'_progressbar').find('.progressbar').first().progressbar("value", 0);
        }
      }
      
      // Set status icon (on/off)
      if(data.motors[i].enabled === true) {
        $('#motor_'+data.motors[i].id+'_regular').find('.statusIcon').first().addClass("ui-state-active");
      }
      else {
        $('#motor_'+data.motors[i].id+'_regular').find('.statusIcon').first().removeClass("ui-state-active");
      }
    }
  }


  booze.brew.handleStep(data.actualStep);

  for (i = 0; i < data.events.length; i++) {
    booze.brew.handleEvent(data.events[i]);
  }

  } catch(e){console.log(e)}
  
  // Update pause/resume button
  if (data.pause === true) {
    $('#pauseButton').hide();
    $('#resumeButton').show();
  }
  else {
    $('#pauseButton').show();
    $('#resumeButton').hide();
  }

  // Update the elapsed time info
  $('#timeElapsed').html(data.timeElapsed);

  booze.brew.updateStatus = 1;
  if (booze.brew.dialogs.updateErrorDialog) {
    $(booze.brew.dialogs.updateErrorDialog).dialog("destroy");
    booze.brew.dialogs.updateErrorDialog = null;
  }
  if (booze.brew.dialogs.stalled) {
    $(booze.brew.dialogs.stalled).dialog("destroy");
    booze.brew.dialogs.stalled = null
  }
};

/**
 * Callback for update error
 *
 * @param {Object} jqxhr  jqXHR object
 * @type void
 */
BoozeBrew.prototype.updateError = function(jqxhr) {
  booze.log.error("Ajax error for brew/update");
  
  if (booze.brew.dialogs.updateErrorDialog) {
    $(booze.brew.dialogs.updateErrorDialog).dialog("destroy");
    booze.brew.dialogs.updateErrorDialog = null
  }
  booze.brew.dialogs.updateErrorDialog = booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationError"), 
    {
      callback: function() { $(booze.brew.dialogs.updateErrorDialog).dialog("destroy"); booze.brew.dialogs.updateErrorDialog = null}
    }
  );

  jqxhr.abort();
  delete jqxhr;
};

/**
 * Pauses the brew process
 *
 * @type void
 */
BoozeBrew.prototype.pause = function() {
  booze.log.info("Calling brew/pause");
  
  $.get(APPLICATION_ROOT + '/brew/pause', {
    processId: booze.brew.processId
    }) 
  .success(function(data) {
    if (data.success === true) {
      booze.log.info("Successfully paused brew process");
      $('#pauseButton').hide();
      $('#resumeButton').show();
    }
    else {
      booze.notifier.error(data.message);
    }

    delete data;
  })
  .error(booze.brew.ajaxError);
};


/**
 * Resumes the brew process
 *
 * @type void
 */
BoozeBrew.prototype.resume = function() {
  booze.log.info("Calling brew/resume");
  
  $.get(APPLICATION_ROOT + '/brew/resume', {
    processId: booze.brew.processId
    }) 
  .success(function(data) {
    if (data.success === true) {
      booze.log.info("Successfully resumed brew process");
      $('#pauseButton').show();
      $('#resumeButton').hide();
    }
    else {
      booze.notifier.error(data.message);
    }

    delete data;
  })
  .error(booze.brew.ajaxError);
};


/**
 * Cancels the brew process
 *
 * @param {Object} options 
 * @type void
 */
BoozeBrew.prototype.cancel = function(options) {
  
  if(!options) options = {}
  
  booze.log.info("cancelling brew process");
  console.log(options);
  if (options.force) {
    booze.brew.stopUpdating = true;
    
    $.get(APPLICATION_ROOT + '/brew/cancel', {
      processId: booze.brew.processId
      }) 
    .success(function(data) {
      if (data.success === true) {
        window.location.href = APPLICATION_HOME;
      }
      else {
        booze.notifier.error(data.message);
      }

      delete data;
    })
    .error(booze.brew.ajaxError);
  }
  else {
    booze.brew.dialogs.cancelDialog = booze.notifier.confirm(booze.messageSource.message("js.booze.brew.cancelConfirmation"),
      { proceedCallback: booze.brew.cancel, 
        proceedCallbackOptions: {force: true},
        proceedText: booze.messageSource.message("js.booze.notifier.confirmCancellation"),
        cancelText:booze.messageSource.message("js.booze.notifier.keepOnBrewing"),
        cancelCallback: function() {booze.brew.dialogs.cancelDialog.dialog("destroy")}
      }
    );
  }
};


/**
 * Handles the actual event from an server update call
 *
 * @param {Object} event
 * @type void
 */
BoozeBrew.prototype.handleEvent = function(event) {
  if (event.message) {
    var li = $('<li></li>').html(event.message)
    $('#protocol').prepend(li);
  }

  if (event.playSound) {
    $.sound.play(APPLICATION_ROOT + "/sounds/notification.wav");
  }

  if (event.dialog) {
    booze.brew.openDialog(event.dialog, event.args, event.message);
  }
};

/**
 * Opens a dialog
 *
 * @param {String} dialog    Dialog name
 * @param {Array} args      Arguments for the dialog
 * @type void
 */
BoozeBrew.prototype.openDialog = function(dialog, args, message) {
  /**
     * Possible dialogs
     * - addHop
     * - cookingFinished
     * - mashingTemperatureReached
     * - lauterTemperatureReached
     * - mashingElongationFinished
     * - pressureExceeded
     */

  booze.log.info("Trying to open dialog: " + dialog);

  // Call dialog
  try {
    var myFunction = eval("booze.brew." + dialog);
    myFunction(args, message);
  }
  catch(e) {
    booze.notifier.error(booze.messageSource.message("js.booze.brew.couldNotOpenDialog") + dialog);
    booze.log.error("could not call dialog: " + dialog);
  }
};

/**
 * Opens a dialog for pressure exceedance
 *
 * @param {Array}   args     Arguments
 * @param {String}  message
 */
BoozeBrew.prototype.pressureExceeded = function(args, message) {
  if (booze.brew.dialogs.pressureExceededDialog) {
    booze.brew.dialogs.pressureExceededDialog.dialog("destroy");
    booze.brew.dialogs.pressureExceededDialog = null;
  }
  
  // Callback for OK and close
  var cb = function() {
    booze.brew.dialogs.pressureExceededDialog.dialog("destroy");
    booze.brew.dialogs.pressureExceededDialog = null;
    booze.brew.resume();
  }
  
  booze.brew.dialogs.pressureExceededDialog = booze.notifier.error(message, {
    modal: true,
    callback: cb
  });
  
};

/**
 * Opens a addHop dialog
 *
 * @param {Array} args     Arguments
 * @param {String} message
 */
BoozeBrew.prototype.addHop = function(args, message) {

  booze.log.info("Opening window for addHop");
    
  booze.notifier.notify($('#addHopDialogTemplate').tmpl(args), 
    {title: booze.messageSource.message('js.booze.brew.addHopDialog.title'),
  });
};

/**
 * Opens a dialog for lauterTemperatureReached
 *
 * @param {Array} args      Arguments
 * @param {String} message
 * @type void
 */
BoozeBrew.prototype.lauterTemperatureReached = function(args, message) {
  booze.log.info("Opening window for lauterTemperatureReached event");

  // Display brew initialization window
  booze.brew.dialogs.lauterTemperatureReachedDialog = booze.notifier.notify($('#lauterTemperatureReachedDialogTemplate').tmpl(args), 
    {
      title: booze.messageSource.message('js.booze.brew.lauterTemperatureReachedDialog.title'),
      callback: booze.brew.startCooking
    }
  );
}



/**
 * Opens a dialog for cookingFinished
 *
 * @param {Array} args      Arguments
 * @param {String} message
 * @type void
 */
BoozeBrew.prototype.cookingFinished = function(args, message) {
  booze.log.info("Opening dialog for cookingFinished event");
  
  var cb = booze.brew.finish
  if(booze.brew.coolingStep == true) {
    booze.log.info("preparing cooling step");
    cb = booze.brew.startCooling;
  }

  // Display brew initialization window
  booze.brew.dialogs.cookingFinishedDialog = booze.notifier.notify($('#cookingFinishedDialogTemplate').tmpl(args), 
    {
      title: booze.messageSource.message('js.booze.brew.cookingFinishedDialog.title'),
      buttonText: booze.messageSource.message('js.booze.brew.cookingFinishedDialog.finish'),
      callback: cb
    }
  );
};

/**
 * Opens a dialog for mashingTemperatureReached
 *
 * @param {Array} args      Arguments
 * @param {String} message
 * @type void
 */
BoozeBrew.prototype.mashingTemperatureReached = function(args, message) {
  booze.log.info("Opening window for mashingTemperatureReached event");
  
  booze.brew.dialogs.mashingTemperatureReachedDialog = booze.notifier.notify($('#mashingTemperatureReachedDialogTemplate').tmpl(args),
    {
      title: booze.messageSource.message('js.booze.brew.mashingTemperatureReachedDialog.title'),
      callback: booze.brew.commitFill,
    }
  )
}

/**
 * Commits the fill completion and proceeds to mashing
 *
 * @type void
 */
BoozeBrew.prototype.commitFill = function() {
  
  $.get(APPLICATION_ROOT + '/brew/commitFill', {
      processId: booze.brew.processId
      }) 
    .success(function(data) {
      if (data.success === true) {
        booze.log.info("Successfully commited fill");
        booze.brew.dialogs.mashingTemperatureReachedDialog.dialog("destroy");
        booze.brew.dialogs.mashingTemperatureReachedDialog = null;
      }
      else {
        booze.notifier.error(data.message);
      }

      delete data;
    })
    .error(booze.brew.ajaxError);
}

/**
 * Gracefully finish the brew process
 *
 * @type void
 */
BoozeBrew.prototype.finish = function() {
  this.stopUpdating = true;
  
  $.get(APPLICATION_ROOT + '/brew/finish', {
      processId: booze.brew.processId,
      finalOriginalWort: $('brewCookingTemperatureReachedWDialog_finalOriginalWort').value,
      finalVolume: $('brewCookingTemperatureReachedDialog_finalVolume').value,
      dilutionWaterVolume: $('brewCookingTemperatureReachedDialog_dilutionWaterVolume').value
      }) 
    .success(function(data) {
      if (data.success === true) {
        booze.log.info("Successfully finished brew process");
        window.location.href=data.redirect;
      }
      else {
        booze.notifier.error(data.message);
      }

      delete data;
    })
    .error(booze.brew.ajaxError);
};

/**
 * Starts the cooling process
 *
 * @type void
 */
BoozeBrew.prototype.startCooling = function() {
  booze.log.info("Calling brew/startCooling");

  $.get(APPLICATION_ROOT + '/brew/startCooling', {
      processId: booze.brew.processId
      }) 
    .success(function(data) {
      if (data.success === true) {
        $(booze.brew.dialogs.cookingFinishedDialog).dialog("destroy");
        booze.brew.dialogs.cookingFinishedDialog = null;
        
        if(booze.brew.dialogs.coolingDialog) {
          $(booze.brew.dialogs.coolingDialog).dialog("destroy");
          booze.brew.dialogs.coolingDialog = null;
        }
        
        booze.brew.dialogs.coolingDialog = booze.notifier.notify($('#coolingDialogTemplate').tmpl({}),
          {
            title: booze.messageSource.message('js.booze.brew.coolingDialog.title'),
            callback: booze.brew.finish,
            buttonText: booze.messageSource.message('js.booze.brew.finishBrewProcess')
          }
        )
      }
      else {
        booze.notifier.error(data.message);
      }

      delete data;
    })
    .error(booze.brew.ajaxError);
};

/**
 * Starts the cooking process
 *
 * @type void
 */
BoozeBrew.prototype.startCooking = function() {
  booze.log.info("Calling brew/startCooking");

  $.get(APPLICATION_ROOT + '/brew/startCooking', {
      processId: booze.brew.processId,
      finalSpargingWaterVolume: $('#brewLauterTemperatureReachedDialog_finalSpargingWaterVolume').val(),
      finalPostSpargingWort: $('#brewLauterTemperatureReachedDialog_finalPostSpargingWort').val(),
      finalPreSpargingWort: $('#brewLauterTemperatureReachedDialog_finalPreSpargingWort').val()
      }) 
    .success(function(data) {
      if (data.success === true) {
        
        booze.log.info("Successfully started cooking");
        booze.brew.dialogs.lauterTemperatureReachedDialog.dialog("destroy");
        booze.brew.dialogs.lauterTemperatureReachedDialog = null;
      }
      else {
        booze.notifier.error(data.message);
      }

      delete data;
    })
    .error(booze.brew.ajaxError);
};


/**
 * Elongates the mashing process for a given amount of time
 *
 * @type void
 */
BoozeBrew.prototype.elongateMashing = function() {
  booze.log.info("Calling brew/elongateMashing");
  
  $.get(APPLICATION_ROOT + '/brew/elongateMashing', {
      processId: booze.brew.processId,
      time: $('#brewLauterTemperatureReachedDialog_elongateMashingTime').val()
      }) 
    .success(function(data) {
      if (data.success === true) {
        booze.log.info("Successfully started cooking");
        booze.brew.dialogs.lauterTemperatureReachedDialog.dialog("destroy");
      }
      else {
        booze.notifier.error(data.message);
      }

      delete data;
    })
    .error(booze.brew.ajaxError);
};

/**
 * Elongates the cooking process for a given amount of time
 *
 * @type void
 */
BoozeBrew.prototype.elongateCooking = function() {
  booze.log.info("Calling brew/elongateCooking");
  
  $.get(APPLICATION_ROOT + '/brew/elongateCooking', {
      processId: booze.brew.processId,
      time: $('#brewCookingTemperatureReachedDialog_elongateCookingTime').val(),
      temperature: $('#cookingTemperatureSlider').slider("value")
      }) 
    .success(function(data) {
      if (data.success === true) {
        booze.log.info("Successfully started cooking");
        booze.brew.dialogs.cookingFinishedDialog.dialog("destroy");
        booze.brew.dialogs.cookingFinishedDialog = null;
      }
      else {
        booze.notifier.error(data.message);
      }

      delete data;
    })
    .error(booze.brew.ajaxError);
};

/**
 * Handles display of step info
 *
 * @param {Object} step  Step info
 * @type void
 */
BoozeBrew.prototype.handleStep = function(step) {
  booze.log.info("Handling step with type: " + step.type);
  
  $('#stepInfoHeadline').html(step.headline);

  switch (step.type) {
    case "BrewInitStep":

      break;

    case "BrewTargetMashingTemperatureStep":
      var children = $('#stepInfo').children();
      for (var i = 0; i < children.length; i++) {
        if (children[i].id != "targetMashingTemperatureInfo") $(children[i]).hide(); else $(children[i]).show();
      }
      $('#targetMashingTemperatureInfo_targetTemperature').html(step.targetTemperature);
      $('#targetMashingTemperatureInfo_stepStartTime').html(step.stepStartTime);
      break;

    case "BrewMashingTemperatureReachedStep":
      var children = $('#stepInfo').children();
      for (var i = 0; i < children.length; i++) {
        if (children[i].id != "mashingTemperatureReachedInfo") $(children[i]).hide(); else $(children[i]).show();
      }
      break;

    case "BrewRestStep":
      var children = $('#stepInfo').children();
      for (var i = 0; i < children.length; i++) {
        if (children[i].id != "restInfo") $(children[i]).hide(); else $(children[i]).show();
      }
      $('#restInfo_targetTemperature').html(step.targetTemperature);
      $('#restInfo_timeToGo').html(step.timeToGo);
      $('#restInfo_stepStartTime').html(step.stepStartTime);
      if (step.targetTemperatureReached) {
        $('#restInfo_temperatureReached_true').html(step.targetTemperatureReachedTime);
        $('#restInfo_temperatureReached_true').show();
        $('#restInfo_temperatureReached_false').hide();
      }
      else {
        $('#restInfo_temperatureReached_true').hide();
        $('#restInfo_temperatureReached_false').show();
      }
      break;

    case "BrewTargetLauterTemperatureStep":
      var children = $('#stepInfo').children();
      for (var i = 0; i < children.length; i++) {
        if (children[i].id != "mashingFinishedInfo") $(children[i]).hide(); else $(children[i]).show();
      }
      $('#mashingFinishedInfo_targetTemperature').html(step.targetTemperature);
      $('#mashingFinishedInfo_stepStartTime').html(step.stepStartTime);

      break;

    case "BrewInitCookingStep":
      var children = $('#stepInfo').children();
      for (var i = 0; i < children.length; i++) {
        if (children[i].id != "initCookingInfo") $(children[i]).hide(); else $(children[i]).show();
      }
      break;

    case "BrewCookingStep":
      $('#cookingTemperatureBox').show();
      var children = $('#stepInfo').children();
      for (var i = 0; i < children.length; i++) {
        if (children[i].id != "cookingInfo") $(children[i]).hide(); else $(children[i]).show();
      }
      $('#cookingInfo_targetTemperature').html(step.targetTemperature);
      $('#cookingInfo_timeToGo').html(step.timeToGo);
      $('#cookingInfo_stepStartTime').html(step.stepStartTime);
      if (step.targetTemperatureReached) {
        $('#cookingInfo_temperatureReached_true').html(step.targetTemperatureReachedTime);
        $('#cookingInfo_temperatureReached_true').show();
        $('#cookingInfo_temperatureReached_false').hide();
      }
      else {
        $('#cookingInfo_temperatureReached_true').hide();
        $('#cookingInfo_temperatureReached_false').show();
      }
      break;

    case "BrewCookingFinishedStep":
      var children = $('#stepInfo').children();
      for (var i = 0; i < children.length; i++) {
        if (children[i].id != "initCookingInfo") $(children[i]).hide(); else $(children[i]).show();
      }
      break;

    case "BrewElongateMashingStep":
      var children = $('#stepInfo').children();
      for (var i = 0; i < children.length; i++) {
        if (children[i].id != "elongateMashingInfo") $(children[i]).hide(); else $(children[i]).show();
      }
      $('#elongateMashingInfo_timeToGo').html(step.timeToGo);
      $('#elongateMashingInfo_stepStartTime').html(step.stepStartTime);
      $('#elongateMashingInfo_targetTemperature').html(step.targetTemperature);

      break;

    case "BrewElongateCookingStep":
      $('#cookingTemperatureBox').show();
      
      var children = $('#stepInfo').children();
      for (var i = 0; i < children.length; i++) {
        if (children[i].id != "cookingInfo" && children[i].id != "elongateCookingInfo") $(children[i]).hide(); else $(children[i]).show();
      }
            
      $('#elongateCookingInfo_timeToGo').html(step.timeToGo);
      $('#elongateCookingInfo_stepStartTime').html(step.stepStartTime);
      $('#elongateCookingInfo_targetTemperature').html(step.targetTemperature);

      break;
      
    case "BrewCoolingStep":
      $('#cookingTemperatureBox').hide();
      
      var children = $('#stepInfo').children();
      for (var i = 0; i < children.length; i++) {
        if (children[i].id != "coolingInfo") $(children[i]).hide(); else $(children[i]).show();
      }
            
      $('#coolingInfo_stepStartTime').html(step.stepStartTime);

      break;
  }
};

/**
 * Initializes the slider for heatingTemperatureOffset
 *
 * @param {Number} value
 * @type void
 */
BoozeBrew.prototype.setHysteresis = function(value) {
  
  $.get(APPLICATION_ROOT + '/brew/setHysteresis', {
      processId: booze.brew.processId,
      hysteresis: value
      }) 
    .success(function(data) {
      if (data.success == true) {
        booze.log.info('successfuly set hysteresis to ' + value)
      }
      else {
        booze.notifier.error(data.message);
      }

      delete data;
    })
    .error(booze.brew.ajaxError);
};

/**
 * Initializes the slider for cookingTemperature
 * 
 * @param {Number} value
 * @type void
 */
BoozeBrew.prototype.setCookingTemperature = function(value) {
  console.log("Setting cooking temperature to "+value);
  
  $.get(APPLICATION_ROOT + '/brew/setCookingTemperature', {
      processId: booze.brew.processId,
      cookingTemperature: value
      }) 
    .success(function(data) {
      if (data.success == true) {
        booze.log.info('successfuly set cookingTemperature to ' + value)
      }
      else {
        booze.notifier.error(data.message);
      }

      delete data;
    })
    .error(booze.brew.ajaxError);
};

/**
 * Adds a comment to the protocol
 *
 * @type void
 */
BoozeBrew.prototype.addComment = function() {
  
  $.get(APPLICATION_ROOT + '/brew/addComment', {
      processId: booze.brew.processId,
      comment: $('#brewCommentField').val()
      }) 
    .success(function(data) {
      if (data.success === true) {
          booze.log.info("Successfully added comment");
        $('#brewCommentField').val("");
      }
      else {
        booze.notifier.error(data.message);
      }

      delete data;
    })
    .error(booze.brew.ajaxError);
}

/**
 * Displays a pump mode selector for a pump
 *
 * @param {Object} event    Calling event
 * @param {Element} callee
 */
BoozeBrew.prototype.pumpModeSelector = function(event, callee) {
  event.stop();

  var pms = callee.up().select('.pumpModeSelector').first();

  pms.clonePosition(callee, {
    offsetLeft: (pms.getDimensions().width * -1 + callee.getDimensions().width), 
    setWidth: false, 
    setHeight: false
  });
  pms.show();

  pms.style.zIndex = 10000;

  this.pmsClickHandle = Element.observe(window.document, 'click', this.hidePumpModeSelector.bindAsEventListener(this, pms))
}


/**
 * Hides a pump mode selector
 *
 * @param {Object} event    Calling event
 * @param {Element} pms     Pump mode selector dialog
 * @type void
 */
BoozeBrew.prototype.hidePumpModeSelector = function(event, pms) {
  event.stop();

  pms.hide();
  Element.stopObserving(window.document, this.pmsClickHandle);
}

/**
 * Forces the pump to the selected pump mode
 *
 * @param {Integer} pump Pump Id
 * @param {Integer} pumpMode Pump mode id
 * @type void
 */
BoozeBrew.prototype.forcePumpMode = function(pump, pumpMode) {
  new Ajax.Request(APPLICATION_ROOT + '/brew/forcePumpMode', {
    parameters: {
      processId: this.processId, 
      pumpMode: pumpMode, 
      pump: pump
    },
    onSuccess: function(response) {
      if (response.responseJSON) {
        var data = response.responseJSON;
        if (data.success === true) {
          booze.log.info("cleared forced pump mode");
        }
        else {
          booze.notifier.error(data.message);
        }
      }
      else {
        booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationError"));
      }
    }.bindAsEventListener(this),
    onFailure: this.ajaxError.bindAsEventListener(this)
  });
}

/**
 * Toggles the forced mode for a heater
 *
 * @param {Integer} heater Heater id
 * @type void
 */
BoozeBrew.prototype.toggleForceHeater = function(heater) {
  $.get(APPLICATION_ROOT + '/brew/toggleForceHeater', {
      processId: booze.brew.processId,
      heater: heater
      }) 
    .success(function(data) {
      if (data.success === true) {
          booze.log.info("toggled heater: "+heater);
      }
      else {
        booze.notifier.error(data.message);
      }

      delete data;
    })
    .error(booze.brew.ajaxError);
}


/**
 * Unforces a forced pump mode
 *
 * @param {Integer} pump
 * @type void
 */
BoozeBrew.prototype.unforcePumpMode = function(pump) {
  new Ajax.Request(APPLICATION_ROOT + '/brew/unforcePumpMode', {
    parameters: {
      processId: this.processId, 
      pump: pump
    },
    onSuccess: function(response) {
      if (response.responseJSON) {
        var data = response.responseJSON;
        if (data.success === true) {
          booze.log.info("cleared forced pump mode");
        }
        else {
          booze.notifier.error(data.message);
        }
      }
      else {
        booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationError"));
      }
    }.bindAsEventListener(this),
    onFailure: this.ajaxError.bindAsEventListener(this)
  });
}

/**
 * Spins the throbber which indicates the server connection
 *
 * @type void
 */
BoozeBrew.prototype.spinAliveThrobber = function() {
  this.aliveThrobber++;
  if (this.aliveThrobber > 7) {
    this.aliveThrobber = 0;
  }
  $('#aliveThrobber').attr('src', APPLICATION_ROOT+"/images/icons/brew/throbber/"+this.aliveThrobber+".png");
};

BoozeBrew.prototype.showCalculator = function() {
  booze.log.info("Opening window for calculator");

  // Display brew initialization dialog
  booze.brew.showCalculatorDialog = booze.notifier.notify($('#brewCalculatorDialogTemplate').tmpl({}), 
    {
      title: booze.messageSource.message('js.booze.brew.calculator.title')
    }
  );
}

/**
 * Shows the edit window for edit protocol data
 *
 * @type void
 */
BoozeBrew.prototype.editProtocolData = function() {
  if(this.editProtocolDialog != null) {
    this.editProtocolDialog.dialog("moveToTop");
    return;
  }
  
  $.get(APPLICATION_ROOT + '/brew/editProtocolData', {
      processId: booze.brew.processId,
      }) 
    .success(function(data) {
      if (data.success === true) {
          booze.brew.dialogs.editProtocolDialog = booze.notifier.notify(data.html, 
            {
              title: booze.messageSource.message('js.booze.brew.editProtocolDialog.title'),
              closeCallback: function() {booze.brew.dialogs.editProtocolDialog = null},
              callback: booze.brew.saveProtocolData
            }
          );
      }
      else {
        booze.notifier.error(data.message);
      }

      delete data;
    })
    .error(booze.brew.ajaxError);
};

/**
 * Saves protocol data to server
 *
 * @type void
 */
BoozeBrew.prototype.saveProtocolData = function() {
  $.get(APPLICATION_ROOT + '/brew/saveProtocolData', {
      processId: booze.brew.processId,
      }) 
    .success(function(data) {
      if (data.success === true) {
        if(data.close == true) {
          booze.brew.dialogs.editProtocolDialog.dialog("close");
        }
        else {
          booze.brew.dialogs.editProtocolDialog.html(data.html);  
        }
      }

      delete data;
    })
    .error(booze.brew.ajaxError);   
}

BoozeBrew.prototype.showTemperatureChart = function() {
  booze.log.info("Opening window for temperature chart");
  
  var now = new Date();
  booze.brew.dialogs.temperatureChart =  $('<div><img src="'+$('#temperatureChartDialogTemplate').html()+'&q='+now.getTime()+'" style="width: 100%" /></div>')
		.dialog({
      width: "50%",
      minHeight: '20%',
      position: "top",
      dialogClass: "showCloseButton",
      close: function() { $(this).destroy(); booze.brew.dialogs.temperatureChart = null},
			title: booze.messageSource.message("js.booze.brew.temperatureChart"),
      modal: false,
      buttons: []
		});
}

BoozeBrew.prototype.showPressureChart = function() {
  booze.log.info("Opening window for pressure chart");
  
  var now = new Date();
  booze.brew.dialogs.pressureChart =  $('<div><img src="'+$('#pressureChartDialogTemplate').html()+'&q='+now.getTime()+'" style="width: 100%" /></div>')
		.dialog({
      width: "50%",
      minHeight: '20%',
      position: "top",
      dialogClass: "showCloseButton",
      close: function() { $(this).destroy(); booze.brew.dialogs.pressureChart = null},
			title: booze.messageSource.message("js.booze.brew.pressureChart"),
      modal: false,
      buttons: []
		});
}

/**
 * Returns a hex color string for the actual temperature
 *
 * @type String
 */
BoozeBrew.prototype.calculateTemperatureColor = function(actualTemperature, targetTemperature) {
  if(actualTemperature < (targetTemperature - 3)) {
    return "#"+(this.toHex(0)+this.toHex(0)+this.toHex(180));
  }
  else if(actualTemperature > (targetTemperature + 3)) {
    return "#"+(this.toHex(180)+this.toHex(0)+this.toHex(0));
  }

  var r, g, b = 0;

  if(actualTemperature < targetTemperature) {
    b = 0 + Math.round((targetTemperature - actualTemperature) * 60)
    g = 180 - Math.round((targetTemperature - actualTemperature) * 60)
  }
  else {
    r = 0 + Math.round((actualTemperature - targetTemperature) * 60)
    g = 180 - Math.round((actualTemperature - targetTemperature) * 60)
  }

  return "#"+(this.toHex(r)+this.toHex(g)+this.toHex(b));
}

/**
 * Converts a decimal number to a hex string
 *
 * @type String
 */
BoozeBrew.prototype.toHex = function(dec) {
  // create list of hex characters
  var hexCharacters = "0123456789ABCDEF"
  // if number is out of range return limit
  if (dec < 0)
    return "00"
  if (dec > 255)
    return "FF"
  // decimal equivalent of first hex character in converted number
  var i = Math.floor(dec / 16)
  // decimal equivalent of second hex character in converted number
  var j = dec % 16
  // return hexadecimal equivalent
  return hexCharacters.charAt(i) + hexCharacters.charAt(j)
}

// Create instance within booze namespace
booze.brew = new BoozeBrew();


