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
 * Booze form helper methods
 *
 * @author Andreas Kotsias <akotsias@esnake.de>
 */
function BoozeForm() { }

/**
 * Inserts a row into the bottom of a table using a
 * tr with class "newRowTemplate
 *
 * @param {Element} callee
 * @type void
 */
BoozeForm.prototype.insertRow = function(callee, template) {
    var tbody = $(callee).parent().parent().parent().parent().children('tbody').first();

    var tArgs = [{ index: tbody.children().length }];
    $('#'+template).tmpl(tArgs).appendTo(tbody);
};


/**
 * Removes a row from a table
 *
 * @param {Element} callee
 * @param {Object} options
 * @type void
 */
BoozeForm.prototype.deleteRow = function(callee, options) {
    // Get tbodys
    var tbody = $(callee).parent().parent().parent().select('.tbody').first();

    var min = 1;
    if (options && options.min !== null) {
        min = options.min;
    }

    // Remove only if there is more than one row left
    if (tbody.children('tr').length > (min)) {
        $(callee).parent().parent().remove();

        // Correct all other row's indices
        var rows = tbody.children("tr");

        for (var i = 0; i < rows.length; i++) {
            var indices = $(rows[i]).find('.index');
            for(var p=0; p < indices.length; p++) {
                indices[p].value = i;
            }

            var inputs = $(rows[i]).find('input');
            for (var q = 0; q < inputs.length; q++) {
                inputs[q].name = inputs[q].name.replace(/\[\d+\]/, "[" + i + "]");
            }
        }
    }
    else {
        booze.notifier.notify(booze.messageSource.message("js.booze.recipe.removeRow.lastRowLeft"));
    }
};

booze.form = new BoozeForm();