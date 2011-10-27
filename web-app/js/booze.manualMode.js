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
 * BoozeManualMode class contains all methods
 * for manually handling the brew hardware
 *
 */
function BoozeManualMode() {

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
 * Inits the manual mode
 *
 * @param processId
 * @type void
 */
BoozeManualMode.prototype.init = function(processId) {

    this.processId = processId;

    // Start status updates
    this.update();
};

/**
 * Resumes a lost manual mode session
 *
 * @param processId
 * @type void
 */
BoozeManualMode.prototype.resumeLostSession = function(processId) {

    this.processId = processId;

    // Start status updates
    this.update();
};

/**
 * Default callback for ajax errors
 *
 * @param {Object} response  Server response
 * @type void
 */
BoozeManualMode.prototype.ajaxError = function(response) {
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
BoozeManualMode.prototype.update = function() {
    booze.log.info("Calling manualMode/update");

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
            this.dialogs.stalled = booze.notifier.error(booze.messageSource.message("js.booze.manualMode.serverCommunicationStalled"));
            this.updateOmits = 0;
        }
    }
    else {
        this.updateOmits = 0;
    }


    this.updateStatus = 0;

    // Init AJAX call
    new Ajax.Request(APPLICATION_ROOT + '/manualMode/readStatus', {
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
BoozeManualMode.prototype.updateCallback = function(response) {

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

    booze.log.info("manualMode/update successfully called");

    var sensor, temperature, pressure, heater, pump, pumpModes;

    for (var i = 0; i < data.innerTemperatureSensors.length; i++) {
        sensor = $('temperatureSensor_' + data.innerTemperatureSensors[i].id);
        temperature = sensor.select('.temperature').first();
        temperature.update(data.innerTemperatureSensors[i].label);
        temperature.setStyle({'width': Math.round(data.innerTemperatureSensors[i].temperature / 1.1) + '%'});

    }

    for (var i = 0; i < data.outerTemperatureSensors.length; i++) {
        sensor = $('temperatureSensor_' + data.outerTemperatureSensors[i].id);
        temperature = sensor.select('.temperature').first();
        temperature.update(data.outerTemperatureSensors[i].label);
        temperature.setStyle({'width': Math.round(data.outerTemperatureSensors[i].temperature / 1.1) + '%'});

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
BoozeManualMode.prototype.updateError = function(response) {
    booze.log.error("Ajax error for manualMode/update");
    if (this.dialogs.updateErrorDialog) {
        this.dialogs.updateErrorDialog.close();
    }

    this.dialogs.updateErrorDialog = booze.notifier.error(response.responseJSON.message);

    delete response;
};


/**
 * Stops the manual mode
 *
 * @type void
 */
BoozeManualMode.prototype.stop = function() {
    this.stopUpdating = true;
    new Ajax.Request(APPLICATION_ROOT + '/manualMode/stop', {
        parameters: {processId: this.processId},
        onSuccess: this.stopCallback.bindAsEventListener(this),
        onFailure: this.ajaxError.bindAsEventListener(this)
    });
};

/**
 * Callback for cancel server call
 *
 * @param {Object} response
 * @type void
 */
BoozeManualMode.prototype.stopCallback = function(response) {
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
        booze.notifier.error(booze.messageSource.message("js.booze.manualMode.serverCommunicationError"));
    }
    delete response;
};


/**
 * Displays a pump mode selector for a pump
 *
 * @param {Object} event    Calling event
 * @param {Element} callee
 */
BoozeManualMode.prototype.pumpModeSelector = function(event, callee) {
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
BoozeManualMode.prototype.hidePumpModeSelector = function(event, pms) {
    event.stop();

    pms.hide();
    Element.stopObserving(window.document, this.pmsClickHandle);
}

/**
 * Sets the pump to the selected pump mode
 *
 * @param {Integer} pump Pump Id
 * @param {Integer} pumpMode Pump mode id
 * @type void
 */
BoozeManualMode.prototype.setPumpMode = function(pump, pumpMode) {
    new Ajax.Request(APPLICATION_ROOT + '/manualMode/setPumpMode', {
        parameters: {processId: this.processId, pumpMode: pumpMode, pump: pump},
        onSuccess: function(response) {
            if (response.responseJSON) {
                var data = response.responseJSON;
                if (data.success === true) {
                    booze.log.info("set pump mode");
                }
                else {
                    booze.notifier.error(data.message);
                }
            }
            else {
                booze.notifier.error(booze.messageSource.message("js.booze.manualMode.serverCommunicationError"));
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
BoozeManualMode.prototype.spinAliveThrobber = function() {
    this.aliveThrobber++;
    if (this.aliveThrobber > 8) {
        this.aliveThrobber = 0;
    }
    $('aliveThrobber').className = "s" + this.aliveThrobber;
};

/**
 * Shows the brew calculator window
 */
BoozeManualMode.prototype.showCalculator = function() {
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
 * Toggle heater on/off status
 *
 * @param {Number} id    Heater ID
 */
BoozeManualMode.prototype.toggleHeater = function(id) {
    new Ajax.Request(APPLICATION_ROOT + '/manualMode/toggleHeater', {
        parameters: {processId: this.processId, heater: id},
        onSuccess: function(response) {
            if (response.responseJSON) {
                var data = response.responseJSON;
                if (data.success === true) {
                    booze.log.info("toggled heater");
                }
                else {
                    booze.notifier.error(data.message);
                }
            }
            else {
                booze.notifier.error(booze.messageSource.message("js.booze.manualMode.serverCommunicationError"));
            }
        }.bindAsEventListener(this),
        onFailure: this.ajaxError.bindAsEventListener(this)
    });
}


// Create instance within booze namespace
booze.manualMode = new BoozeManualMode();


