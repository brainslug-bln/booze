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
 * BoozeBrew class contains all methods
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
     * Handle for meshingTemperature window
     *
     * @type Window
     */
    this.meshingTemperatureReachedWindow;

    /**
     * Handle for initialization window
     *
     * @type Window
     */
    this.initWindow;

    /**
     * Handle for cooking finished window
     *
     * @type Window
     */
    this.cookingFinishedWindow;

    /**
     * Handle for fill temperature reached windows
     *
     * @type Window
     */
    this.fillTemperatureReachedWindow;

    /**
     * Slider instance for cooking temperature adjustion
     *
     * @type Control.Slider
     */
    this.cookingTemperatureSlider;

    /**
     * Slider instance for adjustion of
     * heating temperature offset
     *
     * @type Control.Slider
     */
    this.temperatureOffsetSlider;

    /**
     * Cancel dialog handle
     *
     * @type Object
     */
    this.cancelDialog;

    /**
     * Edit protocol dialog handle
     *
     * @type Object
     */
    this.editProtocolDialog;

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
     * Unique process id to identify us
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
}

/**
 * Inits the brew process
 *
 * @param processId
 * @type void
 */
BoozeBrew.prototype.init = function(processId) {

    this.processId = processId;

    // Display brew initialization window
    this.initWindow = new Window('initWindow', {title: booze.messageSource.message("js.booze.brew.initWindow.title"),
        minimizable: false,
        maximizable: false,
        resizable: false,
        closable: false,
        recenterAuto: true,
        width: "400px",
        /*height: "420px",*/
        maxHeight: 420,
        minHeight: 330,
        showEffectOptions: {duration: 0.2},
        hideEffectOptions: {duration: 0.2},
        destroyOnClose: true});

    this.initWindow.setHTMLContent($('initWindowContent').innerHTML);
    this.initWindow.showCenter(true);

    this.initTemperatureOffsetSlider();
    this.initCookingTemperatureSlider();
    $('cookingTemperatureBox').hide();

    // Start status updates
    this.update();
};

/**
 * Resumes a lost brew session
 *
 * @param processId
 * @type void
 */
BoozeBrew.prototype.resumeLostSession = function(processId) {

    this.processId = processId;

    this.initTemperatureOffsetSlider();
    this.initCookingTemperatureSlider();
    $('cookingTemperatureBox').hide();

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
    new Ajax.Request(APPLICATION_ROOT + '/brew/start', {
        parameters: {processId: this.processId},
        onSuccess: this.startBrewingCallback.bindAsEventListener(this),
        onFailure: this.ajaxError.bindAsEventListener(this)
    });
};

/**
 * Callback for startBrewing server call
 *
 * @param {Object} response  Server response
 * @type void
 */
BoozeBrew.prototype.startBrewingCallback = function(response) {
    if (response.responseJSON) {
        var data = response.responseJSON;
        if (data.success === true) {
            booze.log.info("Brew process successfully started");
            this.initWindow.close();
        }
        else {
            booze.notifier.error(data.message);
        }
    }
    else {
        booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationError"));
    }
    delete response;
};

/**
 * Default callback for ajax errors
 *
 * @param {Object} response  Server response
 * @type void
 */
BoozeBrew.prototype.ajaxError = function(response) {
    booze.log.error("Ajax error");
    booze.notifier.error(response.responseJSON.message);
    delete response;
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
    booze.log.info("Calling brew/update");

    // Sets a new timeout for the next update call
    if (this.stopUpdating === true) {
        return;
    }
    this.updateTimeoutHandle = setTimeout(this.update.bind(this), 1000);

    // Don't proceed if there is a still unfinished update call
    // Omit this turn and wait for the next
    if (this.updateStatus == 0) {
        if (this.updateOmits < this.maxUpdateOmits) {
            booze.log.info("Omitting update");
            this.updateOmits++;
            return;
        }
        else {
            booze.log.info("Omitted " + this.maxUpdateOmits + " updates, proceeding");
            if (this.dialogs.stalled) {
                this.dialogs.stalled.close();
            }
            this.dialogs.stalled = booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationStalled"));
            this.updateOmits = 0;
        }
    }
    else {
        this.updateOmits = 0;
    }


    this.updateStatus = 0;

    // Init AJAX call
    new Ajax.Request(APPLICATION_ROOT + '/brew/readStatus', {
        parameters: {processId: this.processId},
        onSuccess: this.updateCallback.bindAsEventListener(this),
        onFailure: this.updateError.bindAsEventListener(this)
    });
};

