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
 * 
 * Handles displaying of notifications and error messages
 * messages
 *
 * @author Andreas Kotsias <akotsias@esnake.de>
 */
function BoozeNotifier() {

}

/**
 * Displays a simple notification
 * with one "OK" button
 * 
 * options: {modal: true/false}
 * 
 * @param {String} message
 * @param {Map} options
 * @type void
 */
BoozeNotifier.prototype.notify = function(message, options) {
  if (!options) {
        options = {};
    }
    
    if(!options.modal) options.modal = false
    if(!options.title) options.title = booze.messageSource.message("js.booze.notifier.notification");
    if(!options.callback) options.callback = function() { $(this).dialog("destroy")};
    if(!options.buttonText) options.buttonText = booze.messageSource.message("js.booze.notifier.close")
    
    var dialog = $('<div></div>')
		.html(message)
		.dialog({
      width: "30%",
      minWidth: "35em",
			title: options.title,
      modal: options.modal,
      buttons:  
        [
          { text: options.buttonText,
            click: options.callback
          }
        ]
		});
    
    return dialog;
    
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
    
    if(!options.title) options.title = booze.messageSource.message("js.booze.notifier.error");
    if(!options.modal) options.modal = false;
    if(!options.callback) options.callback = function() { $(this).dialog('destroy') };
    if(!options.buttonText) options.buttonText = booze.messageSource.message("js.booze.notifier.close")
    
    var dialog = $('<div></div>')
		.html(message)
		.dialog({
      width: "30%",
      minWidth: "35em",
			title: options.title,
      modal: options.modal,
      buttons:  
        [
          { text: options.buttonText,
            click: options.callback
          }
        ]
		});
    
    $.sound.play(APPLICATION_ROOT + "/sounds/notification.mp3", {timeout: 10000});

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
    
    if(!options.title) options.title = booze.messageSource.message("js.booze.notifier.confirm");
    if(!options.modal) options.modal = false;
    var pc = function() { $(this).dialog('destroy') };
    if(options.proceedCallback) pc = options.proceedCallback;
    if(options.proceedCallbackOptions) pc = function() { options.proceedCallback(options.proceedCallbackOptions)}
    if(!options.proceedText) options.proceedText = booze.messageSource.message("js.booze.notifier.proceed")
    
    var cb = function() { $(this).dialog('destroy') };
    if(options.cancelCallback) cb = options.cancelCallback;
    if(options.cancelCallbackOptions) cb = function() { options.cancelCallback(options.cancelCallbackOptions)}
    if(!options.cancelText) options.cancelText = booze.messageSource.message("js.booze.notifier.cancel")
    
    var dialog = $('<div></div>')
		.html(message)
		.dialog({
      width: "30%",
      minWidth: "35em",
			title: options.title,
      modal: options.modal,
      buttons:  
        [
          { text: options.cancelText,
            click: cb
          },
          { text: options.proceedText,
            click: pc
          }
        ]
		});
    
    if(options.playSound) {
      $.sound.play(APPLICATION_ROOT + "/sounds/notification.mp3", {timeout: 10000});
    }

    return dialog;
};



BoozeNotifier.prototype.statusMessage = function(msg) {
  booze.log.info(msg);
  
  this.clearStatusMessage();
  $('#statusMessage').html(msg);
  $('#statusMessage').slideDown("slow");
  $('#statusMessage').delay(5000);
  $('#statusMessage').slideUp("slow");
}

BoozeNotifier.prototype.clearStatusMessage = function() {
  $('#statusMessage').clearQueue("fx");
  $('#statusMessage').html("");
}


booze.notifier = new BoozeNotifier();

