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
 * Booze recipe helper methods
 *
 * @author Andreas Kotsias <akotsias@esnake.de>
 */
function BoozeRecipe() { 
  this.tabs = $("#recipeNav").children("ul").first().children();
}

BoozeRecipe.prototype.initCreate = function() {
  for(var i=1; i<this.tabs.length; i++) {
    $(this.tabs[i]).addClass("ui-state-disabled");
  }
}

/**
 * Inits the recipe/edit environment
 */
BoozeRecipe.prototype.initEdit = function(recipe) {
  for(var i=0; i<this.tabs.length; i++) {
    $(this.tabs[i]).click({
      tab: this.tabs[i]
      }, this.tabClick);
  }
  this.activeTab = this.tabs[0];
  this.recipeId = recipe;
}

/**
 * Callback for tab click event
 */
BoozeRecipe.prototype.tabClick = function(event) {
  event.stopPropagation();
  event.preventDefault();
    
  // Identify active tab form and update
  var at = $(booze.recipe.activeTab).children("a").first().attr("rel");
  booze.recipe.update($("#"+at+"Form"), {
    tabToShow: event.data.tab
    });
}

/**
 * Displays a tab and hides all other tabs
 */
BoozeRecipe.prototype.displayTab = function(tabToShow) {
  
  var tabName = $(tabToShow).children("a").first().attr("rel");
  
  $.post(APPLICATION_ROOT+"/recipe/tab", {'recipe.id': booze.recipe.recipeId, tab: tabName}, 
    function(data) {
      if(data.message) {
        booze.showStatusMessage(data.message);
      }
      if(data.success) {
        $('#'+tabName).html(data.html);
        
        for(var i=0; i<booze.setting.tabs.length; i++) {
          $(booze.setting.tabs[i]).removeClass("active");
        }
        $(tabToShow).addClass("active");

        var tabContents = $("#recipeTabsContent").children();

        for(var i=0; i<tabContents.length; i++) {
          $(tabContents[i]).hide();
        }

        $("#"+tabName).show();

        booze.setting.activeTab = tabToShow;
      }
      else {
        if(data.error) {
          booze.showStatusMessage(data.error);
          console.log(data.error)
        }
      }
    }
  );
}

/**
 * Updates recipe data and optionally displays a new tab
 * Options: {tabToShow: LI-Element}
 */
BoozeRecipe.prototype.update = function(form, options) {
  
  $.post(APPLICATION_ROOT+"/recipe/update", $(form).serialize(), 
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
        if(data.error) {
          booze.showStatusMessage(data.error);
          console.log(data.error)
        }
      }
      
      if(data.tab && data.html) {
        $("#"+data.tab+"TabContent").html(data.html);
      }
    }, "json")
}

/**
 * Inserts a row into the bottom of a table using a
 * tr with class "newRowTemplate
 *
 * @param {Element} callee
 * @type void
 */
BoozeRecipe.prototype.insertRow = function(callee, template) {
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
BoozeRecipe.prototype.deleteRow = function(callee, options) {
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
        booze.notifier.notify(booze.messageSource.message("js.booze.recipe.removeRow.lastRowLeft"), {duration: 5000});
    }
};


BoozeRecipe.prototype.submit = function(event) {}

booze.recipe = new BoozeRecipe();