/**
 * Callback for update server calls
 * Updates all fields reported by the server
 *
 * @param {Object} response  Server response
 * @type void
 */
BoozeBrew.prototype.updateCallback = function(response) {

    if (response.responseJSON) {
        var my = response.responseJSON;
        delete response;
    }
    else {
        booze.log.error("Not a valid json response for updateCallback");
        if (this.dialogs.updateErrorDialog) {
            this.dialogs.updateErrorDialog.close();
        }
        this.dialogs.updateErrorDialog = booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationError"));
        booze.log.error("Update: No valid JSON response");
        delete response;
        return;
    }

    if (my.success === false) {
        booze.log.error("Update status failed");
        return;
    }

    this.spinAliveThrobber();

    var data = my.status;

    booze.log.info("brew/update successfully called");

    var sensor, temperature, pressure, heater, pump, pumpModes;

    for (var i = 0; i < data.innerTemperatureSensors.length; i++) {
        sensor = $('temperatureSensor_' + data.innerTemperatureSensors[i].id);
        temperature = sensor.select('.temperature').first();
        temperature.update(data.innerTemperatureSensors[i].label);
        temperature.setStyle({'width': Math.round(data.innerTemperatureSensors[i].temperature / 1.1) + '%', "backgroundColor": this.calculateTemperatureColor(data.outerTemperatureSensors[i].temperature, data.targetTemperature)});

    }

    for (var i = 0; i < data.outerTemperatureSensors.length; i++) {
        sensor = $('temperatureSensor_' + data.outerTemperatureSensors[i].id);
        temperature = sensor.select('.temperature').first();
        temperature.update(data.outerTemperatureSensors[i].label);
        temperature.setStyle({'width': Math.round(data.outerTemperatureSensors[i].temperature / 1.1) + '%', "backgroundColor": this.calculateTemperatureColor(data.innerTemperatureSensors[i].temperature, data.targetTemperature)});

    }

    for (var i = 0; i < data.pressureSensors.length; i++) {
        sensor = $('pressureSensor_' + data.pressureSensors[i].id);
        pressure = sensor.select('.pressure').first();
        pressure.update(data.pressureSensors[i].label);
        pressure.setStyle({'width': Math.round(data.pressureSensors[i].pressure / 10) + '%'});

    }

    for (var i = 0; i < data.heaters.length; i++) {
        heater = $('heater_' + data.heaters[i].id);
        if (data.heaters[i].enabled === true) {
            heater.select('.onIcon').first().addClassName('active');
            heater.select('.offIcon').first().removeClassName('active');
        }
        else {
            heater.select('.offIcon').first().addClassName('active');
            heater.select('.onIcon').first().removeClassName('active');
        }
    }

    if (data.pump) {
        pump = $('pump_' + data.pump.id);
        if (data.pump.enabled === true) {
            pump.select('.onIcon').first().addClassName('active');
            pump.select('.offIcon').first().removeClassName('active');
        }
        else {
            pump.select('.offIcon').first().addClassName('active');
            pump.select('.onIcon').first().removeClassName('active');
        }

        pumpModes = pump.select('.pumpMode').first().select('input');

        if (data.pumpMode) {
            for (var c = 0; c < pumpModes.length; c++) {
                if (pumpModes[c].hasClassName("mode_" + data.pumpMode.mode)) {
                    pumpModes[c].show();
                    pumpModes[c].title = data.pumpMode.message;
                }
                else {
                    pumpModes[c].hide();
                }
            }
        }

        if (data.pumpModeForced) {
            pump.select('.forced').first().show();
            pump.select('.unforcePumpModeButton').first().show();
        }
        else {
            pump.select('.forced').first().hide();
            pump.select('.unforcePumpModeButton').first().hide();
        }

        if (data.temperatureReference) {
            $('temperatureReference').update(data.temperatureReference);
        }
    }


    this.handleStep(data.actualStep);

    for (var i = 0; i < data.events.length; i++) {
        this.handleEvent(data.events[i]);
    }

    // Update pause/resume button
    if (data.pause === true) {
        $('pauseButton').hide();
        $('resumeButton').show();
    }
    else {
        $('pauseButton').show();
        $('resumeButton').hide();
    }

    // Update the elapsed time info
    $('timeElapsed').update(data.timeElapsed);

    this.updateStatus = 1;
    if (this.dialogs.updateErrorDialog) {
        this.dialogs.updateErrorDialog.close();
    }
    if (this.dialogs.stalled) {
        this.dialogs.stalled.close();
    }
};

