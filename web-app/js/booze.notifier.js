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
 * BoozeNotifier class
 * Handles displaying of notifications and error messages
 * messages
 *
 * @author Andreas Kotsias <akotsias@esnake.de>
 */
function BoozeNotifier() {

}

/**
 * Displays a simple notification
 *
 * @param {String} message
 * @param {Map} options
 * @type void
 */
BoozeNotifier.prototype.notify = function(message, options) {

    if (!options) {
        options = {};
    }

    var dialog = Dialog.alert(message, {
        minimizable: false,
        maximizable: false,
        title: booze.messageSource.message("js.booze.notifier.notification"),
        resizable: false,
        width: "300px",
        maxHeight: "500px",
        showEffectOptions: {duration: 0.2},
        hideEffectOptions: {duration: 0.2},
        destroyOnClose: true,
        className: 'notification',
        okLabel: booze.messageSource.message("js.booze.notifier.close"),
        onOk:function(r) {
            r.close();
        }
    });

    if (options.duration) {
        window.setTimeout(function(d) {
            d.close();
        }.curry(dialog), options.duration);
    }
};

/**
 * Displays an error notification
 *
 * @param {String} message
 * @param {Map} options
 * @type void
 */
BoozeNotifier.prototype.error = function(message, options) {

    if (!options) {
        options = {};
    }

    var dialog = Dialog.alert(message, {
        minimizable: false,
        maximizable: false,
        title: booze.messageSource.message("js.booze.notifier.error"),
        resizable: false,
        width: "300px",
        maxHeight: "400px",
        showEffectOptions: {duration: 0.2},
        hideEffectOptions: {duration: 0.2},
        destroyOnClose: true,
        className: 'error',
        closable: true,
        okLabel: booze.messageSource.message("js.booze.notifier.close"),
        onOk:function(r, callback) {
            if (callback) {
                callback();
            }
            r.close();
        }.bindAsEventListener(this, options.callback)
    });

    if (options.duration) {
        window.setTimeout(function(d) {
            d.close();
        }.curry(dialog), options.duration);
    }

    return dialog;
};

/**
 * Displays an confirmation
 *
 * @param {String} message
 * @param {Map} options
 * @type void
 */
BoozeNotifier.prototype.confirm = function(message, options) {

    if (!options) {
        options = {};
    }

    var dialog = Dialog.confirm(message, {
        minimizable: false,
        maximizable: false,
        title: booze.messageSource.message("js.booze.notifier.confirm"),
        resizable: false,
        width: "300px",
        maxHeight: "500px",
        showEffectOptions: {duration: 0.2},
        hideEffectOptions: {duration: 0.2},
        destroyOnClose: true,
        className: 'notification',
        okLabel: booze.messageSource.message("js.booze.notifier.ok"),
        cancelLabel: booze.messageSource.message("js.booze.notifier.cancel"),
        onOk:function(r, callback) {
            if (callback) {
                callback();
            }
            r.close();
        }.bindAsEventListener(this, options.callback),
        onCancel: function(r) {
            r.close();
        }
    });
}

/**
 * Displays an information (without any buttons)
 *
 * @param {String} message
 * @param {Map} options
 * @type void
 */
BoozeNotifier.prototype.info = function(message, options) {

    if (!options) {
        options = {};
    }

    var dialog = Dialog.info(message, {
        minimizable: false,
        maximizable: false,
        title: booze.messageSource.message("js.booze.notifier.info"),
        resizable: false,
        width: "300px",
        maxHeight: "500px",
        showEffectOptions: {duration: 0.2},
        hideEffectOptions: {duration: 0.2},
        destroyOnClose: true
    });
}


booze.notifier = new BoozeNotifier();

