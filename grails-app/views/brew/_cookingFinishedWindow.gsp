<div class="cookingFinishedWindow">
  <h4><g:message code="brew.init.cookingFinished"/></h4>
  <p><g:message code="brew.init.originalWortInfo" args="${[formatNumber(format: '##0.0', number:brewProcess.recipe.originalWort)]}"/></p>

  <p><g:message code="brew.init.cookingFinishedInfo"/></p>
  <p><g:message code="brew.init.elongateCookingInfo"/></p>

  <div class="elongateCookingArea">

    <div class="buttonArea">
      <input type="button" name="elongateCooking" onclick="booze.brew.elongateCooking();" value="${message(code: 'brew.init.elongateCooking')}"/>
    </div>

    <div class="time">
      <input type="text" name="elongateCookingTime" id="brewCookingTemperatureReachedWindow_elongateCookingTime" value="5"/> <g:message code="default.unit.minutes"/>
    </div>
  </div>

  <div class="clearfix"></div>

  <div class="finish">

    <p><g:message code="brew.init.enterAdditionalValueInfo"/></p>

    <div class="editProtocolArea">
      <div class="yform">

        <div class="twoColumn">
          <div class="column">
            <fieldset>
              <label><g:message code="protocol.dilutionWaterVolume"/></label>
              <div class="type-text">
                <input type="text" id="brewCookingTemperatureReachedWindow_dilutionWaterVolume" name="dilutionWaterVolume" value="" /> <g:message code="default.unit.liter"/>
              </div>

              <label><g:message code="protocol.finalVolume"/></label>
              <div class="type-text">
                <input type="text" id="brewCookingTemperatureReachedWindow_finalVolume" name="finalVolume" value="" /> <g:message code="default.unit.liter"/>
              </div>
            </fieldset>
          </div>
          <div class="column">
            <fieldset>
              <label><g:message code="protocol.finalOriginalWort"/></label>
              <div class="type-text">
                <input type="text" name="finalOriginalWort" id="brewCookingTemperatureReachedWindow_finalOriginalWort" value="" /> <g:message code="default.unit.plato"/>
              </div>
            </fieldset>
          </div>
        </div>
      </div>
    </div>

    <div class="clearfix"></div>

    <div class="buttonArea">
      <input type="button" name="finish" onclick="booze.brew.finish();" value="${message(code: 'brew.init.finish')}"/>
    </div>
  </div>

</div>