/**
 * Callback for update error
 *
 * @param {Object} response  Server response
 * @type void
 */
BoozeBrew.prototype.updateError = function(response) {
    booze.log.error("Ajax error for brew/update");
    if (this.dialogs.updateErrorDialog) {
        this.dialogs.updateErrorDialog.close();
    }

    this.dialogs.updateErrorDialog = booze.notifier.error(response.responseJSON.message);

    delete response;
};

/**
 * Pauses the brew process
 *
 * @type void
 */
BoozeBrew.prototype.pause = function() {
    booze.log.info("Calling brew/pause");
    new Ajax.Request(APPLICATION_ROOT + '/brew/pause', {
        parameters: {processId: this.processId},
        onSuccess: this.pauseCallback.bindAsEventListener(this),
        onFailure: this.ajaxError.bindAsEventListener(this)
    });
};

/**
 * Callback for pause
 *
 * @param {Object} response  Server response
 * @type void
 */
BoozeBrew.prototype.pauseCallback = function(response) {
    if (response.responseJSON) {
        var data = response.responseJSON;
        if (data.success === true) {
            booze.log.info("Successfully paused brew process");
            $('pauseButton').hide();
            $('resumeButton').show();
        }
        else {
            booze.notifier.error(data.message);
        }
    }
    else {
        booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationError"));
    }
    delete response;
};

/**
 * Resumes the brew process
 *
 * @type void
 */
BoozeBrew.prototype.resume = function() {
    booze.log.info("Calling brew/resume");
    new Ajax.Request(APPLICATION_ROOT + '/brew/resume', {
        parameters: {processId: this.processId},
        onSuccess: this.resumeCallback.bindAsEventListener(this),
        onFailure: this.ajaxError.bindAsEventListener(this)
    });
};

/**
 * Callback for resume
 *
 * @param {Object} response  Server response
 * @type void
 */
BoozeBrew.prototype.resumeCallback = function(response) {
    if (response.responseJSON) {
        var data = response.responseJSON;
        if (data.success === true) {
            booze.log.info("Successfully resumed brew process");
            $('pauseButton').show();
            $('resumeButton').hide();
        }
        else {
            booze.notifier.error(data.message);
        }
    }
    else {
        booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationError"));
    }
    delete response;
};


/**
 * Cancels the brew process
 *
 * @param {boolean} force Set to true to override confirmation message
 * @type void
 */
BoozeBrew.prototype.cancel = function(force) {
    booze.log.info("cancelling brew process");
    if (force) {
        this.stopUpdating = true;
        new Ajax.Request(APPLICATION_ROOT + '/brew/cancel', {
            parameters: {processId: this.processId},
            onSuccess: this.cancelCallback.bindAsEventListener(this),
            onFailure: this.ajaxError.bindAsEventListener(this)
        });
    }
    else {
        this.cancelDialog = Dialog.confirm(booze.messageSource.message("js.booze.brew.cancelConfirmation"), {
            title: booze.messageSource.message("js.booze.brew.cancelConfirmation"),
            width:"400px",
            height: "160px",
            minimizable: false,
            maximizable: false,
            closable: true,
            recenterAuto: true,
            resizable: false,
            destroyOnClose: true,
            okLabel: booze.messageSource.message("js.booze.brew.proceed"),
            cancelLabel: booze.messageSource.message("js.booze.brew.cancel"),
            onOk:function(r) {
                this.cancel(true);
            }.bindAsEventListener(this),
            onCancel: function(r) {
                this.cancelDialog.close();
            }.bindAsEventListener(this)
        });
    }
};

