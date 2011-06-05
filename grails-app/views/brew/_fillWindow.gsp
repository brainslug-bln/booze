<div class="brewFillWindow">
  <h4><g:message code="brew.init.fillHeadline"/></h4>

  <p><g:message code="brew.init.fillInfo"/></p>

  <div class="yform">
    <div class="fieldsetHeadline"><g:message code="brew.init.fill"/></div>
    <fieldset>
      <ul>
        <g:each in="${brewProcess.recipe.malts}" var="malt">
          <li><g:message code="brew.init.malt" args="${[formatNumber(format:'##0.0', number: malt.amount), malt.name.encodeAsHTML()]}"/></li>
        </g:each>
      </ul>
    </fieldset>
  </div>

  <div class="buttonArea">
    <div style="float: left"><input type="button" name="cancelBrewing" onclick="booze.brew.cancel();" value="${message(code: 'brew.init.cancelBrewing')}"/></div>
    <div style="float: right"><input type="button" name="proceed" onclick="booze.brew.commitFill();" value="${message(code: 'brew.init.commitFill')}"/></div>
  </div>
</div>