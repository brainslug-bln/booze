<div class="brewMeshingTemperatureReachedWindow">
  <h4><g:message code="brew.init.meshingTemperatureReached" args="${[formatNumber(format: '##0.0', number:brewProcess.recipe.meshTemperature)]}"/></h4>
  <g:if test="${brewProcess.recipe.preCookingWort}">
    <p><g:message code="brew.init.preCookingWortInfo" args="${[formatNumber(format: '##0.0', number:brewProcess.recipe.preCookingWort)]}"/></p>
  </g:if>

  <p><g:message code="brew.init.elongateMeshingInfo"/></p>

  <div class="elongateMeshingArea">

    <div class="buttonArea">
      <input type="button" name="elongateMeshing" onclick="booze.brew.elongateMeshing();" value="${message(code: 'brew.init.elongateMeshing')}"/>
    </div>

    <div class="time">
      <input type="text" name="elongateMeshingTime" id="brewMeshingTemperatureReachedWindow_elongateMeshingTime" value="5"/> <g:message code="default.unit.minutes"/>
    </div>
  </div>

  <div class="clearfix"></div>

  <p><g:message code="brew.init.enterAdditionalValueInfo"/></p>

  <div class="editProtocolArea">
    <div class="yform">

      <div class="twoColumn">
        <div class="column">
          <fieldset>
            <label><g:message code="protocol.finalMeshingWort"/></label>
            <div class="type-text">
              <input type="text" name="finalMeshingWort" value="" /> <g:message code="default.unit.plato"/>
            </div>

            <label><g:message code="protocol.finalPreCookingWort"/></label>
            <div class="type-text">
              <input type="text" name="finalPreCookingWort" value="" id="brewMeshingTemperatureReachedWindow_finalPreCookingWort"/> <g:message code="default.unit.plato"/>
            </div>
          </fieldset>
        </div>
        <div class="column">
          <fieldset>
            <label><g:message code="protocol.finalSecondWaterVolume"/></label>
            <div class="type-text">
              <input type="text" name="finalSecondWaterVolume" value="" /> <g:message code="default.unit.liter"/>
            </div>
          </fieldset>
        </div>
      </div>
    </div>
  </div>

  <div class="clearfix"></div>

  <div class="startCooking">
    <div class="buttonArea">
      <input type="button" name="startCooking" onclick="booze.brew.startCooking();" value="${message(code: 'brew.init.startCooking')}"/>
    </div>
  </div>

</div>