/**
 * Callback for cancel server call
 *
 * @param {Object} response
 * @type void
 */
BoozeBrew.prototype.cancelCallback = function(response) {
    if (response.responseJSON) {
        var data = response.responseJSON;
        if (data.success === true) {
            window.location.href = APPLICATION_HOME;
        }
        else {
            booze.notifier.error(data.message);
        }
    }
    else {
        booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationError"));
    }
    delete response;
};

/**
 * Handles the actual event from an server update call
 *
 * @param {Object} event
 * @type void
 */
BoozeBrew.prototype.handleEvent = function(event) {
    booze.log.info("Handling event: " + event.message);
    if (event.message) {
        var li = new Element("LI").update(event.message);
        $('protocol').insert({top:li});
    }

    if (event.playSound) {
        Sound.play(APPLICATION_ROOT + "/sounds/notification.wav", {replace:true});
    }

    if (event.dialog) {
        this.openDialog(event.dialog, event.args, event.message);
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
     * - meshingTemperatureReached
     * - meshingElongationFinished
     * - pressureExceeded
     */

    booze.log.info("Trying to open dialog: " + dialog);

    // Call dialog
    try {
        var myFunction = eval("this." + dialog).bind(this);
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
    if (this.pressureExceededDialog) {
        this.pressureExceededDialog.close();
        this.pressureExceededDialog = null;
    }
    this.pressureExceededDialog = Dialog.confirm(message, {
        width:400,
        height:200,
        minimizable: false,
        maximizable: false,
        closable: true,
        recenterAuto: true,
        resizable: false,
        destroyOnClose: true,
        okLabel: booze.messageSource.message("js.booze.brew.proceed"),
        cancelLabel: booze.messageSource.message("js.booze.brew.cancel"),
        onOk:function(r) {
            this.pressureExceededDialog.close();
            this.pressureExceededDialog = null;
            this.resume();
        }.bindAsEventListener(this),
        onCancel: function(r) {
            this.pressureExceededDialog.close();
        }.bindAsEventListener(this)
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

    // Display brew initialization window
    this.addHopWindow = new Window('addHopWindow_' + args.id, {title: booze.messageSource.message('js.booze.brew.addHopWindow.title'),
        minimizable: false,
        maximizable: false,
        resizable: false,
        recenterAuto: true,
        closable: true,
        width: "400px",
        height: "260px",
        showEffectOptions: {duration: 0.2},
        hideEffectOptions: {duration: 0.2},
        destroyOnClose: true});

    var myTemplate = new Template($('addHopWindowTemplate').innerHTML);

    this.addHopWindow.setHTMLContent(myTemplate.evaluate(args));
    this.addHopWindow.showCenter(true);
};

BoozeBrew.prototype.fillTemperatureReached = function(args, message) {
    booze.log.info("Opening window for fillTemperatureReached event");

    // Display brew initialization window
    this.fillTemperatureReachedWindow = new Window('fillTemperatureReachedWindow',
    {title: booze.messageSource.message('js.booze.brew.fillTemperatureReachedWindow.title'),
        minimizable: false,
        maximizable: false,
        resizable: false,
        recenterAuto: true,
        closable: false,
        width: "400px",
        minHeight: "180",
        maxHeight: "400",
        showEffectOptions: {duration: 0.2},
        hideEffectOptions: {duration: 0.2},
        destroyOnClose: true});

    this.fillTemperatureReachedWindow.setHTMLContent($('fillTemperatureReachedWindowContent').innerHTML);
    this.fillTemperatureReachedWindow.showCenter(true);
}

/**
 * Opens a dialog for meshingTemperatureReached
 *
 * @param {Array} args      Arguments
 * @param {String} message
 * @type void
 */
BoozeBrew.prototype.meshingTemperatureReached = function(args, message) {
    booze.log.info("Opening window for meshingTemperatureReached event");

    // Display brew initialization window
    this.meshingTemperatureReachedWindow = new Window('meshingTemperatureReachedWindow',
    {title: booze.messageSource.message('js.booze.brew.meshingTemperatureReachedWindow.title'),
        minimizable: false,
        maximizable: false,
        resizable: false,
        recenterAuto: true,
        closable: false,
        width: "400px",
        minHeight: "450",
        showEffectOptions: {duration: 0.2},
        hideEffectOptions: {duration: 0.2},
        destroyOnClose: true});

    var myTemplate = new Template($('meshingTemperatureReachedWindowTemplate').innerHTML);

    this.meshingTemperatureReachedWindow.setHTMLContent(myTemplate.evaluate(args));
    this.meshingTemperatureReachedWindow.showCenter(true);
};

/**
 * Opens a dialog for cookingFinished
 *
 * @param {Array} args      Arguments
 * @param {String} message
 * @type void
 */
BoozeBrew.prototype.cookingFinished = function(args, message) {
    booze.log.info("Opening window for cookingFinished event");

    // Display brew initialization window
    this.cookingFinishedWindow = new Window('cookingFinished',
    {title: booze.messageSource.message('js.booze.brew.cookingFinishedWindow.title'),
        minimizable: false,
        maximizable: false,
        resizable: false,
        recenterAuto: true,
        closable: false,
        width: "400px",
        height: "460px",
        showEffectOptions: {duration: 0.2},
        hideEffectOptions: {duration: 0.2},
        destroyOnClose: true});

    var myTemplate = new Template($('cookingFinishedWindowTemplate').innerHTML);

    this.cookingFinishedWindow.setHTMLContent(myTemplate.evaluate(args));
    this.cookingFinishedWindow.showCenter(true);
};

/**
 * Commits the fill completion and proceeds to meshing
 *
 * @type void
 */
BoozeBrew.prototype.commitFill = function() {
    new Ajax.Request(APPLICATION_ROOT + '/brew/commitFill', {
        parameters: {processId: this.processId},
        onSuccess: function(response) {
            if (response.responseJSON) {
                var data = response.responseJSON;
                if (data.success === true) {
                    booze.log.info("Successfully commited fill");
                    this.fillTemperatureReachedWindow.close();
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
 * Gracefully finished the brew process
 *
 * @type void
 */
BoozeBrew.prototype.finish = function() {
    this.stopUpdating = true;
    var queryParams = Object.toQueryString({
        processId: this.processId,
        finalOriginalWort: $('brewCookingTemperatureReachedWindow_finalOriginalWort').value,
        finalVolume: $('brewCookingTemperatureReachedWindow_finalVolume').value,
        dilutionWaterVolume: $('brewCookingTemperatureReachedWindow_dilutionWaterVolume').value,
    });
    window.location.href = APPLICATION_ROOT + '/brew/finish?' + queryParams;
};

/**
 * Starts the cooking process
 *
 * @type void
 */
BoozeBrew.prototype.startCooking = function() {
    booze.log.info("Calling brew/startCooking");

    new Ajax.Request(APPLICATION_ROOT + '/brew/startCooking', {
        parameters: {processId: this.processId, finalPreCookingWort: $('brewMeshingTemperatureReachedWindow_finalPreCookingWort').value},
        onSuccess: this.startCookingCallback.bindAsEventListener(this),
        onFailure: this.ajaxError.bindAsEventListener(this)
    });
};

/**
 * Callback for startCooking
 *
 * @param {Object} response
 * @type void
 */
BoozeBrew.prototype.startCookingCallback = function(response) {
    if (response.responseJSON) {
        var data = response.responseJSON;
        if (data.success === true) {
            booze.log.info("Successfully started cooking");
            this.meshingTemperatureReachedWindow.close();
        }
        else {
            booze.notifier.error(data.message);
        }
    }
    else {
        booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationError"));
    }
    delete response;
};

/**
 * Elongates the meshing process for a given amount of time
 *
 * @type void
 */
BoozeBrew.prototype.elongateMeshing = function() {
    booze.log.info("Calling brew/elongateMeshing");
    new Ajax.Request(APPLICATION_ROOT + '/brew/elongateMeshing', {
        parameters: {time: $('brewMeshingTemperatureReachedWindow_elongateMeshingTime').value,
            processId: this.processId
        },
        onSuccess: function(response) {
            if (response.responseJSON) {
                var data = response.responseJSON;
                if (data.success === true) {
                    booze.log.info("Successfully started meshing elongation");
                    this.meshingTemperatureReachedWindow.close();
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
};

/**
 * Elongates the cooking process for a given amount of time
 *
 * @type void
 */
BoozeBrew.prototype.elongateCooking = function() {
    booze.log.info("Calling brew/elongateCooking");
    new Ajax.Request(APPLICATION_ROOT + '/brew/elongateCooking', {
        parameters: {time: $('brewCookingTemperatureReachedWindow_elongateCookingTime').value,
            temperature: $('cookingTemperatureHandle').innerHTML,
            processId: this.processId
        },
        onSuccess: function(response) {
            if (response.responseJSON) {
                var data = response.responseJSON;
                if (data.success === true) {
                    booze.log.info("Successfully started cooking elongation");
                    this.cookingFinishedWindow.close();
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
};

/**
 * Handles display of step info
 *
 * @param {Object} step  Step info
 * @type void
 */
BoozeBrew.prototype.handleStep = function(step) {
    booze.log.info("Handling step with type: " + step.type);

    $('stepInfoHeadline').update(step.headline);

    switch (step.type) {
        case "BrewInitStep":

            break;

        case "BrewFillTemperatureStep":
            var children = $('stepInfo').childElements();
            for (var i = 0; i < children.length; i++) {
                if (children[i].id != "fillTemperatureInfo") children[i].hide(); else children[i].show();
            }
            $('fillTemperatureInfo_targetTemperature').update(step.targetTemperature);
            $('fillTemperatureInfo_stepStartTime').update(step.stepStartTime);
            break;

        case "BrewFillTemperatureReachedStep":
            var children = $('stepInfo').childElements();
            for (var i = 0; i < children.length; i++) {
                if (children[i].id != "fillTemperatureReachedInfo") children[i].hide(); else children[i].show();
            }
            break;

        case "BrewMeshStep":
            var children = $('stepInfo').childElements();
            for (var i = 0; i < children.length; i++) {
                if (children[i].id != "restInfo") children[i].hide(); else children[i].show();
            }
            $('restInfo_targetTemperature').update(step.targetTemperature);
            $('restInfo_timeToGo').update(step.timeToGo);
            $('restInfo_stepStartTime').update(step.stepStartTime);
            if (step.targetTemperatureReached) {
                $('restInfo_temperatureReached_true').update(step.targetTemperatureReachedTime);
                $('restInfo_temperatureReached_true').show();
                $('restInfo_temperatureReached_false').hide();
            }
            else {
                $('restInfo_temperatureReached_true').hide();
                $('restInfo_temperatureReached_false').show();
            }
            break;

        case "BrewMeshingFinishedStep":
            var children = $('stepInfo').childElements();
            for (var i = 0; i < children.length; i++) {
                if (children[i].id != "meshingFinishedInfo") children[i].hide(); else children[i].show();
            }
            $('meshingFinishedInfo_targetTemperature').update(step.targetTemperature);
            $('meshingFinishedInfo_stepStartTime').update(step.stepStartTime);

            break;

        case "BrewInitCookingStep":
            var children = $('stepInfo').childElements();
            for (var i = 0; i < children.length; i++) {
                if (children[i].id != "initCookingInfo") children[i].hide(); else children[i].show();
            }
            break;

        case "BrewCookingStep":
            $('cookingTemperatureBox').show();
            var children = $('stepInfo').childElements();
            for (var i = 0; i < children.length; i++) {
                if (children[i].id != "cookingInfo") children[i].hide(); else children[i].show();
            }
            $('cookingInfo_targetTemperature').update(step.targetTemperature);
            $('cookingInfo_timeToGo').update(step.timeToGo);
            $('cookingInfo_stepStartTime').update(step.stepStartTime);
            if (step.targetTemperatureReached) {
                $('cookingInfo_temperatureReached_true').update(step.targetTemperatureReachedTime);
                $('cookingInfo_temperatureReached_true').show();
                $('cookingInfo_temperatureReached_false').hide();
            }
            else {
                $('cookingInfo_temperatureReached_true').hide();
                $('cookingInfo_temperatureReached_false').show();
            }
            break;

        case "BrewCookingFinishedStep":
            var children = $('stepInfo').childElements();
            for (var i = 0; i < children.length; i++) {
                if (children[i].id != "initCookingInfo") children[i].hide(); else children[i].show();
            }
            break;

        case "BrewElongateMeshingStep":
            var children = $('stepInfo').childElements();
            for (var i = 0; i < children.length; i++) {
                if (children[i].id != "elongateMeshingInfo") children[i].hide(); else children[i].show();
            }
            $('elongateMeshingInfo_timeToGo').update(step.timeToGo);
            $('elongateMeshingInfo_stepStartTime').update(step.stepStartTime);
            $('elongateMeshingInfo_targetTemperature').update(step.targetTemperature);

            break;

        case "BrewElongateCookingStep":
            $('cookingTemperatureBox').show();
            var children = $('stepInfo').childElements();
            for (var i = 0; i < children.length; i++) {
                if (children[i].id != "cookingInfo") children[i].hide(); else children[i].show();
            }
            
            var children = $('stepInfo').childElements();
            for (var i = 0; i < children.length; i++) {
                if (children[i].id != "elongateCookingInfo") children[i].hide(); else children[i].show();
            }
            $('elongateCookingInfo_timeToGo').update(step.timeToGo);
            $('elongateCookingInfo_stepStartTime').update(step.stepStartTime);
            $('elongateCookingInfo_targetTemperature').update(step.targetTemperature);

            break;
    }
};

/**
 * Initializes the slider for heatingTemperatureOffset
 *
 * @type void
 */
BoozeBrew.prototype.initTemperatureOffsetSlider = function() {
    this.temperatureOffsetSlider = new Control.Slider('temperatureOffsetHandle', 'temperatureOffsetSlider', {
        range: $R(8, 0),
        sliderValue: $('defaultTemperatureOffset').value,
        values: [8.0, 7.5, 7.0, 6.5, 6.0, 5.5, 5.0, 4.5, 4.0, 3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 0.5, 0.0],
        onSlide: function(to) {
            $('temperatureOffsetHandle').update(to);
        },
        onChange: function(temperatureOffset) {
            new Ajax.Request(APPLICATION_ROOT + '/brew/setTemperatureOffset', {
                parameters: {temperatureOffset: temperatureOffset, processId: this.processId},
                onSuccess: function(response) {
                    if (response.responseJSON) {
                        var data = response.responseJSON;
                        if (data.success == true) {
                            booze.log.info('successfuly set temperatureOffsetSlider to ' + temperatureOffset)
                        }
                        else {
                            booze.notifier.error(data.message);
                        }
                    }
                    else {
                        booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationError"));
                    }
                },
                onFailure: this.ajaxError.bindAsEventListener(this)
            });
        }.bind(this)
    });
};

/**
 * Initializes the slider for cookingTemperature
 *
 * @type void
 */
BoozeBrew.prototype.initCookingTemperatureSlider = function() {
    this.cookingTemperatureSlider = new Control.Slider('cookingTemperatureHandle', 'cookingTemperatureSlider', {
      range: $R(95,108),
      sliderValue: $('defaultCookingTemperature').value,
      values: [95.0, 95.5, 96.0, 96.5, 97.0, 97.5, 98.0, 98.5, 99.0, 99.5, 100.0, 100.5, 101.0, 101.5, 102.0, 102.5, 103.0, 103.5, 104.0, 104.5, 105.0, 105.5, 106.0, 106.5, 107.0, 107.5, 108.0],
      onSlide: function(ct) {
          $('cookingTemperatureHandle').update(ct);
      },
      onChange: function(cookingTemperature) {
        new Ajax.Request(APPLICATION_ROOT+'/brew/setCookingTemperature', {
            parameters: {cookingTemperature: cookingTemperature, processId: this.processId},
            onSuccess: function() { 
                if(response.responseJSON) {
                    var data = response.responseJSON;
                    if(data.success == true) {
                        booze.log.info('successfuly set cookingTemperatureSlider to '+cookingTemperature) 
                    }
                    else {
                        booze.notifier.error(booze.messageSource.message("js.booze.brew.serverCommunicationError"));
                    }

                }
	     },
             onFailure: this.ajaxError.bindAsEventListener(this)
           });
        }.bind(this)
    });
};

/**
 * Adds a comment to the protocol
 *
 * @type void
 */
BoozeBrew.prototype.addComment = function() {
    var commentField = $('brewCommentField');

    new Ajax.Request(APPLICATION_ROOT + '/brew/addComment', {
        parameters: {comment: commentField.value,
            processId: this.processId
        },
        onSuccess: function(response) {
            if (response.responseJSON) {
                var data = response.responseJSON;
                if (data.success === true) {
                    booze.log.info("Successfully added comment");
                    $('brewCommentField').value = "";
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
 * Displays a pump mode selector for a pump
 *
 * @param {Object} event    Calling event
 * @param {Element} callee
 */
BoozeBrew.prototype.pumpModeSelector = function(event, callee) {
    event.stop();

    var pms = callee.up().select('.pumpModeSelector').first();

    pms.clonePosition(callee, {offsetLeft: (pms.getDimensions().width * -1 + callee.getDimensions().width), setWidth: false, setHeight: false});
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
        parameters: {processId: this.processId, pumpMode: pumpMode, pump: pump},
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
 * Unforces a forced pump mode
 *
 * @param {Integer} pump
 * @type void
 */
BoozeBrew.prototype.unforcePumpMode = function(pump) {
    new Ajax.Request(APPLICATION_ROOT + '/brew/unforcePumpMode', {
        parameters: {processId: this.processId, pump: pump},
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
    if (this.aliveThrobber > 8) {
        this.aliveThrobber = 0;
    }
    $('aliveThrobber').className = "s" + this.aliveThrobber;
};

BoozeBrew.prototype.showCalculator = function() {
    booze.log.info("Opening window for calculator");

    // Display brew initialization window
    this.showCalculatorWindow = new Window('brewCalculatorWindow', {title: booze.messageSource.message('js.booze.brew.calculator.title'),
        minimizable: false,
        maximizable: false,
        resizable: false,
        recenterAuto: true,
        closable: true,
        width: "500px",
        height: "400px",
        showEffectOptions: {duration: 0.2},
        hideEffectOptions: {duration: 0.2},
        destroyOnClose: true});

    this.showCalculatorWindow.setHTMLContent($('brewCalculatorWindowTemplate').innerHTML);
    this.showCalculatorWindow.showCenter(true);
}

/**
 * Shows the edit window for edit protocol data
 *
 * @type void
 */
BoozeBrew.prototype.editProtocolData = function() {
    if(this.editProtocolDialog != null) {
        this.editProtocolDialog.toFront();
        return;
    }

    this.editProtocolDialog = new Window('editProtocolDialog', {title: booze.messageSource.message('js.booze.brew.editProtocolDialog.title'),
        minimizable: false,
        maximizable: false,
        resizable: false,
        width: "460px",
        minHeight: "365",
        showEffectOptions: {duration: 0.2},
        hideEffectOptions: {duration: 0.2},
        destroyOnClose: true});

    this.editProtocolDialog.setCloseCallback(function() {
        this.editProtocolDialog = null;
        return true;
    }.bind(this));

    this.editProtocolDialog.setAjaxContent(APPLICATION_ROOT + "/brew/editProtocolData",
    {method: 'get'}, true, true);    
};

/**
 * Saves protocol data to server
 *
 * @type void
 */
BoozeBrew.prototype.saveProtocolData = function() {
    $('protocolDataForm').request({
        onSuccess: function(response) {
            if (response.responseJSON) {
                var data = response.responseJSON;
                if (data.success === true) {
                    if(data.close == true) {
                        booze.log.info("Successfully saved protocol data");
                        this.editProtocolDialog.close();
                        this.editProtocolDialog = null;
                    }
                    else {
                        this.editProtocolDialog.setHTMLContent(data.html);    
                    }
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


