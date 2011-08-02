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
 * BoozeMessageSource class
 * Handles i18n messages
 *
 * This is still a stub that returns german values only
 *
 */
function BoozeMessageSource() {
    this.messages = {
        "js.booze.notifier.notification": "Hinweis",
        "js.booze.notifier.close": "Schließen",
        "js.booze.notifier.cancel": "Abbrechen",
        "js.booze.notifier.ok": "Ja",
        "js.booze.notifier.proceed": "Weiter",
        "js.booze.notifier.error": "Fehler",
        'js.booze.notifier.confirm': "Frage",
        'js.booze.notifier.confirmCancellation': "Brauprozess beenden",
        'js.booze.notifier.keepOnBrewing': "Weiterbrauen",
        'js.booze.notifier.info': 'Information',

        'js.booze.brew.initWindow.title': "Brauprozess starten",
        'js.booze.brew.mashingTemperatureReachedDialog.title': "Einmaischtemperatur erreicht",
        "js.booze.brew.serverCommunicationError": "Fehler beim Verbindungsaufbau zum Server",
        "js.booze.brew.cancelConfirmation": 'Brauprozess wirklich abbrechen?',
        "js.booze.brew.couldNotOpenDialog": "Konnte Dialog nicht öffnen: ",
        "js.booze.brew.proceed": "Brauprozess abbrechen",
        "js.booze.brew.cancel": "Weiterbrauen",
        'js.booze.brew.addHopDialog.title': 'Hopfengabe',
        'js.booze.brew.lauterTemperatureReachedDialog.title': 'Abmaischtemperatur erreicht',
        'js.booze.brew.cookingFinishedDialog.title': 'Kochvorgang abgeschlossen',
        'js.booze.brew.cookingFinishedDialog.finish':'Kochvorgang beenden',
        'js.booze.brew.serverCommunicationStalled': 'Der Server antwortet nicht',
        'js.booze.brew.editProtocolDialog.title': 'Messwerte eintragen',
        'js.booze.brew.calculator.title': 'Braurechner',
        'js.booze.brew.finishBrewProcess':'Beenden',
        'js.booze.brew.coolingDialog.title':'Abpumpen/Kühlen',
        'js.booze.brew.temperatureChart':'Temperaturverlauf',
        'js.booze.brew.pressureChart':'Druckverlauf',
        'js.booze.brew.save':"Speichern",

        'js.booze.manualMode.serverCommunicationError': "Fehler beim Verbindungsaufbau zum Server"
    };
}

/**
 * Returns a localized message
 *
 * @param {String} code
 * @param {Array} args
 * @type void
 */
BoozeMessageSource.prototype.message = function(code, args) {
    if (this.messages[code]) {
        return this.messages[code];
    }
    else {
        return code;
    }
};

booze.messageSource = new BoozeMessageSource();