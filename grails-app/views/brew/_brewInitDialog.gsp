<div class="brew">
  <h4><g:message code="brew.init.startMessage"/></h4>


  <span class="fieldsetCaption"><g:message code="brew.init.fill"/></span>
  <fieldset class="ui-widget-content">
    <ul>
      <li><g:message code="brew.init.mashingWaterVolume" args="${[brewProcess.recipe.mashingWaterVolume]}"/></li>
      <g:if test="${!brewProcess.recipe.mashingTemperature}">
        <g:each in="${brewProcess.recipe.malts}" var="malt">
          <li><g:message code="brew.init.malt" args="${[formatNumber(format:'##0.0', number: malt.amount), malt.name.encodeAsHTML()]}"/></li>
        </g:each>
      </g:if>
    </ul>
  </fieldset>

  <g:if test="${brewProcess.recipe.mashingTemperature}">
    <p>
      <g:message code="brew.init.mashingTemperatureInfo" args="${[formatNumber(format:'##0.0', number: brewProcess.recipe.mashingTemperature)]}"/>
    </p>
  </g:if>


  <p class="disclaimer">
    <g:message code="brew.init.disclaimer"/>
  </p>

</div>