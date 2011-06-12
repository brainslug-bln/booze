<div class="cookingFinishedDialog brew brewDialog">
  <h4><g:message code="brew.init.cookingFinished"/></h4>
  <p><g:message code="brew.init.originalWortInfo" args="${[formatNumber(format: '##0.0', number:brewProcess.recipe.originalWort)]}"/></p>

  <p><g:message code="brew.init.cookingFinishedInfo"/></p>
  <p><g:message code="brew.init.elongateCookingInfo"/></p>
  
  <fieldset>
    <label for="brewCookingFinishedDialog_elongateCookingTime"><g:message code="brew.init.elongateCookingTime" /></label>
    <input type="text" name="elongateCookingTime" id="brewCookingTemperatureReachedDialog_elongateCookingTime" value="5"/>
    
    <input class="ui-button ui-state-default" type="button" name="elongateMashing" onclick="booze.brew.elongateCooking();" value="${message(code: 'brew.init.elongateMashing')}"/>
  </fieldset>

  <div class="clear"></div>

  <p><g:message code="brew.init.enterAdditionalValueInfo"/></p>

  <fieldset>
    <div class="leftColumn">
        <label><g:message code="protocol.dilutionWaterVolume"/></label>
        <input id="brewCookingTemperatureReachedDialog_dilutionWaterVolume" type="text" name="dilutionWaterVolume" value="" /> 

        <label><g:message code="protocol.finalBeerVolume"/></label>
        <input type="text" name="finalBeerVolume" value="" id="brewCookingTemperatureReachedDialog_finalBeerVolume"/>
    </div>

    <div class="rightColumn">
        <label><g:message code="protocol.finalOriginalWort"/></label>
        <input type="text" name="finalOriginalWort" value="" id="brewCookingTemperatureReachedDialog_finalOriginalWort" /> 
    </div>
  </fieldset>

  <g:if test="${brewProcess.drainPumpRegulator}">
    <p class="disclaimer"><g:message code="brew.init.drainPumpDisclaimer" /></p>
  </g:if>
    
  <div class="clear"></div>
</div>