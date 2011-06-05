<div class="addHopWindow">
  <h4><g:message code="brew.init.addHop"/></h4>

  <p><g:message code="brew.init.pleaseAddHop"/></p>

  <fieldset>
    <label><g:message code="hop.name"/></label>
    <div class="value">
      #{name}
    </div>

    <div class="clearfix"></div>

    <label><g:message code="hop.percentAlpha"/></label>
    <div class="value">
      #{percentAlpha}
    </div>

    <div class="clearfix"></div>

    <label><g:message code="hop.amount"/></label>
    <div class="value">
      #{amount}
    </div>

    <div class="clearfix"></div>

    <label><g:message code="hop.time"/></label>
    <div class="value">
      #{time}
    </div>

    <div class="clearfix"></div>
  </fieldset>

  <div class="buttonArea">
    <input type="button" name="close" onclick="booze.brew.addHopWindow.close()" value="${message(code: "brew.init.closeWindow")}"/>
  </div>

</div>