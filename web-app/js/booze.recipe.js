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
 * Booze recipe/create helper methods
 *
 * @author Andreas Kotsias <akotsias@esnake.de>
 * 
 * @param {String} mode Save mode; Possible values are "save" for recipe creation or "update" for updating an existing recipe
 */
function BoozeRecipe(mode) { 
  
  /**
   * Save mode
   */
  this.mode = mode;
  
  /**
   * Tab button DOM li elements
   */
  this.tabs = $("#recipeNav").children("ul").first().children();
  
  // Bind tab click event
  for(var i=0; i<this.tabs.length; i++) {
    $(this.tabs[i]).click({
      tab: this.tabs[i]
      }, this.tabClick);
      
      // Disable tabs in save mode
      if(this.mode == "save" && i > 0) {
        $(this.tabs[i]).addClass("ui-state-disabled");
      }
  }
  
  // Store active tab for later use
  this.activeTab = this.tabs[0];
}


/**
 * Callback for tab click event
 * 
 * Check if a clicked tab is disabled,
 * if not submit the tab's form data
 * 
 * @param {Event} event Calling event
 */
BoozeRecipe.prototype.tabClick = function(event) {
  event.stopPropagation();
  event.preventDefault();
    
  // Check if clicked tab is disabled
  if(!$(event.data.tab).hasClass("ui-state-disabled")) {
    
    // Identify active tab form and update
    var at = $(booze.recipe.activeTab).children("a").first().attr("rel");
    booze.recipe.update($("#"+at+"Form"), {
    tabToShow: event.data.tab
    });
  }
  else {
    booze.logger.info("inactive tab clicked");
  }
}

/**
 * Updates recipe data and optionally displays a new tab
 * 
 * Options: {tabToShow: LI-Element}
 * 
 * @param {Form Element} form Form to submit
 * @param {Object} Hashmap with options
 */
BoozeRecipe.prototype.update = function(form, options) {
  
  //var action = $(form).find("[name=tab]").first().val().capitalize();
  
  // Submit form data to the server
  $.post(APPLICATION_ROOT+"/recipe/"+this.mode, $(form).serialize(), 
  
    function(data) {
      booze.clearStatusMessage();
      if(data.message) {
        booze.showStatusMessage(data.message);
      }
      
      // Redirect to a new page if set
      if(data.redirect) {
        window.location.href = data.redirect;
        return
      }
      
      // Replace the active's tab content if set
      if(data.html) {
        // Update the activeTab content
        $('#'+$(booze.recipe.activeTab).children("a").first().attr("rel")).html(data.html);
      }
      
      // Optionally display a new tab on success
      if(data.success) {
        if(options && options.tabToShow) {
          booze.recipe.displayTab(options.tabToShow);
        }
      }
      else {
        if(data.error) {
          booze.showStatusMessage(data.error);
          booze.logger.error(data.error)
        }
      }
    }, "json")
}

/**
 * Displays a tab and hides all other tabs
 * 
 * @param {Element} tabToShow Tab to show
 */
BoozeRecipe.prototype.displayTab = function(tabToShow) {
  
  for(var i=0; i<booze.recipe.tabs.length; i++) {
    $(booze.recipe.tabs[i]).removeClass("active");
  }
  // Update ui-state
  $(tabToShow).removeClass("ui-state-disabled");
  $(tabToShow).addClass("active");
  
  var tabContents = $("#recipeTabsContent").children();
    
  for(var i=0; i<tabContents.length; i++) {
    $(tabContents[i]).hide();
  }

  $("#" + $(tabToShow).children("a").first().attr("rel")).show();
    
  booze.recipe.activeTab = tabToShow;
  
}

/**
 * Submits the form of the active tab
 * 
 * @param {Event} event Calling event
 */
BoozeRecipe.prototype.submit = function(event) {
  event.stopPropagation();
  event.preventDefault();
  
  // Identify active tab form and update
  var at = $(booze.recipe.activeTab).children("a").first().attr("rel");
  
  if(event.data && event.data.finalSave) {
    $('#'+at+'Form').find("[name=finalSave]").first().val(1);
  }
  else {
    $('#'+at+'Form').find("[name=finalSave]").first().val(0);
  }
  
  // Show the actually active tab after updating
  // or the next tab after saving
  var tabToShow = $(booze.recipe.activeTab);
  if(this.mode == "save") {
    tabToShow = $(tabToShow).next();
  }
  
  booze.recipe.update($('#'+at+'Form'), {
    tabToShow: tabToShow
  });
}


/**
 * Inserts a row into the bottom of a table using a
 * tr with class "newRowTemplate
 *
 * @param {Element} callee Calling element
 * @param {string} template ID of the template to use
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
        //booze.showStatusMessage(booze.messageSource.message("js.booze.recipe.removeRow.lastRowLeft"), {duration: 5000});
    }
};


