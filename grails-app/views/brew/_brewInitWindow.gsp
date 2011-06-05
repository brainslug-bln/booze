<div class="brewInitWindow">
  <h4><g:message code="brew.init.startMessage"/></h4>

  <div class="yform">

    <div class="fieldsetHeadline"><g:message code="brew.init.fill"/></div>
    <fieldset>
      <ul>
        <li><g:message code="brew.init.fillWaterAmount" args="${[brewProcess.recipe.mainWaterVolume]}"/></li>
        <g:if test="${!brewProcess.recipe.fillTemperature}">
          <g:each in="${brewProcess.recipe.malts}" var="malt">
            <li><g:message code="brew.init.malt" args="${[formatNumber(format:'##0.0', number: malt.amount), malt.name.encodeAsHTML()]}"/></li>
          </g:each>
        </g:if>
      </ul>
    </fieldset>
  </div>

  <g:if test="${brewProcess.recipe.fillTemperature}">
    <p>
      <g:message code="brew.init.fillTemperatureInfo" args="${[formatNumber(format:'##0.0', number: brewProcess.recipe.fillTemperature)]}"/>
    </p>
  </g:if>


  <p class="disclaimer">
    <g:message code="brew.init.disclaimer"/>
  </p>

  <div class="buttonArea">
    <div style="float: left"><input type="button" name="cancelBrewing" onclick="booze.brew.cancel(true);" value="${message(code: 'brew.init.cancelBrewing')}"/></div>
    <div style="float: right"><input type="button" name="startBrewing" onclick="booze.brew.startBrewing();" value="${message(code: 'brew.init.startBrewing')}"/></div>
  </div>
</div>