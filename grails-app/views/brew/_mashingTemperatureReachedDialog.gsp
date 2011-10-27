<div class="brewFillWindow brew">
  <h4><g:message code="brew.init.mashingTemperatureReachedHeadline"/></h4>

  <p><g:message code="brew.init.mashingTemperatureReachedInfo"/></p>

  <span class="fieldsetCaption"><g:message code="brew.init.fill"/></span>
  <fieldset class="ui-widget-content">
    <ul>
      <li><g:message code="brew.init.mashingWaterVolume" args="${[brewProcess.recipe.mashingWaterVolume]}"/></li>
        <g:each in="${brewProcess.recipe.malts}" var="malt">
          <li><g:message code="brew.init.malt" args="${[formatNumber(format:'##0.0', number: malt.amount), malt.name.encodeAsHTML()]}"/></li>
        </g:each>
    </ul>
  </fieldset>

</div>